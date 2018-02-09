package com.artechra.apollo.calculator

import com.artechra.apollo.types.EnergyEstimate
import com.artechra.apollo.types.ResourceUsage

interface EnergyCalculator {

    fun calculateEnergyForRequests() : Map<String, EnergyEstimate>
}