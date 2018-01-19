package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_net")
public class NetIoMeasurement implements GenericMeasurement {

    @Column(name = "time")
    Instant timeMillis;
    @Column(name = "container_name", tag = true)
    String containerName ;
    @Column(name = "rx_bytes")
    long rxBytes ;
    @Column(name = "tx_bytes")
    long txBytes ;

    public NetIoMeasurement() {}

    public NetIoMeasurement(long _timeMillis, String _containerName, long _rxBytes, long _txBytes) {
        timeMillis = Instant.ofEpochMilli(_timeMillis);
        containerName = _containerName ;
        rxBytes = _rxBytes ;
        txBytes = _txBytes ;
    }

    public long getTimeMillis() {
        return timeMillis.toEpochMilli() ;
    }

    public String getContainerName() {
        return containerName ;
    }

    public long getRxBytes() {
        return rxBytes ;
    }

    public long getTxBytes() {
        return txBytes ;
    }

    public long getMeasurementValue() {
        return this.getRxBytes() + this.getTxBytes() ;
    }

    @Override public String toString() {
        return "NetIoMeasurement{" +
                "timeMillis=" + timeMillis.getEpochSecond() +
                ", containerName='" + containerName + '\'' +
                ", rxBytes=" + rxBytes +
                ", txBytes=" + txBytes +
                '}';
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final NetIoMeasurement that = (NetIoMeasurement) o;
        return rxBytes == that.rxBytes &&
                txBytes == that.txBytes &&
                Objects.equals(timeMillis, that.timeMillis) &&
                Objects.equals(containerName, that.containerName);
    }

    @Override public int hashCode() {

        return Objects.hash(timeMillis, containerName, rxBytes, txBytes);
    }
}
