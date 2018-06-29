package com.artechra.apollo.traces

import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace

class TraceManagerDefaultImplementation : TraceManager {
    private val baseTime = 1515237372000
    private val exampleTrace = Trace("/request1", setOf(Span("54C92796854B15C8", "54C92796854B15C8", "10.10.1.1", baseTime+10000, baseTime+20000)))

    override fun getTraces(): List<Trace> {
        return listOf(exampleTrace)
    }

}