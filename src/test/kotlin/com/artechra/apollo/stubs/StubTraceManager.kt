package com.artechra.apollo.stubs

import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.Trace

class StubTraceManager(private val dummyTraces : List<Trace>) : TraceManager {

    override fun getTraces(): List<Trace> {
        return dummyTraces
    }
}