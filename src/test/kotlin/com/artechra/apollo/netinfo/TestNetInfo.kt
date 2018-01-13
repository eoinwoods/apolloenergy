package com.artechra.apollo.netinfo

import org.junit.Test
import kotlin.test.assertEquals

class TestNetInfo {
    @Test
    fun theDefaultImplementationShouldReturnContainerFor19216811() {
        assertEquals("123456789abc", NetInfoDefaultImplementation().getContainersForAddresses()["192.168.1.1:0"])
    }
    @Test
    fun theDefaultImplementationShouldReturnAddressFor123456789abc() {
        assertEquals("192.168.1.1:0", NetInfoDefaultImplementation().getAddressesForContainers()["123456789abc"])
    }
}