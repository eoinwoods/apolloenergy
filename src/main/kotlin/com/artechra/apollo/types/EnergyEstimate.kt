package com.artechra.apollo.types

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

data class EnergyEstimate(val startTimeMsec : Long, val endTimeMsec : Long, val traceCpuMsec : Long, val hostCpuMsec : Long, val traceCpuPercentage: Int, val traceEnergyEstimateJ : Long, val hostEnergyEstimateJ :Long, val scenarioName : String? = null) {
    val startTimeDateStr = formatter.format(Date(startTimeMsec))
    val endTimeDateStr = formatter.format(Date(endTimeMsec))

    override fun toString() : String {
        return "EnergyEstimate(startTime=$startTimeDateStr, endTime=$endTimeDateStr, " +
                "durationMsec=${endTimeMsec - startTimeMsec}, " +
        "traceCpuMsec=$traceCpuMsec, hostCpuMsec=$hostCpuMsec, traceCpuPercentage=$traceCpuPercentage, " +
        "traceEnergyEstimateJ=$traceEnergyEstimateJ, " +
        "hostEnergyEstimateJ=$hostEnergyEstimateJ, scenarioName=$scenarioName)"
    }
    companion object EnergyEstimate {
        val formatter = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSX")
    }}

