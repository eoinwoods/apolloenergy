package com.artechra.apollo.archdesc

import com.artechra.apollo.types.ArchitecturalDescription
import com.artechra.apollo.types.ArchitecturalElement
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File

class ArchitectureManagerJsonImpl(archDescFileName : String) : ArchitectureManager {
    val architectureDescription : ArchitecturalDescription

    init {
        if (!File(archDescFileName).canRead()) {
            throw IllegalArgumentException("Could not open file ${archDescFileName}")
        }
        architectureDescription = loadFromJsonFile(archDescFileName)
    }

    fun loadFromJsonFile(archDescFileName: String) : ArchitecturalDescription {

        val jsonData: JsonObject? = Parser().parse(archDescFileName) as? JsonObject
        jsonData ?: throw IllegalStateException("Could not convert parser output to JsonObject")

        val adName = jsonData["Name"] as String
        val jsonComponents : JsonArray<JsonObject>? = jsonData["Components"] as? JsonArray<JsonObject>
        jsonComponents ?: throw IllegalStateException("Could not convert parser output to component array of JsonObjects")

        var workingComponents = jsonComponents.map{ val componentName = it["ComponentName"] as String
            componentName to ArchitecturalElement(componentName)
        }.toMap()

        addSubComponents(jsonComponents, workingComponents)

        return ArchitecturalDescription(adName, workingComponents.values.toSet())

    }

    private fun addSubComponents(jsonComponentDefinitions: JsonArray<JsonObject>, components : Map<String, ArchitecturalElement>) {
        jsonComponentDefinitions.forEach {
            val name = it["ComponentName"] as String
            val subComponentNames = it["SubComponents"] as JsonArray<String>

            subComponentNames.forEach {
                val component = components[it]
                if (component == null) {
                    throw IllegalStateException("Architectural description inconsistent - $name component's subcompoment $it does not exist")
                }
                components[name]?.addSubcomponent(component)
            }
        }

    }

    override fun getStructure(): ArchitecturalDescription {
        return architectureDescription
    }

}