package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitecturalDescription
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsage
import com.artechra.apollo.traces.TraceManager

class EnergyCalculatorImpl(val resUsage: ResourceUsage,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitecturalDescription) : EnergyCalculator {
    override fun calculateEnergyForRequests(): Map<String, Long> {
        val traces = traceManager.getRootTraces()
        for (t in traces) {
            //TODO("not implemented")
        }
        // Dummy return to fail tests
        return hashMapOf("trace1" to -1L)
    }
}