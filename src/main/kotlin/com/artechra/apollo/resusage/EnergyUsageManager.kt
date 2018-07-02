package com.artechra.apollo.resusage

interface EnergyUsageManager {
    fun getEnergyUsageForHostForContainerInJoules(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : Long
}