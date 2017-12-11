package com.artechra.apollo.resusage

import com.artechra.apollo.types.ElementIdentifiers
import com.artechra.apollo.types.ResourceUsageMetric

interface ResourceUsageManager {
    fun getResourceUsage(containerId : String, startTime : Long, endTime : Long) : ResourceUsageMetric?
}