package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToLong

class TraceCalculator(val resourceUsageMgr : ResourceUsageManager, val networkMap : NetInfo) {
    val _log = LogManager.getLogger(TraceCalculator::class.qualifiedName)

    val containers = networkMap.getContainersForAddresses()

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
        return EnergyEstimate(totalCpuMsec, estimateJoules)
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
        // TODO - to be implemented as simulated energy data access
        return 100
    }
}