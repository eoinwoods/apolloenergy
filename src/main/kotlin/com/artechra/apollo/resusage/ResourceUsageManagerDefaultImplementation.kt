package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageManagerDefaultImplementation : ResourceUsageManager {
    override fun getResourceUsage(containerId : String, startTime : Long, endTime : Long) : ResourceUsageMetric {
        return ResourceUsageMetric(1234567890, "b38b452428f7", ResourceUsage(2000, 200, 123, 456))
    }

}