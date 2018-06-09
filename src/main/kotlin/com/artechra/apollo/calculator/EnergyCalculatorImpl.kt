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

    override fun calculateEnergyForRequests(): Map<String, EnergyEstimate> {
        val traces = traceManager.getTraces()

        var estimates = HashMap<String, EnergyEstimate>()
        for (t in traces) {
            _log.info("Calculating resource usage for trace ${t.root.spanId} (network address ${t.root.networkAddress})")
            val energyEstimate = calculateEnergyForRequest(t, resUsageManager)
            estimates[t.root.traceId] = energyEstimate
        }
        return estimates
    }

    fun calculateEnergyForRequest(t : Trace, resourceUsageMgr: ResourceUsageManager) : EnergyEstimate {
        val tc = TraceCalculator(t, resourceUsageMgr, netInfo)
        val usage = tc.calculateTotalResourceUsage()
        val energyJ = resourceUsageToEnergyWatts(usage.totalCpuTicks, usage.totalMemoryBytes, usage.totalDiskIoBytes, usage.totalNetIoBytes)
        return EnergyEstimate(usage, energyJ)
    }

    fun resourceUsageToEnergyWatts(cpuTicks : Long, memoryMb : Long, diskIoBytes : Long, netIoBytes : Long) : Long {
        // Call the energy model eventually
        _log.info("resourceUsageToEnergy($cpuTicks, $memoryMb, $diskIoBytes, $netIoBytes)")
        return java.util.Random().nextLong()
    }
}