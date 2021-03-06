package com.artechra.apollo.integration

import com.artechra.apollo.resusage.*
import org.influxdb.dto.Query
import org.influxdb.impl.InfluxDBResultMapper
import org.junit.*
import java.util.logging.Logger
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class TestInfluxDbDecorator {
    private val _log = Logger.getLogger(this.javaClass.name)

    companion object {
        private const val DBURL               = IntegrationTestShared.INFLUX_URL
        private const val DATABASE            = IntegrationTestShared.DB_NAME
        private const val CONTAINER_ID        = IntegrationTestShared.GATEWAY_CONTAINER_ID
        private const val DISKIO_CONTAINER_ID = IntegrationTestShared.INFLUXDB_CONTAINER_ID
        private const val SPAN_TIME_MS        = IntegrationTestShared.SPAN_START_TIME_MS
        private const val HOST_NAME           = IntegrationTestShared.HOST_NAME
        private const val TEST_SET            = IntegrationTestShared.TEST_SET_NAME
    }

    private var influxdb: InfluxDbDecoratorImpl? = null

    @Before
    fun setup() {
        this.influxdb = InfluxDbDecoratorImpl(DBURL, DATABASE)
        assertEquals(TEST_SET, IntegrationTestShared.getTestDataSetLoadedName(influxdb!!, DATABASE))
    }

    @After
    fun cleanup() {
        influxdb?.close()
    }

    // A little helper to work around the need for mutability in the decorator reference
    // due to the use of @Before rather than an init {} block
    private fun getDbConn() : InfluxDbDecoratorImpl {
        return influxdb ?: throw IllegalStateException("No database connection available")
    }

    @Test
    fun testThatCpuMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, host, usage_total FROM docker_container_cpu ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn.query(query)
        val resultMapper = InfluxDBResultMapper()
        val cpuList = resultMapper.toPOJO(result, CpuMeasurement::class.java)
        _log.info("cpuList=$cpuList")
        assertEquals(1, cpuList.size)
        assertEquals("cpuhog", cpuList[0].containerName)
        assertEquals(HOST_NAME, cpuList[0].hostName)
        assertEquals(9254878795, cpuList[0].cpuUsageNsec)
        assertEquals(1528650681000, cpuList[0].timeMillis)
    }

    @Test
    fun testThatHostCpuMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, host, time_active FROM cpu ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn.query(query)
        val resultMapper = InfluxDBResultMapper()
        val hostCpuList = resultMapper.toPOJO(result, HostCpuMeasurement::class.java)
        _log.info("hostCpuList=" + hostCpuList[0])
        assertEquals(1, hostCpuList.size)
        assertEquals(HOST_NAME, hostCpuList[0].hostName)
        assertEquals(294400, hostCpuList[0].cpuUsageMsec)
        assertEquals(1528650680000, hostCpuList[0].timeMillis)
    }

    @Test
    fun testThatMemMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, usage FROM docker_container_mem ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn.query(query)
        val resultMapper = InfluxDBResultMapper()
        val memList = resultMapper.toPOJO(result, MemMeasurement::class.java)
        assertEquals(1, memList.size)
        _log.info("memList: " + memList[0])
        assertEquals("cpuhog", memList[0].containerName)
        assertEquals(413880320, memList[0].memUsage)
        assertEquals(1528650681000, memList[0].timeMillis)
    }

    @Test
    fun testThatDiskIoMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, io_service_bytes_recursive_total FROM docker_container_blkio ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn.query(query)
        val resultMapper = InfluxDBResultMapper()
        val diskIoList = resultMapper.toPOJO(result, DiskIoMeasurement::class.java)
        assertEquals(1, diskIoList.size)
        _log.info("diskIoList=" + diskIoList[0])
        assertEquals("influxdb", diskIoList[0].containerName)
        assertEquals(94208, diskIoList[0].diskIoBytes)
        assertEquals(1528650681000, diskIoList[0].timeMillis)
    }

    @Test
    fun testThatNetIoMeasurementMappingIsCorrect() {
        val query = Query("SELECT time, container_name, rx_bytes, tx_bytes FROM docker_container_net " +
                                    "WHERE rx_bytes > 0 and tx_bytes > 0 ORDER BY time LIMIT 1", DATABASE)
        val dbconn = getDbConn().getInfluxDbConnection()
        val result = dbconn.query(query)
        val resultMapper = InfluxDBResultMapper()
        val netIoList = resultMapper.toPOJO(result, NetIoMeasurement::class.java)
        assertEquals(1, netIoList.size)
        _log.info("netIoList=" + netIoList[0])
        assertEquals("influxdb", netIoList[0].containerName)
        assertEquals(5503, netIoList[0].rxBytes)
        assertEquals(1460, netIoList[0].txBytes)
        assertEquals(1528650681000, netIoList[0].timeMillis)
    }

    @Test
    fun testThatCpuUsageIsReturned() {
        val cpuUsageMsec = getDbConn().getBestCpuMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(31949, cpuUsageMsec)
    }

    @Test
    fun testThatHostCpuUsageIsReturned() {
        val hostCpuMsecs = getDbConn().getBestHostCpuMsecMeasureForTime(HOST_NAME, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(171326, hostCpuMsecs)
    }

    @Test
    fun testThatMemUsageIsReturned() {
        val memUsage = getDbConn().getBestMemMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(980621722, memUsage)
    }

    @Test
    fun testThatDiskIoUsageIsReturned() {
        val diskIoUsage = getDbConn().getBestDiskIoMeasureForTime(DISKIO_CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(21189427, diskIoUsage)
    }

    @Test
    fun testThatNetIoUsageIsReturned() {
        val netIoUsage = getDbConn().getBestNetIoMeasureForTime(CONTAINER_ID, SPAN_TIME_MS)
        // Manually calculated value
        assertEquals(788, netIoUsage)
    }

    @Test
    fun testThatHostIsReturnedForContainer() {
        val host = getDbConn().getHostForContainerAtTime(CONTAINER_ID, SPAN_TIME_MS)
        assertEquals(HOST_NAME, host)

    }

    @Test(expected = IllegalStateException::class)
    fun testThatExceptionThrownForMissingHostForContainer() {
        val host = getDbConn().getHostForContainerAtTime("NO_SUCH_ID", SPAN_TIME_MS)
        assertEquals(HOST_NAME, host)
    }

    @Test
    fun testThatHostCpuCountIsReturned() {
        val cpuCount = getDbConn().getHostCpuCount(HOST_NAME)
        assertEquals(4, cpuCount)
    }

    @Test(expected = IllegalStateException::class)
    fun testThatHostCpuCountForNonExistentHostThrowsException() {
        getDbConn().getHostCpuCount("NO_SUCH_HOST")
    }

    @Test
    fun testThatHostUtilisationIsCredible() {
        val utilisation = getDbConn().getHostCpuUtilisationDuringPeriod(HOST_NAME,
                IntegrationTestShared.SPAN_START_TIME_MS,
                IntegrationTestShared.SPAN_END_TIME_MS)
        // In the test data set, the host isn't busy at all, hence the
        // utilisation is small (about 0.3%) hence the small numbers here
        assertTrue(utilisation > 0.002 && utilisation < 0.004)
    }

    @Test(expected = IllegalStateException::class)
    fun testThatHostUtilisationForNonExistentHostThrowsException() {
        getDbConn().getHostCpuUtilisationDuringPeriod("NO_SUCH_HOST",
                IntegrationTestShared.SPAN_START_TIME_MS,
                IntegrationTestShared.SPAN_END_TIME_MS)

    }
}