package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import org.junit.Test

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
        assert(resUsage.usage.totalCpu > 0)
    }
}