package finance.routes

import finance.processors.InsertTransactionProcessor
import finance.processors.JsonTransactionProcessor
import org.apache.camel.builder.RouteBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class MasterRoute : RouteBuilder() {

    @Value("\${project.json-files-input-path}")
    private val jsonFilesInputPath: String? = null

    @Autowired
    var jsonTransactionProcessor: JsonTransactionProcessor? = null

    @Autowired
    var insertTransactionProcessor: InsertTransactionProcessor? = null

    @Throws(Exception::class)
    override fun configure() {
        LOGGER.info("jsonFilesInputPath: " + jsonFilesInputPath)
        from("file:" + jsonFilesInputPath + "?delete=true&moveFailed=.failedWithErrors")
                .autoStartup(true)
                .choice()
                .`when`(header("CamelFileName").endsWith(".json"))
                    .log("\$simple{file:onlyname.noext}_\$simple{date:now:yyyy-MM-dd}.json")
                    .process(jsonTransactionProcessor)
                    .to("direct:processTransactions")
                    .log("completed")
                    //.to("file:" + jsonFilesInputPath + File.separator + ".processed")
                    //.to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${header.CamelSplitIndex}.json")
                .otherwise()
                    .to("file:" + jsonFilesInputPath + File.separator + ".notProcessed")
                .end()

                from("direct:processTransactions")
                        .autoStartup(true)
                        .split(body())
                        .process(insertTransactionProcessor)
                        //.convertBodyTo(Transaction::class.java)
                        .convertBodyTo(String::class.java)
                        .to("file:" + jsonFilesInputPath + File.separator + ".processed?fileName=\${id}.json&autoCreate=true")
                        .end()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MasterRoute::class.java)
    }
}
