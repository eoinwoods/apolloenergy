package com.artechra.apollo.resusage

import com.artechra.apollo.integration.TestConstants
import com.artechra.apollo.resusage.CpuMeasurement
import org.junit.Test
import kotlin.test.assertEquals

class TestInfluxDbDecorator {

    fun getCpuMeasurements() : MutableList<CpuMeasurement>{
        val ret = mutableListOf<CpuMeasurement>()

        // 4 intervals 10 sec apart
        // example data taken from Docker via data set 3
        ret.add(CpuMeasurement(1515237422000, "gateway", 30305812441))
        ret.add(CpuMeasurement(1515237432000, "gateway", 30318691887))
        ret.add(CpuMeasurement(1515237442000, "gateway", 32183833555))
        ret.add(CpuMeasurement(1515237452000, "gateway", 32231405970))

        return ret
    }

    @Test
    fun testThatFindingBestPointInListFindsMidValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecorator.findMeasurementsAroundPointInTime(getCpuMeasurements(), TestConstants.SPAN_START_TIME_MS)
        assertEquals(1515237432000, before.getTimeMillis())
        assertEquals(1515237442000, after.getTimeMillis())
    }

    @Test
    fun testThatFindingBestPointInListFindsFirstValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecorator.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237422000)
        assertEquals(1515237422000, before.getTimeMillis())
        assertEquals(1515237432000, after.getTimeMillis())
    }

    @Test
    fun testThatFindingBestPointInListFindsLastValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                InfluxDbDecorator.findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237452000)
        assertEquals(1515237442000, before.getTimeMillis())
        assertEquals(1515237452000, after.getTimeMillis())
    }

    @Test
    fun testThatInterpolationOfLongIsValid() {

        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1031L

        assertEquals(4669, InfluxDbDecorator.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationToFirstPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1000L
        assertEquals(4598, InfluxDbDecorator.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationToLastPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1050L
        assertEquals(4712, InfluxDbDecorator.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

}