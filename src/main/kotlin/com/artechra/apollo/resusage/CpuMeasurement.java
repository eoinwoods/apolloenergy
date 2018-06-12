package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import com.artechra.apollo.types.Util;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_cpu")
public class CpuMeasurement implements GenericMeasurement {

    @Column(name = "time")
    Instant timeMillis;
    @Column(name = "container_name", tag = true)
    String containerName ;
    @Column(name = "host", tag = true)
    String hostName ;
    @Column(name = "usage_total")
    long cpuUsageNsec;

    public CpuMeasurement() {}

    public CpuMeasurement(long _timeMillis, String _containerName, String _hostName, long _cpuUsageNsec) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        containerName = _containerName ;
        hostName = _hostName ;
        cpuUsageNsec = _cpuUsageNsec ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getContainerName() {
        return containerName ;
    }

    public String getHostName() { return hostName ; }

    public long getCpuUsageNsec() {
        return cpuUsageNsec ;
    }

    public long getCpuUsageMsec() {
        return Util.nanosecToMSec(cpuUsageNsec) ;
    }

    public long getMeasurementValue() {
        return this.getCpuUsageMsec();
    }



    @Override public String toString() {
        return "CpuMeasurement{" +
                "timeMillis=" + timeMillis.getEpochSecond() +
                ", containerName='" + containerName + '\'' +
                ", hostName='"      + hostName + '\'' +
                ", cpuUsageNsec="   + cpuUsageNsec +
                ", cpuUsageMsec="   + getCpuUsageMsec() +
                '}';
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final CpuMeasurement that = (CpuMeasurement) o;
        return Objects.equals(timeMillis, that.timeMillis) &&
                Objects.equals(containerName, that.containerName) &&
                Objects.equals(hostName, that.hostName) &&
                Objects.equals(cpuUsageNsec, that.cpuUsageNsec);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, containerName, hostName, cpuUsageNsec);
    }
}
