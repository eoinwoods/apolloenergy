#!/bin/bash -x
if [ ! -r $1 ]
then
   echo Usage: $0 datasetfile.tar
fi

tarfile=$1
dataset_name=$(basename $tarfile .tar)
telegraf_file=telegraf.line.dmp
zipkin_file=zipkin_db.sql

echo Adding name $dataset_name to data set in file $tarfile

mkdir $dataset_name
tar -C $dataset_name -f $tarfile -x

influxrow="apollo_check,data_set=$dataset_name,application=apollo value=1 1527811200000000000"
zipkinrow="insert into zipkin.zipkin_spans values(0, 0, 1, '$dataset_name', 0, null, 1527811200000, 0);"
(cd $dataset_name; echo $influxrow >> $telegraf_file)
(cd $dataset_name; echo $zipkinrow >> $zipkin_file)

tar -C $dataset_name -cf $tarfile .

#rm -rf $dataset_name

