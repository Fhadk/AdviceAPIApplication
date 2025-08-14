//package com.descenedigital.config;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@SecurityScheme(
//    name = "bearerAuth",
//    type = SecuritySchemeType.HTTP,
//    bearerFormat = "JWT",
//    scheme = "bearer"
//)
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Descene Digital Advice API")
//                        .description("API for managing advice and ratings")
//                        .version("v1.0")
//                        .contact(new Contact()
//                                .name("Descene Digital Support")
//                                .email("support@descenedigital.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("http://springdoc.org")));
//    }
//}