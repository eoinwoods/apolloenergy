#!/bin/bash 
export MYSQL_USER=zipkin
export MYSQL_PASS=zipkin
export MYSQL_DB=zipkin
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export ZIPKIN_DATAFILE=zipkin_db.sql

export INFLUX_DB=telegraf
export INFLUX_HOST=localhost
export INFLUX_PORT=8086
export INFLUX_METRICS_DATAFILE=telegraf.line.dmp

USAGE="$0 datasetname"
if [ $# -ne 1 ]
then
   echo $USAGE
fi
setname=$1
setarchive=$(ls *$setname*tar)
if [ ! -d $setname -a ! -f $setarchive ]
then
   echo "Could not find directory $setname or dataset archive $setarchive"
   exit 1
fi

dataDir=""
if [ ! -d $setname ] 
then
	dataDir=$TMPDIR/$setname
	mkdir $dataDir
	tar -C $dataDir -xf $setarchive   
else
   dataDir=$PWD/$setname
fi
echo "Loading data set $setname from $dataDir"

which -s mysql
export notFoundMysql=$?
which -s influx
export notFoundInflux=$?

if (( ( $notFoundMysql ) || ( $notFoundInflux ) ))
then
   echo "Cannot find 'mysql' or 'influx' commands"
   exit 1
fi

echo "Dropping database $MYSQL_DB from MySQL"
mysql -h $MYSQL_HOST -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASS <<EOF
drop database if exists $MYSQL_DB ;
EOF

echo "Dropping datbase $INFLUX_DB from InfluxDB"
influx -host $INFLUX_HOST -port $INFLUX_PORT <<EOF
drop database $INFLUX_DB
EOF

echo "Loading MySQL Zipkin data from $ZIPKIN_DATAFILE"
(echo "create database $MYSQL_DB;" | mysql  -h $MYSQL_HOST -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASS)
(cd $dataDir; mysql -h $MYSQL_HOST -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASS -D$MYSQL_DB < $ZIPKIN_DATAFILE)

echo "Loading Influx DB data from $INFLUX_METRICS_DATAFILE"
(cd $dataDir; influx -import -path $INFLUX_METRICS_DATAFILE)


