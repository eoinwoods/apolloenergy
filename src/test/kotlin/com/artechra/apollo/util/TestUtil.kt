package com.artechra.apollo.util

class TestUtil {

    companion object {
        fun getDataFilePath(fileName : String) : String {
            return ClassLoader.getSystemResource(fileName).path
        }
    }
}