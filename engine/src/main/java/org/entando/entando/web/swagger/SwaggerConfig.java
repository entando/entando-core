package org.entando.entando.web.swagger;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.*;
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

    private static final String ENTANDO_AUTH = "entando_auth";

    private BaseConfigManager baseConfigManager;

    public SwaggerConfig(BaseConfigManager baseConfigManager) {
        this.baseConfigManager = baseConfigManager;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(singletonList(oAuth()))
                .securityContexts(singletonList(securityContext()))
                .directModelSubstitute(UserAuthoritiesRequest.class, String.class);
    }

    private OAuth oAuth() {
        return new OAuthBuilder()
                .name(ENTANDO_AUTH)
                .grantTypes(grantTypes())
                .scopes(singletonList(globalScope()))
                .build();
    }

    private List<GrantType> grantTypes() {
        String authUrl = baseConfigManager.getParam(SystemConstants.PAR_APPL_BASE_URL);

        return singletonList(new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpointBuilder()
                        .url(authUrl + "OAuth2/access_token")
                        .build())
                .tokenRequestEndpoint(new TokenRequestEndpointBuilder()
                        .url(authUrl + "OAuth2/authorize")
                        .build())
                .build());
    }

    private AuthorizationScope globalScope() {
        return new AuthorizationScope("global", "accessEverything");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))//<17>
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope globalScope
                = new AuthorizationScope("global", "Access everything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = globalScope;

        return singletonList(SecurityReference.builder()
                .reference(ENTANDO_AUTH)
                .scopes(authorizationScopes)
                .build());
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