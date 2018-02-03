package com.artechra.apollo.types

data class ArchitecturalDescription(val name : String, val elements : Set<ArchitecturalElement>) {
    fun findElementByName(name : String) : ArchitecturalElement? {
        elements.forEach {
            if (it.name.equals(name)) {
                return it
            }
        }
        return null
    }
}