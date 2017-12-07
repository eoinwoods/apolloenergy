package com.artechra.apollo.types

data class Span(val spanId: Int, val elementId: String, val startTime: Long, val endTime: Long, val parent: Span? = null) {
    init {
        if (startTime > endTime) {
            throw IllegalArgumentException("Cannot create span with start time later than end time")
        }
    }
}

data class Trace(val spans : Set<Span>) {
    val root : Span
    val children : Set<Span>
    init {
        root = findRoot(spans) ?: throw IllegalArgumentException("No root span found in Trace: " + spans)
        children = spans.minus(root)
        if (findRoot(children) != null) {
            throw IllegalArgumentException("Cannot create Trace with more than one root span: " + spans)
        }
        if (!validateTimes(spans)) {
            throw IllegalArgumentException("Spans within Trace have start or end times outside root span period: " + spans)
        }
    }

    fun getStartTime() : Long {
        return root.startTime
    }

    fun getEndTime() : Long {
        return root.endTime
    }

    private fun findRoot(spans : Set<Span>) : Span? {
        for (s in spans) {
            if (s.parent == null)
                return s
        }
        return null
    }

    private fun validateTimes(spans : Set<Span>) : Boolean {
        var valid : Boolean = true

        var minTime : Long = Long.MAX_VALUE
        var maxTime : Long = 0

        for (s in spans) {
            minTime = if (minTime < s.startTime) minTime else s.startTime
            maxTime = if (maxTime > s.endTime)   maxTime else s.endTime
        }
        val root = findRoot(spans) ?: throw IllegalStateException("Could not find root in Span set: " + spans)
        return root.startTime <= minTime && root.endTime >= maxTime
    }
}
