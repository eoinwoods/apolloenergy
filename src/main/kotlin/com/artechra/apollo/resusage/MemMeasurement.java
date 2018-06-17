package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_mem")
public class MemMeasurement implements GenericMeasurement {

    @Column(name = "time") private
    Instant timeMillis;
    @Column(name = "container_name", tag = true) private
    String containerName ;
    @Column(name = "usage") private
    long memUsage ;

    public MemMeasurement() {}

    public MemMeasurement(long _timeMillis, String _containerName, long _memUsage) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        containerName = _containerName ;
        memUsage = _memUsage ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getContainerName() {
        return containerName ;
    }

    public long getMemUsage() {
        return memUsage ;
    }

    public long getMeasurementValue() {
        return this.getMemUsage();
    }



    @Override public String toString() {
        return "MemMeasurement{" +
                "timeMillis=" + timeMillis.getEpochSecond() +
                ", containerName='" + containerName + '\'' +
                ", memUsage=" + memUsage +
                '}';
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MemMeasurement that = (MemMeasurement) o;
        return Objects.equals(timeMillis, that.timeMillis) &&
                Objects.equals(containerName, that.containerName) &&
                Objects.equals(memUsage, that.memUsage);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, containerName, memUsage);
    }
}

