package com.finalProject.Back.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Security + MyBatis API")
                        .version("1.0.0")
                        .description("Spring Boot 3.1.4 기반의 API 문서입니다.")
                        .termsOfService("https://your-terms.com")
                        .contact(new Contact()
                                .name("Your Name")
                                .url("https://your-website.com")
                                .email("your-email@domain.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("API 문서 더 보기")
                        .url("https://your-api-docs.com"));
    }
}
