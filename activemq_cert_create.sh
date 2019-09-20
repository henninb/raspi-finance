#!/bin/sh

SERVERNAME=hornsup
APP=activemq
PASSWD=monday1

rm -rf *.jks
echo generate private key
openssl genrsa -out $HOME/ca.key.pem 4096

#-storepass
keytool -genkey -keyalg RSA -alias ${APP}-${SERVERNAME} -keystore ${APP}-${SERVERNAME}-keystore.jks -storepass $PASSWD -keypass $PASSWD -validity 365 -keysize 4096 -dname "CN=$SERVERNAME, OU=$SERVERNAME, O=BrianLLC, L=Denton, ST=Texas, C=US"
keytool -export -alias ${APP}-${SERVERNAME} -file ${APP}-${SERVERNAME}.der -keystore ${APP}-${SERVERNAME}-keystore.jks -storepass $PASSWD -keypass $PASSWD
#keytool -list -v -keystore keystore.jks
#-dname "CN=$SERVERNAME, OU=$SERVERNAME, O=BrianLLC, L=Denton, ST=Texas, C=US"

echo generate CSR - certificate signing request
openssl req -new -key $HOME/ca.key.pem -out $HOME/ca.csr -subj "/C=US/ST=Texas/L=Denton/O=BrianLLC/OU=$SERVERNAME/CN=$SERVERNAME"

openssl req -new -key $HOME/ca.key.pem -out $HOME/${APP}-${SERVERNAME}.csr.pem -subj "/C=US/ST=Texas/L=Denton/O=BrianLLC/OU=$SERVERNAME/CN=$SERVERNAME"

# Generate Self Signed Key
openssl x509 -req -days 365 -in $HOME/ca.csr -signkey $HOME/ca.key.pem -out /$HOME/ca.crt.pem
openssl x509 -req -days 365 -in $HOME/${APP}-${SERVERNAME}.csr.pem -signkey $HOME/ca.key.pem -out $HOME/${APP}-${SERVERNAME}.crt.pem

  ## Create the broker keystore
  keytool -genkey -noprompt -alias ${APP}-${SERVERNAME}-server -keyalg RSA -keysize 4096 -validity 365 -keystore ${APP}-${SERVERNAME}-server-keystore.jks -dname "CN=${SERVERNAME}, OU=${SERVERNAME}, O=BrianLLC, L=Denton, ST=TX, C=US" -storepass   $PASSWD -keypass $PASSWD

  ## Export the broker certificate from the keystore
  keytool -export -alias ${APP}-${SERVERNAME}-server -keystore ${APP}-${SERVERNAME}-server-keystore.jks -file ${APP}-${SERVERNAME}-server-cert -storepass $PASSWD -keypass $PASSWD

  ## Create the CLIENT keystore
  keytool -genkey -noprompt -alias ${APP}-${SERVERNAME}-client -keyalg RSA -keysize 4096 -validity 365 -keystore ${APP}-${SERVERNAME}-client-keystore.jks -dname "CN=${SERVERNAME}, OU=${SERVERNAME}, O=BrianLLC, L=Denton, ST=TX, C=US" -storepass $PASSWD -keypass $PASSWD

  ## Import the previous exported broker's certificate into a CLIENT truststore
  keytool -import -alias ${APP}-${SERVERNAME}-server -keystore ${APP}-${SERVERNAME}-client-truststore.jks -file ${APP}-${SERVERNAME}-server-cert -storepass $PASSWD -keypass $PASSWD

  ## If you want to make trusted also the client, you must export the client's certificate from the keystore
  keytool -export -alias ${APP}-${SERVERNAME}-client -keystore ${APP}-${SERVERNAME}-client-keystore.jks -file ${APP}-${SERVERNAME}-client-cert -storepass $PASSWD -keypass $PASSWD

  ## Import the client's exported certificate into a broker SERVER truststore
  keytool -import -alias ${APP}-${SERVERNAME}-client -keystore ${APP}-${SERVERNAME}-server-truststore.jks -file ${APP}-${SERVERNAME}-client-cert -storepass $PASSWD -keypass $PASSWD

cp -v activemq-hornsup-client-keystore.jks ssl
cp -v activemq-hornsup-client-truststore.jks ssl

cp -v activemq-hornsup-client-keystore.jks /usr/local/Cellar/activemq/5.15.9/libexec/conf
cp -v activemq-hornsup-server-keystore.jks /usr/local/Cellar/activemq/5.15.9/libexec/conf

echo /usr/local/Cellar/activemq/5.15.9/libexec/conf
brew services restart activemq
exit 0

 <broker xmlns="http://activemq.apache.org/schema/core" brokerName="hornsup" dataDirectory="${activemq.data}">

        <transportConnectors>
            <transportConnector name="ssl" uri="ssl://0.0.0.0:61617?trace=true&amp;needClientAuth=true"/>
            <transportConnector name="stomp+ssl" uri="stomp+ssl://0.0.0.0:61612"/>
        </transportConnectors>
        <sslContext>
            <sslContext
            keyStore="amq-server.ks" keyStorePassword="monday1"
            trustStore="amq-client.ks" trustStorePassword="monday1"/>
        </sslContext>
