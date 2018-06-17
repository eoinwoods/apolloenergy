package com.artechra.apollo.calculator

import com.artechra.apollo.types.EnergyEstimate

interface EnergyCalculator {

    fun calculateEnergyForRequests() : Map<String, EnergyEstimate>
}