package com.artechra.apollo.resusage

import com.artechra.apollo.types.Util
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
        val utilisation = Util.truncateDecimalPlaces(hostUtilisation , 2)
        println("HU=$hostUtilisation U=$utilisation")
        val lowBound = percentageDecimalToTenPercentValue(utilisation)
        assert(lowBound % 10 == 0 && lowBound <= 100 && lowBound >= 0)
        val highBound = lowBound + 10
        assert(highBound % 10 == 0 && highBound <= 100 && highBound >= 0)


        val secIncrement = (utilisation*100 - lowBound).roundToLong()
        val rp = calcRequiredPoint(startTimeMsec, endTimeMsec, secondsIncrement = secIncrement)

        val lowValue = powerConsumptionMetrics[lowBound]!!
        val highValue = powerConsumptionMetrics[highBound]!!
        val powerEstimateTimes100 = Util.interpolateBetweenPoints(startTimeMsec, (lowValue * 100).roundToLong(),
                endTimeMsec, (highValue*100).roundToLong(), rp)

        // TODO - this is returning Power (Watts) total which is clearly nonsence.  Need to
        // convert to actual energy - Joules.
        return powerEstimateTimes100 / 100
    }

    fun calcRequiredPoint(startTimeMsec : Long, endTimeMsec : Long, secondsIncrement : Long) : Long {
        return startTimeMsec + (secondsIncrement*1000)
    }

    fun percentageDecimalToTenPercentValue(percentage : Double) : Int {
        assert(percentage < 1.0 && percentage > 0.0)
        return (percentage*10).roundToInt() * 10
    }

}