package com.artechra.apollo.resusage

import com.artechra.apollo.types.ResourceUsageMetric
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory

class ResourceUsageManagerInfluxDbImpl(dbUrl : String, dbUser: String?, dbPassword : String?) : ResourceUsageManager {

    val influxdb : InfluxDB

    init {
        if (dbUser != null && dbUser.length > 0) {
            influxdb = InfluxDBFactory.connect(dbUrl, dbUser, dbPassword)
        } else {
            influxdb = InfluxDBFactory.connect(dbUrl)
        }
    }

    override fun getResourceUsage(containerId: String, startTime: Long, endTime: Long): ResourceUsageMetric? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}