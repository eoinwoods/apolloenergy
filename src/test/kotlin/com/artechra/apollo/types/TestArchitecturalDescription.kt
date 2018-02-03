package com.artechra.apollo.types

import com.artechra.apollo.util.TestUtil.Companion.getDataFilePath
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestArchitecturalDescription {

    @Test
    fun testThatCorrectNumberOfElementsArePresent() {
        val ad = ArchitecturalDescription.loadFromJsonFile(getDataFilePath("test_architectural_description.json"))
        assertEquals(7, ad.elements.size)
    }

    @Test
    fun testThatTopLevelElementIsFoundByName() {
        val ad = ArchitecturalDescription.loadFromJsonFile(getDataFilePath("test_architectural_description.json"))
        assertEquals("C1", ad.findElementByName("C1")?.name)
    }

    @Test
    fun testThatChildElementsArePresent() {
        val ad = ArchitecturalDescription.loadFromJsonFile(getDataFilePath("test_architectural_description.json"))
        assertEquals(2, ad.findElementByName("C2")?.subComponents?.size)
    }

    @Test
    fun testThatChildElementsAreCorrect() {
        val ad = ArchitecturalDescription.loadFromJsonFile(getDataFilePath("test_architectural_description.json"))
        assertEquals("C3.1", ad.findElementByName("C3")?.subComponents!![0].name)
    }

    @Test
    fun testThatGrandchildElementsAreCorrect() {
        val ad = ArchitecturalDescription.loadFromJsonFile(getDataFilePath("test_architectural_description.json"))
        assertEquals("C2.1.1", ad.findElementByName("C2")?.subComponents!![0].subComponents[0].name)
    }
}