package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement
import com.artechra.apollo.types.Util
import org.apache.logging.log4j.LogManager

class ResourceUsageManagerInfluxDbImpl(val influxdb : InfluxDbDecorator) : ResourceUsageManager {

    private val _log = LogManager.getLogger(this::class.qualifiedName)

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMeasurement {

        _log.info("Get resource usage for container $containerId from $startTimeMsec to $endTimeMsec")
        val cpuUsage = getCpuUsage(containerId, startTimeMsec, endTimeMsec)
        assert(cpuUsage >= 0)
        val memUsage = getMemUsage(containerId, startTimeMsec, endTimeMsec)
        assert(memUsage >= 0)
        val diskIo   = getDiskIo(containerId, startTimeMsec, endTimeMsec)
        assert(diskIo >= 0)
        val netIo    = getNetIo(containerId, startTimeMsec, endTimeMsec)
        assert(netIo >= 0)
        // This is a pragmatic decision.  In theory it is possible to query for a valid time interval
        // and get zero back from all of the queries but in practice it seems to be very unlikely as
        // generally no usage means no record is written.  Hence if everything is zero we've probably
        // asked the wrong question (i.e. an invalid container/time combination)
        if (cpuUsage == 0L && memUsage == 0L && diskIo == 0L && netIo == 0L) {
            throw IllegalStateException("Found zero cpu, memory, diskio and netio for container $containerId between $startTimeMsec and $endTimeMsec")
        }
        return ResourceUsageMeasurement(startTimeMsec, containerId, ResourceUsage(cpuUsage, memUsage, diskIo, netIo))
    }

    private fun getCpuUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestCpuMeasureForTime(containerId, startTimeMsec)
        val endTimeEstimate = influxdb.getBestCpuMeasureForTime(containerId, endTimeMsec)
        return endTimeEstimate - startTimeEstimate
    }

    private fun getMemUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestMemMeasureForTime(containerId, startTimeMsec)
        val endTimeEstimate = influxdb.getBestMemMeasureForTime(containerId, endTimeMsec)
        // Memory is different to the other measurements as you are always using memory
        // and the metric is a point in time, not an increasing amount so we return an
        // interpolated estimate of usage, not the difference in value between start and end
        val midPoint = startTimeMsec + ((endTimeMsec - startTimeMsec)/2)
        return Util.interpolateBetweenPoints(startTimeMsec, startTimeEstimate,
                endTimeMsec, endTimeEstimate, midPoint)
    }

    private fun getDiskIo(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestDiskIoMeasureForTime(containerId, startTimeMsec)
        val endTimeEstimate = influxdb.getBestDiskIoMeasureForTime(containerId, endTimeMsec)
        return endTimeEstimate - startTimeEstimate
    }

    private fun getNetIo(containerId: String, startTimeMsec: Long, endTimeMsec: Long) : Long {
        val startTimeEstimate = influxdb.getBestNetIoMeasureForTime(containerId, startTimeMsec)
        val endTimeEstimate = influxdb.getBestNetIoMeasureForTime(containerId, endTimeMsec)
        return endTimeEstimate - startTimeEstimate
    }
}