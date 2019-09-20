#!/bin/sh

mkdir -p logs ssl json_in
HOST_BASEDIR=$(pwd)
GUEST_BASEDIR=/opt/raspi_finance
export LOGS=$BASEDIR/logs
touch env
echo ./mvnw package -Dmaven.test.skip=true
./gradlew clean build
docker build -t raspi_finance .
docker run -it --add-host hornsup:172.17.0.1 -p 8080:8080 --env-file env -v $HOST_BASEDIR/logs:$GUEST_BASEDIR/logs -v $HOST_BASEDIR/ssl:$GUEST_BASEDIR/ssl -v $HOST_BASEDIR/json_in:$GUEST_BASEDIR/json_in --rm --name raspi_finance raspi_finance

exit 0
