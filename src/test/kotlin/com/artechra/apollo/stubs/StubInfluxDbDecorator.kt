package com.artechra.apollo.stubs

import com.artechra.apollo.resusage.InfluxDbDecorator

class StubInfluxDbDecorator(val hostCpuUtilisation :Map<Long, Double>) : InfluxDbDecorator {

    override fun getHostCpuUtilisationDuringPeriod(hostName: String, startTimeMsec: Long, endTimeMsec: Long): Double {
        val startValue= hostCpuUtilisation.getValue(startTimeMsec)
        val endValue= hostCpuUtilisation.getValue(endTimeMsec)
        val delta = endValue - startValue
        val duration = (endTimeMsec - startTimeMsec)/1000
        return startValue + (delta/duration)*(duration/2)
    }


    override fun getBestCpuMeasureForTime(containerId: String, timeMsec: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestMemMeasureForTime(containerId: String, timeMsec: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestDiskIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestNetIoMeasureForTime(containerId: String, timeMsec: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestHostCpuMsecMeasureForTime(hostName: String, timeMsec: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHostForContainerAtTime(containerId: String, timeMsec: Long): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHostCpuCount(hostName: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}