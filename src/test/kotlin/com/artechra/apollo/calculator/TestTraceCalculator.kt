package com.artechra.apollo.calculator

import com.artechra.apollo.stubs.StubNetInfo
import com.artechra.apollo.stubs.StubResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestTraceCalculator {

    @Test(expected = IllegalStateException::class)
    fun trivialTraceShouldReturnRootUsage() {
        val emptyNetInfo = StubNetInfo(emptyMap<String,String>(), emptyMap<String,String>())
        val emptyResourceUsageManager = StubResourceUsageManager(emptyMap<String, ResourceUsage>())
        val nullTrace = Trace(setOf(Span(100, "192.168.1.2:0", 100000, 110000)))
        val tc = TraceCalculator(nullTrace, emptyResourceUsageManager, emptyNetInfo)
        val resUsage = tc.calculateTotalResourceUsage()

        assertEquals("Expected zero usage for empty trace", 0,
                resUsage.totalCpu + resUsage.totalMemory + resUsage.totalDiskIo + resUsage.totalNetIo)
    }
}