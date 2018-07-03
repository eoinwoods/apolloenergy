package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.EnergyUsageManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToLong

class TraceCalculator(private val resourceUsageMgr : ResourceUsageManager,
                      private val networkMap : NetInfo,
                      private val energyManager : EnergyUsageManager) {
    private val _log = LogManager.getLogger(TraceCalculator::class.qualifiedName)

    private val containers = networkMap.getContainersForAddresses()

    fun calculateCpuMsecAndEnergyJoulesEstimateForTrace(t : Trace) : EnergyEstimate {
        var estimateJoules = 0L
        var totalCpuMsec = 0L
        for (span in t.spans) {
            val containerUsage = calculateResourceUsageForSpan(span)
            totalCpuMsec += containerUsage.usage.totalCpuMsec
            _log.debug("Calculated container usage of ${containerUsage} ")

            val hostCpuMsecUsage = getHostResourceUsageForSpan(span)
            val hostEnergyUsage = getEnergyJoulesForHostForSpan(span)
            _log.debug("Calculated host CPU usage of ${hostCpuMsecUsage} ms and host energy of ${hostEnergyUsage}J")

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

    private fun getEnergyJoulesForHostForSpan(s : Span) : Long {
        val containerId = getContainerForNetworkAddress(s.networkAddress)
        return energyManager.getEnergyUsageForHostForContainerInJoules(containerId, s.startTimeMsec, s.endTimeMsec)
    }

    private fun getContainerForNetworkAddress(address : String) : String {
        return containers[address]
                        ?: throw IllegalStateException("No container found for ipAddr $address")
    }
}