package com.artechra.apollo.traces

import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace

class TraceManagerDefaultImplementation : TraceManager {
    override fun getTraces(): List<Trace> {
        return listOf(Trace(setOf(Span(100, "Trace100", 10000, 20000))))
    }

}