package com.artechra.apollo.netinfo

import com.artechra.apollo.integration.IntegrationTestConstants
import com.artechra.apollo.util.TestUtil.Companion.getDataFilePath
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TestNetInfoDockerJsonImpl {

    fun defaultDataFile() : String {
        return getDataFilePath("test_docker_network.json")
    }

    @Test
    fun testInitialisationLoadsData() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertEquals(6, obj.getNumberOfContainers(), "Wrong number of containers loaded")
    }

    @Test
    fun testExpectedContainerHasNetworkAddress() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val influxAddr = obj.getAddressForContainerId(IntegrationTestConstants.INFLUXDB_CONTAINER_ID)
        assertEquals("172.18.0.3", influxAddr, "Wrong address for InfluxDb container")
    }

    @Test
    fun testExpectedContainerHasName() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val influxAddr = obj.getNameForContainerId(IntegrationTestConstants.INFLUXDB_CONTAINER_ID)
        assertEquals("influxdb", influxAddr, "Wrong name for InfluxDb container")
    }


    @Test
    fun testNetworkAddressResolvesToCorrectContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayAddr = obj.getContainerIdWithAddress("172.18.0.7")
        assertEquals(IntegrationTestConstants.GATEWAY_CONTAINER_ID, gatewayAddr, "Wrong ID for Gateway container address")
    }

    @Test
    fun testContainerNameResolvesToCorrectContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayName = obj.getContainerIdWithName("gateway")
        assertEquals(IntegrationTestConstants.GATEWAY_CONTAINER_ID, gatewayName, "Wrong ID for Gateway container name")
    }

}