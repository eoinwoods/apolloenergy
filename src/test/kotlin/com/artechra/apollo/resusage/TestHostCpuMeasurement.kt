package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
import org.junit.Test

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull

class TestHostCpuMeasurement {

    @Test
    fun testThatDefaultMeasurementIsEmpty() {
        val hcm = HostCpuMeasurement()
        assertNull(hcm.timeMillis)
        assertNull(hcm.hostName)
        assertEquals(0.0, hcm.cpuUsageSeconds)
        assertEquals(0, hcm.cpuUsageMsec)
    }

    @Test
    fun testThatConstructorWorks() {
        val now = System.nanoTime()
        val nowMillis = Util.nanosecToMSec(now)
        val m = HostCpuMeasurement(nowMillis, "host1", 10000)
        assertEquals(nowMillis, m.timeMillis.toEpochMilli())
        assertEquals("host1", m.hostName)
        assertEquals(10.0, m.cpuUsageSeconds)
        assertEquals(10000, m.cpuUsageMsec)
    }

    @Test
    fun testThatSecondsRoundsCorrectlyToMsec() {
        val now = System.nanoTime()
        val nowMillis = Util.nanosecToMSec(now)
        val m = HostCpuMeasurement(nowMillis, "host1", 10000)
        m.cpuUsageSeconds = 172.72000000000003
        assertEquals(172720, m.cpuUsageMsec)
    }

}