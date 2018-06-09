package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecorator
import org.influxdb.dto.Query
import kotlin.test.assertEquals

class IntegrationTestShared {
    companion object {
        const val INFLUX_URL = "http://localhost:8086"
        const val DB_NAME    = "telegraf"

        const val MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver"
        const val MYSQL_DB   = "zipkin"
        const val MYSQL_URL  = "jdbc:mysql://localhost" + "/" + MYSQL_DB
        val MYSQL_USER = "zipkin"
        val MYSQL_PASS = "zipkin"

        // These values correspond to data in test data set '20180604-v2-cpu-data-mix'
        val TEST_SET_NAME = "20180604-v2-cpu-data-mix"
        val GATEWAY_CONTAINER_ID = "2c7c7e5cc08f8bbf8d64bc506e84e4ba84f6aca91fefd53d133c481762fdac3a"
        val INFLUXDB_CONTAINER_ID = "8b9e3f7bd3c59485dc55db54529be84ba260c0e79e76184f2144c7dd085630f0"
        val SPAN_START_TIME_MS = 1528144292000
        val SPAN_END_TIME_MS   = 1528144299000
        val HOST_NAME = "8b9e3f7bd3c5"

        fun getTestDataSetLoadedName(dbConn : InfluxDbDecorator, databaseName : String) : String {
            val query = Query("SELECT * FROM apollo_check WHERE value = 1", databaseName)
            val result = dbConn.getInfluxDbConnection().query(query)
            assertEquals("data_set", result?.results?.get(0)?.series?.get(0)?.columns?.get(2))
            // This is a little arcane but is a reflection of InfluxDB's quite complex result set structure
            return result!!.results[0]!!.series[0]!!.values[0][2] as String
        }
    }

}