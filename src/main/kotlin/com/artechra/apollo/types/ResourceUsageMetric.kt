package com.artechra.apollo.types

data class ResourceUsageMetric(val timestamp : Long, val containerId : String, val usage : ResourceUsage)