package com.artechra.apollo

import com.artechra.apollo.archdesc.ArchitectureManagerDefaultImpl
import com.artechra.apollo.calculator.EnergyCalculator
import com.artechra.apollo.calculator.EnergyCalculatorImpl
import com.artechra.apollo.netinfo.NetInfoDockerJsonImpl
import com.artechra.apollo.resusage.InfluxDbDecorator
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import com.artechra.apollo.traces.MySqlZipkinTraceManagerImpl
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap


class Application {
    fun assemble(configuration : Map<String, String>) : EnergyCalculator {
        val archMgr = ArchitectureManagerDefaultImpl()
        val netInfo = NetInfoDockerJsonImpl(configuration["apollo.network.info.filename"]!!)
        val influxDbDecorator = InfluxDbDecorator(configuration["apollo.resdb.url"]!!, configuration["apollo.resdb.dbname"]!!,
                configuration["apollo.resdb.username"], configuration["apollo.resdb.password"])
        val resUsageMgr = ResourceUsageManagerInfluxDbImpl(influxDbDecorator)

        val traceMgr = MySqlZipkinTraceManagerImpl(createJdbcTemplate(configuration))

        return EnergyCalculatorImpl(resUsageMgr, traceMgr, netInfo, archMgr)
    }

    fun createJdbcTemplate(configuration : Map<String, String>) : JdbcTemplate {
            // Creates a new instance of DriverManagerDataSource and sets
            // the required parameters such as the Jdbc Driver class,
            // Jdbc URL, database user name and password.
            val dataSource = DriverManagerDataSource()
            dataSource.setDriverClassName(configuration["apollo.tracedb.driver.class"])
            dataSource.url = configuration["apollo.tracedb.url"]
            dataSource.username = configuration["apollo.tracedb.username"]
            dataSource.password = configuration["apollo.tracedb.password"]
            return JdbcTemplate(dataSource)
    }

    fun loadConfiguration(configFileName : String) : Map<String, String> {
        val configFileLocation = ClassLoader.getSystemResource(configFileName)

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

    val app = Application()
    val config = app.loadConfiguration("TOSUCHFILE")
    val calc = app.assemble(config)
    calc.calculateEnergyForRequests()
}