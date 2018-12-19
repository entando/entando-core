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

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DigitalExchangeOAuth2RestTemplateFactoryImpl implements DigitalExchangeOAuth2RestTemplateFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int DEFAULT_TIMEOUT = 10000;

    @Override
    public OAuth2RestTemplate createOAuth2RestTemplate(DigitalExchange digitalExchange) {
        OAuth2ProtectedResourceDetails resourceDetails = getResourceDetails(digitalExchange);
        if (resourceDetails == null) {
            return null;
        }
        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails);
        template.setRequestFactory(getRequestFactory(digitalExchange));
        template.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());
        return template;
    }

    private OAuth2ProtectedResourceDetails getResourceDetails(DigitalExchange digitalExchange) {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAuthenticationScheme(AuthenticationScheme.header);
        resourceDetails.setClientId(digitalExchange.getClientKey());
        resourceDetails.setClientSecret(digitalExchange.getClientSecret());
        try {
            resourceDetails.setAccessTokenUri(getTokenUri(digitalExchange));
        } catch (IllegalArgumentException ex) {
            logger.error("DigitalExchange {} has been configured with a wrong URL: {}",
                    digitalExchange.getName(), digitalExchange.getUrl());
            return null;
        }
        return resourceDetails;
    }

    private String getTokenUri(DigitalExchange digitalExchange) {
        return UriComponentsBuilder
                .fromHttpUrl(digitalExchange.getUrl())
                .pathSegment("api", "oauth", "token")
                .toUriString();
    }

    private ClientHttpRequestFactory getRequestFactory(DigitalExchange digitalExchange) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        int timeout = digitalExchange.getTimeout() > 0 ? digitalExchange.getTimeout() : DEFAULT_TIMEOUT;

        requestFactory.setConnectionRequestTimeout(timeout);
        requestFactory.setConnectTimeout(timeout);
        requestFactory.setReadTimeout(timeout);
        return requestFactory;
    }
}
