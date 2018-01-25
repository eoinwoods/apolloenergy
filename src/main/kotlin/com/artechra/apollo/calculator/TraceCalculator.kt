package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.Trace

class TraceCalculator(val trace : Trace, val resourceUsageMgr : ResourceUsageManager, val networkMap : NetInfo) {

    fun calculateTotalResourceUsage() : ResourceUsage {
        var totalCpuTicks    = 0L
        var totalMemoryMb    = 0L
        var totalDiskIoBytes = 0L
        var totalNetIoBytes  = 0L

        val containers = networkMap.getContainersForAddresses()
        for (span in trace.spans) {
            val containerId =
                    containers.get(span.networkAddress) ?:
                            throw IllegalStateException("No container found for ipAddr " + span.networkAddress + " in span " + span.spanId)
            val resourceMetrics =
                    resourceUsageMgr.getResourceUsage(containerId, span.startTimeMsec, span.endTimeMsec)
            totalCpuTicks    += resourceMetrics.usage.totalCpu
            totalMemoryMb    += resourceMetrics.usage.totalMemory
            totalDiskIoBytes += resourceMetrics.usage.totalDiskIo
            totalNetIoBytes  += resourceMetrics.usage.totalNetIo
        }
        return ResourceUsage(totalCpuTicks, totalMemoryMb, totalDiskIoBytes, totalNetIoBytes)
    }

}