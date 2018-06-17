package com.artechra.apollo.types

import kotlin.math.roundToLong

class Util {
    companion object {
        private const val MSEC_TO_NANOSEC_MULTIPLIER = 1000000L
        private const val MSEC_TO_USEC_MULTIPLIER = 1000L
        private const val SEC_TO_MSEC_MULTIPLIER = 1000L

        @JvmStatic fun usecToMsec(usec : Long) : Long {
            return usec / MSEC_TO_USEC_MULTIPLIER
        }

        @JvmStatic fun msecToNanoSec(msec: Long): Long {
            return msec * MSEC_TO_NANOSEC_MULTIPLIER
        }

        @JvmStatic fun nanosecToMSec(nsec : Long) : Long {
            return nsec / MSEC_TO_NANOSEC_MULTIPLIER
        }

        @JvmStatic fun msecToSeconds(msec : Long) : Double {
            return msec / (SEC_TO_MSEC_MULTIPLIER * 1.0)
        }

        @JvmStatic fun secondsToMsec(seconds: Double) : Long {
            return (seconds * SEC_TO_MSEC_MULTIPLIER).roundToLong()
        }

        fun roundToNDecimalPlaces(number : Double, decimalPlaces : Long) : Double {
            val multiplier = Math.pow(10.0, decimalPlaces.toDouble())
            val intValue = (number * multiplier).roundToLong()
            return intValue / multiplier
        }

        fun interpolateBetweenPoints(point1: Long, value1: Long, point2: Long, value2: Long, requiredPoint: Long): Long {
            // in principle this interpolation works for any point on the line described
            // by the points but to keep things simple limit to use between the points
            assert(requiredPoint in point1..point2, { "expected $point1 <= $requiredPoint <= $point2"})

            val timeDiff = point2 - point1
            val valueDiff = value2 - value1

            val delta = valueDiff / timeDiff.toDouble()

            return value1 + ((requiredPoint - point1) * delta).roundToLong()
        }

    }
}