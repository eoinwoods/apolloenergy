package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageDefaultImplementation : ResourceUsage {
    override fun getResourceUsage(elementId : String, startTime : Long, endTime : Long) : ResourceUsageMetric {
        return ResourceUsageMetric(1234567890, "element100", 2000, 200, 123, 456)
    }

}