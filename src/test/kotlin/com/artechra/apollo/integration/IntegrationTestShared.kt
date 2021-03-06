package com.artechra.apollo.integration

import com.artechra.apollo.resusage.InfluxDbDecoratorImpl
import org.influxdb.dto.Query
import kotlin.test.assertEquals

class IntegrationTestShared {
    companion object {
        const val INFLUX_URL = "http://localhost:8086"
        const val DB_NAME    = "telegraf"

        const val MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver"
        const val MYSQL_DB   = "zipkin"
        const val MYSQL_URL  = "jdbc:mysql://localhost/$MYSQL_DB"
        const val MYSQL_USER = "zipkin"
        const val MYSQL_PASS = "zipkin"

        // These values correspond to data in test data set 'itest-dataset-20180610'
        const val TEST_SET_NAME = "itest-dataset-20180610"
        const val GATEWAY_CONTAINER_ID = "ffd31b51662c9e786bc476329474ed51b4ba780652bebd9f8b73eee7915d7da2"
        const val INFLUXDB_CONTAINER_ID = "7a822ae188738e97b404f7fd4f5249676d41763c8b634f2055c139781e7347fe"
        const val SPAN_START_TIME_MS = 1528666358000
        const val SPAN_END_TIME_MS   = 1528666372000
        const val HOST_NAME = "7a822ae18873"
        const val MULTI_SPAN_ZIPKIN_TRACE_ID = "468CE081D5B05907"
        const val MULTI_SPAN_ZIPKIN_TRACE_NAME = "http:/invoke/cpu-data-mix"

        fun getTestDataSetLoadedName(dbConn : InfluxDbDecoratorImpl, databaseName : String) : String {
            val query = Query("SELECT * FROM apollo_check WHERE value = 1", databaseName)
            val result = dbConn.getInfluxDbConnection().query(query)
            assertEquals("data_set", result?.results?.get(0)?.series?.get(0)?.columns?.get(2))
            // This is a little arcane but is a reflection of InfluxDB's quite complex result set structure
            return result!!.results[0]!!.series[0]!!.values[0][2] as String
        }
    }

}