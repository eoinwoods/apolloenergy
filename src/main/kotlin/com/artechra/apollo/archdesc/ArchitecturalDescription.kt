package com.artechra.apollo.archdesc

data class Element(val name : String)

interface ArchitecturalDescription {
    fun getStructure() : List<Element>
}