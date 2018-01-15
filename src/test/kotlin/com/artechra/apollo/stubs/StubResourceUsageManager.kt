package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class StubResourceUsageManager(val resourceRecords: Map<String, ResourceUsage>) : ResourceUsageManager {

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMetric {
        val rr = resourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID " + containerId)
        val rum = ResourceUsageMetric(System.currentTimeMillis(), containerId, rr)
        return rum
    }
}