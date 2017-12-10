package com.artechra.apollo.traces

import org.junit.Test
import kotlin.test.assertEquals

class TestTraces {
    @Test
    fun theDefaultImplementationShouldReturnEmptyList() {
        assertEquals(1, TraceManagerDefaultImplementation().getTraces().size)
    }
}