package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.EnergyUsageManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class TraceCalculator(private val resourceUsageMgr : ResourceUsageManager,
                      networkMap : NetInfo,
                      private val energyManager : EnergyUsageManager) {
    private val _log = LogManager.getLogger(TraceCalculator::class.qualifiedName)

    private val containers = networkMap.getContainersForAddresses()

    fun calculateCpuMsecAndEnergyJoulesEstimateForTrace(t : Trace) : EnergyEstimate {
        var estimateJoules = 0L
        var totalCpuMsec = 0L

        val containerIds = t.spans.map {s -> s.networkAddress}.map {a -> getContainerForNetworkAddress(a)}.toSet()
        val containerResourceUsage = containerIds.map { id -> id to resourceUsageMgr.getResourceUsage(id, t.getStartTime(), t.getEndTime())}.toMap()
        val hostResourceUsageByContainer = containerIds.map { id -> id to resourceUsageMgr.getHostResourceUsageForContainer(id, t.getStartTime(), t.getEndTime())}.toMap()
        val hostEnergyUsageByContainer = containerIds.map { id -> id to energyManager.getEnergyUsageForHostForContainerInJoules(id, t.getStartTime(), t.getEndTime())}.toMap()
        val hostList = hostResourceUsageByContainer.values.map{ it -> it.hostname}
        val hostResourceUsage = hostList.map {it -> it to resourceUsageMgr.getHostResourceUsage(it, t.getStartTime(), t.getEndTime())}.toMap()
        val hostEnergyUsage = hostList.map {it -> it to energyManager.getEnergyUsageForHostInJoules(it, t.getStartTime(), t.getEndTime())}.toMap()

        for (id in containerIds) {
            val containerCpuMsec = containerResourceUsage[id]!!.usage.totalCpuMsec
            totalCpuMsec += containerCpuMsec

            val containerHostCpuMsec = hostResourceUsageByContainer[id]!!.cpuUsageMsec

            val containerUsagePercentage = containerCpuMsec /
                    (containerHostCpuMsec*1.0)
            val containerEnergyJ = hostEnergyUsageByContainer[id]!! * containerUsagePercentage
            estimateJoules += containerEnergyJ.roundToLong()
            _log.debug("Container $id traceCpuMsec=$containerCpuMsec hostCpuMsec=$containerHostCpuMsec containerUsage=$containerUsagePercentage containerEnergy=$containerEnergyJ")
        }

        val totalHostMsec = hostResourceUsage.values.map{it -> it.cpuUsageMsec}.reduce{ cpu1, cpu2 -> cpu1 + cpu2}
        val totalHostEnergyJ = hostEnergyUsage.values.reduce{ j1, j2 -> j1+j2 }

        val totalContainerPercentage = (totalCpuMsec / (totalHostMsec * 1.0) * 100).roundToInt()
        return EnergyEstimate(totalCpuMsec, totalHostMsec, totalContainerPercentage, estimateJoules, totalHostEnergyJ, t.name)
    }

    private fun getContainerForNetworkAddress(address : String) : String {
        return containers[address]
                        ?: throw IllegalStateException("No container found for ipAddr $address")
    }
}