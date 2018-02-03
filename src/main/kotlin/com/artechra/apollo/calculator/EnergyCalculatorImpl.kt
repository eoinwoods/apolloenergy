package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManager
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.Trace
import com.artechra.apollo.traces.TraceManager
import org.apache.logging.log4j.LogManager

class EnergyCalculatorImpl(val resUsageManager: ResourceUsageManager,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitectureManager) : EnergyCalculator {
    val _log = LogManager.getLogger(EnergyCalculatorImpl::class.qualifiedName)

    override fun calculateEnergyForRequests(): Map<String, String> {
        val traces = traceManager.getTraces()

        _log.warn("Ignoring architecture description with ${archDesc.getStructure().elements.size} elements")

        var estimates = HashMap<String, String>()
        for (t in traces) {
            _log.info("Calculating resource usage for trace ${t.root.spanId} (network address ${t.root.networkAddress})")
            val energyEstimate = calculateEnergyForRequest(t, resUsageManager)
            estimates[t.root.traceId] = energyEstimate
        }
        return estimates
    }

    fun calculateEnergyForRequest(t : Trace, resourceUsageMgr: ResourceUsageManager) : String {
        val tc = TraceCalculator(t, resourceUsageMgr, netInfo)
        val usage = tc.calculateTotalResourceUsage()
        return resourceUsageToEnergy(usage.totalCpu, usage.totalMemory, usage.totalDiskIo, usage.totalNetIo)
    }

    fun resourceUsageToEnergy(cpuTicks : Long, memoryMb : Long, diskIoBytes : Long, netIoBytes : Long) : String {
        // Call the energy model eventually
        _log.info("resourceUsageToEnergy($cpuTicks, $memoryMb, $diskIoBytes, $netIoBytes)")
        return "[cpuTicks=$cpuTicks, memoryMbytes=$memoryMb, diskIoBytes=$diskIoBytes, netIoBytes=$netIoBytes]"
    }
}