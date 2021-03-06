package com.artechra.apollo.types

data class Trace(val name: String, val spans : Set<Span>) {
    val root : Span
    val traceId : String
    val children : Set<Span>
    init {
        root = findRoot(spans) ?: throw IllegalArgumentException("No root span found in Trace: $spans")
        traceId = root.traceId
        children = spans.minus(root)
        if (findRoot(children) != null) {
            throw IllegalArgumentException("Cannot create Trace with more than one root span: $spans")
        }
        if (!validateTimes(spans)) {
            throw IllegalArgumentException("Spans within Trace have start or end times outside root span period: $spans")
        }
    }

    fun getStartTime() : Long {
        return root.startTimeMsec
    }

    fun getEndTime() : Long {
        return root.endTimeMsec
    }

    fun findChildrenOfSpan(s : Span) : Set<Span> {
        return HashSet(children.filter {it.parentId == s.spanId})
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
        val root = findRoot(spans) ?: throw IllegalStateException("Could not find root in Span set: $spans")
        return root.startTimeMsec <= minTime && root.endTimeMsec >= maxTime
    }
}
