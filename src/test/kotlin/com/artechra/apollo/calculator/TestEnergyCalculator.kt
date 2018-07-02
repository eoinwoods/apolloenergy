package com.artechra.apollo.calculator

import com.artechra.apollo.netinfo.NetInfoDefaultImplementation
import com.artechra.apollo.resusage.ResourceUsageManagerDefaultImplementation
import com.artechra.apollo.stubs.StubEnergyManager
import com.artechra.apollo.stubs.StubTraceManager
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestEnergyCalculator {

    @Test
    fun calculationWithStubElementsShouldResultInSingleResult() {
        val baseTime = 1515237362000
        val traceManager = StubTraceManager(listOf(Trace("t1", setOf(Span("54C92796854B15C8", "54C92796854B15C8", "192.168.1.1", baseTime + 10000, baseTime + 20000)))))
        val ec = EnergyCalculatorImpl(
                ResourceUsageManagerDefaultImplementation(),
                traceManager,
                NetInfoDefaultImplementation(),
                StubEnergyManager())
        val result = ec.calculateEnergyForRequests()
        assertEquals(1, result.size)
        assertEquals("54C92796854B15C8", result.keys.toTypedArray()[0])
        assertTrue(result["54C92796854B15C8"]!!.totalCpuMsec > 0)
        assertTrue(result["54C92796854B15C8"]!!.energyUsageJoules > 0)
    }
}