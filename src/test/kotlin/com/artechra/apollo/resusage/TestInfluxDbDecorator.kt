package com.artechra.apollo.resusage

import org.junit.Test
import kotlin.test.assertEquals

class TestInfluxDbDecorator {

    private fun getCpuMeasurements() : MutableList<CpuMeasurement>{
        val ret = mutableListOf<CpuMeasurement>()

        // 4 intervals 10 sec apart
        // example data taken from Docker via data set 3
        ret.add(CpuMeasurement(1515237422000, "gateway", "host1", 30305812441))
        ret.add(CpuMeasurement(1515237432000, "gateway", "host1", 30318691887))
        ret.add(CpuMeasurement(1515237442000, "gateway", "host1", 32183833555))
        ret.add(CpuMeasurement(1515237452000, "gateway", "host1", 32231405970))

        return ret
    }

    @Test
    fun testThatFindingBestPointInListFindsMidValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecoratorImpl.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237437456)
        assertEquals(1515237432000, before.timeMillis)
        assertEquals(1515237442000, after.timeMillis)
    }

    @Test
    fun testThatFindingBestPointInListFindsFirstValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecoratorImpl.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237422000)
        assertEquals(1515237422000, before.timeMillis)
        assertEquals(1515237432000, after.timeMillis)
    }

    @Test
    fun testThatFindingBestPointInListFindsLastValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecoratorImpl.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237452000)
        assertEquals(1515237442000, before.timeMillis)
        assertEquals(1515237452000, after.timeMillis)
    }

    @Test
    fun testThatTwoItemListFindsBestValue() {
        val cpuMeasurements = getCpuMeasurements()
        cpuMeasurements.removeAt(cpuMeasurements.size - 1)
        cpuMeasurements.removeAt(0)

        assertEquals(2, cpuMeasurements.size)
        val(before, after) =
                InfluxDbDecoratorImpl.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237434000)
        assertEquals(1515237432000, before.timeMillis)
        assertEquals(1515237442000, after.timeMillis)
    }

    @Test
    fun testThatCpuUtilisationCalculationReturnsCorrectPercentage() {
        val utilisation = InfluxDbDecoratorImpl.calculateHostCpuUtilisation(1515237432000, 1515237442000,
                10219360, 10259160, 4)
        assertEquals(0.995, utilisation)
    }

}