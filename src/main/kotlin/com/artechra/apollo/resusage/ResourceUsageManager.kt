package com.artechra.apollo.resusage

import com.artechra.apollo.types.HostResourceMeasurement
import com.artechra.apollo.types.ResourceUsageMeasurement

interface ResourceUsageManager {
    fun getResourceUsage(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : ResourceUsageMeasurement
    fun getHostResourceUsage(hostname: String, startTimeMsec : Long, endTimeMsec : Long) : HostResourceMeasurement
    fun getHostResourceUsageForContainer(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : HostResourceMeasurement
}