package com.artechra.apollo.archdesc

import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestArchitecturalDescription {

    @Test
    fun theNullImplementationShouldReturnEmptyList() {
        assertTrue(ArchitecturalDescriptionDefaultImpl().getStructure().size == 0 )
    }
}