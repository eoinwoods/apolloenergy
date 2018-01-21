package com.artechra.apollo.types

data class Span(internal val spanId: String, val networkAddress: String, val startTime: Long, val endTime: Long, val parent: Span? = null) {
    val IP_V4_PATTERN = "[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}"
    val HEX_PATTERN = "[0-9ABCDEFabcdef]+"
    init {
        if (!spanId.matches(Regex(HEX_PATTERN))) {
            throw IllegalArgumentException("Expected hexadecimal span ID not '${spanId}'")
        }
        if (startTime > endTime) {
            throw IllegalArgumentException("Cannot create span with start time later than end time")
        }
        if (!networkAddress.matches(Regex(IP_V4_PATTERN))) {
            throw IllegalArgumentException("Network address must be IPV4 format (e.g. 10.10.10.10)")
        }
    }
}