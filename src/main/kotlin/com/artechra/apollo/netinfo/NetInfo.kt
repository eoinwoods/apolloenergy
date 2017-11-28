package com.artechra.apollo.netinfo

typealias ContainerAddressMap = Map<String, String>
typealias AddressContainerMap = Map<String, String>
interface NetInfo {
    fun getAddressesForContainers() : ContainerAddressMap
    fun getContainersForAddresses() : AddressContainerMap
}