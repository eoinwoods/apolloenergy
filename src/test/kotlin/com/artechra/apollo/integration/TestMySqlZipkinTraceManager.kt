package com.artechra.apollo.integration

import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

const val MULTI_SPAN_TRACE_ID = IntegrationTestShared.MULTI_SPAN_ZIPKIN_TRACE_ID
const val MULTI_SPAN_TRACE_NAME = IntegrationTestShared.MULTI_SPAN_ZIPKIN_TRACE_NAME
const val TEST_DATA_SET = IntegrationTestShared.TEST_SET_NAME

class TestMySqlZipkinTraceManager {

    @Before
    fun checkPreconditions() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val name = jdbcTemplate.queryForObject("select name from zipkin.zipkin_spans where trace_id = 0 and id = 1", String::class.java)
        assertEquals(TEST_DATA_SET, name)
    }

    @Test
    fun testThatSetOfTracesIsReturned() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val traces = traceManager.getTraces()
        assertThat(traces.size, equalTo(1))
    }

    @Test
    fun testThatATraceIsPopulated() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val aTrace = traceManager.getTraces()[0]
        assertEquals(MULTI_SPAN_TRACE_ID, aTrace.traceId)
        assertEquals(MULTI_SPAN_TRACE_NAME, aTrace.name)
    }

    @Test
    fun testThatCorrectNumberOfSpansContainedInMultiSpanTrace() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val trace = traceManager.getTraces().filter{t -> t.traceId == MULTI_SPAN_TRACE_ID}[0]
        assertThat(trace.root.spanId, equalTo(MULTI_SPAN_TRACE_ID))
    }

    @Test
    fun testThatExampleSpanIsPopulated() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val traces = traceManager.getTraces()
        val trace = traces.filter{t -> t.traceId == MULTI_SPAN_TRACE_ID}[0]
        val exampleSpans = trace.spans.asIterable().filter { it.parentId != null}
        val exampleSpan = exampleSpans[0]
        assertTrue(exampleSpan.startTimeMsec < exampleSpan.endTimeMsec)
        assertNotNull(exampleSpan.parentId)
        assertNotNull(exampleSpan.networkAddress)
        assertTrue(exampleSpan.networkAddress.matches(Regex("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}")))
        java.math.BigInteger(exampleSpan.spanId, 16)
        java.math.BigInteger(exampleSpan.traceId, 16)
        java.math.BigInteger(exampleSpan.parentId, 16)
    }

    @Test
    fun testThatSpanTimesAreCredibleInMultiSpanTrace() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val multiSpanTraceId = MULTI_SPAN_TRACE_ID
        val trace = traceManager.getTraces().filter{t -> t.traceId == multiSpanTraceId}[0]
        assertThat(trace.root.spanId, equalTo(multiSpanTraceId))
        var earliestStartMsec : Long = Long.MAX_VALUE
        var latestEndMsec : Long = 0
        for (s in trace.spans) {
            if (s.startTimeMsec < earliestStartMsec) earliestStartMsec = s.startTimeMsec
            if (s.endTimeMsec > latestEndMsec) latestEndMsec = s.endTimeMsec
        }
        assertThat(trace.root.startTimeMsec, equalTo(earliestStartMsec))
        assertThat(trace.root.endTimeMsec, equalTo(latestEndMsec))
    }

    private fun getDataSource(): DataSource {
        // Creates a new instance of DriverManagerDataSource and sets
        // the required parameters such as the Jdbc Driver class,
        // Jdbc URL, database user name and password.
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(IntegrationTestShared.MYSQL_DRIVER)
        dataSource.url = IntegrationTestShared.MYSQL_URL
        dataSource.username = IntegrationTestShared.MYSQL_USER
        dataSource.password = IntegrationTestShared.MYSQL_PASS
        return dataSource
    }
}