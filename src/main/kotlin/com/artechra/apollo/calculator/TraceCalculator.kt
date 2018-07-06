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

    fun calculateResourceUsageForContainer(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMeasurement {
        return resourceUsageMgr.getResourceUsage(containerId, startTimeMsec, endTimeMsec)
    }

    fun calculateCpuMsecAndEnergyJoulesEstimateForTrace(t : Trace) : EnergyEstimate {
        var estimateJoules = 0L
        var totalCpuMsec = 0L
        val containerIds = t.spans.map {s -> s.networkAddress}.map {a -> getContainerForNetworkAddress(a)}

        val containerUsage = containerIds.map {id -> id to calculateResourceUsageForContainer(id, t.getStartTime(), t.getEndTime())}.toMap()
        val hostUsage = containerIds.map {id -> id to resourceUsageMgr.getHostResourceUsageForContainer(id, t.getStartTime(), t.getEndTime())}.toMap()
        val hostEnergy = containerIds.map { id -> id to energyManager.getEnergyUsageForHostForContainerInJoules(id, t.getStartTime(), t.getEndTime())}.toMap()

        for (id in containerIds) {
            val containerCpuMsec = containerUsage[id]!!.usage.totalCpuMsec
            val hostCpuMsecForContainer = hostUsage[id]!!.cpuUsageMsec
            totalCpuMsec += containerCpuMsec
            val containerUsagePercentage = containerCpuMsec /
                    (hostCpuMsecForContainer*1.0)
            val containerEnergyJ = hostEnergy[id]!! * containerUsagePercentage
            estimateJoules += containerEnergyJ.roundToLong()
            _log.debug("Container $id totalCpuMsec=$containerCpuMsec hostCpuMsec=$hostCpuMsecForContainer containerUsage=$containerUsagePercentage containerEnergy=$containerEnergyJ")
        }

        return EnergyEstimate(totalCpuMsec, estimateJoules, t.name)
    }

    private fun getContainerForNetworkAddress(address : String) : String {
        return containers[address]
                        ?: throw IllegalStateException("No container found for ipAddr $address")
    }
}