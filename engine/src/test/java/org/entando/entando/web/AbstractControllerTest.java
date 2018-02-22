package org.entando.entando.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.common.handlers.RestExceptionHandler;
import org.entando.entando.web.common.model.PagedMetadata;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class AbstractControllerTest {

    /**
     * The returned list contains an {@link HandlerExceptionResolver} built with an instance of 
     * a {@link ResourceBundleMessageSource}, that points to the default baseName 
     * and with an instance of {@link RestExceptionHandler}, the global exceptionHandler
     * 
     * A typical use is:
     * <pre>
     * <code>
     * mockMvc = MockMvcBuilders.standaloneSetup(someControllerUnderTest)
                     .addInterceptors(...)
                     .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                     .build();
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

}
