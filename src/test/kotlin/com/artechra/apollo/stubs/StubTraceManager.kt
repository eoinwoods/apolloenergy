package com.artechra.apollo.stubs

import com.artechra.apollo.types.Trace
import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.Span

class StubTraceManager(val dummyTraces : List<Trace>) : TraceManager {
    override fun getTraces(): List<Trace> {
        return dummyTraces
    }
}