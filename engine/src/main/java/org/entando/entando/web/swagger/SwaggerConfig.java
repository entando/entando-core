package org.entando.entando.web.swagger;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.fasterxml.classmate.TypeResolver;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.lang.reflect.WildcardType;
import java.net.*;
import java.util.*;

import static java.util.Collections.singletonList;

@ComponentScan
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements ServletContextAware {

    private static final String ENTANDO_AUTH = "entando_auth";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ServletContext servletContext;

    private final TypeResolver typeResolver;
    private final BaseConfigManager baseConfigManager;

    @Autowired
    public SwaggerConfig(TypeResolver typeResolver, BaseConfigManager baseConfigManager) {
        this.typeResolver = typeResolver;
        this.baseConfigManager = baseConfigManager;
    }

    @Override
    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathProvider(relativePathProvider())
                .apiInfo(apiInfo())
                .securitySchemes(singletonList(oAuth()))
                .securityContexts(singletonList(securityContext()))
                .directModelSubstitute(UserAuthoritiesRequest.class, String.class)
                .alternateTypeRules(
                        // This rule is necessary to allow Swagger resolving Map<String, List<String>> types
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(
                                        Map.class,
                                        String.class,
                                        typeResolver.resolve(List.class, String.class)),
                                typeResolver.resolve(
                                        Map.class,
                                        String.class,
                                        WildcardType.class),
                                Ordered.HIGHEST_PRECEDENCE)
                        );
    }

    private String getBaseConfigUrl() {
        String baseUrl = "";

        try {
            URL configUrl = new URL(baseConfigManager.getParam(SystemConstants.PAR_APPL_BASE_URL));
            baseUrl = configUrl.getPath();

        } catch (MalformedURLException e) {
            logger.warn("Couldn't configure Swagger baseUrl");
        }

        return baseUrl;
    }

    private RelativePathProvider relativePathProvider() {
        return new RelativePathProvider(servletContext) {
            @Override
            public String getApplicationBasePath(){
                return getBaseConfigUrl();
            }
        };
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

        return singletonList(new ResourceOwnerPasswordCredentialsGrant(
                authUrl + "api/oauth/token"));
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
        return new ApiInfoBuilder()
                .title("Entando Rest API")
                .description("Entando AppBuilder Rest APIs")
                .contact(new Contact("Entando", "http://www.entando.com", null))
                .build();
    }
}
