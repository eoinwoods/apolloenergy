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
        val baseTime = 1515237362000
        val ec = EnergyCalculatorImpl(archDesc = ArchitectureManagerDefaultImpl(),
                                      netInfo = NetInfoDefaultImplementation(),
                                      resUsageManager = ResourceUsageManagerDefaultImplementation(),
                                      traceManager = StubTraceManager(listOf(Trace(setOf(Span("54C92796854B15C8", "54C92796854B15C8","192.168.1.1", baseTime+10000, baseTime+20000))))))
        val result = ec.calculateEnergyForRequests()
        assertEquals(1, result.size)
        assertEquals("192.168.1.1", result.keys.toTypedArray()[0])
        assertEquals(-1, result["192.168.1.1"])
    }
}