#!/bin/sh

docker build -t my-java-app .
docker run -it --rm --name my-running-app my-java-app

exit 0
