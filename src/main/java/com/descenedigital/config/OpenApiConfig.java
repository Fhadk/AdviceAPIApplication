package com.descenedigital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI adviceOpenAPI() {
    return new OpenAPI()
            .info(new Info().title("Advice API").version("v2")
                    .description("JWT-authenticated Advice API"))
            .components(new Components().addSecuritySchemes(
                    "bearer-jwt",
                    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
  }

}
