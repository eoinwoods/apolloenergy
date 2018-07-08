package com.artechra.apollo.resusage

interface EnergyUsageManager {
    fun getEnergyUsageForHostInJoules(hostname : String, startTimeMsec : Long, endTimeMsec : Long) : Long
    fun getEnergyUsageForHostForContainerInJoules(containerId : String, startTimeMsec : Long, endTimeMsec : Long) : Long
}