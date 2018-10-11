package finance.configs

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.ActiveMQSslConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.jms.ConnectionFactory

@Configuration
@EnableTransactionManagement
@Profile("combo")
open class ActivemqConfig {
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

    private val sslEnabled: Boolean = false

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    //activemq JMS endpoint for camel
    @Bean(name = arrayOf("activemq-ssl"))
    open fun activeMQJmsComponent(cachingConnectionFactory: ActiveMQConnectionFactory): JmsComponent {
        val jmsComponent = JmsComponent()
        jmsComponent.setConnectionFactory(cachingConnectionFactory)
        jmsComponent.setTransacted(true)
        jmsComponent.setReceiveTimeout(receiveTimeout)
        return jmsComponent
    }

    /*
    //activemq-ssl JMS endpoint for camel
    @Bean(name = arrayOf("activemq-ssl"))
    open fun activeMQSslJmsComponent(cachingConnectionFactory: ActiveMQSslConnectionFactory): JmsComponent {
        val jmsComponent = JmsComponent()
        jmsComponent.setConnectionFactory(cachingConnectionFactory)
        jmsComponent.setTransacted(true)
        jmsComponent.setReceiveTimeout(receiveTimeout)
        return jmsComponent
    }
    */

    //activeMQ
    @Bean
    open fun activeMQConnectionFactoryNoParmsBean(): ActiveMQConnectionFactory {
        if( sslEnabled == true ) {
            val activeMQSslConnectionFactory = ActiveMQSslConnectionFactory()

            activeMQSslConnectionFactory.keyStore = sslKeystore
            activeMQSslConnectionFactory.keyStorePassword = sslKeystorePassword
            activeMQSslConnectionFactory.trustStore = sslTruststore
            activeMQSslConnectionFactory.trustStorePassword = sslTruststorePassword
            activeMQSslConnectionFactory.userName = amqUsername
            activeMQSslConnectionFactory.password = amqPassword
            activeMQSslConnectionFactory.brokerURL = amqBrokerUrl

            return activeMQSslConnectionFactory;
        } else {
            val activeMQConnectionFactory = ActiveMQConnectionFactory()

            activeMQConnectionFactory.brokerURL = amqBrokerUrl
            activeMQConnectionFactory.userName = amqUsername
            activeMQConnectionFactory.password = amqPassword

            return activeMQConnectionFactory;
        }
    }

    //activeMQ
    @Bean
    @Primary
    open fun activeMQConnectionFactoryBean(connectionFactory: ActiveMQConnectionFactory): ConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()

        activeMQConnectionFactory.brokerURL = amqBrokerUrl
        activeMQConnectionFactory.userName = amqUsername
        activeMQConnectionFactory.password = amqPassword

        return activeMQConnectionFactory
    }

    //activemq-ssl
    @Bean
    open fun activeMQSslConnectionFactoryBean(connectionFactory: ActiveMQSslConnectionFactory): ConnectionFactory {
        val activeMQSslConnectionFactory = ActiveMQSslConnectionFactory()

        activeMQSslConnectionFactory.keyStore = sslKeystore
        activeMQSslConnectionFactory.keyStorePassword = sslKeystorePassword
        activeMQSslConnectionFactory.trustStore = sslTruststore
        activeMQSslConnectionFactory.trustStorePassword = sslTruststorePassword
        activeMQSslConnectionFactory.brokerURL = amqBrokerUrl
        activeMQSslConnectionFactory.userName = amqUsername
        activeMQSslConnectionFactory.password = amqPassword

        return activeMQSslConnectionFactory
    }
}
