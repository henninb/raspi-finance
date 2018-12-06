Capabilites TODO
Tomcat SSL

home
SPRING_PROFILES_ACTIVE=home
DATASOURCE=jdbc:postgresql://192.168.100.25:5432/finance_db
DATASOURCE_USERNAME=henninb
DATASOURCE_PASSWORD=monday1
JSON_FILES_INPUT_PATH=c:\usr\finance_data\json_in
SERVER_PORT=8080
AMQ_BROKER_URL=ssl://hornsup:61617
AMQ_USER=
AMQ_PWD=
SSL_TRUSTSTORE=amq-client_hornsup.ts
SSL_TRUSTSTORE_PASSOWRD=monday1
SSL_KEYSTORE=amq-client_hornsup.ks
SSL_KEYSTORE_PASSOWRD=monday1
LOGS=logs
DATASOURCE_DRIVER=org.postgresql.Driver
MONGO_DATABASE=finance_db
MONGO_HOSTNAME=192.168.100.25
MONGO_PORT=27017
MONGO_URI=mongodb://192.168.100.25/finance_db
ACTIVEMQ_SSL_BEANS_ENABLED=true
ACTIVEMQ_NONSSL_BEANS_ENABLED=false
DATABASE_PLATFORM=h2
HIBERNATE_DDL=none
ACTIVEMQ_SSL_ENABLE=true

offline
SPRING_PROFILES_ACTIVE=offline
DATASOURCE=jdbc:h2:mem:finance_db;DB_CLOSE_DELAY\=-1
DATASOURCE_USERNAME=henninb
DATASOURCE_PASSWORD=monday1
DATASOURCE_DRIVER=org.h2.Driver
JSON_FILES_INPUT_PATH=c:\usr\finance_data\json_in
SERVER_PORT=8080
AMQ_BROKER_URL=ssl://hornsup:61617
AMQ_USER=
AMQ_PWD=
SSL_TRUSTSTORE=amq-client_hornsup.ts
SSL_TRUSTSTORE_PASSOWRD=monday1
SSL_KEYSTORE=amq-client_hornsup.ks
SSL_KEYSTORE_PASSOWRD=monday1
LOGS=logs
MONGO_DATABASE=finance_db
MONGO_HOSTNAME=192.168.100.25
MONGO_PORT=27017
MONGO_URI=mongodb://192.168.100.25/finance_db
ACTIVEMQ_SSL_BEANS_ENABLED=true
ACTIVEMQ_NONSSL_BEANS_ENABLED=false
DATABASE_PLATFORM=postgres
HIBERNATE_DDL=create-drop
ACTIVEMQ_SSL_ENABLE=true

export SPRING_PROFILES_ACTIVE=offline
export DATASOURCE=jdbc:postgresql://192.168.100.25:5432/finance_db
export DATASOURCE_USERNAME=henninb
export DATASOURCE_PASSWORD=monday1
export DATASOURCE_DRIVER=org.postgresql.Driver
export JSON_FILES_INPUT_PATH=json_in
export SERVER_PORT=8080
export AMQ_BROKER_URL=ssl://archlinux:61617
export AMQ_USER=
export AMQ_PWD=
export SSL_TRUSTSTORE=amq-client_archlinux.ts
export SSL_TRUSTSTORE_PASSOWRD=monday1
export SSL_KEYSTORE=amq-client_archlinux.ks
export SSL_KEYSTORE_PASSOWRD=monday1
export LOGS=logs
export MONGO_DATABASE=finance_db
export MONGO_HOSTNAME=192.168.100.218
export MONGO_PORT=27017
export MONGO_URI=mongodb://192.168.100.218/finance_db
export ACTIVEMQ_SSL_BEANS_ENABLED=true
export ACTIVEMQ_NONSSL_BEANS_ENABLED=false
export DATABASE_PLATFORM=postgres
export HIBERNATE_DDL=none
export ACTIVEMQ_SSL_ENABLE=true

gradle bootRun
java -jar build/libs/raspi_finance*.jar --spring.config.location=src/main/resources/application.properties
java -jar build/libs/raspi_finance*.jar --spring.config.location=src/main/resources/application.home.properties
java -jar target/raspi_finance*.jar --spring.config.location=src/main/resources/application.home.properties

IntelliJ
Setting the VM Options with -Dspring.profiles.active=work -Dspring.config.location=application.work.properties
Setting the VM Options with -Dspring.profiles.active=offline

mvn package
gradle build

??? spring.datasource.platform=h2
https://www.thomasvitale.com/https-spring-boot-ssl-certificate/

gradle dependencyInsight --dependency slf4j-log4j12
gradle dependencyInsight --dependency log4j-over-slf4j
gradle dependencyInsight --dependency logback-classic
gradle dependencies

openssl s_client -connect 192.168.100.25:8080 -CAfile /path/to/cert/crt.pem

./gradlew wrapper --gradle-version 4.10

psql -h 192.168.100.25 -p 5432 -U henninb -d finance_db

@RequestMapping - For handling any request type
@GetMapping - For GET request
@PostMapping - For POST request
@PutMapping - for PUT request
@PatchMapping - for PATCH request
@DeleteMapping for DELETE request



    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        blogRespository.delete(blogId);
        return true;
    }

https://medium.com/@salisuwy/building-a-spring-boot-rest-api-part-iii-integrating-mysql-database-and-jpa-81391404046a

git@github.com:vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting.git
git@github.com:sharmagaurav03/spring-data-jpa-with-pagination_POC.git
