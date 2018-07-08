package com.artechra.apollo.types

data class EnergyEstimate(val traceCpuMsec : Long, val hostCpuMsec : Long, val traceCpuPercentage: Int, val traceEnergyEstimateJ : Long, val hostEnergyEstimateJ :Long, val scenarioName : String? = null)