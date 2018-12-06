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

@Configuration
@EnableTransactionManagement
open class ActivemqSslJmsConfig {
    @Value("\${spring.activemq.broker-url}")
    private val amqBrokerUrl: String? = null

    @Value("\${project.queue.receive-timeout}")
    private val receiveTimeout: Long = 0

    @Value("\${spring.activemq.user}")
    private val amqUsername: String? = null

    @Value("\${spring.activemq.password}")
    private val amqPassword: String? = null

    @Value("\${project.ssl.truststore}")
    private val sslTruststore: String? = null

    @Value("\${project.ssl.truststore-password}")
    private val sslTruststorePassword: String? = null

    @Value("\${project.ssl.keystore}")
    private val sslKeystore: String? = null

    @Value("\${project.ssl.keystore-password}")
    private val sslKeystorePassword: String? = null

    @Value("\${project.ssl.enabled}")
    private val sslEnabled: Boolean? = null

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    //activemq-ssl for camel
    @Bean(name = arrayOf("activemq"))
    open fun activeMQSslJmsComponent(): JmsComponent {
        val jmsComponent = JmsComponent()
        if( sslEnabled == true ) {
            jmsComponent.setConnectionFactory(this.activeMQSslConnectionFactory())
            jmsComponent.setTransacted(true)
            jmsComponent.setReceiveTimeout(receiveTimeout)
        } else if (sslEnabled == false ) {
            jmsComponent.setConnectionFactory(this.activeMQConnectionFactory())
            jmsComponent.setTransacted(true)
            jmsComponent.setReceiveTimeout(receiveTimeout)
        } else {
            LOGGER.info("sslEnabled needs to be set to true or false: " + sslEnabled)
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
