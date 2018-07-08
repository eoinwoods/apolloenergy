package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class StubResourceUsageManager(private val containerResourceRecords: Map<String, ResourceUsage>,
                               private val hostCpuResourceRecords: Map<String, HostResourceMeasurement>) : ResourceUsageManager {
    override fun getHostResourceUsageForContainer(containerId: String, startTimeMsec: Long, endTimeMsec: Long): HostResourceMeasurement {
        val rr = hostCpuResourceRecords[containerId] ?: throw IllegalStateException("No host resource record for container $containerId")
        return rr
    }

    override fun getHostResourceUsage(hostname: String, startTimeMsec: Long, endTimeMsec: Long): HostResourceMeasurement {
        val rr = hostCpuResourceRecords.values.filter { it.hostname.equals(hostname) }.first()
        return rr
    }

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMeasurement {
        val rr = containerResourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID $containerId")
        return ResourceUsageMeasurement(System.currentTimeMillis(), containerId, rr)
    }
}