@echo off

set DATASOURCE=jdbc:postgresql://192.168.100.25:5432/finance_db
echo %DATASOURCE%
set DATASOURCE_USERNAME=henninb
echo %DATASOURCE_USERNAME%
set DATASOURCE_PASSWORD=monday1
echo %DATASOURCE_PASSWORD%
set DATASOURCE_DRIVER=org.postgresql.Driver
echo %DATASOURCE_DRIVER%
set JSON_FILES_INPUT_PATH=C:\usr\finance_data\json_in
echo %JSON_FILES_INPUT_PATH%
set SERVER_PORT=8080
echo %SERVER_PORT%
set AMQ_BROKER_URL=ssl://hornsup:61617
echo %AMQ_BROKER_URL%
set AMQ_USER=none
set AMQ_PWD=none
set SSL_TRUSTSTORE=src\main\resources\amq-client_hornsup.ts
set SSL_TRUSTSTORE_PASSOWRD=monday1
set SSL_KEYSTORE=src\main\resources\amq-client_hornsup.ks
set SSL_KEYSTORE_PASSOWRD=monday1
set LOGS=logs
set MONGO_DATABASE=finance_db
set MONGO_HOSTNAME=192.168.100.25
set MONGO_PORT=27017
set MONGO_URI=mongodb://192.168.100.25/finance_db
set ACTIVEMQ_SSL_BEANS_ENABLED=true
set ACTIVEMQ_NONSSL_BEANS_ENABLED=false

java -jar build\libs\raspi_finance.jar --spring.config.location=src\main\resources\application.properties

pause
