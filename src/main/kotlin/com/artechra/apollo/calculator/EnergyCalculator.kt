package com.artechra.apollo.calculator

interface EnergyCalculator {

    fun calculateEnergyForRequests() : Map<String,String>
}