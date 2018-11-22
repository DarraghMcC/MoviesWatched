#! /bin/bash

set -e


USAGE=$'Interact with the movies watched application
Usage: movies.sh <start|stop> 
  Examples: movies.sh start'

usage() {
    echo "${USAGE}"
}

start() {
	echo "Building application"
	gradle clean build docker


	echo "Starting docker environment"
	docker-compose -f docker/docker-compose.yml up -d
}

stop() {
  echo "Stopping docker environment"
  docker-compose -f docker/docker-compose.yml down
}


getProperty() {
    echo `jq '.[] | select(.Env=="'${1}'") | .'${2}'' env-release-properties.json`
}

if [ $# -lt 1 ]; then
  echo Incorrect number of arguments
  usage
  exit 1
fi

OPERATION=$1


 if [ "$OPERATION" == "start" ]; then
 	start
 elif [ "$OPERATION" == "stop" ]; then
 	stop
 else
 	echo Invalid command
 	usage
 fi
