package com.artechra.apollo.integration

import com.artechra.apollo.resusage.CpuMeasurement
import com.artechra.apollo.resusage.InfluxDbDecorator
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class TestInfluxDbDecorator {
    private val DBURL = IntegrationTestConstants.INFLUX_URL
    private val DATABASE = IntegrationTestConstants.DB_NAME

    private val CONTAINERID = IntegrationTestConstants.CONTAINERID
    private val SPAN_TIME_MS = IntegrationTestConstants.SPAN_START_TIME_MS
    private val TEST_SET     = IntegrationTestConstants.TEST_SET_NAME

    private var influxdb: InfluxDbDecorator? = null

    @Before
    fun setup() {
        this.influxdb = InfluxDbDecorator(DBURL, DATABASE)
    }

    @After
    fun cleanup() {
        influxdb?.close()
    }

    // A little helper to work around the need for mutability in the decorator reference
    // due to the use of @Before rather than an init {} block
    fun getDbConn() : InfluxDbDecorator {
        val dbconn = this.influxdb
        if (dbconn == null) throw IllegalStateException("No database connection available")
        return dbconn
    }

    @Test
    fun testThatTestDataSet3IsLoaded() {
        val query = Query("SELECT * FROM apollo_check WHERE value = 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        assertEquals("data_set", result?.results?.get(0)?.getSeries()?.get(0)?.getColumns()?.get(2))
        // This is a little arcane but is a reflection of InfluxDB's quite complex result set structure
        val resultValue = result!!.results[0]!!.series[0]!!.values[0][2] as String
        assertEquals(TEST_SET, resultValue)
    }

    @Test
    fun testThatCpuMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, usage_total FROM docker_container_cpu LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        val resultMapper = InfluxDBResultMapper()
        val cpuList = resultMapper.toPOJO(result, CpuMeasurement::class.java!!)
        assertEquals(1, cpuList.size)
        assertEquals("cpuhog", cpuList[0].containerName)
        assertEquals(2414055725, cpuList[0].cpuUsage)
        assertEquals(1515237202000, cpuList[0].timeMillis)
    }

    @Test
    fun testThatCpuUsageIsReturned() {
        val cpuUsage = getDbConn().getBestCpuMeasureForTime(CONTAINERID, SPAN_TIME_MS)
        assertEquals(31029497377, cpuUsage)
    }

}