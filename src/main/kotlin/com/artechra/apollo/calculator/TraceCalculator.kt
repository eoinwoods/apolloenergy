package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ArchitecturalDescription
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
            val containerId = containers.get(ipAddrFromNetworkAddress(span.networkAddress))
            if (containerId == null) {
                System.err.println("No container found for ipAddr " + ipAddrFromNetworkAddress(span.networkAddress) + " in span " + span.spanId)
                continue
            }
            val resourceMetrics =
                    resourceUsageMgr.getResourceUsage(containerId, span.startTime, span.endTime) ?:
                        throw IllegalStateException("Could not find resource usage for container " + containerId +
                                                    " between " + span.startTime + " and " + span.endTime)
            totalCpuTicks    += resourceMetrics.usage.totalCpu
            totalMemoryMb    += resourceMetrics.usage.totalMemory
            totalDiskIoBytes += resourceMetrics.usage.totalDiskIo
            totalMemoryMb    += resourceMetrics.usage.totalNetIo
        }
        return ResourceUsage(totalCpuTicks, totalMemoryMb, totalDiskIoBytes, totalNetIoBytes)
    }

    private fun ipAddrFromNetworkAddress(networkAddress : String) : String {
        return networkAddress.split(":")[0]
    }
    private fun portFromNetworkAddress(networkAddress : String) : String {
        return networkAddress.split(":")[1]
    }

}