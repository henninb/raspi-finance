package finance.configs

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "project.activemq", ignoreUnknownFields = false)
class ActivemqProperties {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    var username: String = ""
    var password: String = ""
    var scheme: String = ""
    var hostname: String = ""
    var truststore: String = ""
    var truststorePassword: String = ""
    var keystore: String = ""
    var keystorePassword: String = ""
    var port: Int = 0
    var receiveTimeout: Long = 0
    var inMemory: Boolean = false
    var pooled: Boolean = true
}
