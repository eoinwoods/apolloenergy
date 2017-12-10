package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManager
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.Trace
import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.ResourceUsageMetric

class EnergyCalculatorImpl(val resUsageManager: ResourceUsageManager,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitectureManager) : EnergyCalculator {
    override fun calculateEnergyForRequests(): Map<String, Long> {
        val traces = traceManager.getTraces()

        var estimates = HashMap<String, Long>()
        for (t in traces) {
            val usage = findResourceUsageForRequest(t, resUsageManager)
            val energyEstimate = calculateEnergyForRequest(t, usage)
            estimates[t.root.elementId] = energyEstimate
        }
        return estimates
    }

    fun calculateEnergyForRequest(t : Trace, resourceUsage: Set<ResourceUsageMetric>) : Long {
        val tc = TraceCalculator(t, resourceUsage, archDesc.getStructure())
        val usage = tc.calculateTotalResourceUsage()
        return resourceUsageToEnergy(usage.totalCpu, usage.totalMemory, usage.totalDiskIo, usage.totalNetIo)
    }

    fun findResourceUsageForRequest(t: Trace, resUsageManager: ResourceUsageManager) : Set<ResourceUsageMetric> {
        return emptySet<ResourceUsageMetric>()
    }

    fun resourceUsageToEnergy(cpuTicks : Long, memoryMb : Long, diskIoBytes : Long, netIoBytes : Long) : Long {
        // Call the energy model eventually
        return -1
    }
}