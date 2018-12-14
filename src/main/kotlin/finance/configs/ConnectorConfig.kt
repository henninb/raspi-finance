package finance.configs

import org.apache.catalina.connector.Connector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Profile

@Profile("notused")
@Configuration
open class ConnectorConfig {

    @Bean
    open fun servletContainer(): ServletWebServerFactory {
        val tomcat = object : TomcatServletWebServerFactory() {
//            protected fun postProcessContext(context: Context) {
//                val securityConstraint = SecurityConstraint()
//                securityConstraint.userConstraint = "CONFIDENTIAL"
//                val collection = SecurityCollection()
//                collection.addPattern("/*")
//                securityConstraint.addCollection(collection)
//                context.addConstraint(securityConstraint)
//            }
        }
        tomcat.addAdditionalTomcatConnectors(redirectConnector())
        return tomcat
    }

    private fun redirectConnector(): Connector {
        val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
        connector.setScheme("http")
        connector.setPort(8080)
        connector.setSecure(false)
        connector.setRedirectPort(8443)
        return connector
    }

    private val httpConnector: Connector
        get() {
            val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
            connector.setScheme("http")
            connector.setPort(8080)
            connector.setSecure(false)
            connector.setRedirectPort(8443)
            return connector
        }

//    @Bean
//    fun servletContainer(): EmbeddedServletContainerFactory {
//        val tomcat = object : TomcatEmbeddedServletContainerFactory() {
//            protected fun postProcessContext(context: Context) {
//                val securityConstraint = SecurityConstraint()
//                securityConstraint.setUserConstraint("CONFIDENTIAL")
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
