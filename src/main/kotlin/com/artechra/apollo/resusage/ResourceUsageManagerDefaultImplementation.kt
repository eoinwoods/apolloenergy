package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMeasurement

class ResourceUsageManagerDefaultImplementation : ResourceUsageManager {
    override fun getResourceUsage(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMeasurement {
        return ResourceUsageMeasurement(1234567890, "b38b452428f7", ResourceUsage(2000, 200, 123, 456))
    }

}