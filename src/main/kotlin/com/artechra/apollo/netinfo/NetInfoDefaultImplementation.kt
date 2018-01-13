package com.artechra.apollo.netinfo

class NetInfoDefaultImplementation : NetInfo {

    override fun getAddressesForContainers(): ContainerAddressMap {
        return mapOf("123456789abc" to "192.168.1.1:0")
    }

    override fun getContainersForAddresses(): AddressContainerMap {
        return mapOf("192.168.1.1:0" to "123456789abc")
    }

    override fun getContainerIdWithAddress(ipAddr: String): String? {
        return "123456789abc"
    }

    override fun getAddressForContainerId(containerId: String): String? {
        return "192.168.1.1:0"
    }
}