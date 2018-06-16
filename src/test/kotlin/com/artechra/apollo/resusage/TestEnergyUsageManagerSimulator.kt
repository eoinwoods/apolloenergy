package com.artechra.apollo.resusage

import com.artechra.apollo.stubs.StubInfluxDbDecorator
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEnergyUsageManagerSimulator {

    val utilisationMetrics = mapOf(1528666358000 to 0.126583,
                                   1528666368000 to 0.4238574,
                                   1528666378000 to 0.3800383583,
                                   1528666388000 to 0.290383)

    val influxDb = StubInfluxDbDecorator(utilisationMetrics)

    @Test
    fun testUtilisationIsCredible() {
        val simulator = EnergyUsageManagerSimulator(influxDb)
        val energyJ = simulator.getEnergyUsageForHostInJoules("host1", 1528666368000, 1528666378000)
        // This value is derived from the wattage values in the simulator plus the usage values
        // above between ... 368... and ...378... with CPU usage between 42% and 38%
        // with a duration from startTime and endTime above (10 seconds)
        assertEquals(1380, energyJ)
    }

}