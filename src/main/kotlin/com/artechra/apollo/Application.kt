package com.artechra.apollo

import com.artechra.apollo.archdesc.ArchitectureManagerDefaultImpl
import com.artechra.apollo.archdesc.ArchitectureManagerJsonImpl
import com.artechra.apollo.calculator.EnergyCalculator
import com.artechra.apollo.calculator.EnergyCalculatorImpl
import com.artechra.apollo.netinfo.NetInfoDockerJsonImpl
import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import org.apache.logging.log4j.LogManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess


class Application {
    fun assemble(configuration : Map<String, String>) : EnergyCalculator {
        val archDescFile = getConfigItem("apollo.architecture.description.filename", configuration)
        val archMgr = ArchitectureManagerJsonImpl(archDescFile)
        val netInfoFile = getConfigItem("apollo.network.info.filename", configuration)
        val netInfo = NetInfoDockerJsonImpl(netInfoFile)

        val resDbUrl = getConfigItem("apollo.resdb.url", configuration)
        val resDbName = getConfigItem("apollo.resdb.dbname", configuration)
        val resDbUser = getConfigItem("apollo.resdb.dbname", configuration)
        val resDbPass = getConfigItem("apollo.resdb.password", configuration)
        val influxDbDecorator = InfluxDbDecorator(resDbUrl, resDbName, resDbUser, resDbPass)
        val resUsageMgr = ResourceUsageManagerInfluxDbImpl(influxDbDecorator)


        val traceDbDriver = getConfigItem("apollo.tracedb.driver.class", configuration)
        val traceDbUrl = getConfigItem("apollo.tracedb.url", configuration)
        val traceDbUser = getConfigItem("apollo.tracedb.username", configuration)
        val traceDbPass = getConfigItem("apollo.tracedb.password", configuration)
        val traceMgr = MySqlZipkinTraceManagerImpl(createJdbcTemplate(traceDbDriver, traceDbUrl, traceDbUser, traceDbPass))

        return EnergyCalculatorImpl(resUsageMgr, traceMgr, netInfo, archMgr)
    }

    private fun getConfigItem(name : String, config : Map<String,String>) : String {
        return config[name] ?: throw IllegalStateException("No value for configuration item ${name}")
    }

    fun createJdbcTemplate(dbDriver: String, dbUrl : String, dbUser : String, dbPass : String) : JdbcTemplate {
            // Creates a new instance of DriverManagerDataSource and sets
            // the required parameters such as the Jdbc Driver class,
            // Jdbc URL, database user name and password.
            val dataSource = DriverManagerDataSource()
            dataSource.setDriverClassName(dbDriver)
            dataSource.url = dbUrl
            dataSource.username = dbUser
            dataSource.password = dbPass
            return JdbcTemplate(dataSource)
    }

    fun loadConfiguration(configFileName : String) : Map<String, String> {
        val prop = Properties()
        FileInputStream(configFileName).use {
            prop.load(it)
        }

        val ret : MutableMap<String, String> = HashMap()
        for (k in prop.keys) {
            val key = k as String
            val value = prop[key] as String
            ret.put(key, value)
        }
        return ret
    }

}

fun main(args: Array<String>) {

    val _log = LogManager.getLogger(Application::class.java.name)


    if (args.size != 1) {
        println("USAGE: com.artechra.apollo.Application config_props_file")
        exitProcess(1)
    }
    val propsFileName = args[0]
    val app = Application()
    val config = app.loadConfiguration(propsFileName)
    _log.info("CONFIG: " + config)
    val calc = app.assemble(config)
    val energyUsage = calc.calculateEnergyForRequests()
    println("Energy usage for traces:")
    for (u in energyUsage) {
        println("   $u")
    }
}