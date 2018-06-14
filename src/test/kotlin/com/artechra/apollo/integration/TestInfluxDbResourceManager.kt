package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import org.junit.Test
import kotlin.test.assertTrue

class TestInfluxDbResourceManager {
    val resUsageManager: ResourceUsageManager
    val CPUHOG_BUSY_START_MSEC   = 1528666452000
    val CPUHOG_BUSY_END_MSEC     = 1528666482000
    val INFLUXDB_BUSY_START_MSEC = 1528666272000
    val INFLUXDB_BUSY_END_MSEC   = 1528666302000

    init {
        val dbconn = InfluxDbDecorator(IntegrationTestShared.INFLUX_URL, IntegrationTestShared.DB_NAME)
        resUsageManager = ResourceUsageManagerInfluxDbImpl(dbconn)
    }

    @Test
    fun testGetResourceUsageForCpuhogInTestDatasetReturnsValidValues() {
        val resUsage = resUsageManager.getResourceUsage(IntegrationTestShared.GATEWAY_CONTAINER_ID,
                CPUHOG_BUSY_START_MSEC, CPUHOG_BUSY_END_MSEC)
        println("RESUSAGE: ${resUsage}")
        assertTrue(resUsage.usage.totalCpuMsec > 0, "No CPU reported")
        assertTrue(resUsage.usage.totalMemoryBytes > 0, "No memory reported")
        assertTrue(resUsage.usage.totalDiskIoBytes >0 , "Unexpected Disk IO reported")
        assertTrue(resUsage.usage.totalNetIoBytes > 0, "No network IO reported")

    }

    @Test
    fun testGetResourceUsageForDatabaseInTestDatasetReturnsValidValues() {
        val resUsage = resUsageManager.getResourceUsage(IntegrationTestShared.INFLUXDB_CONTAINER_ID,
                INFLUXDB_BUSY_START_MSEC, INFLUXDB_BUSY_END_MSEC)
        println("RESUSAGE: ${resUsage}")
        assertTrue(resUsage.usage.totalCpuMsec > 0, "No CPU reported")
        assertTrue(resUsage.usage.totalMemoryBytes > 0, "No memory reported")
        assertTrue(resUsage.usage.totalDiskIoBytes > 0, "No Disk IO reported")
        assertTrue(resUsage.usage.totalNetIoBytes > 0, "No network IO reported")

    }

    @Test
    fun testGetResourceUsageForHostInTestDatasetReturnsValidValues() {
        val resUsage = resUsageManager.getHostResourceUsageForContainer(IntegrationTestShared.GATEWAY_CONTAINER_ID,
                IntegrationTestShared.SPAN_START_TIME_MS, IntegrationTestShared.SPAN_END_TIME_MS)
        println("REUSAGE: ${resUsage}")
        assertTrue(resUsage.cpuUsageMsec > 0, "No CPU reported")
    }

    @Test(expected = IllegalStateException::class)
    fun testGetResourceUsageForNonExistentHostThrowsException() {
        val resUsage = resUsageManager.getHostResourceUsageForContainer("NO_SUCH_CONTAINER",
                IntegrationTestShared.SPAN_START_TIME_MS, IntegrationTestShared.SPAN_END_TIME_MS)
    }

    @Test(expected = IllegalStateException::class)
    fun testGetResourceUsageForNonExistentDataThrowsException() {
        // Shove the interval back 10 minutes to go beyond the start of data set 3
        resUsageManager.getResourceUsage(IntegrationTestShared.GATEWAY_CONTAINER_ID,
                IntegrationTestShared.SPAN_START_TIME_MS - 600*1000,
                 IntegrationTestShared.SPAN_END_TIME_MS - 600*1000)
    }


}