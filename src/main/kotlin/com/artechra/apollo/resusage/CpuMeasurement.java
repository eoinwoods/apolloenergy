package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_cpu")
public class CpuMeasurement {

    @Column(name = "time")
    Instant timeMillis;
    @Column(name = "container_name", tag = true)
    String containerName ;
    @Column(name = "usage_total")
    long cpuUsage ;

    public CpuMeasurement() {}

    public CpuMeasurement(long _timeMillis, String _containerName, long _cpuUsage) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        containerName = _containerName ;
        cpuUsage = _cpuUsage ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getContainerName() {
        return containerName ;
    }

    public long getCpuUsage() {
        return cpuUsage ;
    }



    @Override public String toString() {
        return "CpuMeasurement{" +
                "timeMillis=" + timeMillis.getEpochSecond() +
                ", containerName='" + containerName + '\'' +
                ", cpuUsage=" + cpuUsage +
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
                Objects.equals(cpuUsage, that.cpuUsage);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, containerName, cpuUsage);
    }
}
