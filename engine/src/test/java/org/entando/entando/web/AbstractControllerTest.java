/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.web.common.handlers.RestExceptionHandler;
import org.entando.entando.web.common.interceptor.EntandoOauth2Interceptor;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Mock
    protected IApiOAuth2TokenManager apiOAuth2TokenManager;

    @Mock
    protected IAuthenticationProviderManager authenticationProviderManager;

    @Mock
    protected IAuthorizationManager authorizationManager;

    @InjectMocks
    protected EntandoOauth2Interceptor entandoOauth2Interceptor;

    /**
     * The returned list contains an {@link HandlerExceptionResolver} built with
     * an instance of a {@link ResourceBundleMessageSource}, that points to the
     * default baseName and with an instance of {@link RestExceptionHandler},
     * the global exceptionHandler
     *
     * A typical use is:
     * <pre>
     * <code>
     * mockMvc = MockMvcBuilders.standaloneSetup(someControllerUnderTest)
     * .addInterceptors(...)
     * .setHandlerExceptionResolvers(createHandlerExceptionResolver())
     * .build();
     * </code>
     * </pre>
     *
     *
     * @return
     */
    protected List<HandlerExceptionResolver> createHandlerExceptionResolver() {
        List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>();
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = createExceptionResolver();
        handlerExceptionResolvers.add(exceptionHandlerExceptionResolver);
        return handlerExceptionResolvers;
    }

    protected ExceptionHandlerExceptionResolver createExceptionResolver() {

        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("rest/messages");
        messageSource.setUseCodeAsDefaultMessage(true);

        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {

            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestExceptionHandler.class).resolveMethod(exception);
                RestExceptionHandler validationHandler = new RestExceptionHandler();
                validationHandler.setMessageSource(messageSource);
                return new ServletInvocableHandlerMethod(validationHandler, method);
            }
        };

        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    protected Object createPagedMetadata(String json) throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue(json, PagedMetadata.class);
        return result;
    }

    protected Object createMetadata(String json, Class classType) throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue(json, classType);
        return result;
    }

    protected byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    protected String mockOAuthInterceptor(UserDetails user) throws Exception, ApsSystemException {
        String accessToken = OAuth2TestUtils.getValidAccessToken();
        when(apiOAuth2TokenManager.getApiOAuth2Token(Mockito.anyString())).thenReturn(OAuth2TestUtils.getOAuth2Token(user.getUsername(), accessToken));
        when(authenticationProviderManager.getUser(user.getUsername())).thenReturn(user);
        when(authorizationManager.isAuthOnPermission(any(UserDetails.class), anyString())).then(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                UserDetails user = (UserDetails) invocation.getArguments()[0];
                String permissionName = (String) invocation.getArguments()[1];
                return new AuthorizationManager().isAuthOnPermission(user, permissionName);
            }
        });
        return accessToken;
    }

}
