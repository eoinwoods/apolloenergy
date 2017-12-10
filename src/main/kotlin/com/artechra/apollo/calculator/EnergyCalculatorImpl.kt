package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManager
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsage
import com.artechra.apollo.traces.Trace
import com.artechra.apollo.traces.TraceManager

class EnergyCalculatorImpl(val resUsage: ResourceUsage,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitectureManager) : EnergyCalculator {
    override fun calculateEnergyForRequests(): Map<String, Long> {
        val traces = traceManager.getRootTraces()

        var estimates = HashMap<String, Long>()
        for (t in traces) {
            assert(t.parent == null)
            val energyEstimate = calculateEnergyForRequest(t)
            estimates[t.elementId] = energyEstimate
        }
        return estimates
    }

    fun calculateEnergyForRequest(t : Trace) : Long {
        return -1
    }
}