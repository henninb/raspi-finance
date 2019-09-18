#!/bin/sh

./mvnw package -Dmaven.test.skip=true
./gradlew clean build
docker build -t raspi_finance .
docker run -it --rm --name raspi_finance raspi_finance

exit 0
