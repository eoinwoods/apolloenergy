package com.artechra.apollo.traces

import com.artechra.apollo.types.Trace

interface TraceManager {
    fun getTraces(): List<Trace>
}