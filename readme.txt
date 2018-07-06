Apollo Energy Calculator - v2
-----------------------------

Implementation of the "Apollo" energy estimation approach as a command
line Kotlin program, using Zipkin traces in MySQL and host and Docker
container usage statistics in InfluxDB.

./gradlew clean test
./gradlew shadowJar # to create single JAR in build/libs
./run.sh # to run with default parameters

Configuration in etc/apollo.properties

This version of the code was completed on 6th July 2018 (tag v2_finalfixes).

It was abandoned due to the problem of estimating usage consumption by
an individual span within sample intervals.  V3 solves this by measuring
resource usage at container level.

