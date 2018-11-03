@echo off

set SPRING_PROFILES_ACTIVE=offline
set DATASOURCE=jdbc:h2:mem:finance_db;DB_CLOSE_DELAY\=-1
set DATASOURCE_USERNAME=henninb
set DATASOURCE_PASSWORD=monday1
set DATASOURCE_DRIVER=org.h2.Driver
set JSON_FILES_INPUT_PATH=c:\usr\finance_data\json_in
set SERVER_PORT=8080
set AMQ_BROKER_URL=ssl://hornsup:61617
set AMQ_USER=none
set AMQ_PWD=none
set SSL_TRUSTSTORE=C:\Users\z037640\projects\raspi_finance\src\main\resources\amq-client_hornsup.ts
set SSL_TRUSTSTORE_PASSOWRD=monday1
set SSL_KEYSTORE=C:\Users\z037640\projects\raspi_finance\src\main\resources\amq-client_hornsup.ks
set SSL_KEYSTORE_PASSOWRD=monday1
set LOGS=logs
set MONGO_DATABASE=finance_db
set MONGO_HOSTNAME=192.168.100.25
set MONGO_PORT=27017
set MONGO_URI=mongodb://192.168.100.25/finance_db
set ACTIVEMQ_SSL_BEANS_ENABLED=true
set ACTIVEMQ_NONSSL_BEANS_ENABLED=false
set DATABASE_PLATFORM=h2
set HIBERNATE_DDL=create-drop

java -jar build\libs\raspi_finance.jar --spring.config.location=src\main\resources\application.properties

pause
