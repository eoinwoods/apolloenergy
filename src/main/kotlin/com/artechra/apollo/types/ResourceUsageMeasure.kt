package com.artechra.apollo.types

data class ResourceUsageMeasure(val timestamp : Long, val containerId : String, val usage : ResourceUsage)