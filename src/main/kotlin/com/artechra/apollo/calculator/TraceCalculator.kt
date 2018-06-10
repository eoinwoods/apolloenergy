package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.*
import kotlin.math.roundToLong

class TraceCalculator(val trace : Trace, val resourceUsageMgr : ResourceUsageManager, val networkMap : NetInfo) {
    val containers = networkMap.getContainersForAddresses()

    fun calculateEnergyEstimateForTrace(t : Trace) : Long {
        var estimateJoules = 0L
        for (span in t.spans) {
            val containerUsage = calculateResourceUsageForSpan(span)
            val hostCpuMsecUsage = getHostResourceUsageForSpan(span)
            val hostEnergyUsage = getEnergyJoulesForHost(span.networkAddress, span.startTimeMsec, span.endTimeMsec)

            assert(containerUsage.usage.totalCpuMsec > 0)
            assert(hostCpuMsecUsage.cpuUsageMsec > 0)
            val containerUsagePercentage = containerUsage.usage.totalCpuMsec / (hostCpuMsecUsage.cpuUsageMsec*1.0)
            val spanEnergyJoules = hostEnergyUsage * containerUsagePercentage
            estimateJoules += spanEnergyJoules.roundToLong()
        }
        return estimateJoules
    }

    fun calculateResourceUsageForSpan(s : Span) : ResourceUsageMeasurement {
        val containerId =
                containers.get(s.networkAddress)
                        ?: throw IllegalStateException("No container found for ipAddr " + s.networkAddress + " in span " + s.spanId)
        val resourceMetrics =
                resourceUsageMgr.getResourceUsage(containerId, s.startTimeMsec, s.endTimeMsec)
        return resourceMetrics
    }

    fun getHostResourceUsageForSpan(s : Span) : HostResourceMeasurement {
        return resourceUsageMgr.getHostResourceUsage(s.networkAddress, s.startTimeMsec, s.endTimeMsec)
    }

    fun getEnergyJoulesForHost(hostName : String, startTimeMsec : Long, endTimeMsec : Long) : Long {
        return 100
    }

    fun calculateTotalResourceUsage() : ResourceUsage {
        var totalCpuTicks    = 0L
        var totalMemoryBytes = 0L
        var totalDiskIoBytes = 0L
        var totalNetIoBytes  = 0L

        val containers = networkMap.getContainersForAddresses()
        for (span in trace.spans) {
            val containerId =
                    containers.get(span.networkAddress) ?:
                            throw IllegalStateException("No container found for ipAddr " + span.networkAddress + " in span " + span.spanId)
            val resourceMetrics =
                    resourceUsageMgr.getResourceUsage(containerId, span.startTimeMsec, span.endTimeMsec)
            totalCpuTicks    += resourceMetrics.usage.totalCpuMsec
            totalMemoryBytes    += resourceMetrics.usage.totalMemoryBytes
            totalDiskIoBytes += resourceMetrics.usage.totalDiskIoBytes
            totalNetIoBytes  += resourceMetrics.usage.totalNetIoBytes
        }
        return ResourceUsage(totalCpuTicks, totalMemoryBytes, totalDiskIoBytes, totalNetIoBytes)
    }

}