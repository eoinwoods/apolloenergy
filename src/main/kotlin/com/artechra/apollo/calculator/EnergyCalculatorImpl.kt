package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManager
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.Trace
import com.artechra.apollo.traces.TraceManager

class EnergyCalculatorImpl(val resUsageManager: ResourceUsageManager,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitectureManager) : EnergyCalculator {
    override fun calculateEnergyForRequests(): Map<String, Long> {
        val traces = traceManager.getTraces()

        var estimates = HashMap<String, Long>()
        for (t in traces) {
            val energyEstimate = calculateEnergyForRequest(t, resUsageManager)
            estimates[t.root.networkAddress] = energyEstimate
        }
        return estimates
    }

    fun calculateEnergyForRequest(t : Trace, resourceUsageMgr: ResourceUsageManager) : Long {
        val tc = TraceCalculator(t, resourceUsageMgr, netInfo)
        val usage = tc.calculateTotalResourceUsage()
        return resourceUsageToEnergy(usage.totalCpu, usage.totalMemory, usage.totalDiskIo, usage.totalNetIo)
    }

    fun resourceUsageToEnergy(cpuTicks : Long, memoryMb : Long, diskIoBytes : Long, netIoBytes : Long) : Long {
        // Call the energy model eventually
        return -1
    }
}