#
# apollo-docker-setup.sh - upgrade Ubuntu OS and install docker and 1.80.0 of docker-compose
#                          to allow the Apollo environment to be started using docker-compose
#                          and the apollo-env.yml compose file
#

sudo apt-get update -y
sudo apt-get upgrade -y

# https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/#install-docker-ce-1
sudo apt-get remove docker docker-engine docker.io
sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
sudo apt-get update -y
sudo apt-get install -y docker-ce
sudo docker run hello-world

sudo curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo docker-compose --version
