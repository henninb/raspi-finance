https://www.thomasvitale.com/https-spring-boot-ssl-certificate/

gradle dependencyInsight --dependency slf4j-log4j12
gradle dependencyInsight --dependency log4j-over-slf4j

openssl s_client -connect 192.168.100.25:8080 -CAfile /path/to/cert/crt.pem

Prod
DATASOURCE=jdbc:postgresql://192.168.100.25:5432/finance_db
DATASOURCE_USERNAME=henninb
DATASOURCE_PASSWORD=monday1
JSON_FILES_INPUT_PATH=c:\usr\finance_data\json_in
SERVER_PORT=8080
AMQ_BROKER=localhost
AMQ_USER=
AMQ_PWD=
SSL_TRUSTSTORE=
SSL_TRUSTSTORE_PASSOWRD=
SSL_KEYSTORE=
SSL_KEYSTORE_PASSOWRD=
LOGS=logs

psql -h 192.168.100.25 -p 5432 -U henninb -d finance_db

@RequestMapping - For handling any request type
@GetMapping - For GET request
@PostMapping - For POST request
@PutMapping - for PUT request
@PatchMapping - for PATCH request
@DeleteMapping for DELETE request


Capabilites
Add
Delete
Update
ActiveMQ
SSL


export DATASOURCE=jdbc:postgresql://192.168.100.25:5432/finance_db
export DATASOURCE_USERNAME=henninb
export DATASOURCE_PASSWORD=monday1
export JSON_FILES_INPUT_PATH=c:\usr\finance_data\json_in
export SERVER_PORT=8080
export AMQ_BROKER=localhost
export AMQ_USER=
export AMQ_PWD=
export SSL_TRUSTSTORE=
export SSL_TRUSTSTORE_PASSOWRD=
export SSL_KEYSTORE=
export SSL_KEYSTORE_PASSOWRD=
export LOGS=logs

gradle bootRun

java -jar build/libs/raspi_finance.jar --spring.config.location=src/main/resources/application.properties
