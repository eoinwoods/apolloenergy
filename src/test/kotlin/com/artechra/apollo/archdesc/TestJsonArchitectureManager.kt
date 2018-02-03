package com.artechra.apollo.archdesc

import com.artechra.apollo.types.ArchitecturalDescription
import com.artechra.apollo.util.TestUtil
import junit.framework.TestCase
import org.junit.Test

class TestJsonArchitectureManager {

    fun defaultArchitectureDescription() : ArchitecturalDescription {
        val archManager = ArchitectureManagerJsonImpl(TestUtil.getDataFilePath("test_architectural_description.json"))
        return archManager.getStructure()
    }
    @Test
    fun testThatCorrectNumberOfElementsArePresent() {
        val ad = defaultArchitectureDescription()
        TestCase.assertEquals(7, ad.elements.size)
    }

    @Test
    fun testThatTopLevelElementIsFoundByName() {
        val ad = defaultArchitectureDescription()
        TestCase.assertEquals("C1", ad.findElementByName("C1")?.name)
    }

    @Test
    fun testThatChildElementsArePresent() {
        val ad = defaultArchitectureDescription()
        TestCase.assertEquals(2, ad.findElementByName("C2")?.subComponents?.size)
    }

    @Test
    fun testThatChildElementsAreCorrect() {
        val ad = defaultArchitectureDescription()
        TestCase.assertEquals("C3.1", ad.findElementByName("C3")?.subComponents!![0].name)
    }

    @Test
    fun testThatGrandchildElementsAreCorrect() {
        val ad = defaultArchitectureDescription()
        TestCase.assertEquals("C2.1.1", ad.findElementByName("C2")?.subComponents!![0].subComponents[0].name)
    }
}