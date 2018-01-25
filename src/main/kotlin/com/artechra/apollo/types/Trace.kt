package com.artechra.apollo.types

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
        return root.startTimeMsec
    }

    fun getEndTime() : Long {
        return root.endTimeMsec
    }

    private fun findRoot(spans : Set<Span>) : Span? {
        for (s in spans) {
            if (s.parentId == null)
                return s
        }
        return null
    }

    private fun validateTimes(spans : Set<Span>) : Boolean {
        var minTime : Long = Long.MAX_VALUE
        var maxTime : Long = 0

        for (s in spans) {
            minTime = if (minTime < s.startTimeMsec) minTime else s.startTimeMsec
            maxTime = if (maxTime > s.endTimeMsec)   maxTime else s.endTimeMsec
        }
        val root = findRoot(spans) ?: throw IllegalStateException("Could not find root in Span set: " + spans)
        return root.startTimeMsec <= minTime && root.endTimeMsec >= maxTime
    }
}
