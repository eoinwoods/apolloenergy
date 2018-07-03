#!/bin/bash
#
# Simple run script for EnergyApollo to run it via the "fat" jar and using
# the default properties file
#
#logging_config=-Dlog4j.configurationFile=etc/log4j.xml
java $logging_config -jar build/libs/EnergyApollo-all.jar etc/apollo.properties
