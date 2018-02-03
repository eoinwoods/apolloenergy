package com.artechra.apollo.types

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File

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