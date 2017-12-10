package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import org.junit.Test
import kotlin.test.assertTrue

class TestResourceUsageManager {

    @Test
    fun theNullImplementationShouldReturnAnItem() {
        val resUsage = ResourceUsageManagerDefaultImplementation().getResourceUsage(ElementIdentifiers("element123", containerId = "123"), 100, 200)
        assertTrue(resUsage.usage.totalCpu  + resUsage.usage.totalMemory + resUsage.usage.totalDiskIo + resUsage.usage.totalNetIo > 0)
    }
}