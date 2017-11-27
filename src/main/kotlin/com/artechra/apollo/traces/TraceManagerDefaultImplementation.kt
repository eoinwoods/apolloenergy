package com.artechra.apollo.traces

class TraceManagerDefaultImplementation : TraceManager {
    override fun getRootTraces(): List<Trace> {
        return listOf(Trace(100, "Trace100", 10000, 20000))
    }

    override fun getTraceElements(traceId : Int): List<Trace> {
        return listOf(Trace(200, "Trace200", 15000, 19000),
                        Trace(100, "Trace100", 10000, 20000))
    }

}