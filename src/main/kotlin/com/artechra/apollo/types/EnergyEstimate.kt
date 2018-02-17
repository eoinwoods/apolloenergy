package com.artechra.apollo.types

data class EnergyEstimate(val totalCpu : Long, val totalMemory : Long, val totalDiskIo : Long, val totalNetIo : Long, val energyUsageJoules : Long) {
    constructor(ru : ResourceUsage, energyUsageJoules: Long) :
        this(ru.totalCpuTicks, ru.totalMemoryBytes, ru.totalDiskIoBytes, ru.totalNetIoBytes, energyUsageJoules)
}