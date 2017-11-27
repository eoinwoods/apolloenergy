package com.artechra.apollo.traces

data class Trace(val traceId: Int, val elementId: String, val startTime: Long, val endTime: Long, val parent: Trace? = null)

interface TraceManager {
    fun getRootTraces() : List<Trace>
    fun getTraceElements(traceId : Int) : List<Trace>
}