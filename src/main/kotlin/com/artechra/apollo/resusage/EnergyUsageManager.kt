package com.artechra.apollo.resusage

interface EnergyUsageManager {
    fun getEnergyUsageForHostInJoules(hostName : String, startTimeMsec : Long, endTimeMsec : Long) : Long
}