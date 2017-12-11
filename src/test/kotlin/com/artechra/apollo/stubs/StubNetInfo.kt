package com.artechra.apollo.stubs

import com.artechra.apollo.netinfo.AddressContainerMap
import com.artechra.apollo.netinfo.ContainerAddressMap
import com.artechra.apollo.netinfo.NetInfo

class StubNetInfo(val addrToContainerMap : AddressContainerMap, val containerToAddrMap : ContainerAddressMap) : NetInfo {

    override fun getContainersForAddresses(): AddressContainerMap {
        return addrToContainerMap
    }

    override fun getAddressesForContainers(): ContainerAddressMap {
        return containerToAddrMap
    }

    override fun getContainerIdWithAddress(ipAddr: String): String? {
        return addrToContainerMap[ipAddr]
    }

    override fun getAddressForContainerId(containerId: String): String? {
        return containerToAddrMap[containerId]
    }
}