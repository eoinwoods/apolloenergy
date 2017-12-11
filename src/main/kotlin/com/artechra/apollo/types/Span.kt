package com.artechra.apollo.types

data class Span(val spanId: Int, val networkAddress: String, val startTime: Long, val endTime: Long, val parent: Span? = null) {
    val IP_PORT_PATTERN = "[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:[0-9]+"
    init {
        if (startTime > endTime) {
            throw IllegalArgumentException("Cannot create span with start time later than end time")
        }
        if (!networkAddress.matches(Regex(IP_PORT_PATTERN))) {
            throw IllegalArgumentException("Network address must be in format ipaddr:port (e.g. 10.10.10.10:123)")
        }
    }
}