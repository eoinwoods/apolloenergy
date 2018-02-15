package com.artechra.apollo.energymodel

import org.junit.Assert.assertNotEquals
import org.junit.Test

class TestTrivialEnergyModel {

    val CPU_TDP_W = 35.0
    val DISK_ENERGY_W = 1.8
    val DISK_SPEED_MB = 100.0
    val MEMORY_ENERGY_W_MB = 4.0
    val NETWORK_ENERGY_W_MB = 1.1

    val testModel  = TrivialEnergyModel(CPU_TDP_W, DISK_ENERGY_W, DISK_SPEED_MB, MEMORY_ENERGY_W_MB, NETWORK_ENERGY_W_MB)

    @Test
    fun testTrivialModelCalculatesValue() {
        val CPU_USAGE_NANOS = 517609127L
        val DISK_IO_BYTES = 2461477L
        val MEMORY_USAGE_BYTES = 5513495L
        val NETWORK_USAGE_BYTES = 952495L

        val energyValue = testModel.calculateEnergyInJoules(CPU_USAGE_NANOS, DISK_IO_BYTES, MEMORY_USAGE_BYTES, NETWORK_USAGE_BYTES)
        assertNotEquals(0, energyValue)
    }

}