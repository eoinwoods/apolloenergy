#!/bin/bash
#
# unpack_dataset.sh - unpack a .tar file into a directory of its basename
#
if [ "$#" -lt 1 ]
then
   echo USAGE: $0 file1.tar [file2.tar [...]]
   exit 1
fi

for f in $*
do
   if [ ! -r "$f" ]
   then
      echo "Warning: could not read $f - skipping"
   fi
   if [ "$f" == "$(basename $f .tar)" ]
   then
      echo "Warning: $f is not a tar file - skipping"
   fi
   dir_name="$(basename $f .tar)"
   echo "$f --> $dir_name"
   mkdir $dir_name
   tar -C $dir_name -xf $f
done
