package com.artechra.apollo.types

import org.junit.Test
import kotlin.test.assertEquals

class TestUtil {

    @Test
    fun testThatInterpolationOfLongIsValid() {

        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1031L

        assertEquals(4669, Util.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationWithReducingValidIsCorrect() {
        val t1 = 1000L
        val v1 = 5000L
        val t2 = 1050L
        val v2 = 3000L
        val requiredPoint = 1031L

        assertEquals(3760, Util.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))

    }

    @Test
    fun testThatInterpolationToFirstPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1000L
        assertEquals(4598, Util.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationToLastPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1050L
        assertEquals(4712, Util.interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

}