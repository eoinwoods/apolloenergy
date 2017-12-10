package com.artechra.apollo.types

data class Span(val spanId: Int, val elementId: String, val startTime: Long, val endTime: Long, val parent: Span? = null) {
    init {
        if (startTime > endTime) {
            throw IllegalArgumentException("Cannot create span with start time later than end time")
        }
    }
}