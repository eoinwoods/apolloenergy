package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric

class ResourceUsageManagerDefaultImplementation : ResourceUsageManager {
    override fun getResourceUsage(elementId : ElementIdentifiers, startTime : Long, endTime : Long) : ResourceUsageMetric {
        return ResourceUsageMetric(1234567890, ElementIdentifiers("element100"), ResourceUsage(2000, 200, 123, 456))
    }

}