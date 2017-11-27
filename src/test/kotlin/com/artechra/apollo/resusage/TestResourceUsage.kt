package com.artechra.apollo.resusage

import org.junit.Test
import kotlin.test.assertTrue

class TestResourceUsage {

    @Test
    fun theNullImplementationShouldReturnAnItem() {
        val resUsage = ResourceUsageDefaultImplementation().getResourceUsage("123", 100, 200)
        assertTrue(resUsage.cpuTicks  + resUsage.memUsage + resUsage.ioBytes + resUsage.networkBytes > 0)
    }
}