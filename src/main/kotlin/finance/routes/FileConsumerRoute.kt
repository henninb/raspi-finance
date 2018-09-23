package finance.routes

import finance.processors.JsonProcessor
import org.apache.camel.builder.RouteBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileConsumerRoute : RouteBuilder() {

    @Value("\${project.json-files-input-path}")
    private val jsonFilesInputPath: String? = null

    @Autowired
    internal var jsonProcessor: JsonProcessor? = null

    @Throws(Exception::class)
    override fun configure() {
        LOGGER.info("jsonFilesInputPath: " + jsonFilesInputPath)
        from("file:" + jsonFilesInputPath + "?delete=true&moveFailed=.failedWithErrors")
                .choice()
                .`when`(header("CamelFileName").endsWith(".json")).log("\$simple{file:onlyname.noext}_\$simple{date:now:yyyyMMdd}.json").process(jsonProcessor).to("file:" + jsonFilesInputPath + File.separator + ".processed")
                .`when`(header("CamelFileName").endsWith(".txt")).to("file:" + jsonFilesInputPath + File.separator + ".notProcessed")

                .otherwise().to("file:" + jsonFilesInputPath + File.separator + ".notProcessed")
                .end()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FileConsumerRoute::class.java)
    }
}
