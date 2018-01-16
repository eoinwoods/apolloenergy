package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import sun.plugin2.gluegen.runtime.CPU

class InfluxDbDecorator(val dbUrl: String, val dbName: String, val dbUser: String? = null, val dbPassword: String? = null) {


    val influxdb: InfluxDB

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

    init {
        if (dbUser != null && dbUser.length > 0) {
            influxdb = InfluxDBFactory.connect(dbUrl, dbUser, dbPassword)
        } else {
            influxdb = InfluxDBFactory.connect(dbUrl)
        }
    }

    fun getInfluxDbConnection(): InfluxDB {
        return influxdb
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

    private fun getBestMeasureForContainerAtTime(queryTemplate: String, mappingClass : Class<GenericMeasurement>, containerId: String, timeMsec: Long): Long {
        val timeAsNanoSec = Util.msecToNanoSec(timeMsec)
        val query = queryTemplate.format(containerId, timeAsNanoSec, QUERY_WINDOW, timeAsNanoSec, QUERY_WINDOW)
        val dbQuery = Query(query, dbName)
        val result = influxdb.query(dbQuery)

        val resultMapper = InfluxDBResultMapper();
        val valueList = resultMapper.toPOJO(result, mappingClass);

        return findBestValueForPointFromList(valueList.toList(), timeMsec)
    }

    fun close() {
        this.influxdb.close()
    }


    companion object {

        fun findBestValueForPointFromList(values: List<GenericMeasurement>, pointTimeMillis: Long): Long {
            if (values.size < 2) {
                throw IllegalStateException("Cannot find best value for list with less than 2 elements: " + values)
            }

            val (before, after) = findMeasurementsAroundPointInTime(values, pointTimeMillis)

            val bestValueForPoint = Util.interpolateBetweenPoints(before.getTimeMillis(), before.getMeasurementValue(),
                    after.getTimeMillis(), after.getMeasurementValue(),
                    pointTimeMillis)

            return bestValueForPoint
        }


        fun findMeasurementsAroundPointInTime(values: List<GenericMeasurement>, timeMillis: Long): Pair<GenericMeasurement, GenericMeasurement> {
            var before: GenericMeasurement? = null
            var after: GenericMeasurement? = null

            for (i in 0..values.size - 2) {
                if (values[i].getTimeMillis() <= timeMillis && timeMillis <= values[i + 1].getTimeMillis()) {
                    before = values[i]
                    break
                }
            }

            for (i in values.size - 1 downTo 1) {
                if (values[i].getTimeMillis() >= timeMillis && timeMillis >= values[i - 1].getTimeMillis()) {
                    after = values[i]
                    break
                }
            }
            val _before: GenericMeasurement = before ?: throw IllegalStateException("No before value found for time ${timeMillis}")
            val _after: GenericMeasurement = after ?: throw IllegalStateException("No after value found for time ${timeMillis}")
            return Pair(_before, _after)
        }
    }
}