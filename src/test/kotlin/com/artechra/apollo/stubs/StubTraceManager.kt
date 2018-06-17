package com.artechra.apollo.stubs

import com.artechra.apollo.traces.TraceManager
import com.artechra.apollo.types.Trace

class StubTraceManager(val dummyTraces : List<Trace>) : TraceManager {
    override fun getTrace(traceId: String): Trace {
        for (t in dummyTraces) {
            if (t.root.spanId.equals(traceId)) {
                return t
            }
        }
        throw IllegalArgumentException("No trace found for ID $traceId")
    }

    override fun getTraces(): List<Trace> {
        return dummyTraces
    }
}