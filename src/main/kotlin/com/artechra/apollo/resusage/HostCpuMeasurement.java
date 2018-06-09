package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import com.artechra.apollo.types.Util;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "cpu")
public class HostCpuMeasurement implements GenericMeasurement {

    @Column(name = "time")
    Instant timeMillis;
    @Column(name = "host", tag = true)
    String hostName ;
    @Column(name = "time_active")
    double cpuUsageSeconds ;

    public HostCpuMeasurement() {}

    public HostCpuMeasurement(long _timeMillis, String _hostName, long _cpuUsageMsec) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        hostName = _hostName ;
        cpuUsageSeconds = Util.msecToSeconds(_cpuUsageMsec) ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getHostName() {
        return hostName ;
    }

    public long getCpuUsageMsec() {
        return Util.secondsToMsec(cpuUsageSeconds) ;
    }

    public long getMeasurementValue() {
        return this.getCpuUsageMsec();
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final HostCpuMeasurement that = (HostCpuMeasurement) o;
        return Double.compare(that.cpuUsageSeconds, cpuUsageSeconds) == 0 &&
                Objects.equals(timeMillis, that.timeMillis) &&
                Objects.equals(hostName, that.hostName);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, hostName, cpuUsageSeconds);
    }

    @Override public String toString() {
        return "HostCpuMeasurement{" +
                "timeMillis=" + timeMillis +
                ", hostName='" + hostName + '\'' +
                ", cpuUsageSeconds=" + cpuUsageSeconds +
                ", cpuUsageMsec=" + getCpuUsageMsec() +
                '}';
    }
}
