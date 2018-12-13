package finance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.h2.server.web.WebServlet
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.core.io.ClassPathResource

@Configuration
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

        val initSchema = ClassPathResource("schema-h2.sql")
        val initData = ClassPathResource("data-h2.sql")
        val databasePopulator = ResourceDatabasePopulator(initSchema, initData)
        //DatabasePopulatorUtils.execute(databasePopulator, dataSource)

        return dataSource
    }

    @Bean
    open fun h2servletRegistration(): ServletRegistrationBean<*> {
        val registration = ServletRegistrationBean(WebServlet())
        registration.addUrlMappings("/h2-console/*")
        return registration
    }
}
