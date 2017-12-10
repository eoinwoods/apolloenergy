package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsageMetric

interface ResourceUsage {
    fun getResourceUsage(elementId : String, startTime : Long, endTime : Long) : ResourceUsageMetric
}