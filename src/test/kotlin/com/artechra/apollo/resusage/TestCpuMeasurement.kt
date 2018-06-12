package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util

import org.junit.Test

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull

class TestCpuMeasurement {

    @Test
    fun testThatDefaultCpuMeasurementIsEmpty() {
        val m = CpuMeasurement()
        assertNull(m.timeMillis)
        assertNull(m.containerName)
        assertNull(m.hostName)
        assertEquals(0, m.cpuUsageNsec)
    }

    @Test
    fun testThatConstructorWorks() {
        val now = System.nanoTime()
        val nowMillis = Util.nanosecToMSec(now)
        val m = CpuMeasurement(nowMillis, "c1", "host1", 10000)
        assertEquals(nowMillis, m.timeMillis.toEpochMilli())
        assertEquals("c1", m.containerName)
        assertEquals("host1", m.hostName)
        assertEquals(10000, m.cpuUsageNsec)
    }

    @Test
    fun testThatNsecToMsecConverstionWorks() {
        val now = System.nanoTime()
        val nowMillis = Util.nanosecToMSec(now)
        val m = CpuMeasurement(nowMillis, "c1", "host1", 1307154115)
        assertEquals(1307, m.cpuUsageMsec)
    }
}
