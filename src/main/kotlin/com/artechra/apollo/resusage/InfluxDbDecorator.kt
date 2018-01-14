package com.artechra.apollo.resusage

import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import kotlin.math.roundToLong

class InfluxDbDecorator(val dbUrl: String, val dbName: String, val dbUser: String? = null, val dbPassword: String? = null) {

    val MSEC_TO_NANOSEC_MULTIPLIER = 1000000L

    val influxdb: InfluxDB

    val QUERY_WINDOW = "20s"
    val CPU_QUERY_TEMPLATE = "select time, container_name, usage_total " +
            "from docker_container_cpu " +
            "where cpu = 'cpu-total' " +
            "and container_id = '%s' " +
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
        //select time, container_name, usage_total from docker_container_cpu
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and cpu = 'cpu-total' and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        val timeAsNanoSec = msecToNanoSec(timeMsec)
        val query = CPU_QUERY_TEMPLATE.format(containerId, timeAsNanoSec, QUERY_WINDOW, timeAsNanoSec, QUERY_WINDOW)
        val cpuQuery = Query(query, dbName)
        val result = influxdb.query(cpuQuery)

        val resultMapper = InfluxDBResultMapper();
        val cpuList = resultMapper.toPOJO(result, CpuMeasurement::class.java);

        return findBestValueForPointFromList(cpuList, timeMsec)
    }

    fun close() {
        this.influxdb.close()
    }

    fun findBestValueForPointFromList(cpuValues: MutableList<CpuMeasurement>, pointTimeMillis: Long): Long {
        if (cpuValues.size < 2) {
            throw IllegalStateException("Cannot find best value for list with less than 2 elements: " + cpuValues)
        }

        val (before, after) = findMeasurementsAroundPointInTime(cpuValues, pointTimeMillis)

        val estimatedCpuTime = interpolateBetweenPoints(before.getTimeMillis(), before.getCpuUsage(),
                                                              after.getTimeMillis(), after.getCpuUsage(),
                                                              pointTimeMillis)

        return estimatedCpuTime
    }

    fun interpolateBetweenPoints(point1 : Long, value1 : Long, point2 : Long, value2 : Long, requiredPoint : Long) : Long {
        val timeDiff = point2 - point1
        val valueDiff = value2 - value1

        val delta = valueDiff/timeDiff.toDouble()

        val requiredValue = value1 + ((requiredPoint - point1) * delta).roundToLong()
        return requiredValue
    }

    fun findMeasurementsAroundPointInTime(cpuValues: MutableList<CpuMeasurement>, timeMillis: Long): Pair<CpuMeasurement, CpuMeasurement> {
        var before: CpuMeasurement? = null
        var after: CpuMeasurement? = null

        for (i in 0..cpuValues.size - 2) {
            if (cpuValues[i].getTimeMillis() <= timeMillis && timeMillis <= cpuValues[i + 1].getTimeMillis()) {
                before = cpuValues[i]
                break
            }
        }

        for (i in cpuValues.size - 1 downTo 1) {
            if (cpuValues[i].getTimeMillis() >= timeMillis && timeMillis >= cpuValues[i - 1].getTimeMillis()) {
                after = cpuValues[i]
                break
            }
        }
        val _before: CpuMeasurement = before ?: throw IllegalStateException("No before value found for time ${timeMillis}")
        val _after: CpuMeasurement = after ?: throw IllegalStateException("No after value found for time ${timeMillis}")
        return Pair(_before, _after)
    }

    fun msecToNanoSec(msec : Long) : Long {
        return msec * MSEC_TO_NANOSEC_MULTIPLIER
    }


}