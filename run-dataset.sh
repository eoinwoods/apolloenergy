#!/bin/bash
# 
# A run script for EnergyApollo to run it via the "fat" jar and execute
# against a specific data set using the default properties file
#
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DATADIR=$DIR/data

if [ "$#" -lt 1 ]
then
   echo USAGE: $0 data-set-name [data-set-name [...]]
   exit 1
fi

for data_set in $*
do
	echo "Data Set $data_set"
	if [ ! -d "$DATADIR/$data_set" -a ! -f "$DATADIR/${data_set}.tar" ]
	then
	   echo "No sign of data set $data_set as a directory or tar(1) file"
	   exit 2
	fi

	if [ ! -d "$DATADIR/$data_set" ]
	then
	   (cd $DATADIR; ./unpack_dataset.sh ${data_set}.tar)
	fi

	(cd $DATADIR && $DATADIR/load_dataset.sh $data_set)

	#logging_config=-Dlog4j.configurationFile=etc/log4j.xml
	# -Dapollo.network.info.filename=abc123
	java $logging_config  \
	    -Dapollo.network.info.filename=$DATADIR/$data_set/docker_network.json \
	    -jar build/libs/EnergyApollo-all.jar \
	    etc/apollo.properties
done
