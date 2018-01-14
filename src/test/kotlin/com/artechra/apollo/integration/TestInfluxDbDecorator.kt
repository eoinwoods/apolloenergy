package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import org.influxdb.dto.Query
import org.junit.*
import kotlin.test.assertEquals

class TestInfluxDbDecorator {
    val DBURL = "http://localhost:8086"

    // These values correspond to data in test data set 3
    private val CONTAINERID = "5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228"
    private val STARTTIME = 1515237435811000000
    private val DATABASE = "telegraf"

    private var influxdb: InfluxDbDecorator? = null

    @Before
    fun setup() {
        this.influxdb = InfluxDbDecorator(DBURL, DATABASE)
    }

    @After
    fun cleanup() {
        influxdb?.close()
    }

    @Test
    fun testThatTestDataSet3IsLoaded() {
        val query = Query("SELECT * FROM apollo_check WHERE value = 1", DATABASE)
        val dbconn = this.influxdb?.getInfluxDbConnection()
        val result = dbconn?.query(query)
        assertEquals("data_set", result?.results?.get(0)?.getSeries()?.get(0)?.getColumns()?.get(2))
        // This is a little arcane but is a reflection of InfluxDB's quite complex result set structure
        val resultValue = result!!.results[0]!!.series[0]!!.values[0][2] as String
        assertEquals("set3", resultValue)
    }

    @Test
    fun testThatCpuUsageIsReturned() {
        val cpuUsage = influxdb?.getBestCpuMeasureForTime(CONTAINERID, STARTTIME)
        assertEquals(31027445695, cpuUsage)
    }

}