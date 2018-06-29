package com.artechra.apollo.types

data class EnergyEstimate(val totalCpuMsec : Long, val energyUsageJoules : Long, val scenarioName : String? = null)