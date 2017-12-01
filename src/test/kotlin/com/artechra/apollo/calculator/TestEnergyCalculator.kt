package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitecturalDescriptionDefaultImpl
import com.artechra.apollo.netinfo.NetInfoDefaultImplementation
import com.artechra.apollo.resusage.ResourceUsageDefaultImplementation
import com.artechra.apollo.traces.TraceManagerDefaultImplementation
import org.junit.Test
import kotlin.test.assertEquals

class TestEnergyCalculator {


    @Test
    fun calculationWithStubElementsShouldResultInSingleResult() {
        val ec = EnergyCalculatorImpl(archDesc = ArchitecturalDescriptionDefaultImpl(),
                                      netInfo = NetInfoDefaultImplementation(),
                                      resUsage = ResourceUsageDefaultImplementation(),
                                      traceManager = TraceManagerDefaultImplementation())
        val result = ec.calculateEnergyForRequests()
        assertEquals(1, result.size)
        assertEquals("trace1", result.keys.toTypedArray()[0])
        assertEquals(100, result["trace1"])
    }
}