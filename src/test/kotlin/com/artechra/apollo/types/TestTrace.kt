package com.artechra.apollo.types

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestTrace {
    val root  = Span(1, "rootSpan", 0, 500)
    val span1 = Span(100, "span100", 100, 200, root)
    val span2 = Span(200, "span200", 150, 300, root)


    // perhaps this should be in a TestSpan test, but Span is really part of Trace
    // so testing it here seems ok
    @Test(expected = IllegalArgumentException::class)
    fun spanWithStartAfterEndShouldBeRejected() {
        val s = Span(100, "s100", 100, 99)
    }
    @Test(expected = IllegalArgumentException::class)
    fun emptyTraceShouldBeRejected() {
        Trace(emptySet())
    }

    @Test
    fun rootSpanShouldBeIdentifiedWithChildren() {
        val t = Trace(
            setOf(root, span1, span2)
        )

        assertEquals("wrong root for trace", t.root, root)
    }

    @Test
    fun rootSpanShouldBeIdentifiedWithSingleSpan() {
        val t = Trace(setOf(root))
        assertEquals("wrong root for trace", t.root, root)
    }

    @Test
    fun childrenShouldBeSpansLessRoot() {
        val t = Trace(setOf(root, span1, span2))
        assertEquals("unexpected set of children", t.children, setOf(span1, span2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun traceWithTwoRootsShouldBeRejected() {
        val t = Trace(setOf(
                Span(100, "s100", 10, 20),
                Span(200, "s200", 10, 20)
        ))
    }

    @Test(expected = IllegalArgumentException::class)
    fun childrenBeforeRootSpanTimesCauseException() {
        val root  = Span(1, "rootSpan", 100, 500)
        val span1 = Span(100, "span100", 100, 200, root)
        val span2 = Span(200, "span200", 99, 300, root)
        val t = Trace(setOf(root, span1, span2))
    }

    @Test
    fun traceStartTimeAndEndTimeMatchRoot() {
        val root  = Span(1, "rootSpan", 100, 500)
        val span1 = Span(100, "span100", 100, 200, root)
        val span2 = Span(200, "span200", 110, 300, root)
        val t = Trace(setOf(root, span1, span2))
        assertTrue("wrong Trace start or end times", t.getStartTime() == 100L && t.getEndTime() == 500L)
    }
}