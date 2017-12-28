#
#  run-influxdb-client.sh - script to record how to start the InfluxDB command
#                           line client ("influx") using Docker in the Apollo
#                           environment
#
# Note: "-it" == interactive and allocate a tty
sudo docker run -it --net=container:influxdb influxdb influx
