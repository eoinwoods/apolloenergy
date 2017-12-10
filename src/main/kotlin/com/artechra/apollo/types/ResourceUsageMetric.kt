package com.artechra.apollo.types

data class ResourceUsageMetric(val timestamp : Long, val elementId : ElementIdentifiers, val usage : ResourceUsage)