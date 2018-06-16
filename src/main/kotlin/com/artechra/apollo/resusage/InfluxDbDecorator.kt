package com.artechra.apollo.resusage

interface InfluxDbDecorator {
    fun getBestCpuMeasureForTime(containerId: String, timeMsec: Long): Long
    fun getBestMemMeasureForTime(containerId: String, timeMsec: Long): Long
    fun getBestDiskIoMeasureForTime(containerId: String, timeMsec: Long): Long
    fun getBestNetIoMeasureForTime(containerId: String, timeMsec: Long): Long
    fun getBestHostCpuMsecMeasureForTime(hostName: String, timeMsec: Long) : Long
    fun getHostForContainerAtTime(containerId: String, timeMsec: Long) : String
    fun getHostCpuUtilisationDuringPeriod(hostName : String, startTimeMsec : Long, endTimeMsec : Long) : Double
    fun getHostCpuCount(hostName : String) : Long
}