package com.artechra.apollo.integration

import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.junit.Test
import kotlin.test.assertTrue

const val DB_URL = "http://localhost:8086"

class TestInfluxDbAccess {

    @Test
    fun testConnectionIsSuccessful() {
        val influxDB = InfluxDBFactory.connect(DB_URL)
        influxDB.close()
    }

    @Test
    fun testThatQueryReturnsData() {
        val influxDB = InfluxDBFactory.connect(DB_URL)
        val query = Query("SELECT time, NumGC, hostname FROM runtime LIMIT 1", "_internal")
        val result = influxDB.query(query)
        assertTrue(result.results[0].series[0].values[0].size == 3)
        influxDB.close()
    }
}