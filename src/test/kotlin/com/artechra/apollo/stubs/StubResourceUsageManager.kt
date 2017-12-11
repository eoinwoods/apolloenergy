package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class StubResourceUsageManager(val resourceRecords: Map<String, ResourceUsage>) : ResourceUsageManager {

    override fun getResourceUsage(containerId: String, startTime: Long, endTime: Long): ResourceUsageMetric? {
        val rr = resourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID " + containerId)
        return ResourceUsageMetric(System.currentTimeMillis(), containerId, rr)
    }
}