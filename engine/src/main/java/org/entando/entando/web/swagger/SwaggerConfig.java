package org.entando.entando.web.swagger;

import io.swagger.models.auth.In;
import org.apache.http.HttpHeaders;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

@ComponentScan
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

    private static final String API_KEY = "apiKey";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
                .directModelSubstitute(UserAuthoritiesRequest.class, String.class);
    }

    private ApiKey apiKey() {
        return new ApiKey(API_KEY,
                        HttpHeaders.AUTHORIZATION,
                        In.HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))//<17>
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference(API_KEY, authorizationScopes));//<18>
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Entando Rest API",
                                  "Entando AppBuilder Rest APIs ",
                                  "1",
                                  "",
                                  new Contact("Entando", "http://www.entando.com", null),
                                  "",
                                  "",
                                      new ArrayList<>());
    }
}