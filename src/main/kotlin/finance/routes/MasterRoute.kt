package finance.routes

import finance.models.Transaction
import finance.processors.InsertTransactionProcessor
import finance.processors.JsonTransactionProcessor
import finance.processors.StringTransactionProcessor
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import org.apache.camel.LoggingLevel
import org.apache.camel.Processor

@Component
class MasterRoute : RouteBuilder() {

    @Value("\${project.json-files-input-path}")
    private val jsonFilesInputPath: String? = null

    @Autowired
    var stringTransactionProcessor: StringTransactionProcessor? = null

    @Autowired
    var jsonTransactionProcessor: JsonTransactionProcessor? = null

    @Autowired
    var insertTransactionProcessor: InsertTransactionProcessor? = null

    @Throws(Exception::class)
    override fun configure() {
        LOGGER.info("jsonFilesInputPath: " + jsonFilesInputPath)

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

        from("file:" + jsonFilesInputPath + "?delete=true&moveFailed=.failedWithErrors")
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

        from("direct:processEachTransaction")
                .autoStartup(true)
                .routeId("processEachTransaction")
                .split(body())
                .convertBodyTo(Transaction::class.java)
                //.log(LoggingLevel.INFO, "*** processEachTransaction: " + "\${body}")
                .process(stringTransactionProcessor)
                .convertBodyTo(String::class.java)
                .to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${id}.json&autoCreate=true")
                .removeHeaders("*", "CamelFileLength", "CamelFileNameProduced")
                .to("activemq:queue:" + "finance_drop")
        .end()

        from("activemq:queue:" + "finance_drop" + "?concurrentConsumers=1&maxConcurrentConsumers=1")
                .autoStartup(true)
                .routeId("insertTransaction")
                //.log(LoggingLevel.INFO, "insertTransaction: " + "\${body}")
                .convertBodyTo(String::class.java)
                .process(insertTransactionProcessor)
                .log(LoggingLevel.INFO, "inserted Transaction successfully.")
                .convertBodyTo(String::class.java)
                .removeHeaders("*")
                .to("activemq:queue:" + "finance_drop_complete")
        .end()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MasterRoute::class.java)
    }
}
