package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class StubResourceUsageManager(val resourceRecords: Map<String, ResourceUsage>) : ResourceUsageManager {

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMeasurement {
        val rr = resourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID " + containerId)
        val rum = ResourceUsageMeasurement(System.currentTimeMillis(), containerId, rr)
        return rum
    }
}