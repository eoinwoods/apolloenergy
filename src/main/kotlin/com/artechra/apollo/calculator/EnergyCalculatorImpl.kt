package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManager
import com.artechra.apollo.netinfo.NetInfo
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.*
import org.apache.logging.log4j.LogManager

class EnergyCalculatorImpl(val resUsageManager: ResourceUsageManager,
                           val traceManager: TraceManager,
                           val netInfo: NetInfo,
                           val archDesc: ArchitectureManager) : EnergyCalculator {
    val _log = LogManager.getLogger(EnergyCalculatorImpl::class.qualifiedName)

    override fun calculateEnergyForRequests(): Map<String, EnergyEstimate> {
        val traces = traceManager.getTraces()

        val augmentedTraces = addSyntheticSpansForArchitecturalElements(traces, archDesc.getStructure(), netInfo)

        var estimates = HashMap<String, EnergyEstimate>()
        for (t in augmentedTraces) {
            _log.info("Calculating resource usage for trace ${t.root.spanId} (network address ${t.root.networkAddress})")
            val energyEstimate = calculateEnergyForRequest(t, resUsageManager)
            estimates[t.root.traceId] = energyEstimate
        }
        return estimates
    }

    fun addSyntheticSpansForArchitecturalElements(traces : List<Trace>, archElements : ArchitecturalDescription, netInfo: NetInfo) : List<Trace> {
        var retVal = mutableListOf<Trace>()
        var syntheticCounter = 256 ; // Start synthetic ids at 0x100
        for (t in traces) {
            var spans = mutableSetOf<Span>()
            for (s in t.spans) {
                spans.add(s)
                val spanContainerName = netInfo.getNameForContainerAddress(s.networkAddress)
                        ?: throw IllegalStateException("Could not find container name for network address ${s.networkAddress}")
                val element = archElements.findElementByName(spanContainerName)
                        ?: throw IllegalStateException("Could not find architecture element named $spanContainerName")
                val containerNames = getContainerNamesForSpans(t.findChildrenOfSpan(s))
                val subComponentNames = element.subComponents.map {it.name}
                val missingElementNames = subComponentNames.minus(containerNames)
                for (elementName in missingElementNames) {
                    val networkAddress = netInfo.getAddressForContainerName(elementName)
                            ?: throw IllegalStateException("Could not find address for container named $elementName")
                    val syntheticId = (syntheticCounter++).toString(16)
                    val syntheticSpan = Span(s.traceId, syntheticId, networkAddress,
                            s.startTimeMsec, s.endTimeMsec, s.spanId)
                    _log.info("Adding Synthetic Span $syntheticId for $networkAddress ($elementName) in span ${s.spanId}")
                    spans.add(syntheticSpan)
                }
            }
            retVal.add(Trace(spans))
        }
        return retVal
    }

    fun getContainerNamesForSpans(spans : Set<Span>) : List<String> {
        return spans.map{netInfo.getNameForContainerAddress(it.networkAddress) ?: ""}.filter{it.length > 0}
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