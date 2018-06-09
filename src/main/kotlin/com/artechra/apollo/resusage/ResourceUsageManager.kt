package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsageMeasure

interface ResourceUsageManager {
    fun getResourceUsage(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMeasure
}