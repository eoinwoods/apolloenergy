package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.EnergyUsageManager

class StubEnergyManager : EnergyUsageManager {
    override fun getEnergyUsageForHostInJoules(hostname: String, startTimeMsec: Long, endTimeMsec: Long): Long {
        return 100
    }
    override fun getEnergyUsageForHostForContainerInJoules(containerId: String, startTimeMsec: Long, endTimeMsec: Long): Long {
        return 100
    }
}