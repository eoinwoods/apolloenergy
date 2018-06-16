package com.artechra.apollo.netinfo

import com.artechra.apollo.integration.IntegrationTestShared
import com.artechra.apollo.util.TestUtil.Companion.getDataFilePath
import junit.framework.TestCase.assertNull
import org.junit.Test
import kotlin.test.assertEquals

class TestNetInfoDockerJsonImpl {

    fun defaultDataFile() : String {
        return getDataFilePath("test_docker_network.json")
    }

    @Test
    fun testInitialisationLoadsData() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertEquals(2, obj.getNumberOfContainers(), "Wrong number of containers loaded")
    }

    @Test
    fun testExpectedContainerHasNetworkAddress() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val influxAddr = obj.getAddressForContainerId(IntegrationTestShared.INFLUXDB_CONTAINER_ID)
        assertEquals("172.18.0.3", influxAddr, "Wrong address for InfluxDb container")
    }

    @Test
    fun testExpectedContainerHasName() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val influxAddr = obj.getNameForContainerId(IntegrationTestShared.INFLUXDB_CONTAINER_ID)
        assertEquals("influxdb", influxAddr, "Wrong name for InfluxDb container")
    }


    @Test
    fun testNetworkAddressResolvesToCorrectContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayAddr = obj.getContainerIdWithAddress("172.18.0.7")
        assertEquals(IntegrationTestShared.GATEWAY_CONTAINER_ID, gatewayAddr, "Wrong ID for Gateway container address")
    }

    @Test
    fun testNonExistentNetworkAddressReturnsNullContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getContainerIdWithAddress("1.1.1.1"))
    }

    @Test
    fun testNonExistentNameReturnsNullContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getContainerIdWithName("NoSuchContainer"))
    }

    @Test
    fun testNonExistentContainerIdReturnsNullName() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getNameForContainerId("a34488c68b81c5b07fcdf81d6a691b4b462018b62437ab87386b297a95e775FF"))
    }

    @Test
    fun testNonExistentContainerIdReturnsNullAddress() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getAddressForContainerId("a34488c68b81c5b07fcdf81d6a691b4b462018b62437ab87386b297a95e775FF"))
    }

    @Test
    fun testContainerNameResolvesToCorrectContainer() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayName = obj.getContainerIdWithName("gateway")
        assertEquals(IntegrationTestShared.GATEWAY_CONTAINER_ID, gatewayName, "Wrong ID for Gateway container name")
    }

    @Test
    fun testContainerNameResolvesToCorrectAddress() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayIp = obj.getAddressForContainerName("gateway")
        assertEquals("172.18.0.7", gatewayIp, "Wrong network address for Gateway container")

    }

    @Test
    fun testAddressResolvesToCorrectContainerName() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        val gatewayName = obj.getNameForContainerAddress("172.18.0.7")
        assertEquals("gateway", gatewayName, "Wrong name for Gateway container")
    }

    @Test
    fun testNonExistentAddressReturnsNullName() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getNameForContainerAddress("1.1.1.1"))
    }

    @Test
    fun testNonExistentNameReturnsNullAddress() {
        val obj = NetInfoDockerJsonImpl(defaultDataFile())
        assertNull(obj.getAddressForContainerName("NoSuchContainer"))
    }

}