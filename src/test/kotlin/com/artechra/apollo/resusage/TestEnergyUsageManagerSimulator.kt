package com.artechra.apollo.resusage

import com.artechra.apollo.stubs.StubInfluxDbDecorator
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEnergyUsageManagerSimulator {

    private val utilisationMetrics = mapOf(1528666358000 to 0.126583,
                                   1528666368000 to 0.5738574,
                                   1528666378000 to 0.9900383583,
                                   1528666388000 to 0.290383,
                                   1528666398000 to 0.02)

    private val influxDb = StubInfluxDbDecorator(utilisationMetrics)

    @Test
    fun testUtilisationIsCredible() {
        val simulator = EnergyUsageManagerSimulator(influxDb)
        val energyJ = simulator.getEnergyUsageForHostForContainerInJoules("0xanycontainerid", 1528666368000, 1528666378000)
        // This value is derived from the wattage values in the simulator plus the usage values
        // above between ... 368... and ...378... with CPU usage between 57% and 99%
        // with a duration from startTime and endTime above (10 seconds)
        assertEquals(2002, energyJ)
    }

    @Test
    fun checkThatLowUtilisationWorksCorrectly() {
        val simulator = EnergyUsageManagerSimulator(influxDb)
        val energyJ = simulator.getEnergyUsageForHostForContainerInJoules("0xanycontainerid", 1528666388000, 1528666398000)
        // This value is derived from the wattage values in the simulator plus the usage values
        // above between ... 368... and ...378... with CPU usage between 29% and 2%
        // with a duration from startTime and endTime above (10 seconds)
        assertEquals(951, energyJ)
    }

}