package com.artechra.apollo.archdesc

import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestArchitectureManager {

    @Test
    fun theNullImplementationShouldReturnEmptyList() {
        assertTrue(ArchitectureManagerDefaultImpl().getStructure().elements.size == 1 )
    }
}