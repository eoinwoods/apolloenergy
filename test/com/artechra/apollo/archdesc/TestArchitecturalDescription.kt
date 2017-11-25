package com.artechra.apollo.archdesc

import org.junit.Test
import kotlin.test.assertTrue

class TestArchitecturalDescription {

    @Test
    fun theNullImplementationShouldReturnEmptyList() {
        assertTrue { ArchitecturalDescriptionDefaultImpl().getStructure().size == 0 }
    }
}