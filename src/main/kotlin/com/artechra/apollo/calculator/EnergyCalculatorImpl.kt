package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager

class EnergyCalculatorImpl(val resUsageManager: ResourceUsageManager,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo) : EnergyCalculator {
    val _log = LogManager.getLogger(EnergyCalculatorImpl::class.qualifiedName)
    val tc = TraceCalculator(resUsageManager, netInfo)

    override fun calculateEnergyForRequests(): Map<String, EnergyEstimate> {
        val traces = traceManager.getTraces()

        var estimates = HashMap<String, EnergyEstimate>()
        for (t in traces) {
            _log.info("Calculating resource usage for trace ${t.root.spanId} (network address ${t.root.networkAddress})")
            val energyEstimate =  tc.calculateCpuMsecAndEnergyJoulesEstimateForTrace(t)
            estimates[t.root.traceId] = energyEstimate
        }
        return estimates
    }
}