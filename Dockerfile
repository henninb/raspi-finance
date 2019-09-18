FROM openjdk:8

RUN mkdir -p /opt/raspi_finance
COPY ./build/libs/raspi_finance.jar /opt/raspi_finance
WORKDIR /opt/raspi_finance
#CMD ["java", "-jar" "/opt/raspi_finance/raspi_finance.jar"]

CMD java -jar raspi_finance.jar
