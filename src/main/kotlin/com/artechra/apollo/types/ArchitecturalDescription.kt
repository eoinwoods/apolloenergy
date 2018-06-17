package com.artechra.apollo.types

data class ArchitecturalDescription(val name : String, private val elements : Set<ArchitecturalElement>) {
    fun findElementByName(name : String) : ArchitecturalElement? {
        elements.forEach {
            if (it.name == name) {
                return it
            }
        }
        return null
    }
}