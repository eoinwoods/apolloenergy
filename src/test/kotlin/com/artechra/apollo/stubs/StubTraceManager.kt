package com.artechra.apollo.stubs

import com.artechra.apollo.traces.Trace
import com.artechra.apollo.traces.TraceManager

class StubTraceManager(val dummyTraces : List<Trace>) : TraceManager {
    override fun getRootTraces(): List<Trace> {
        var roots = ArrayList<Trace>()
        for (t in dummyTraces) {
            if (t.parent == null) {
                roots.add(t)
            }
        }
        return roots
    }

    override fun getTraceElements(traceId: Int): List<Trace> {
        return dummyTraces
    }
}