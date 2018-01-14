package com.artechra.apollo.resusage

import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper

class InfluxDbDecorator(val dbUrl: String, val dbName: String, val dbUser: String? = null, val dbPassword: String? = null) {
    val influxdb: InfluxDB

    val QUERY_WINDOW = "20s"
    val CPU_QUERY_TEMPLATE = "select time, container_name, usage_total " +
                             "from docker_container_cpu " +
                             "where cpu = 'cpu-total' " +
                             "and container_id = '%s' " +
                             "and time > %d - %s and time < %d + %s"

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

    fun getBestCpuMeasureForTime(containerId: String, time: Long): Long {
        //select time, container_name, usage_total from docker_container_cpu
        // where container_id = '5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228'
        // and cpu = 'cpu-total' and time > 1515237435811000000 - 1m and time < 1515237438736327000 + 1m

        val query = CPU_QUERY_TEMPLATE.format(containerId, time, QUERY_WINDOW, time, QUERY_WINDOW)
        println(query)
        val cpuQuery = Query(query, dbName)
        val result = influxdb.query(cpuQuery)

        val resultMapper = InfluxDBResultMapper();
        val cpuList = resultMapper.toPOJO(result, CpuMeasurement::class.java);

        return findBestValueForPointFromList(cpuList, time)
    }

    fun close() {
        this.influxdb.close()
    }

    private fun findBestValueForPointFromList(cpuValues : MutableList<CpuMeasurement>, timeMillis : Long) : Long {
        if (cpuValues.size < 2) {
            throw IllegalStateException("Cannot find best value for list with less than 2 elements: " + cpuValues)
        }

        if (timeMillis < cpuValues[0].time.toEpochMilli()) {
            throw IllegalStateException("Cannot find best value for time " + timeMillis +
                    " smaller than start of value list " + cpuValues[0].time)
        }

        var before : CpuMeasurement?
        var after  : CpuMeasurement?

        var t : Long = 0
        for (v in cpuValues) {
            TODO("CONTINUE FROM HERE TO FIND CONTAINING VALUES")
        }

        return -1
    }


}