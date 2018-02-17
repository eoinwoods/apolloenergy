package com.artechra.apollo.types

data class ResourceUsage(val totalCpuTicks : Long, val totalMemoryBytes : Long, val totalDiskIoBytes : Long, val totalNetIoBytes : Long)