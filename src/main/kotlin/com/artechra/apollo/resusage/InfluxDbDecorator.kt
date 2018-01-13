package com.artechra.apollo.resusage

import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory

class InfluxDbDecorator(dbUrl : String, dbUser : String?, dbPassword : String?) {
    val influxdb : InfluxDB

    init {
        if (dbUser != null && dbUser.length > 0) {
            influxdb = InfluxDBFactory.connect(dbUrl, dbUser, dbPassword)
        } else {
            influxdb = InfluxDBFactory.connect(dbUrl)
        }
    }

}