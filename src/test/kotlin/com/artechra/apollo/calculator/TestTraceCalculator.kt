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
        val networkAddr = "192.168.1.2:0"
        val containerId = "abcdefghi123"
        val emptyNetInfo = StubNetInfo(hashMapOf(networkAddr to containerId),
                hashMapOf(containerId to networkAddr))
        val emptyResourceUsageManager = StubResourceUsageManager(hashMapOf(containerId to ResourceUsage(1, 2, 3, 4)))
        val nullTrace = Trace(setOf(Span(100, "192.168.1.2:0", 100000, 110000)))
        val tc = TraceCalculator(nullTrace, emptyResourceUsageManager, emptyNetInfo)
        val resUsage = tc.calculateTotalResourceUsage()
        assertEquals("Wrong CPU total", 1, resUsage.totalCpu)
        assertEquals("Wrong memory total", 2, resUsage.totalMemory)
        assertEquals("Wrong diskio total", 3, resUsage.totalDiskIo)
        assertEquals("Wrong netio total", 4, resUsage.totalNetIo)
    }
}