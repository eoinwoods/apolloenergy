package com.artechra.apollo.types

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestTrace {
    private val baseTimeMsec = 1515237412000
    private val root  =   Span("54C92796854B15C8", "54C92796854B15C8","192.168.1.1", baseTimeMsec+0, baseTimeMsec+500)
    private val span1 =   Span("54C92796854B15C8", "54C92796854B1600", "192.168.1.2", baseTimeMsec+100, baseTimeMsec+200, root.spanId)
    private val span2 =   Span("54C92796854B15C8", "54C92796854B1700", "192.168.1.3", baseTimeMsec+150, baseTimeMsec+300, root.spanId)
    private val span2_1 = Span("54C92796854B15C8", "54C92796854B1701", "192.168.1.3", baseTimeMsec+150, baseTimeMsec+300, span2.spanId)
    private val span2_2 = Span("54C92796854B15C8", "54C92796854B1702", "192.168.1.3", baseTimeMsec+150, baseTimeMsec+300, span2.spanId)


    // perhaps this should be in a TestSpan test, but Span is really part of Trace
    // so testing it here seems ok
    @Test(expected = IllegalArgumentException::class)
    fun spanWithStartAfterEndShouldBeRejected() {
        Span("C925BFAC9556A68A", "54C92796854B15C8","s100", baseTimeMsec, baseTimeMsec-1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun spanWithIllegalNetworkAddressFormatShouldBeRejected() {
        Span("C925BFAC9556A68A", "54C92796854B15C8","192.168.1.1/16", baseTimeMsec, baseTimeMsec+100)
    }

    @Test(expected = IllegalArgumentException::class)
    fun spanWithEmptyNetworkAddressShouldBeRejected() {
        Span("C925BFAC9556A68A", "54C92796854B15C8","", baseTimeMsec, baseTimeMsec+100)
    }

    @Test(expected = IllegalArgumentException::class)
    fun emptyTraceShouldBeRejected() {
        Trace("", emptySet())
    }

    @Test
    fun rootSpanShouldBeIdentifiedWithChildren() {
        val t = Trace("trace1", setOf(root, span1, span2))
        assertEquals("wrong root for trace", t.root, root)
    }

    @Test
    fun rootSpanShouldBeIdentifiedWithSingleSpan() {
        val t = Trace("trace2", setOf(root))
        assertEquals("wrong root for trace", t.root, root)
    }

    @Test
    fun childrenShouldBeSpansLessRoot() {
        val t = Trace("trace3", setOf(root, span1, span2))
        assertEquals("unexpected set of children", t.children, setOf(span1, span2))
    }

    @Test
    fun childrenShouldBeCorrectlyReturned() {
        val t = Trace("trace4",
                setOf(root, span1, span2, span2_1, span2_2))
        assertEquals("Wrong child set", setOf(span2_1, span2_2), t.findChildrenOfSpan(span2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun traceWithTwoRootsShouldBeRejected() {
        Trace("trace5", setOf(
                Span("fd45", "54C92796854B15C8","192.168.1.1", baseTimeMsec, baseTimeMsec+10),
                Span("F59EAF6D7B9C5C49", "54C92796854B15C8","10.10.10.1", baseTimeMsec, baseTimeMsec+10)
        ))
    }

    @Test(expected = IllegalArgumentException::class)
    fun childrenBeforeRootSpanTimesCauseException() {
        val root  = Span("C925BFAC9556A68A", "C925BFAC9556A68A", "1.1.1.1", baseTimeMsec+100, baseTimeMsec+500)
        val span1 = Span("C925BFAC9556A68A","cf56342a", "10.10.10.1", baseTimeMsec+100, baseTimeMsec+200, root.spanId)
        val span2 = Span("C925BFAC9556A68A","c45e6a37", "192.168.1.1", baseTimeMsec+99, baseTimeMsec+300, root.spanId)
        Trace("trace6", setOf(root, span1, span2))
    }

    @Test
    fun traceStartTimeAndEndTimeMatchRoot() {
        val root  = Span("F59EAF6D7B9C5C49","F59EAF6D7B9C5C49", "1.1.1.1", baseTimeMsec+100, baseTimeMsec+500)
        val span1 = Span("F59EAF6D7B9C5C49","cf56342f", "10.10.10.1", baseTimeMsec+100, baseTimeMsec+200, root.spanId)
        val span2 = Span("F59EAF6D7B9C5C49","cf56342a", "192.168.1.1", baseTimeMsec+110, baseTimeMsec+300, root.spanId)
        val t = Trace("trace7", setOf(root, span1, span2))
        assertTrue("wrong Trace start or end times", t.getStartTime() == baseTimeMsec+100L && t.getEndTime() == baseTimeMsec+500L)
    }
}