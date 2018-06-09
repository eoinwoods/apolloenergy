package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.ResourceUsageManager
import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class StubResourceUsageManager(val containerResourceRecords: Map<String, ResourceUsage>,
                               val hostCpuResourceRecords: Map<String, Long>) : ResourceUsageManager {
    override fun getHostResourceUsage(hostName: String, startTimeMsec: Long, endTimeMsec: Long): HostResourceMeasurement {
        val rr = hostCpuResourceRecords[hostName] ?: throw IllegalStateException("No resource record for host " + hostName)
        val hrm = HostResourceMeasurement(System.currentTimeMillis(), hostName, rr)
        return hrm
    }

    override fun getResourceUsage(containerId: String, startTimeMsec: Long, endTimeMsec: Long): ResourceUsageMeasurement {
        val rr = containerResourceRecords[containerId] ?: throw IllegalStateException("No resource record for CID " + containerId)
        val rum = ResourceUsageMeasurement(System.currentTimeMillis(), containerId, rr)
        return rum
    }
}