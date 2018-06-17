package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class EnergyUsageManagerSimulator(val influxdb : InfluxDbDecorator) : EnergyUsageManager {

    // Dell PowerEdge R730 (Intel Xeon E5-2699 v4 2.20 GHz)
    // Average watts power consumption at different loads:
    //  100%  90%  80%  70%  60%  50%  40%  30%  20% 10%    ActIdle
    //  272	 238  205  181  163  150  136  120  102  84.8	44.6
    //
    // Measured via the SPEC Power SSJ 2008 test: https://www.spec.org/power_ssj2008/

    val powerConsumptionMetrics = mapOf(
            100 to 272.0,
             90 to 238.0,
             80 to 205.0,
             70 to 181.0,
             60 to 163.0,
             50 to 150.0,
             40 to 136.0,
             30 to 120.0,
             20 to 102.0,
             10 to 84.8,
              0 to 44.6
            )

    override fun getEnergyUsageForHostInJoules(hostName: String, startTimeMsec: Long, endTimeMsec: Long): Long {

        val hostUtilisation = influxdb.getHostCpuUtilisationDuringPeriod(hostName, startTimeMsec, endTimeMsec)
        val utilisation = Util.roundToNDecimalPlaces(hostUtilisation , 2)
        val lowBound = calculateLowerPercentageBound(utilisation)
        assert(lowBound % 10 == 0 && lowBound <= 90 && lowBound >= 0, {-> "invalid low bound $lowBound"})
        val highBound = lowBound + 10
        assert(highBound % 10 == 0 && highBound <= 100 && highBound >= 10, {-> "invalid high bound $highBound"})


        // The data (above) from the benchmark gives us power consumption at 10% utilisation intervals
        // so calculate an interpolated power value for the power consumption at the actual CPU utilisation
        val lowValue = powerConsumptionMetrics[lowBound]!!
        val highValue = powerConsumptionMetrics[highBound]!!
        val powerEstimateW = calculatePowerValueEstimateForUtilisationInW(lowBound, lowValue, highBound, highValue, utilisation)

        val durationSec = (endTimeMsec - startTimeMsec)/1000
        // J = W * seconds
        return (powerEstimateW * durationSec).roundToLong()
    }

    // Converts decimal between 0 and 0.99 to next lowest 10% value (e.g. 0.47 to 40)
    fun calculateLowerPercentageBound(percentage : Double) : Int {
        assert(percentage < 1.0 && percentage > 0.0)
        return floor(percentage*10).roundToInt() * 10
    }

    fun calculatePowerValueEstimateForUtilisationInW(lowBound : Int, lowPowerValue : Double,
                                                  highBound : Int, highPowerValue : Double,
                                                  actualUtilisation : Double) : Double {
        val powerEstimateTimes100 = Util.interpolateBetweenPoints(lowBound.toLong(),(lowPowerValue * 100).roundToLong(),
                highBound.toLong(), (highPowerValue*100).roundToLong(), (actualUtilisation*100).roundToLong())
        return powerEstimateTimes100 / 100.0

    }

}