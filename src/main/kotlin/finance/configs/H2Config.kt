package finance.configs

import finance.models.Account
import finance.models.Transaction
import finance.repositories.AccountRepository
import finance.repositories.TransactionRepository
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.apache.tomcat.jni.SSL.setPassword
import org.h2.server.web.WebServlet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource
import org.springframework.boot.web.servlet.ServletRegistrationBean
import java.sql.Timestamp

@Configuration
//@EnableJpaRepositories(basePackages = "org.baeldung.repository")
//@PropertySource("h2-offline.properties")
@EnableTransactionManagement
@Profile("offline")
open class H2Config {
    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.url = "jdbc:h2:mem:finance_db;DB_CLOSE_DELAY=-1"
        dataSource.username = "sa"
        dataSource.password = ""

        return dataSource
    }

    @Bean
    open fun h2servletRegistration(): ServletRegistrationBean<*> {
        val registration = ServletRegistrationBean(WebServlet())
        registration.addUrlMappings("/h2-console/*")
        return registration
    }
}
