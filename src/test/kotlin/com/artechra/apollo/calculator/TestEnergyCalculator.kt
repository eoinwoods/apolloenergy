package com.artechra.apollo.calculator

import com.artechra.apollo.archdesc.ArchitectureManagerDefaultImpl
import com.artechra.apollo.netinfo.NetInfoDefaultImplementation
import com.artechra.apollo.resusage.ResourceUsageManagerDefaultImplementation
import com.artechra.apollo.stubs.StubTraceManager
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import org.junit.Test
import kotlin.test.assertEquals

class TestEnergyCalculator {

    @Test
    fun calculationWithStubElementsShouldResultInSingleResult() {
        val ec = EnergyCalculatorImpl(archDesc = ArchitectureManagerDefaultImpl(),
                                      netInfo = NetInfoDefaultImplementation(),
                                      resUsageManager = ResourceUsageManagerDefaultImplementation(),
                                      traceManager = StubTraceManager(listOf(Trace(setOf(Span(100, "Trace100", 10000, 20000))))))
        val result = ec.calculateEnergyForRequests()
        assertEquals(1, result.size)
        assertEquals("Trace100", result.keys.toTypedArray()[0])
        assertEquals(-1, result["Trace100"])
    }
}