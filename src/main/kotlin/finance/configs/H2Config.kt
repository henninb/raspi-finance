package finance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.h2.server.web.WebServlet
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@Configuration
@EnableTransactionManagement
@Profile("offline")
@PropertySource("classpath:database-h2.properties")
open class H2Config {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var environment: Environment

    private val URL = "url"
    private val USERNAME = "dbuser"
    private val DRIVER = "driver"
    private val PASSWORD = "dbpassword"

//https://www.tutorialspoint.com/spring_boot/spring_boot_database_handling.htm
//    @Bean(name = arrayOf("dbuserservice"))
//    @ConfigurationProperties(prefix = "spring.dbuserservice")
//    open fun createUserServiceDataSource(): DataSource {
//        return DataSourceBuilder.create().build()
//    }
//
//    @Autowired
//    open fun createJdbcTemplate_UserService(@Qualifier("dbuserservice") userServiceDS: DataSource): JdbcTemplate {
//        return JdbcTemplate(userServiceDS)
//    }

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        val driverName = environment.getProperty(DRIVER);
        if( driverName == null ) {
            //TODO: need to take action
            LOGGER.info("driverName is NULL.")
        } else {
            dataSource.setDriverClassName(driverName)
        }
        dataSource.url = environment.getProperty(URL)
        dataSource.username = environment.getProperty(USERNAME)
        dataSource.password = environment.getProperty(PASSWORD)

        //TODO: The block below is not working
        //val initSchema = ClassPathResource("schema-h2.sql")
        //val initData = ClassPathResource("data-h2.sql")
        //val databasePopulator = ResourceDatabasePopulator(initSchema, initData)
        //DatabasePopulatorUtils.execute(databasePopulator, dataSource)

        return dataSource
    }

    @Bean
    open fun h2servletRegistration(): ServletRegistrationBean<*> {
        val registration = ServletRegistrationBean(WebServlet())
        registration.addUrlMappings("/h2-console/*")
        return registration
    }

    @Bean
    open fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        LOGGER.info("JdbcTemplate is called.")
        return JdbcTemplate(dataSource)
    }

    //should not be needed
    @Bean("jdbcTemplate1")
    open fun jdbcTemplate1(dataSource: DataSource): JdbcTemplate {
        LOGGER.info("JdbcTemplate is called.")
        return JdbcTemplate(dataSource)
    }
}
