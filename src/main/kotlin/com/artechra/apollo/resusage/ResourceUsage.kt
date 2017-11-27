package com.artechra.apollo.resusage

data class ResourceUsageMetric(val timestamp : Long, val elementId : String, val cpuTicks : Long,
                               val memUsage : Long, val ioBytes : Long, val networkBytes : Long)

interface ResourceUsage {
    fun getResourceUsage(elementId : String, startTime : Long, endTime : Long) : ResourceUsageMetric
}