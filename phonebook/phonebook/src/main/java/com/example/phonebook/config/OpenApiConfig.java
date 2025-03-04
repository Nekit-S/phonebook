package com.example.phonebook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Phonebook API")
                        .version("1.0")
                        .description("REST API для управления пользователями телефонной книги")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")));
    }
}