package com.artechra.apollo.integration

class TestConstants {
    companion object {
        val INFLUX_URL = "http://localhost:8086"
        val DB_NAME    = "telegraf"

        // These values correspond to data in test data set 3
        val CONTAINERID = "5843205e6e17aaefcae8be0f6109baf1334c6b55a051f43e1dd4e959492aa228"
        val SPAN_START_TIME_MS = 1515237435811
        val SPAN_END_TIME_MS   = 1515237438736
        val TEST_SET_NAME = "set3"

    }
}