package com.books.utility.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenUiConfiguration implements WebMvcConfigurer {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("books-management").description("Managing personal books").version("v0.0.1")
                        .license(new License().name("MWGA Limited").url("http://mwga.com")))
                .externalDocs(new ExternalDocumentation().description("some description")
                        .url("https://github.com/AmirhoseinFiroozi/books-management"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("public").pathsToMatch("/pub/**").build();
    }

    @Bean
    public GroupedOpenApi identifiedApi() {
        return GroupedOpenApi.builder().group("identified").pathsToMatch("/idn/**").build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder().group("member").pathsToMatch("/member/**").build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/**").build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/doc", "/swagger-ui.html");
    }
}
