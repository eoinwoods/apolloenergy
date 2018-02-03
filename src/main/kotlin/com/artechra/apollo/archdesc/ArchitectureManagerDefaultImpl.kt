package com.artechra.apollo.archdesc

import com.artechra.apollo.types.ArchitecturalDescription
import com.artechra.apollo.types.ArchitecturalElement

class ArchitectureManagerDefaultImpl : ArchitectureManager {
    override fun getStructure(): ArchitecturalDescription = ArchitecturalDescription("System1", emptySet<ArchitecturalElement>())
}