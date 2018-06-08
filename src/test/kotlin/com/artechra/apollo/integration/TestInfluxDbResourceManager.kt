package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import org.junit.Test
import kotlin.test.assertTrue

class TestInfluxDbResourceManager {
    val resUsageManager: ResourceUsageManager

    init {
        val dbconn = InfluxDbDecorator(IntegrationTestShared.INFLUX_URL, IntegrationTestShared.DB_NAME)
        resUsageManager = ResourceUsageManagerInfluxDbImpl(dbconn)
    }

    @Test
    fun testGetResourceUsageForCpuhogInDataSet3ReturnsValidValues() {
        val resUsage = resUsageManager.getResourceUsage(IntegrationTestShared.GATEWAY_CONTAINER_ID,
                IntegrationTestShared.SPAN_START_TIME_MS, IntegrationTestShared.SPAN_END_TIME_MS)
        println("RESUSAGE: ${resUsage}")
        assertTrue(resUsage.usage.totalCpuTicks > 0, "No CPU reported")
        assertTrue(resUsage.usage.totalMemoryBytes > 0, "No memory reported")
        // This container has no disk io during the specified period (hence the test case)
        assertTrue(resUsage.usage.totalDiskIoBytes == 0L, "Unexpected Disk IO reported")
        assertTrue(resUsage.usage.totalNetIoBytes > 0, "No network IO reported")

    }

    @Test
    fun testGetResourceUsageForDatbaseInDataSet3ReturnsValidValues() {
        val resUsage = resUsageManager.getResourceUsage(IntegrationTestShared.INFLUXDB_CONTAINER_ID,
                IntegrationTestShared.SPAN_START_TIME_MS, IntegrationTestShared.SPAN_END_TIME_MS)
        println("RESUSAGE: ${resUsage}")
        assertTrue(resUsage.usage.totalCpuTicks > 0, "No CPU reported")
        assertTrue(resUsage.usage.totalMemoryBytes > 0, "No memory reported")
        assertTrue(resUsage.usage.totalDiskIoBytes > 0, "Unexpected Disk IO reported")
        assertTrue(resUsage.usage.totalNetIoBytes > 0, "No network IO reported")

    }

    @Test(expected = IllegalStateException::class)
    fun testGetResourceUsageForNonExistentDataThrowsException() {
        // Shove the interval back 10 minutes to go beyond the start of data set 3
        resUsageManager.getResourceUsage(IntegrationTestShared.GATEWAY_CONTAINER_ID,
                IntegrationTestShared.SPAN_START_TIME_MS - 600*1000,
                 IntegrationTestShared.SPAN_END_TIME_MS - 600*1000)
    }


}