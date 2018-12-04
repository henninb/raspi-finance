package finance.configs

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
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

    //activemq nonssl for camel
    @Bean(name = arrayOf("activemq-nonssl"))
    open fun activeMQJmsComponent(): JmsComponent {
        val jmsComponent = JmsComponent()
        jmsComponent.setConnectionFactory(this.activeMQConnectionFactory())
        jmsComponent.setTransacted(true)
        jmsComponent.setReceiveTimeout(receiveTimeout)
        return jmsComponent
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
