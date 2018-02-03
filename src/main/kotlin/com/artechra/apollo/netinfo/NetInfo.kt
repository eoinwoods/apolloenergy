package com.artechra.apollo.netinfo

typealias ContainerAddressMap = Map<String, String>
typealias AddressContainerMap = Map<String, String>

/**
 * The interface to the internal service that maps between Docker container IDs
 * and IP based network addresses.
 *
 * The interface doesn't try to create complex types for these parameters and both
 * are treated as strings, so some care is necessary.
 *
 * Container IDs are either short (12 lowercase hex digits e.g. "ad6d5d32576a")
 * or long (64  lowercase hex digits
 * e.g. "ad6d5d32576ad3cb1fcaa59b564b8f6f22b079631080ab1a3bbac9199953eb7d")
 *
 * The implementation of this interface will take reasonable care to match
 * containers as well as it can, by converting upper case to lower case hex
 * digits and for short IDs trying to match them as the prefix of a long ID
 * if a short ID doesn't match.
 *
 * Network addresses are the IPv4 address in dotted form plus a port number,
 * following the customary format "192.168.1.2:123"  If the port number is
 * unknown or not significant then "0" can be used and this will become a
 * wild card when matching.
 *
 */
interface NetInfo {
    fun getAddressesForContainers() : ContainerAddressMap
    fun getContainersForAddresses() : AddressContainerMap

    fun getContainerIdWithAddress(ipAddr : String) : String?
    fun getAddressForContainerId(containerId : String) : String?
    fun getNameForContainerId(containerId: String) : String?
    fun getContainerIdWithName(name : String) : String?
}