package com.artechra.apollo.types

data class ResourceUsageMeasurement(val timestamp : Long, val containerId : String, val usage : ResourceUsage)