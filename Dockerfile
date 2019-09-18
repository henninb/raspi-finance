FROM openjdk:8

RUN mkdir -p /opt/raspi_finance
COPY . /opt/raspi_finance
WORKDIR /opt/raspi_finance
#RUN javac Main.java
CMD ["java", "Main"]
