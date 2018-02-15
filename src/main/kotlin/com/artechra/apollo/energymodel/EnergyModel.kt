package com.artechra.apollo.energymodel

interface EnergyModel {
    fun calculateEnergyInJoules(cpuUsageNanos : Long, memUsageMb : Long, diskIoBytes : Long, netIoBytes : Long) : Long
}