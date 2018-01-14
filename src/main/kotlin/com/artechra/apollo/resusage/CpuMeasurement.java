package com.artechra.apollo.resusage;

import java.time.Instant;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "docker_container_cpu")
public class CpuMeasurement {

    @Column(name = "time")
    Instant time ;
    @Column(name = "container_name", tag = true)
    String containerName ;
    @Column(name = "usage_total")
    Long cpuUsage ;
}
