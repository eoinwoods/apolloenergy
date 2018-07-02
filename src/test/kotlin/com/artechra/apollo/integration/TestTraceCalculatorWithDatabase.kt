package com.artechra.apollo.integration

import com.artechra.apollo.calculator.TraceCalculator
import com.artechra.apollo.resusage.InfluxDbDecoratorImpl
import com.artechra.apollo.resusage.ResourceUsageManagerInfluxDbImpl
import com.artechra.apollo.stubs.StubEnergyManager
import com.artechra.apollo.stubs.StubNetInfo
import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import org.junit.Test
import kotlin.test.assertTrue

class TestTraceCalculatorWithDatabase {

    private val traceCalculator: TraceCalculator

    init {

    }
    init {
        // Container and network addresses from the 20180610-cpu-data-mix test data set
        // This avoids needing to find the docker_network.json corresponding to the test set
        // at the cost of a code update if the test set data changes
        val containerNetworkMap = mapOf(
                "1e8b4fa5f52eb9bb6986f03a566f3d35d11c4631f493d6190266c59bb99d6b66" to "172.18.0.4",
                "3b74c6bc66d39d41d32c51a3b8a8b43f13711f3fd2e7d74017bc0b286acce104" to "172.18.0.5",
                "7a822ae188738e97b404f7fd4f5249676d41763c8b634f2055c139781e7347fe" to "172.18.0.3",
                "86cf00e40897e6a1a3ea34aa02662c3db8db0fa155edaa18b3a876b273962524" to "172.18.0.2",
                "9cb96ed3f1d1fadde1555254be046a0455847dbff245b493f106f18429d7b4d4" to "172.18.0.6",
                "da3bf272f26218d250d6f52d7e2736bc5783996e7cef803623f2b066752aefdb" to "172.18.0.7",
                "ffd31b51662c9e786bc476329474ed51b4ba780652bebd9f8b73eee7915d7da2" to "172.18.0.8"
        )


        val dbconn = InfluxDbDecoratorImpl(IntegrationTestShared.INFLUX_URL, IntegrationTestShared.DB_NAME)
        val resUsageManager = ResourceUsageManagerInfluxDbImpl(dbconn)
        val stubNetInfo = StubNetInfo(containerNetworkMap.map{(k,v) -> v to k}.toMap(), containerNetworkMap)
        assertTrue(stubNetInfo.addrToContainerMap.keys.toTypedArray() contentEquals
                stubNetInfo.containerToAddrMap.values.toTypedArray())
        assertTrue(stubNetInfo.containerToAddrMap.keys.toTypedArray() contentEquals
                stubNetInfo.addrToContainerMap.values.toTypedArray())
        traceCalculator = TraceCalculator(resUsageManager, stubNetInfo, StubEnergyManager())
    }

    // Note that this mocked up test relies on values in the InfluxDB database matching
    // the 20180610-cpu-and-data-mix dataset for network address and start and end time
    @Test
    fun testThatCalculationForTraceReturnsNonZeroValues() {
        val testTrace = Trace("test_trace1", setOf(Span("468CE081D5B05907", "EA3F1D9042A036B0",
                "172.18.0.4", 1528666470513, 1528666470832)))
        val estimate = traceCalculator.calculateCpuMsecAndEnergyJoulesEstimateForTrace(testTrace)
        assertTrue(estimate.energyUsageJoules > 0)
        assertTrue(estimate.totalCpuMsec > 0)
    }

}