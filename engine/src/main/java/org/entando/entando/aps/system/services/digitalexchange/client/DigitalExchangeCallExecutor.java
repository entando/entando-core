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
package org.entando.entando.aps.system.services.digitalexchange.client;

import java.net.SocketTimeoutException;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import static org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientImpl.*;

public class DigitalExchangeCallExecutor<R extends RestResponse<?, ?>, C> {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeCallExecutor.class);

    private final MessageSource messageSource;
    private final DigitalExchange digitalExchange;
    private final OAuth2RestTemplate restTemplate;
    private final DigitalExchangeCall<R, C> call;

    protected DigitalExchangeCallExecutor(MessageSource messageSource,
            DigitalExchange digitalExchange, OAuth2RestTemplate restTemplate,
            DigitalExchangeCall<R, C> call) {
        
        this.messageSource = messageSource;
        this.digitalExchange = digitalExchange;
        this.restTemplate = restTemplate;
        this.call = call;
    }

    protected R getResponse() {

        String url;

        try {
            url = call.getURL(digitalExchange);
        } catch (IllegalArgumentException ex) {
            logger.error("Error calling {}. Invalid URL", digitalExchange.getUrl());
            return getErrorResponse(ERRCODE_DE_INVALID_URL, "digitalExchange.invalidUrl", digitalExchange.getName(), digitalExchange.getUrl());
        }

        try {
            ResponseEntity<R> responseEntity = restTemplate
                    .exchange(url, call.getMethod(), call.getEntity(), call.getParameterizedTypeReference());

            R response = responseEntity.getBody();

            if (call.isResponseParsable(response)) {
                return response;
            } else {
                logger.error("Error calling {}. Unable to parse response", url);
                return getErrorResponse(ERRCODE_DE_WRONG_PAYLOAD, "digitalExchange.unparsableResponse", digitalExchange.getName());
            }

        } catch (RestClientResponseException ex) { // Error response

            logger.error("Error calling {}. Status code: {}", url, ex.getRawStatusCode());
            return getErrorResponse(ERRCODE_DE_HTTP_ERROR, "digitalExchange.httpError", digitalExchange.getName(), ex.getRawStatusCode());

        } catch (ResourceAccessException ex) { // Other (e.g. unknown host)

            return manageResourceAccessException(url, ex);

        } catch (OAuth2Exception ex) {

            if (ex.getCause() instanceof ResourceAccessException) { // Server down, unknown host, ...
                return manageResourceAccessException(url, (ResourceAccessException) ex.getCause());
            } else {
                logger.error("Error calling {}. Exception message: {}", url, ex.getMessage());
                return getErrorResponse(ERRCODE_DE_AUTH, "digitalExchange.oauth2Error", digitalExchange.getName());
            }

        } catch (Throwable t) {
            logger.error("Error calling {}", url);
            logger.error("Unexpected exception", t);
            throw t;
        }
    }

    private R manageResourceAccessException(String url, ResourceAccessException ex) {

        if (ex.getCause() instanceof SocketTimeoutException) {
            logger.error("Timeout calling {}", url);

            return getErrorResponse(ERRCODE_DE_TIMEOUT, "digitalExchange.timeout",
                    digitalExchange.getName(), digitalExchange.getTimeout());
        }

        logger.error("Error calling {}. Exception message: {}", url, ex.getMessage());
        return getErrorResponse(ERRCODE_DE_UNREACHABLE, "digitalExchange.unreachable", digitalExchange.getName());
    }

    private R getErrorResponse(String errorCode, String msgCode, Object... msgParams) {
        R errorResponse = call.getEmptyRestResponse();
        String errorMessage = messageSource.getMessage(msgCode, msgParams, null);
        errorResponse.addError(new RestError(errorCode, errorMessage));
        return errorResponse;
    }
}
