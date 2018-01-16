package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import org.junit.Test
import kotlin.test.assertTrue

public class TestInfluxDbResourceManager {
    val resUsageManager: ResourceUsageManager

    init {
        val dbconn = InfluxDbDecorator(IntegrationTestConstants.INFLUX_URL, IntegrationTestConstants.DB_NAME)
        resUsageManager = ResourceUsageManagerInfluxDbImpl(dbconn)
    }

    @Test
    fun testGetResourceUsageForDataSet3ReturnsValidValues() {
        val resUsage = resUsageManager.getResourceUsage(IntegrationTestConstants.CPUHOG_CONTAINER_ID,
                IntegrationTestConstants.SPAN_START_TIME_MS, IntegrationTestConstants.SPAN_END_TIME_MS)
        println("RESUSAGE: ${resUsage}")
        assertTrue(resUsage.usage.totalCpu > 0, "No CPU reported")
        assertTrue(resUsage.usage.totalMemory > 0, "No memory reported")
        assertTrue(resUsage.usage.totalDiskIo > 0, "No Disk IO reported")

    }
}