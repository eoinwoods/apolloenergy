package com.artechra.apollo.resusage

import org.junit.Test
import kotlin.test.assertTrue

class TestResourceUsageManager {

    @Test
    fun theNullImplementationShouldReturnAnItem() {
        val cid = "b38b452428f79542a48a01b786b24bceed82bd2b55650db649bdc622eb27f66a"
        val resUsage = ResourceUsageManagerDefaultImplementation().getResourceUsage(cid, 100, 200)!!
        assertTrue(resUsage.usage.totalCpuTicks  + resUsage.usage.totalMemoryBytes + resUsage.usage.totalDiskIoBytes + resUsage.usage.totalNetIoBytes > 0)
    }
}