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
package org.entando.entando.aps.system.services.digitalexchange;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class DigitalExchangesManagerTest {

    private static final String DE_1 = "DE 1";
    private static final String URL = "http://www.entando.com/";

    @Mock
    private ConfigInterface configManager;

    @Mock
    private DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory;

    @InjectMocks
    private DigitalExchangesManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRestTemplateFactory();
        mockValidDigitalExchangesConfig();
        manager.refresh();
    }

    @Test
    public void shouldReturnDigitalExchanges() {

        List<DigitalExchange> digitalExchanges = manager.getDigitalExchanges();

        assertThat(digitalExchanges)
                .isNotNull()
                .hasSize(1)
                .element(0)
                // Testing JAXB deserialization
                .extracting(DigitalExchange::getName, DigitalExchange::getUrl,
                        DigitalExchange::getClientKey, DigitalExchange::getClientSecret,
                        DigitalExchange::isActive, DigitalExchange::getTimeout)
                .asList()
                .containsExactly(DE_1, URL, "client-id", "client-secret", true, 5000);

        assertThat(manager.getRestTemplate(DE_1)).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListForMissingConfiguration() {

        // missing configuration
        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn(null);
        manager.refresh();

        assertThat(manager.getDigitalExchanges())
                .isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyListForInvalidConfiguration() {

        // invalid configuration
        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("");
        manager.refresh();

        assertThat(manager.getDigitalExchanges())
                .isNotNull().isEmpty();
    }

    @Test
    public void shouldFindDigitalExchange() {
        assertTrue(manager.findByName(DE_1).isPresent());
        assertThat(manager.getRestTemplate(DE_1)).isNotNull();
    }

    @Test
    public void shouldFindNothing() {
        assertFalse(manager.findByName("Inexistent DE").isPresent());
    }

    @Test
    public void shouldAddDigitalExchange() {
        String newDigitalExchangeName = "DE 2";

        assertThat(manager.create(getDigitalExchange(newDigitalExchangeName)))
                .isNotNull()
                .extracting(DigitalExchange::getName)
                .isEqualTo(newDigitalExchangeName);

        assertThat(manager.getRestTemplate(DE_1)).isNotNull();
        assertThat(manager.getRestTemplate(newDigitalExchangeName)).isNotNull();
    }

    @Test
    public void shouldNotAddExistingDigitalExchange() {
        assertThat(manager.create(getDigitalExchange(DE_1))).isNull();
    }

    @Test
    public void shouldNotAddDisabledDigitalExchangeTemplateOnCreate() {
        String newDigitalExchangeName = "DE 2";
        DigitalExchange digitalExchange = getDigitalExchange(newDigitalExchangeName);
        digitalExchange.setActive(false);

        assertThat(manager.create(digitalExchange)).isNotNull();
        assertTrue(manager.findByName(newDigitalExchangeName).isPresent());
        assertThat(manager.getRestTemplate(newDigitalExchangeName)).isNull();
    }

    @Test
    public void shouldUpdateDigitalExchange() {
        DigitalExchange digitalExchange = getDigitalExchange(DE_1);
        int timeout = 1000;
        digitalExchange.setTimeout(timeout);

        assertThat(manager.update(digitalExchange))
                .isNotNull().extracting(DigitalExchange::getName, DigitalExchange::getTimeout)
                .asList().containsExactly(DE_1, timeout);

        assertThat(manager.getRestTemplate(DE_1)).isNotNull();
        assertThat(manager.getRestTemplate(DE_1)).isNotNull();
    }

    @Test
    public void shouldNotUpdateInexistentDigitalExchange() {
        assertThat(manager.update(getDigitalExchange("Inexistent DE"))).isNull();
    }

    @Test
    public void shouldNotAddDisabledDigitalExchangeTemplateOnUpdate() {
        DigitalExchange digitalExchange = getDigitalExchange(DE_1);
        digitalExchange.setActive(false);

        assertThat(manager.update(digitalExchange)).isNotNull();
        assertTrue(manager.findByName(DE_1).isPresent());
        assertThat(manager.getRestTemplate(DE_1)).isNull();
    }

    @Test
    public void shouldDeleteDigitalExchange() {
        manager.delete(DE_1);
        assertFalse(manager.findByName(DE_1).isPresent());
        assertThat(manager.getRestTemplate(DE_1)).isNull();
    }

    @Test
    public void shouldFailSafelyWhenUnableToCreateOAuth2RestTemplate() {
        
        when(restTemplateFactory.createOAuth2RestTemplate(any())).thenReturn(null);
        
        String newDigitalExchangeName = "DE 2";

        manager.create(getDigitalExchange(newDigitalExchangeName));
        assertTrue(manager.findByName(newDigitalExchangeName).isPresent());
        assertFalse(manager.findByName(newDigitalExchangeName).get().isActive());
        assertThat(manager.getRestTemplate(newDigitalExchangeName)).isNull();
    }

    private void mockRestTemplateFactory() {
        when(restTemplateFactory.createOAuth2RestTemplate(any()))
                .thenReturn(new OAuth2RestTemplate(new ClientCredentialsResourceDetails()));
    }

    private void mockValidDigitalExchangesConfig() {

        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("<digitalExchanges>"
                        + "  <digitalExchange>"
                        + "    <name>" + DE_1 + "</name>"
                        + "    <url>" + URL + "</url>"
                        + "    <key>client-id</key>"
                        + "    <secret>client-secret</secret>"
                        + "    <timeout>5000</timeout>"
                        + "    <active>true</active>"
                        + "  </digitalExchange>"
                        + "</digitalExchanges>");
    }

    private DigitalExchange getDigitalExchange(String name) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        digitalExchange.setUrl(URL);
        digitalExchange.setActive(true);
        return digitalExchange;
    }
}
