package com.artechra.apollo

import com.artechra.apollo.calculator.EnergyCalculator
import com.artechra.apollo.calculator.EnergyCalculatorImpl
import com.artechra.apollo.netinfo.NetInfoDockerJsonImpl
import com.artechra.apollo.resusage.EnergyUsageManagerSimulator
import com.artechra.apollo.resusage.InfluxDbDecoratorImpl
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import org.apache.logging.log4j.LogManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess

val netInfoFile = Key("apollo.network.info.filename", stringType)
val resDbUrl = Key("apollo.resdb.url", stringType)
val resDbName = Key("apollo.resdb.dbname", stringType)
val resDbUser = Key("apollo.resdb.dbname", stringType)
val resDbPass = Key("apollo.resdb.password", stringType)
val traceDbDriver = Key("apollo.tracedb.driver.class", stringType)
val traceDbUrl = Key("apollo.tracedb.url", stringType)
val traceDbUser = Key("apollo.tracedb.username", stringType)
val traceDbPass = Key("apollo.tracedb.password", stringType)

class Application {
    fun assemble(configuration : Configuration) : EnergyCalculator {
        val netInfoFile = configuration.get(netInfoFile)
        val netInfo = NetInfoDockerJsonImpl(netInfoFile)

        val resDbUrl = configuration.get(resDbUrl)
        val resDbName = configuration.get(resDbName)
        val resDbUser = configuration.get(resDbUser)
        val resDbPass = configuration.get(resDbPass)
        val influxDbDecorator = InfluxDbDecoratorImpl(resDbUrl, resDbName, resDbUser, resDbPass)
        val resUsageMgr = ResourceUsageManagerInfluxDbImpl(influxDbDecorator)

        val energyManager = EnergyUsageManagerSimulator(influxDbDecorator)

        val traceDbDriver = configuration.get(traceDbDriver)
        val traceDbUrl = configuration.get(traceDbUrl)
        val traceDbUser = configuration.get(traceDbUser)
        val traceDbPass = configuration.get(traceDbPass)
        val traceMgr = MySqlZipkinTraceManagerImpl(createJdbcTemplate(traceDbDriver, traceDbUrl, traceDbUser, traceDbPass))

        return EnergyCalculatorImpl(resUsageMgr, traceMgr, netInfo, energyManager)
    }

    private fun getConfigItem(name : String, config : Map<String,String>) : String {
        return config[name] ?: throw IllegalStateException("No value for configuration item $name")
    }

    private fun createJdbcTemplate(dbDriver: String, dbUrl : String, dbUser : String, dbPass : String) : JdbcTemplate {
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

    fun loadConfiguration(configFileName : String) : Configuration {

        val config = systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromFile(File(configFileName))

        return config
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
    _log.info("CONFIG: $config")
    val calc = app.assemble(config)
    val energyUsage = calc.calculateEnergyForRequests()
    println("Energy usage for traces:")
    for (u in energyUsage) {
        println("   $u")
    }
}