#!/bin/bash -x
# 
# A run script for EnergyApollo to run it via the "fat" jar and execute
# against a specific data set using the default properties file
#
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DATADIR=$DIR/data

if [ "$#" -lt 1 ]
then
   echo USAGE: $0 data-set-name
   exit 1
fi
data_set=$1

if [ ! -d "$DATADIR/$data_set" -a ! -f "$DATADIR/${data_set}.tar" ]
then
   echo "No sign of data set $data_set as a directory or tar(1) file"
   exit 2
fi

(cd $DATADIR && $DATADIR/load_dataset.sh $data_set)

if [ ! -d "$DATADIR/$data_set" ]
then
   $DATA_DIR/unpack_dataset.sh $data_set
fi

#logging_config=-Dlog4j.configurationFile=etc/log4j.xml
# -Dapollo.network.info.filename=abc123
java $logging_config  \
    -Dapollo.network.info.filename=$DATADIR/$data_set/docker_network.json \
    -jar build/libs/EnergyApollo-all.jar \
    etc/apollo.properties

