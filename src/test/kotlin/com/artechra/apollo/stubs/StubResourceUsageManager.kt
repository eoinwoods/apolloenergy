package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class StubResourceUsageManager(val containerResourceRecords: Map<String, ResourceUsage>,
                               val containerToNetworkRecords: Map<String, String>,
                               val hostCpuResourceRecords: Map<String, Long>) : ResourceUsageManager {
    override fun getHostResourceUsageForContainer(containerId: String, startTimeMsec: Long, endTimeMsec: Long): HostResourceMeasurement {
        val rr = hostCpuResourceRecords[containerId] ?: throw IllegalStateException("No host resource record for container " + containerId)
        val host = containerToNetworkRecords[containerId]
        return HostResourceMeasurement(System.currentTimeMillis(), host!!, rr)
    }

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMeasurement {
        val rr = containerResourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID " + containerId)
        return ResourceUsageMeasurement(System.currentTimeMillis(), containerId, rr)
    }
}