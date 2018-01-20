package com.artechra.apollo.integration

import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


class TestMySqlZipkinTraceManager {

    @Test
    fun testThatSetOfTracesIsReturned() {
        val jdbcTemplate = JdbcTemplate(getDataSource())
        val traceManager = MySqlZipkinTraceManagerImpl(jdbcTemplate)
        val traces = traceManager.getRootSpans()
        println(traces)
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