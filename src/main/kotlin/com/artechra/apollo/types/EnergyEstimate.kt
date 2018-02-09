package com.artechra.apollo.types

data class EnergyEstimate(val totalCpu : Long, val totalMemory : Long, val totalDiskIo : Long, val totalNetIo : Long, val energyUsageWatts : Long) {
    constructor(ru : ResourceUsage, energyUsageWatts: Long) :
        this(ru.totalCpu, ru.totalMemory, ru.totalDiskIo, ru.totalNetIo, energyUsageWatts)
}