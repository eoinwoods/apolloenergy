package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
import org.apache.logging.log4j.LogManager
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper

class InfluxDbDecorator(private val dbUrl: String, private val dbName: String, private val dbUser: String? = null, private val dbPassword: String? = null) {

    private val _log = LogManager.getLogger(this.javaClass.name)

    private val _influxdb: InfluxDB

    init {
        if (dbUser != null && dbUser.isEmpty()) {
            _influxdb = InfluxDBFactory.connect(dbUrl, dbUser, dbPassword)
        } else {
            _influxdb = InfluxDBFactory.connect(dbUrl)
        }
    }

    fun getInfluxDbConnection(): InfluxDB {
        return _influxdb
    }

    fun getBestCpuMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, container_name, usage_total from docker_container_cpu
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and cpu = 'cpu-total' and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        return getBestMeasureForContainerAtTime(CPU_QUERY_TEMPLATE, CpuMeasurement::class.java as Class<GenericMeasurement>, containerId, timeMsec)
    }

    fun getBestMemMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, container_name, usage_total from docker_container_mem
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        return getBestMeasureForContainerAtTime(MEM_QUERY_TEMPLATE, MemMeasurement::class.java as Class<GenericMeasurement>, containerId, timeMsec)
    }

    fun getBestDiskIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, container_name, io_service_bytes_recursive_total from docker_container_blkio
        // where container_id = 'cd299a7d035895a05937c72ce127459d613ba028e2bb33218892cc3cd201301c'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        return getBestMeasureForContainerAtTime(DISKIO_QUERY_TEMPLATE, DiskIoMeasurement::class.java as Class<GenericMeasurement>, containerId, timeMsec)
    }

    fun getBestNetIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, rx_bytes, tx_bytes from docker_container_net
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        return getBestMeasureForContainerAtTime(NETIO_QUERY_TEMPLATE, NetIoMeasurement::class.java as Class<GenericMeasurement>, containerId, timeMsec)
    }

    private fun getBestMeasureForContainerAtTime(queryTemplate: String, mappingClass : Class<GenericMeasurement>, containerId: String, timeMsec: Long): Long {
        val timeAsNanoSec = Util.msecToNanoSec(timeMsec)
        val query = queryTemplate.format(containerId, timeAsNanoSec, QUERY_WINDOW, timeAsNanoSec, QUERY_WINDOW)
        _log.info("Querying InfluxDB for ${mappingClass.name} via query $query")
        val dbQuery = Query(query, dbName)
        val result = _influxdb.query(dbQuery)

        val resultMapper = InfluxDBResultMapper()
        val valueList = resultMapper.toPOJO(result, mappingClass)

        return findBestValueForPointFromList(valueList.toList(), timeMsec)
    }

    fun close() {
        this._influxdb.close()
    }


    companion object {

        val QUERY_WINDOW = "20s"

        val CPU_QUERY_TEMPLATE = "select time, container_name, usage_total " +
                "from docker_container_cpu " +
                "where cpu = 'cpu-total' " +
                "and container_id = '%s' " +
                "and time > %d - %s and time < %d + %s " +
                "order by time"

        val MEM_QUERY_TEMPLATE = "select time, container_name, usage " +
                "from docker_container_mem " +
                "where container_id = '%s' " +
                "and time > %d - %s and time < %d + %s " +
                "order by time"

        val DISKIO_QUERY_TEMPLATE = "select time, container_name, io_service_bytes_recursive_total " +
                "from docker_container_blkio " +
                "where container_id = '%s' " +
                "and time > %d - %s and time < %d + %s " +
                "order by time"

        val NETIO_QUERY_TEMPLATE = "select time, container_name, rx_bytes, tx_bytes " +
                "from docker_container_net " +
                "where container_id = '%s' " +
                "and time > %d - %s and time < %d + %s " +
                "order by time"

        fun findBestValueForPointFromList(values: List<GenericMeasurement>, pointTimeMillis: Long): Long {
            // This state implies that we didn't get at least two rows back from the database
            // meaning we've asked about a non-existent state (e.g. no such container) or
            // we've asked for a measurement not associated with the container during the requested
            // time (e.g. a container that didn't do disk IO during the period)
            //
            // This ambiguity means that we return 0 here, but at a higher level we need to check for
            // situations with a series of 0 values as possible situations where an invalid query
            // has been issued
            if (values.size < 2) {
                return 0
            }

            val (before, after) = findMeasurementsAroundPointInTime(values, pointTimeMillis)

            return Util.interpolateBetweenPoints(before.timeMillis, before.measurementValue,
                    after.timeMillis, after.measurementValue,
                    pointTimeMillis)
        }


        fun findMeasurementsAroundPointInTime(values: List<GenericMeasurement>, timeMillis: Long): Pair<GenericMeasurement, GenericMeasurement> {
            var before: GenericMeasurement? = null
            var after: GenericMeasurement? = null

            for (i in 0..values.size - 2) {
                if (values[i].timeMillis <= timeMillis && timeMillis <= values[i + 1].timeMillis) {
                    before = values[i]
                    break
                }
            }

            for (i in values.size - 1 downTo 1) {
                if (values[i].timeMillis >= timeMillis && timeMillis >= values[i - 1].timeMillis) {
                    after = values[i]
                    break
                }
            }
            val beforeReturn: GenericMeasurement = before ?: throw IllegalStateException("No before value found for time ${timeMillis}")
            val afterReturn: GenericMeasurement = after ?: throw IllegalStateException("No after value found for time ${timeMillis}")
            return Pair(beforeReturn, afterReturn)
        }
    }
}