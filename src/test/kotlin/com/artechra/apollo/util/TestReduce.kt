package com.artechra.apollo.util

import kotlin.test.Test

class TestReduce {
    @Test
    fun testReduce() {
       data class Item(val id : Int, val value : Double)

        val itemList = listOf(Item(1, 100.0), Item(2, 101.0), Item(3, 102.0))

        println(itemList.map{i -> i.value}.reduce{v,w -> v+w})
    }
}