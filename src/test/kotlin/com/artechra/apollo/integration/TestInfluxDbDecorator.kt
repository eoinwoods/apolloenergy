package com.artechra.apollo.integration

import com.artechra.apollo.resusage.*
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.logging.Logger
import kotlin.test.assertEquals


class TestInfluxDbDecorator {
    val LOG = Logger.getLogger(this.javaClass.name)

    private val DBURL    = IntegrationTestConstants.INFLUX_URL
    private val DATABASE = IntegrationTestConstants.DB_NAME

    private val CONTAINER_ID        = IntegrationTestConstants.GATEWAY_CONTAINER_ID
    private val DISKIO_CONTAINER_ID = IntegrationTestConstants.INFLUXDB_CONTAINER_ID
    private val SPAN_TIME_MS        = IntegrationTestConstants.SPAN_START_TIME_MS
    private val TEST_SET            = IntegrationTestConstants.TEST_SET_NAME

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
        val query = Query("SELECT time, container_name, usage_total FROM docker_container_cpu ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        val resultMapper = InfluxDBResultMapper()
        val cpuList = resultMapper.toPOJO(result, CpuMeasurement::class.java)
        LOG.info("cpuList=" + cpuList[0])
        assertEquals(1, cpuList.size)
        assertEquals("cpuhog", cpuList[0].containerName)
        assertEquals(2414055725, cpuList[0].cpuUsage)
        assertEquals(1515237202000, cpuList[0].timeMillis)
    }

    @Test
    fun testThatMemMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, usage FROM docker_container_mem ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        val resultMapper = InfluxDBResultMapper()
        val memList = resultMapper.toPOJO(result, MemMeasurement::class.java)
        assertEquals(1, memList.size)
        LOG.info("memList: " + memList[0])
        assertEquals("cpuhog", memList[0].containerName)
        assertEquals(441532416, memList[0].memUsage)
        assertEquals(1515237202000, memList[0].timeMillis)
    }

    @Test
    fun testThatDiskIoMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, io_service_bytes_recursive_total FROM docker_container_blkio ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        val resultMapper = InfluxDBResultMapper()
        val diskIoList = resultMapper.toPOJO(result, DiskIoMeasurement::class.java)
        assertEquals(1, diskIoList.size)
        LOG.info("diskIoList=" + diskIoList[0])
        assertEquals("influxdb", diskIoList[0].containerName)
        assertEquals(8192, diskIoList[0].diskIoBytes)
        assertEquals(1515237202000, diskIoList[0].timeMillis)
    }

    @Test
    fun testThatNetIoMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, rx_bytes, tx_bytes FROM docker_container_net " +
                                    "WHERE time = 1515237462000000000 ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn?.query(query)
        val resultMapper = InfluxDBResultMapper()
        val netIoList = resultMapper.toPOJO(result, NetIoMeasurement::class.java)
        assertEquals(1, netIoList.size)
        LOG.info("netIoList=" + netIoList[0])
        assertEquals("cpuhog", netIoList[0].containerName)
        assertEquals(5229, netIoList[0].rxBytes)
        assertEquals(5654, netIoList[0].txBytes)
        assertEquals(1515237462000, netIoList[0].timeMillis)
    }

    @Test
    fun testThatCpuUsageIsReturned() {
        val cpuUsage = getDbConn().getBestCpuMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(31029497377, cpuUsage)
    }

    @Test
    fun testThatMemUsageIsReturned() {
        val memUsage = getDbConn().getBestMemMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(1122997373, memUsage)
    }

    @Test
    fun testThatDiskIoUsageIsReturned() {
        val diskIoUsage = getDbConn().getBestDiskIoMeasureForTime(DISKIO_CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(994554, diskIoUsage)
    }

    @Test
    fun testThatNetIoUsageIsReturned() {
        val netIoUsage = getDbConn().getBestNetIoMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(2888, netIoUsage)
    }
}