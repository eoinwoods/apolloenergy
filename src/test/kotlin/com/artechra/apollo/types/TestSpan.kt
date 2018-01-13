package com.artechra.apollo.types

import org.junit.Test
import java.lang.IllegalArgumentException

class TestSpan {

    @Test
    fun testMaximalIpAddressIsAccepted() {
        Span(1, "255.255.255.255:100", 100, 200)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMissingPortIsRejected() {
        Span(1, "255.255.255.255", 100, 200)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMissingPortWithSeparatorIsRejected() {
        Span(1, "255.255.255.255:", 100, 200)
    }

    @Test
    fun testMinimalIpAddressIsAccepted() {
        Span(1, "1.1.1.1:0", 100, 200)
    }

}