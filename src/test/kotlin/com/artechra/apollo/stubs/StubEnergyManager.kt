package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.EnergyUsageManager

class StubEnergyManager : EnergyUsageManager {
    override fun getEnergyUsageForHostForContainerInJoules(containerId: String, startTimeMsec: Long, endTimeMsec: Long): Long {
        return 100
    }
}