package com.artechra.apollo.resusage;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class TestCpuMeasurement {

    @Test
    public void testThatDefaultCpuMeasurementIsEmpty() {
        CpuMeasurement m = new CpuMeasurement() ;
        assertNull(m.timeMillis) ;
        assertNull(m.containerName) ;
        assertEquals(0, m.cpuUsage) ;
    }

    @Test
    public void testThatConstructorWorks() {
        long now = System.nanoTime();
        long nowMillis = now / 1000 ;
        CpuMeasurement m = new CpuMeasurement(now, "c1", 10000) ;
        assertEquals(nowMillis, m.timeMillis.toEpochMilli()) ;
        assertEquals("c1", m.containerName) ;
        assertEquals(10000, m.cpuUsage);
    }
}
