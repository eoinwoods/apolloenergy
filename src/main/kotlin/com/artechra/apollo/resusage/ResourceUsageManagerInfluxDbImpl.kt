package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageManagerInfluxDbImpl(influxdb : InfluxDbDecorator) : ResourceUsageManager {

    override fun getResourceUsage(containerId: String, startTimeMillis: Long, endTimeMillis: Long): ResourceUsageMetric? {

        val cpuUsage = getCpuUsage(containerId, startTimeMillis, endTimeMillis)
        val memUsage = getMemUsage(containerId, startTimeMillis, endTimeMillis)
        val diskIo   = getDiskIo(containerId, startTimeMillis, endTimeMillis)
        val netIo    = getNetIo(containerId, startTimeMillis, endTimeMillis)
        return ResourceUsageMetric(startTimeMillis, containerId, ResourceUsage(cpuUsage, memUsage, diskIo, netIo))
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