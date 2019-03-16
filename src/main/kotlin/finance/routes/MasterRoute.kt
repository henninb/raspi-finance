package finance.routes

import finance.configs.KafkaProperties
import finance.models.Transaction
import finance.processors.InsertTransactionProcessor
import finance.processors.JsonTransactionProcessor
import finance.processors.StringTransactionProcessor
import org.apache.camel.builder.RouteBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import org.apache.camel.LoggingLevel
import org.apache.camel.Predicate
import org.apache.camel.Processor

@Component
class MasterRoute : RouteBuilder() {
    @Value("\${project.json-files-input-path}")
    private val jsonFilesInputPath: String? = null

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Autowired
    lateinit var stringTransactionProcessor: StringTransactionProcessor

    @Autowired
    lateinit var jsonTransactionProcessor: JsonTransactionProcessor

    @Autowired
    lateinit var insertTransactionProcessor: InsertTransactionProcessor

    @Throws(Exception::class)
    override fun configure() {
        logger.info("jsonFilesInputPath: $jsonFilesInputPath")

        //val p1 = header("name").isEqualTo(constant("does-not-apply"))
        //connection related exception
        onException(java.net.ConnectException::class.java)
          .useOriginalMessage()
          .maximumRedeliveries(0)
          .redeliveryDelay(0)
          //.handled(true)
          .log(LoggingLevel.ERROR, "\${exception.class} :: \${exception.message}")
        .end()

        //default exception
        onException(Exception::class.java)
          .useOriginalMessage()
          .retryAttemptedLogLevel(LoggingLevel.INFO)
          .maximumRedeliveries(0).redeliveryDelay(0)
          .handled(true)
          .log(LoggingLevel.ERROR, "\${exception.class} :: \${exception.message}")
        .end()

        from("file:$jsonFilesInputPath?delete=true&moveFailed=.failedWithErrors")
          .autoStartup(true)
          .routeId("processJsonInputFiles")
          .choice()
          .`when`(header("CamelFileName").endsWith(".json"))
            .log(LoggingLevel.INFO, "\$simple{file:onlyname.noext}_\$simple{date:now:yyyy-MM-dd}.json")
            .process(jsonTransactionProcessor)
            .to("direct:processEachTransaction")
            .log(LoggingLevel.INFO, "JSON file processed successfully.")
            //.to("file:" + jsonFilesInputPath + File.separator + ".processed")
            //.to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${header.CamelSplitIndex}.json")
          .otherwise()
            .to("file:" + jsonFilesInputPath + File.separator + ".notProcessed")
          .end()
        .end()

        from("direct:processEachTransaction")
          .autoStartup(true)
          .routeId("processEachTransaction")
          .split(body())
          .convertBodyTo(Transaction::class.java)
          .process(stringTransactionProcessor)
          //.log("headers - \${headers}")
          .convertBodyTo(String::class.java)

          .process(Processor { exchange ->
            exchange.setProperty("myparm", "test")
          })
          .log("myparm = \${property.myparm}")

          //.to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${id}.json&autoCreate=true")
          .log(LoggingLevel.INFO, "guid = \${property.guid}")
          .to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${property.guid}.json&autoCreate=true")
          .removeHeaders("*", "CamelFileLength", "CamelFileNameProduced")

          .setHeader("isValid", simple("Yes"))
          .choice()
          .`when`(Predicate { true })
          //.`when`(simple("Yes == Yes"))
          //.`when`(simple("\${header.isValid} == Yes"))
            //TODO: need to add logic here for Kafka
            .to("activemq:queue:" + "finance_drop")
            .log(LoggingLevel.INFO, " ***** consume activemq *****")
          .otherwise()
            //.to("direct:publishToKafkaRoute")
            .log(LoggingLevel.INFO, " ***** kafka *****")
          .end()
        .end()

        from("activemq:queue:" + "finance_drop" + "?concurrentConsumers=1&maxConcurrentConsumers=1")
          .autoStartup(true)
          .routeId("activemqInsertTransaction")
          .convertBodyTo(String::class.java)
          .process(insertTransactionProcessor)
          .log(LoggingLevel.INFO, "inserted Transaction successfully.")
          .convertBodyTo(String::class.java)
          .removeHeaders("*")
          .to("activemq:queue:" + "finance_drop_complete")
        .end()

//        from("direct:publishToKafkaRoute")
//          .log(LoggingLevel.INFO, "INFO: publish to topic: " + kafkaProperties.topic)
//          // .process { exchange -> exchange.`in`.setHeader(KafkaConstants.PARTITION_KEY, 0) }
//          // .process { exchange -> exchange.`in`.setHeader(KafkaConstants.KEY, "1") }
//          .choice()
//          .`when`(Predicate { true })
//            .log(LoggingLevel.INFO, "always true")
//          .otherwise()
//            .to("kafka:" + kafkaProperties.serverNamePort + "?topic=" + kafkaProperties.topic
//              + "&serializerClass=org.apache.kafka.common.serialization.StringSerializer&keySerializerClass=org.apache.kafka.common.serialization.StringSerializer"
//              + "&partitioner=org.apache.kafka.clients.producer.internals.DefaultPartitioner&sslKeymanagerAlgorithm=PKIX"
//              + "&brokers=" + kafkaProperties.serverNamePort
//              // +"&securityProtocol=SSL" + "&sslKeystoreLocation="+kafkaProperties.getSslKeystoreLocation()+"&sslKeystorePassword="+ kafkaProperties.getSslKeystorePassword() +"&sslTruststoreLocation="+kafkaProperties.getSslTruststoreLocation()+"&sslTruststorePassword=" +kafkaProperties.getSslTruststorePassword()
//            )
//          .end()
//        .end()
//
//        from("direct:readFromKafkaRoute")
//          .autoStartup(true)
//          .routeId("kafkaInsertTransaction")
//          .convertBodyTo(String::class.java)
//          .process(insertTransactionProcessor)
//          .log(LoggingLevel.INFO, "inserted Transaction successfully.")
//        .end()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MasterRoute::class.java)
    }
}
