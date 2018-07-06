Apollo Energy Calculator - V3
-----------------------------

Implementation of the "Apollo" energy estimation approach as a command
line Kotlin program, using Zipkin traces in MySQL and host and Docker
container usage statistics in InfluxDB.

./gradlew clean test
./gradlew shadowJar # to create single JAR in build/libs
./run.sh # to run with default parameters

Configuration in etc/apollo.properties

