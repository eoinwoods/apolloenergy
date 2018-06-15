package com.artechra.apollo.resusage;

import java.time.Instant;
import java.util.Objects;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "system")
public class HostCpuCountMeasurement {

    @Column(name = "host", tag = true)
    String hostName ;
    @Column(name = "n_cpus")
    long cpuCount;

    public String getHostName() {
        return hostName;
    }

    public long getCpuCount() {
        return cpuCount;
    }

    @Override public String toString() {
        return "HostCpuCountMeasurement{" +
                "hostName='" + hostName + '\'' +
                ", cpuCount=" + cpuCount +
                '}';
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final HostCpuCountMeasurement that = (HostCpuCountMeasurement) o;
        return cpuCount == that.cpuCount &&
                Objects.equals(hostName, that.hostName);
    }

    @Override public int hashCode() {

        return Objects.hash(hostName, cpuCount);
    }
}
