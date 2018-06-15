package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
import org.apache.logging.log4j.LogManager
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import kotlin.reflect.KClass

class InfluxDbDecorator(dbUrl: String, private val dbName: String, dbUser: String? = null, dbPassword: String? = null) {

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

        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = CpuMeasurement::class as KClass<GenericMeasurement>
        return getBestMeasureForContainerOrHostAtTime(CPU_QUERY_TEMPLATE, mappingClass, containerId, timeMsec)
    }

    fun getBestMemMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, container_name, usage_total from docker_container_mem
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = MemMeasurement::class as KClass<GenericMeasurement>
        return getBestMeasureForContainerOrHostAtTime(MEM_QUERY_TEMPLATE, mappingClass, containerId, timeMsec)
    }

    fun getBestDiskIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, container_name, io_service_bytes_recursive_total from docker_container_blkio
        // where container_id = 'cd299a7d035895a05937c72ce127459d613ba028e2bb33218892cc3cd201301c'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = DiskIoMeasurement::class as KClass<GenericMeasurement>
        return getBestMeasureForContainerOrHostAtTime(DISKIO_QUERY_TEMPLATE, mappingClass, containerId, timeMsec)
    }

    fun getBestNetIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        // select time, rx_bytes, tx_bytes from docker_container_net
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = NetIoMeasurement::class as KClass<GenericMeasurement>
        return getBestMeasureForContainerOrHostAtTime(NETIO_QUERY_TEMPLATE, mappingClass, containerId, timeMsec)
    }

    fun getBestHostCpuMsecMeasureForTime(hostName: String, timeMsec: Long) : Long {
        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = HostCpuMeasurement::class as KClass<GenericMeasurement>
        return getBestMeasureForContainerOrHostAtTime(HOST_CPU_QUERY_TEMPLATE, mappingClass,
                hostName, timeMsec)
    }

    fun getHostForContainerAtTime(containerId: String, timeMsec: Long) : String {
        @Suppress("UNCHECKED_CAST") // Due to generics not allowing type equivalence
        val mappingClass = CpuMeasurement::class as KClass<GenericMeasurement>
        val cpuMeasures = runInfluxDbQueryForMeasurement(CPU_QUERY_TEMPLATE, mappingClass, containerId, timeMsec )
        if (cpuMeasures.size < 1) {
            throw IllegalStateException("Could not find CPU measurements to extract host from for container $containerId at time $timeMsec")
        }
        val cpuMeasurement = cpuMeasures[0] as CpuMeasurement
        return cpuMeasurement.hostName
    }

    fun getHostCpuUtilisationDuringPeriod(hostName : String, startTimeMsec : Long, endTimeMsec : Long) : Double {
        val hostCpuAtStart = getBestHostCpuMsecMeasureForTime(hostName, startTimeMsec)
        val hostCpuAtEnd = getBestHostCpuMsecMeasureForTime(hostName, endTimeMsec)

        return 0.0
    }

    fun getHostCpuCount(hostName : String) : Long {
        val query = HOST_CPU_COUNT_QUERY_TEMPLATE.format(hostName)
        _log.debug("Querying InfluxDB for host CPU count via query $query")
        val dbQuery = Query(query, dbName)
        val result = _influxdb.query(dbQuery)
        val resultMapper = InfluxDBResultMapper()
        val resultList = resultMapper.toPOJO(result, HostCpuCountMeasurement::class.java)
        if (resultList.size != 1) {
            throw IllegalStateException("No results returned for CPU count for host $hostName (count=${resultList.size})")
        }
        return resultList[0].cpuCount
    }

    private fun getBestMeasureForContainerOrHostAtTime(queryTemplate: String, mappingClass : KClass<GenericMeasurement>, containerOrHostId: String, timeMsec: Long): Long {
        val valueList = runInfluxDbQueryForMeasurement(queryTemplate, mappingClass, containerOrHostId, timeMsec)

        return findBestValueForPointFromList(valueList.toList(), timeMsec)
    }

    private fun runInfluxDbQueryForMeasurement(queryTemplate: String, mappingKClass : KClass<GenericMeasurement>, containerOrHostId: String, timeMsec: Long): List<GenericMeasurement> {
        val mappingClass = mappingKClass.java
        val timeAsNanoSec = Util.msecToNanoSec(timeMsec)
        val query = queryTemplate.format(containerOrHostId, timeAsNanoSec, QUERY_WINDOW, timeAsNanoSec, QUERY_WINDOW)
        _log.debug("Querying InfluxDB for ${mappingClass.name} via query $query")
        val dbQuery = Query(query, dbName)
        val result = _influxdb.query(dbQuery)

        val resultMapper = InfluxDBResultMapper()
        val valueList = resultMapper.toPOJO(result, mappingClass)
        return valueList
    }


    fun close() {
        this._influxdb.close()
    }


    companion object {

        val QUERY_WINDOW = "20s"

        val CPU_QUERY_TEMPLATE = "select time, container_name, host, usage_total " +
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

        val HOST_CPU_QUERY_TEMPLATE = "select time, host, time_active from cpu " +
                "where host = '%s' " +
                "and cpu = 'cpu-total' " +
                "and time > %d - %s and time < %d + %s " +
                "order by time"

        val HOST_CPU_COUNT_QUERY_TEMPLATE = "select n_cpus from system " +
                "where host = '%s' limit 1"

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