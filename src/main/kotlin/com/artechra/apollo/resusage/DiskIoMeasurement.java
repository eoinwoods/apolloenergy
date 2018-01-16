package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_blkio")
public class DiskIoMeasurement implements GenericMeasurement {

    @Column(name = "time")
    Instant timeMillis;
    @Column(name = "container_name", tag = true)
    String containerName ;
    @Column(name = "io_service_bytes_recursive_total")
    long diskIoBytes ;

    public DiskIoMeasurement() {}

    public DiskIoMeasurement(long _timeMillis, String _containerName, long _diskIoBytes) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        containerName = _containerName ;
        diskIoBytes = _diskIoBytes ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getContainerName() {
        return containerName ;
    }

    public long getDiskIoBytes() {
        return diskIoBytes ;
    }

    public long getMeasurementValue() {
        return this.getDiskIoBytes();
    }

    @Override public String toString() {
        return "DiskIoMeasurement{" +
                "timeMillis=" + timeMillis.getEpochSecond() +
                ", containerName='" + containerName + '\'' +
                ", diskIoBytes=" + diskIoBytes +
                '}';
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final DiskIoMeasurement that = (DiskIoMeasurement) o;
        return Objects.equals(timeMillis, that.timeMillis) &&
                Objects.equals(containerName, that.containerName) &&
                Objects.equals(diskIoBytes, that.diskIoBytes);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, containerName, diskIoBytes);
    }

}
