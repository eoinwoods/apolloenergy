package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory

class ResourceUsageManagerInfluxDbImpl(dbUrl : String, dbUser: String?, dbPassword : String?) : ResourceUsageManager {

    val MILLI_TO_MICRO = 1000
    val SECOND_TO_MILLI = 1000000
    val INTERVAL_EXTENSION_SEC = 20L

    val influxdb : InfluxDB

    init {
        if (dbUser != null && dbUser.length > 0) {
            influxdb = InfluxDBFactory.connect(dbUrl, dbUser, dbPassword)
        } else {
            influxdb = InfluxDBFactory.connect(dbUrl)
        }
    }

    override fun getResourceUsage(containerId: String, startTime: Long, endTime: Long): ResourceUsageMetric? {
        val selectPeriodStart = calculatePeriodStart(startTime)
        val selectPeriodEnd = calculatePeriodEnd(endTime)

        val cpuUsage = getCpuUsage(containerId, selectPeriodStart, selectPeriodEnd)
        val memUsage = getMemUsage(containerId, selectPeriodStart, selectPeriodEnd)
        val diskIo   = getDiskIo(containerId, selectPeriodStart, selectPeriodEnd)
        val netIo    = getNetIo(containerId, selectPeriodStart, selectPeriodEnd)
        return ResourceUsageMetric(startTime, containerId, ResourceUsage(cpuUsage, memUsage, diskIo, netIo))
    }

    private fun calculatePeriodStart(startTime: Long) : Long {
        return startTime * MILLI_TO_MICRO - secondsToMicroSeconds(INTERVAL_EXTENSION_SEC)
    }

    private fun calculatePeriodEnd(endTime: Long) : Long {
        return endTime * MILLI_TO_MICRO + secondsToMicroSeconds(INTERVAL_EXTENSION_SEC)
    }

    private fun secondsToMicroSeconds(seconds : Long) : Long {
        return seconds * SECOND_TO_MILLI * MILLI_TO_MICRO
    }

    private fun getCpuUsage(containerId: String, startTime: Long, endTime: Long) : Long {
        return 0
    }

    private fun getMemUsage(containerId: String, startTime: Long, endTime: Long) : Long {
        return 0
    }

    private fun getDiskIo(containerId: String, startTime: Long, endTime: Long) : Long {
        return 0
    }

    private fun getNetIo(containerId: String, startTime: Long, endTime: Long) : Long {
        return 0
    }
}