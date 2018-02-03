package com.artechra.apollo.types

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestArchitecturalDescription {

    @Test
    fun testThatElementsAreReturnedByName() {
        val ad = ArchitecturalDescription("System1", setOf<ArchitecturalElement>(
                ArchitecturalElement("C1"),
                ArchitecturalElement("C2"),
                ArchitecturalElement("C2")
        ))

        assertEquals("C2", ad.findElementByName("C2")?.name)
    }

}