package com.artechra.apollo.types

data class Span(val traceId : String, val spanId: String, val networkAddress: String, val startTimeMsec: Long, val endTimeMsec: Long, var parentId: String? = null) {
    val IP_V4_PATTERN = "[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}"
    val HEX_PATTERN = "[0-9ABCDEFabcdef]+"
    val MSEC_DIGITS = 13
    init {
        if (!traceId.matches(Regex(HEX_PATTERN))) {
            throw IllegalArgumentException("Expected hexadecimal trace ID not '${traceId}'")
        }
        if (!spanId.matches(Regex(HEX_PATTERN))) {
            throw IllegalArgumentException("Expected hexadecimal span ID not '${spanId}'")
        }
        if (startTimeMsec > endTimeMsec) {
            throw IllegalArgumentException("Cannot create span with start time later than end time")
        }
        if (!networkAddress.matches(Regex(IP_V4_PATTERN))) {
            throw IllegalArgumentException("Span network address must be IPV4 format (e.g. 10.10.10.10)")
        }
        if (("" + startTimeMsec).length != 13) {
            throw IllegalArgumentException("Span start time must be specified in msec")
        }
        if (("" + endTimeMsec).length != 13) {
            throw IllegalArgumentException("Span end time must be specified in msec")
        }
    }
}