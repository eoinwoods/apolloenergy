package com.artechra.apollo.types

import kotlin.math.roundToLong

class Util {
    companion object {
        val MSEC_TO_NANOSEC_MULTIPLIER = 1000000L
        val MSEC_TO_USEC_MULTIPLIER = 1000L

        fun usecToMsec(usec : Long) : Long {
            return usec / MSEC_TO_USEC_MULTIPLIER
        }

        fun msecToNanoSec(msec: Long): Long {
            return msec * MSEC_TO_NANOSEC_MULTIPLIER
        }

        fun nanosecToMSec(nsec : Long) : Long {
            return nsec / MSEC_TO_NANOSEC_MULTIPLIER
        }

        fun interpolateBetweenPoints(point1: Long, value1: Long, point2: Long, value2: Long, requiredPoint: Long): Long {
            // in principle this interpolation works for any point on the line described
            // by the points but to keep things simple limit to use between the points
            assert(requiredPoint >= point1 && requiredPoint <= point2)

            val timeDiff = point2 - point1
            val valueDiff = value2 - value1

            val delta = valueDiff / timeDiff.toDouble()

            val requiredValue = value1 + ((requiredPoint - point1) * delta).roundToLong()
            return requiredValue
        }

    }
}