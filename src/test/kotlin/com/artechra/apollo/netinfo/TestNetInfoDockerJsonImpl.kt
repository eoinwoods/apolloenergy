package com.artechra.apollo.netinfo

import com.artechra.apollo.integration.IntegrationTestConstants
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TestNetInfoDockerJsonImpl {

    fun getDataFilePath(fileName : String) : String {
        return ClassLoader.getSystemResource(fileName).path
    }

    @Test
    fun testInitialisationLoadsData() {
        val obj = NetInfoDockerJsonImpl(getDataFilePath("test_docker_network.json"))
        assertEquals(6, obj.getNumberOfContainers(), "Wrong number of containers loaded")
    }

    @Test
    fun testExpectedContainerHasNetworkAddress() {
        val obj = NetInfoDockerJsonImpl(getDataFilePath("test_docker_network.json"))
        val influxAddr = obj.getAddressForContainerId(IntegrationTestConstants.INFLUXDB_CONTAINER_ID)
        assertEquals("172.18.0.3", influxAddr, "Wrong address for InfluxDb container")

    }

    @Test
    fun testNetworkAddressResolvesToCorrectContainer() {
        val obj = NetInfoDockerJsonImpl(getDataFilePath("test_docker_network.json"))
        val gatewayAddr = obj.getContainerIdWithAddress("172.18.0.7")
        assertEquals(IntegrationTestConstants.GATEWAY_CONTAINER_ID, gatewayAddr, "Wrong ID for Gateway container address")
    }

}