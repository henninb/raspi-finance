package finance.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import java.util.Arrays
import java.util.HashSet

import org.apache.activemq.TransportLoggerSupport.spi

@Configuration
@EnableSwagger2
@EnableWebMvc
open class SwaggerConfig {

    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
    }

    companion object {
        val DEFAULT_API_INFO = ApiInfo(
                "Awesome API Title", "Awesome API Description", "1.0",
                "urn:tos", "contact name",
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0")

        private val DEFAULT_PRODUCES_AND_CONSUMES = HashSet(Arrays.asList("application/json",
                "application/xml"))
    }
}