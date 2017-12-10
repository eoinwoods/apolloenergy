package com.artechra.apollo.types

data class ResourceUsageMetric(val timestamp : Long, val elementId : ElementIdentifiers, val cpuTicks : Long,
                               val memUsage : Long, val ioBytes : Long, val networkBytes : Long)