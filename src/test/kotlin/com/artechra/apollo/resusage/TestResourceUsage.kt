package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import org.junit.Test
import kotlin.test.assertTrue

class TestResourceUsage {

    @Test
    fun theNullImplementationShouldReturnAnItem() {
        val resUsage = ResourceUsageDefaultImplementation().getResourceUsage(ElementIdentifiers("element123", containerId = "123"), 100, 200)
        assertTrue(resUsage.cpuTicks  + resUsage.memUsage + resUsage.ioBytes + resUsage.networkBytes > 0)
    }
}