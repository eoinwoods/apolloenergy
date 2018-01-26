package com.artechra.apollo.netinfo

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File

class NetInfoDockerJsonImpl(netInfoFileName: String) : NetInfo {
    private val containerToAddress: MutableMap<String, String> = HashMap<String, String>()
    private val addressToContainer: MutableMap<String, String> = HashMap<String, String>()
    private val containerIdToName: MutableMap<String, String> = HashMap<String, String>()

    init {
        if (!File(netInfoFileName).canRead()) {
            throw IllegalArgumentException("Could not open file ${netInfoFileName}")
        }

        val jsonData: JsonArray<JsonObject>? = Parser().parse(netInfoFileName) as? JsonArray<JsonObject>
        jsonData ?: throw IllegalStateException("Could not convert parser output to JsonArray")
        jsonData.forEach {
            val containers = it["Containers"] as JsonObject
            containers.forEach {
                val (containerId, _containerData) = it
                val containerData = _containerData as JsonObject
                val containerName = containerData["Name"] as String
                val netAddr = cidrAddressToNetworkAddress(containerData["IPv4Address"] as String)

                containerIdToName.put(containerId, containerName)
                containerToAddress.put(containerId, netAddr)
                addressToContainer.put(netAddr, containerId)
            }
        }
    }

    fun getNumberOfContainers() : Int {
        if (!containerIdToName.size.equals(addressToContainer.size) ||
                !containerIdToName.size.equals(containerToAddress.size)) {
            throw IllegalStateException("NetInfo maps are different sizes ${containerToAddress.size} != ${containerToAddress} != ${addressToContainer.size}")
        }
        return containerIdToName.size
    }
    override fun getAddressesForContainers(): ContainerAddressMap {
        return containerToAddress
    }

    override fun getContainersForAddresses(): AddressContainerMap {
        return addressToContainer
    }

    override fun getContainerIdWithAddress(ipAddr: String): String? {
        return addressToContainer[ipAddr]
    }

    override fun getAddressForContainerId(containerId: String): String? {
        return containerToAddress[containerId]
    }

    // This removes port number and "CIDR" information from the network address by splitting
    // and just taking the first piece which is the IP address or host name
    private fun cidrAddressToNetworkAddress(cidrAddress: String): String {
        val addressItems = cidrAddress.split("/", ":")
        return addressItems[0]
    }

}