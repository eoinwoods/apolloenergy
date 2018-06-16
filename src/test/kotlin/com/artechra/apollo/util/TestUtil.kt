package com.artechra.apollo.util

import com.artechra.apollo.types.Util
import org.junit.Test
import kotlin.test.assertEquals

class TestUtil {

    companion object {
        fun getDataFilePath(fileName : String) : String {
            return ClassLoader.getSystemResource(fileName).path
        }
    }

    @Test
    fun testThatTruncatingSeveralPlacesWorks() {
        assertEquals(0.003, Util.truncateDecimalPlaces(0.003456789, 3))
    }

    @Test
    fun testThatTruncatingZeroPlacesWorks() {
        assertEquals(2.0, Util.truncateDecimalPlaces(2.003456789, 0))
    }

}