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
    private val DBURL = TestConstants.INFLUX_URL
    private val DATABASE = TestConstants.DB_NAME

    private val CONTAINERID = TestConstants.CONTAINERID
    private val SPAN_TIME_MS = TestConstants.SPAN_START_TIME_MS
    private val TEST_SET     = TestConstants.TEST_SET_NAME

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

    fun getCpuMeasurements() : MutableList<CpuMeasurement>{
        val ret = mutableListOf<CpuMeasurement>()

        // 4 intervals 10 sec apart
        // example data taken from Docker via data set 3
        ret.add(CpuMeasurement(1515237422000, "gateway", 30305812441))
        ret.add(CpuMeasurement(1515237432000, "gateway", 30318691887))
        ret.add(CpuMeasurement(1515237442000, "gateway", 32183833555))
        ret.add(CpuMeasurement(1515237452000, "gateway", 32231405970))

        return ret
    }

    @Test
    fun testThatFindingBestPointInListFindsMidValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                getDbConn().findMeasurementsAroundPointInTime(getCpuMeasurements(), SPAN_TIME_MS)
        assertEquals(1515237432000, before.getTimeMillis())
        assertEquals(1515237442000, after.getTimeMillis())
    }

    @Test
    fun testThatFindingBestPointInListFindsFirstValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                getDbConn().findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237422000)
        assertEquals(1515237422000, before.getTimeMillis())
        assertEquals(1515237432000, after.getTimeMillis())
    }

    @Test
    fun testThatFindingBestPointInListFindsLastValue() {
        // findMeasurementsAroundPointInTime
        val(before, after) =
                getDbConn().findMeasurementsAroundPointInTime(getCpuMeasurements(), 1515237452000)
        assertEquals(1515237442000, before.getTimeMillis())
        assertEquals(1515237452000, after.getTimeMillis())
    }

    @Test
    fun testThatInterpolationOfLongIsValid() {

        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1031L

        assertEquals(4669, getDbConn().interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationToFirstPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1000L
        assertEquals(4598, getDbConn().interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

    @Test
    fun testThatInterpolationToLastPointIsCorrect() {
        val t1 = 1000L
        val v1 = 4598L
        val t2 = 1050L
        val v2 = 4712L
        val requiredPoint = 1050L
        assertEquals(4712, getDbConn().interpolateBetweenPoints(t1, v1, t2, v2, requiredPoint))
    }

}