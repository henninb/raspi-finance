package finance.configs

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.jms.ConnectionFactory

//@Configuration
@EnableTransactionManagement
open class ActivemqJmsConfig {
    @Value("\${spring.activemq.broker-url}")
    private val amqBrokerUrl: String? = null

    @Value("\${project.queue.receive-timeout}")
    private val receiveTimeout: Long = 0

    @Value("\${spring.activemq.user}")
    private val amqUsername: String? = null

    @Value("\${spring.activemq.password}")
    private val amqPassword: String? = null

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    //activemq JMS endpoint for camel
    @Bean(name = arrayOf("activemq"))
    open fun activeMQJmsComponent(cachingConnectionFactory: ActiveMQConnectionFactory): JmsComponent {
        val jmsComponent = JmsComponent()
        jmsComponent.setConnectionFactory(cachingConnectionFactory)
        jmsComponent.setTransacted(true)
        jmsComponent.setReceiveTimeout(receiveTimeout)
        return jmsComponent
    }

    //activeMQ
    @Bean
    open fun activeMQConnectionFactoryNoParmsBean(): ActiveMQConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()

        activeMQConnectionFactory.brokerURL = amqBrokerUrl
        activeMQConnectionFactory.userName = amqUsername
        activeMQConnectionFactory.password = amqPassword

        return activeMQConnectionFactory
    }

    //activeMQ
    @Bean
    //@Primary
    open fun activeMQConnectionFactoryBean(connectionFactory: ActiveMQConnectionFactory): ConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()

        activeMQConnectionFactory.brokerURL = amqBrokerUrl
        activeMQConnectionFactory.userName = amqUsername
        activeMQConnectionFactory.password = amqPassword

        return activeMQConnectionFactory
    }
}
