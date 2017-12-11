package com.artechra.apollo.netinfo

typealias ContainerAddressMap = Map<String, String>
typealias AddressContainerMap = Map<String, String>
interface NetInfo {
    fun getAddressesForContainers() : ContainerAddressMap
    fun getContainersForAddresses() : AddressContainerMap

    fun getContainerIdWithAddress(ipAddr : String) : String?
    fun getAddressForContainerId(containerId : String) : String?
}