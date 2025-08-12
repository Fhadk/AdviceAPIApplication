package com.descenedigital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Advice API")
                        .version("v1")
                        .description("API for managing advice entries"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local server"));
    }
}
