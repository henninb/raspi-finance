package finance.configs

import org.apache.activemq.ActiveMQSslConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.jms.ConnectionFactory

@Qualifier
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

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    //activemq-ssl JMS endpoint for camel
    @Bean(name = arrayOf("activemq-ssl"))
    open fun activeMQSslJmsComponent(cachingConnectionFactory: ActiveMQSslConnectionFactory): JmsComponent {
        val jmsComponent = JmsComponent()
        jmsComponent.setConnectionFactory(cachingConnectionFactory)
        jmsComponent.setTransacted(true)
        jmsComponent.setReceiveTimeout(receiveTimeout)
        return jmsComponent
    }
        //activemq-ssl
        @Bean
        open fun activeMQSslConnectionFactoryNoParmsBean(): ActiveMQSslConnectionFactory {
            val activeMQSslConnectionFactory = ActiveMQSslConnectionFactory()

            try {
                LOGGER.info("activeMQSslConnectionFactory: setup for ssl")
                activeMQSslConnectionFactory.keyStore = sslKeystore
                activeMQSslConnectionFactory.keyStorePassword = sslKeystorePassword
                activeMQSslConnectionFactory.trustStore = sslTruststore
                activeMQSslConnectionFactory.trustStorePassword = sslTruststorePassword
                activeMQSslConnectionFactory.userName = amqUsername
                activeMQSslConnectionFactory.password = amqPassword
                activeMQSslConnectionFactory.brokerURL = amqBrokerUrl
            } catch (e: Exception) {
                LOGGER.error(e.message)
                //e.printStackTrace()
            }

            return activeMQSslConnectionFactory
        }

    //activemq-ssl
    @Bean
    open fun activeMQSslConnectionFactoryBean(connectionFactory: ActiveMQSslConnectionFactory): ConnectionFactory {
        val activeMQSslConnectionFactory = ActiveMQSslConnectionFactory()
        try {
            LOGGER.info("activeMQSslConnectionFactoryBean: setup for ssl")
            LOGGER.info("sslKeystore: $sslKeystore")
            activeMQSslConnectionFactory.keyStore = sslKeystore
            activeMQSslConnectionFactory.keyStorePassword = sslKeystorePassword
            activeMQSslConnectionFactory.trustStore = sslTruststore
            LOGGER.info("sslTruststore: $sslTruststore")
            activeMQSslConnectionFactory.trustStorePassword = sslTruststorePassword
            activeMQSslConnectionFactory.brokerURL = amqBrokerUrl
            activeMQSslConnectionFactory.userName = amqUsername
            activeMQSslConnectionFactory.password = amqPassword
        } catch (e: Exception) {
            LOGGER.error(e.message)
            //e.printStackTrace()
        }

        return activeMQSslConnectionFactory
    }
}
