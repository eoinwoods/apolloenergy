package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageManagerInfluxDbImpl(val influxdb : InfluxDbDecorator) : ResourceUsageManager {

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMetric {

        val cpuUsage = getCpuUsage(containerId, startTimeMsec, endTimeMsec)
        val memUsage = getMemUsage(containerId, startTimeMsec, endTimeMsec)
        val diskIo   = getDiskIo(containerId, startTimeMsec, endTimeMsec)
        val netIo    = getNetIo(containerId, startTimeMsec, endTimeMsec)
        return ResourceUsageMetric(startTimeMsec, containerId, ResourceUsage(cpuUsage, memUsage, diskIo, netIo))
    }

    private fun getCpuUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestCpuMeasureForTime(containerId, startTimeMsec) ;
        val endTimeEstimate = influxdb.getBestCpuMeasureForTime(containerId, endTimeMsec) ;
        return endTimeEstimate - startTimeEstimate
    }

    private fun getMemUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestMemMeasureForTime(containerId, startTimeMsec) ;
        val endTimeEstimate = influxdb.getBestMemMeasureForTime(containerId, endTimeMsec) ;
        return endTimeEstimate - startTimeEstimate
    }

    private fun getDiskIo(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestDiskIoMeasureForTime(containerId, startTimeMsec) ;
        val endTimeEstimate = influxdb.getBestDiskIoMeasureForTime(containerId, endTimeMsec) ;
        return endTimeEstimate - startTimeEstimate
    }

    private fun getNetIo(containerId: String, startTime: Long, endTime: Long) : Long {
        return 0
    }
}