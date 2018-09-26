package finance.configs

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConnectorConfig {
    private val httpConnector: Connector
        get() {
            val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
            connector.setScheme("http")
            connector.setPort(8080)
            connector.setSecure(false)
            connector.setRedirectPort(8443)
            return connector
        }
//
//    @Bean
//    fun servletContainer(): EmbeddedServletContainerFactory {
//        val tomcat = object : TomcatEmbeddedServletContainerFactory() {
//            override fun postProcessContext(context: Context) {
//                val securityConstraint = SecurityConstraint()
//                securityConstraint.userConstraint = "CONFIDENTIAL"
//                val collection = SecurityCollection()
//                collection.addPattern("/*")
//                securityConstraint.addCollection(collection)
//                context.addConstraint(securityConstraint)
//            }
//        }
//        tomcat.addAdditionalTomcatConnectors(httpConnector)
//        return tomcat
//    }

}