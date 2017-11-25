package com.artechra.apollo.traces

class TraceManagerDefaultImplementation : TraceManager {
    override fun getRootTraces(): List<Trace> {
        return emptyList()
    }

    override fun getTraceElements(traceId : Int): List<Trace> {
        return emptyList()
    }

}