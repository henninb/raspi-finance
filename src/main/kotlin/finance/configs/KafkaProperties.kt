package finance.configs

//import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "project.kafka", ignoreUnknownFields = false)
class KafkaProperties {
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    var serverNamePort: String? = null
    var zookeeperServerPort: String? = null
    var topic: String? = null
    var sslKeystore: String? = null
    var sslKeystorePassword: String? = null
    var sslTruststore: String? = null
    var sslTruststorePassword: String? = null
}
