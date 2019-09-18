#!/bin/sh

touch env
echo ./mvnw package -Dmaven.test.skip=true
./gradlew clean build
docker build -t raspi_finance .
docker run -it --env-file env --rm --name raspi_finance raspi_finance

exit 0
