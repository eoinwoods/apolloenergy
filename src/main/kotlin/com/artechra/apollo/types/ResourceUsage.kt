package com.artechra.apollo.types

data class ResourceUsage(val totalCpuMsec : Long, val totalMemoryBytes : Long, val totalDiskIoBytes : Long, val totalNetIoBytes : Long)