package com.artechra.apollo.calculator

import com.artechra.apollo.stubs.StubNetInfo
import com.artechra.apollo.stubs.StubResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestTraceCalculator {

    @Test
    fun trivialTraceShouldReturnRootUsage() {
        val baseTime = 1515237362000
        val networkAddr = "192.168.1.2"
        val containerId = "abcdefghi123"
        val emptyNetInfo = StubNetInfo(hashMapOf(networkAddr to containerId),
                hashMapOf(containerId to networkAddr))
        val emptyResourceUsageManager = StubResourceUsageManager(hashMapOf(containerId to ResourceUsage(1, 2, 3, 4)), hashMapOf())
        val nullTrace = Trace(setOf(Span("54C92796854B15C8","54C92796854B15C8", "192.168.1.2",baseTime+100000, baseTime+110000)))
        val tc = TraceCalculator(nullTrace, emptyResourceUsageManager, emptyNetInfo)
        val resUsage = tc.calculateTotalResourceUsage()
        assertEquals("Wrong CPU total", 1, resUsage.totalCpuTicks)
        assertEquals("Wrong memory total", 2, resUsage.totalMemoryBytes)
        assertEquals("Wrong diskio total", 3, resUsage.totalDiskIoBytes)
        assertEquals("Wrong netio total", 4, resUsage.totalNetIoBytes)
    }
}