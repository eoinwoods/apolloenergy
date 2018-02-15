package com.artechra.apollo.energymodel

class TrivialEnergyModel(val cpuTdcW : Double, val diskUsageW : Double, val diskSpeedMb : Double,
                         val memoryUsageWGb : Double, val netUsageWMb : Double) : EnergyModel {

    override fun calculateEnergyInJoules(cpuUsageNanos: Long, memUsageMb: Long, diskIoBytes: Long, netIoBytes: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}