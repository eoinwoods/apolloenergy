package com.artechra.apollo.calculator

import com.artechra.apollo.stubs.StubEnergyManager
import com.artechra.apollo.stubs.StubNetInfo
import com.artechra.apollo.stubs.StubResourceUsageManager
import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestTraceCalculator {

    @Test
    fun trivialTraceShouldReturnRootSpanEstimate() {
        val baseTime = 1515237362000
        val networkAddr = "192.168.1.2"
        val containerId = "abcdefghi123"
        val testNetInfo = StubNetInfo(hashMapOf(networkAddr to containerId),
                hashMapOf(containerId to networkAddr))
        val testResourceUsageManager = StubResourceUsageManager(hashMapOf(containerId to ResourceUsage(12, 2, 3, 4)),
                hashMapOf(containerId to HostResourceMeasurement(baseTime, networkAddr, 123L)))
        val trivialTrace = Trace("t2",setOf(Span( "54C92796854B15C8","54C92796854B15C8", "192.168.1.2",baseTime+100000, baseTime+110000)))
        val tc = TraceCalculator(testResourceUsageManager, testNetInfo, StubEnergyManager())
        val estimate = tc.calculateCpuMsecAndEnergyJoulesEstimateForTrace(trivialTrace)
        assertEquals(10, estimate.traceEnergyEstimateJ)
        assertEquals(12, estimate.traceCpuMsec)

    }
}