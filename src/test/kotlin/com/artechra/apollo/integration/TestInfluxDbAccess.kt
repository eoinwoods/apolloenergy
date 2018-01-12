package com.artechra.apollo.integration

import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.junit.Test
import kotlin.test.assertTrue

class TestInfluxDbAccess {

    val DB_URL = "http://localhost:8086"

    @Test
    fun testConnectionIsSuccessful() {
        var influxDB = InfluxDBFactory.connect(DB_URL)
        influxDB.close()
    }

    @Test
    fun testThatQueryReturnsData() {
        var influxDB = InfluxDBFactory.connect(DB_URL)
        val query = Query("SELECT time, NumGC, hostname FROM runtime LIMIT 1", "_internal")
        val result = influxDB.query(query)
        assertTrue(result.results[0].series[0].values[0].size == 3)
        influxDB.close()
    }
}