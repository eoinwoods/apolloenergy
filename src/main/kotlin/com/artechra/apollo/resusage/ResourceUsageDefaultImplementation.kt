package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageDefaultImplementation : ResourceUsage {
    override fun getResourceUsage(elementId : ElementIdentifiers, startTime : Long, endTime : Long) : ResourceUsageMetric {
        return ResourceUsageMetric(1234567890, ElementIdentifiers("element100"), 2000, 200, 123, 456)
    }

}