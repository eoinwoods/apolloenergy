package com.artechra.apollo.archdesc

import com.artechra.apollo.types.ArchitecturalDescription

data class Element(val name : String)

interface ArchitecturalDescription {
    fun getStructure() : ArchitecturalDescription
}