package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import com.artechra.apollo.types.ResourceUsageMetric

interface ResourceUsageManager {
    fun getResourceUsage(elementId : ElementIdentifiers, startTime : Long, endTime : Long) : ResourceUsageMetric
}