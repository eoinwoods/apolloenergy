package com.artechra.apollo.netinfo

class NetInfoDefaultImplementation : NetInfo {
    override fun getAddressesForContainers(): ContainerAddressMap {
        return mapOf("123456789abc" to "192.168.1.1")
    }

    override fun getContainersForAddresses(): AddressContainerMap {
        return mapOf("192.168.1.1" to "123456789abc")
    }
}