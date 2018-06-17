package com.artechra.apollo.types

data class ArchitecturalElement(val name : String, private val subComponents: MutableList<ArchitecturalElement> = mutableListOf()) {
    fun addSubcomponent(subcomponent : ArchitecturalElement) {
        subComponents.add(subcomponent)
    }
}