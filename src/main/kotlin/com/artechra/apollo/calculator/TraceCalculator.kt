package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.Trace

class TraceCalculator(val trace : Trace, val resourceUsageMgr : ResourceUsageManager, val networkMap : NetInfo) {

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
            totalCpuTicks    += resourceMetrics.usage.totalCpuTicks
            totalMemoryBytes    += resourceMetrics.usage.totalMemoryBytes
            totalDiskIoBytes += resourceMetrics.usage.totalDiskIoBytes
            totalNetIoBytes  += resourceMetrics.usage.totalNetIoBytes
        }
        return ResourceUsage(totalCpuTicks, totalMemoryBytes, totalDiskIoBytes, totalNetIoBytes)
    }

}