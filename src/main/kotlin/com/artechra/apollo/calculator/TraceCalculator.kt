package com.artechra.apollo.calculator

import com.artechra.apollo.types.ArchitecturalDescription
import com.artechra.apollo.types.ResourceUsage
import com.artechra.apollo.types.ResourceUsageMetric
import com.artechra.apollo.types.Trace

class TraceCalculator(t : Trace, resourceUsage : Set<ResourceUsageMetric>, architecture : ArchitecturalDescription) {

    fun calculateTotalResourceUsage() : ResourceUsage {
        return ResourceUsage(0, 0, 0, 0)
    }

}