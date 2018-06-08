package com.artechra.apollo.integration

class IntegrationTestConstants {
    companion object {
        val INFLUX_URL = "http://localhost:8086"
        val DB_NAME    = "telegraf"

        val MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver"
        val MYSQL_DB   = "zipkin"
        val MYSQL_URL  = "jdbc:mysql://localhost" + "/" + MYSQL_DB
        val MYSQL_USER = "zipkin"
        val MYSQL_PASS = "zipkin"

        // These values correspond to data in test data set '20180604-v2-cpu-data-mix'
        val GATEWAY_CONTAINER_ID = "2c7c7e5cc08f8bbf8d64bc506e84e4ba84f6aca91fefd53d133c481762fdac3a"
        val INFLUXDB_CONTAINER_ID = "8b9e3f7bd3c59485dc55db54529be84ba260c0e79e76184f2144c7dd085630f0"
        val SPAN_START_TIME_MS = 1528144292000
        val SPAN_END_TIME_MS   = 1528144299000
        val TEST_SET_NAME = "20180604-v2-cpu-data-mix"

    }
}