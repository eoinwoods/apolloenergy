package com.artechra.apollo.types

import org.junit.Test
import java.lang.IllegalArgumentException

class TestSpan {

    @Test
    fun testMaximalIpAddressIsAccepted() {
        Span("3D5775034F9411B8", "3D5775034F9411B8","255.255.255.255", 100, 200)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnexpectedPortIsRejected() {
        Span("3D5775034F9411B8", "3D5775034F9411B8","255.255.255.255:123", 100, 200)
    }

    @Test
    fun testThatDecimalTraceIdIsRejected() {
        Span("100242", "3D5775034F9411B8","255.123.32.1", 1000, 50654)
    }

    @Test
    fun testThatDecimalSpanIdIsRejected() {
        Span("3D5775034F9411B8", "100242","255.123.32.1", 1000, 50654)
    }

    @Test
    fun testThatLowerAndUpperCaseTraceIdIsAccepted() {
        Span("F59EAF6D7B9C5C49", "3D5775034F9411B8","255.255.255.255", 100, 200)
        Span("f59eaf6d7b9c5c49", "3D5775034F9411B8","255.255.255.255", 100, 200)
    }

    @Test
    fun testThatLowerAndUpperCaseSpanIdIsAccepted() {
        Span("F59EAF6D7B9C5C49", "3D5775034F9411B8","255.255.255.255", 100, 200)
        Span("F59EAF6D7B9C5C49", "3d5775034f9411b8","255.255.255.255", 100, 200)
    }

    @Test
    fun testMinimalIpAddressIsAccepted() {
        Span("3D5775034F9411B8", "3D5775034F9411B8","1.1.1.1", 100, 200)
    }

}