package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToLong

class TraceCalculator(private val resourceUsageMgr : ResourceUsageManager, networkMap : NetInfo) {
    private val _log = LogManager.getLogger(TraceCalculator::class.qualifiedName)

    private val containers = networkMap.getContainersForAddresses()

    fun calculateCpuMsecAndEnergyJoulesEstimateForTrace(t : Trace) : EnergyEstimate {
        var estimateJoules = 0L
        var totalCpuMsec = 0L
        for (span in t.spans) {
            val containerUsage = calculateResourceUsageForSpan(span)
            totalCpuMsec += containerUsage.usage.totalCpuMsec

            val hostCpuMsecUsage = getHostResourceUsageForSpan(span)
            val hostEnergyUsage = getEnergyJoulesForHost(span.networkAddress, span.startTimeMsec, span.endTimeMsec)

            assert(containerUsage.usage.totalCpuMsec > 0)
            assert(hostCpuMsecUsage.cpuUsageMsec > 0)
            val containerUsagePercentage = containerUsage.usage.totalCpuMsec / (hostCpuMsecUsage.cpuUsageMsec*1.0)
            val spanEnergyJoules = hostEnergyUsage * containerUsagePercentage
            _log.debug("Span ${span.spanId}: cpuMsec=${containerUsage.usage.totalCpuMsec}; hostCpuMsec=${hostCpuMsecUsage.cpuUsageMsec}; hostEnergyJ=$hostEnergyUsage; usage%=$containerUsagePercentage spanEnergyJ=$spanEnergyJoules")
            estimateJoules += spanEnergyJoules.roundToLong()
        }
        return EnergyEstimate(totalCpuMsec, estimateJoules, t.name)
    }

    private fun calculateResourceUsageForSpan(s : Span) : ResourceUsageMeasurement {
        val containerId = getContainerForNetworkAddress(s.networkAddress)
        return resourceUsageMgr.getResourceUsage(containerId, s.startTimeMsec, s.endTimeMsec)
    }

    private fun getHostResourceUsageForSpan(s : Span) : HostResourceMeasurement {
        val containerId = getContainerForNetworkAddress(s.networkAddress)
        return resourceUsageMgr.getHostResourceUsageForContainer(containerId, s.startTimeMsec, s.endTimeMsec)
    }

    private fun getEnergyJoulesForHost(hostName : String, startTimeMsec : Long, endTimeMsec : Long) : Long {
        println("getEnergyForHost($hostName, $startTimeMsec, $endTimeMsec)")
        // TODO - to be implemented as simulated energy data access
        return 100
    }

    private fun getContainerForNetworkAddress(address : String) : String {
        return containers[address]
                        ?: throw IllegalStateException("No container found for ipAddr $address")
    }
}