package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsageMetric

interface ResourceUsageManager {
    fun getResourceUsage(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMetric
}