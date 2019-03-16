package finance.configs

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.ActiveMQSslConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import kotlin.system.exitProcess

//@Component
@Configuration
@EnableTransactionManagement
open class ActivemqConfig {

    //@Autowired
    private var activemqProperties: ActivemqProperties = ActivemqProperties()

    @Value("\${spring.activemq.broker-url}")
    private val amqBrokerUrl: String? = null

    @Value("\${project.activemq.receiveTimeout}")
    private val receiveTimeout: Long = activemqProperties.receiveTimeout

    @Value("\${project.activemq.username}")
    private val amqUsername: String = activemqProperties.username

    @Value("\${project.activemq.password}")
    private val amqPassword: String = activemqProperties.password

    @Value("\${project.activemq.truststore}")
    private val sslTruststore: String = activemqProperties.truststore

    @Value("\${project.activemq.truststorePassword}")
    private val sslTruststorePassword: String = activemqProperties.truststorePassword

    @Value("\${project.activemq.keystore}")
    private val sslKeystore: String = activemqProperties.keystore

    @Value("\${project.activemq.keystorePassword}")
    private val sslKeystorePassword: String = activemqProperties.keystorePassword

    @Value("\${project.activemq.scheme}")
    //private val sslEnabled: Boolean? = null
    private val scheme: String = activemqProperties.scheme

    private val logger = LoggerFactory.getLogger(this.javaClass)

    //activemq-ssl for camel
    @Bean(name = [("activemq")])
    open fun activeMQSslJmsComponent(): JmsComponent {
        val jmsComponent = JmsComponent()
        if( scheme == "ssl" ) {
            jmsComponent.setConnectionFactory(this.activeMQSslConnectionFactory())
            jmsComponent.setTransacted(true)
            jmsComponent.setReceiveTimeout(receiveTimeout)
        } else if ( scheme == "tcp" ) {
            jmsComponent.setConnectionFactory(this.activeMQConnectionFactory())
            jmsComponent.setTransacted(true)
            jmsComponent.setReceiveTimeout(receiveTimeout)
        } else {
            logger.info("scheme needs to be set to 'ssl' or 'tcp': $scheme")
            exitProcess(255)
        }
        return jmsComponent
    }

    //activemq-ssl
    open fun activeMQSslConnectionFactory(): ActiveMQSslConnectionFactory {
        val activeMQSslConnectionFactory = ActiveMQSslConnectionFactory()

        activeMQSslConnectionFactory.keyStore = sslKeystore
        activeMQSslConnectionFactory.keyStorePassword = sslKeystorePassword
        activeMQSslConnectionFactory.trustStore = sslTruststore
        activeMQSslConnectionFactory.trustStorePassword = sslTruststorePassword
        activeMQSslConnectionFactory.userName = amqUsername
        activeMQSslConnectionFactory.password = amqPassword
        activeMQSslConnectionFactory.brokerURL = amqBrokerUrl

        return activeMQSslConnectionFactory
    }

    //activemq - nonssl
    open fun activeMQConnectionFactory(): ActiveMQConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()

        activeMQConnectionFactory.brokerURL = amqBrokerUrl
        activeMQConnectionFactory.userName = amqUsername
        activeMQConnectionFactory.password = amqPassword

        return activeMQConnectionFactory
    }
}
