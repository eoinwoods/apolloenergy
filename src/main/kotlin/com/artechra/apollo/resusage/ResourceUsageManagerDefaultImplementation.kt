package com.artechra.apollo.resusage

import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class ResourceUsageManagerDefaultImplementation : ResourceUsageManager {
    override fun getHostResourceUsage(hostName: String, startTimeMsec: Long, endTimeMsec: Long): HostResourceMeasurement {
        return HostResourceMeasurement(1234567890, "host1", 43250)
    }

    override fun getResourceUsage(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMeasurement {
        return ResourceUsageMeasurement(1234567890, "b38b452428f7", ResourceUsage(2000, 200, 123, 456))
    }

}