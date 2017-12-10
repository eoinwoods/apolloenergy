package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import com.artechra.apollo.types.ResourceUsageMetric

interface ResourceUsage {
    fun getResourceUsage(elementId : ElementIdentifiers, startTime : Long, endTime : Long) : ResourceUsageMetric
}