package com.artechra.apollo.integration

import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


class TestMySqlZipkinTraceManager {

    @Test
    fun testThatSetOfTracesIsReturned() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val traces = traceManager.getTraces()
        assertThat(traces.size, equalTo(3))
    }

    @Test
    fun testThatCorrectNumberOfSpansContainedInMultiSpanTrace() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val threeSpanTraceId = "C925BFAC9556A68A"
        val trace = traceManager.getTrace(threeSpanTraceId)
        assertThat(trace.root.spanId, equalTo(threeSpanTraceId))
    }

    @Test
    fun testThatExampleSpanIsPopulated() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val threeSpanTraceId = "C925BFAC9556A68A"
        val trace = traceManager.getTrace(threeSpanTraceId)
        val exampleSpans = trace.spans.asIterable().filter { it.parentId != null}
        val exampleSpan = exampleSpans[0]
        assertTrue(exampleSpan.startTime < exampleSpan.endTime)
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
        val threeSpanTraceId = "C925BFAC9556A68A"
        val trace = traceManager.getTrace(threeSpanTraceId)
        assertThat(trace.root.spanId, equalTo(threeSpanTraceId))
        var earliestStartMsec : Long = Long.MAX_VALUE
        var latestEndMsec : Long = 0
        for (s in trace.spans) {
            if (s.startTime < earliestStartMsec) earliestStartMsec = s.startTime
            if (s.endTime > latestEndMsec) latestEndMsec = s.endTime
        }
        assertThat(trace.root.startTime, equalTo(earliestStartMsec))
        assertThat(trace.root.endTime, equalTo(latestEndMsec))
    }

    private fun getDataSource(): DataSource {
        // Creates a new instance of DriverManagerDataSource and sets
        // the required parameters such as the Jdbc Driver class,
        // Jdbc URL, database user name and password.
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(IntegrationTestConstants.MYSQL_DRIVER)
        dataSource.url = IntegrationTestConstants.MYSQL_URL
        dataSource.username = IntegrationTestConstants.MYSQL_USER
        dataSource.password = IntegrationTestConstants.MYSQL_PASS
        return dataSource
    }
}