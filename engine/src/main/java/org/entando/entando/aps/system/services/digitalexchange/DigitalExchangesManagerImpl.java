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
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchangesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

@Service
public class DigitalExchangesManagerImpl implements DigitalExchangesManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigInterface configManager;
    private final DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory;

    private final List<DigitalExchange> exchanges = new CopyOnWriteArrayList<>();
    private final Map<String, OAuth2RestTemplate> templates = new ConcurrentHashMap<>();

    @Autowired
    public DigitalExchangesManagerImpl(ConfigInterface configManager, DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory) {
        this.configManager = configManager;
        this.restTemplateFactory = restTemplateFactory;
    }

    @PostConstruct
    public void init() {
        loadDigitalExchanges();
        exchanges.forEach(this::addRestTemplate);
    }

    @Override
    public void refresh() {
        exchanges.clear();
        templates.clear();
        init();
    }

    @Override
    public List<DigitalExchange> getDigitalExchanges() {
        return exchanges;
    }

    @Override
    public DigitalExchange create(DigitalExchange digitalExchange) {
        if (!findByName(digitalExchange.getName()).isPresent()) {
            exchanges.add(digitalExchange);
            updateDigitalExchangesConfig();
            if (digitalExchange.isActive()) {
                addRestTemplate(digitalExchange);
            }
            return digitalExchange;
        }
        return null;
    }

    @Override
    public Optional<DigitalExchange> findByName(String digitalExchangeName) {
        return exchanges.stream()
                .filter(de -> digitalExchangeName.equals(de.getName()))
                .findFirst();
    }

    @Override
    public DigitalExchange update(DigitalExchange digitalExchange) {
        OptionalInt index = getIndex(digitalExchange.getName());
        if (index.isPresent()) {
            exchanges.replaceAll(de -> {
                if (de.getName().equals(digitalExchange.getName())) {
                    updateDigitalExchangesConfig();
                    templates.remove(digitalExchange.getName());
                    if (digitalExchange.isActive()) {
                        addRestTemplate(digitalExchange);
                    }
                    return digitalExchange;
                }
                return de;
            });
            return digitalExchange;
        }
        return null;
    }

    @Override
    public void delete(String digitalExchangeName) {
        getIndex(digitalExchangeName)
                .ifPresent(i -> {
                    exchanges.remove(i);
                    updateDigitalExchangesConfig();
                    templates.remove(digitalExchangeName);
                });
    }

    private OptionalInt getIndex(String digitalExchangeName) {
        return IntStream.range(0, exchanges.size())
                .filter(i -> digitalExchangeName.equals(exchanges.get(i).getName()))
                .findFirst();
    }

    @Override
    public OAuth2RestTemplate getRestTemplate(String digitalExchangeName) {
        return templates.get(digitalExchangeName);
    }

    private void loadDigitalExchanges() {
        String digitalExchangesConfig = configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES);

        if (digitalExchangesConfig == null) {
            logger.warn("DigitalExchanges configuration not found");
            return;
        }

        try {
            DigitalExchangesConfig config = JAXB.unmarshal(new StringReader(digitalExchangesConfig), DigitalExchangesConfig.class);
            exchanges.addAll(config.getDigitalExchanges());
        } catch (DataBindingException ex) {
            logger.error("Unable to parse DigitalExchanges XML configuration", ex);
        }
    }

    private void addRestTemplate(DigitalExchange digitalExchange) {
        OAuth2RestTemplate template = restTemplateFactory.createOAuth2RestTemplate(digitalExchange);
        if (template != null) {
            templates.put(digitalExchange.getName(), template);
        } else {
            logger.error("Error creating OAuth2RestTemplate for DigitalExchange {}. This instance will be disabled.",
                    digitalExchange.getName());
            digitalExchange.setActive(false);
            update(digitalExchange);
        }
    }

    private void updateDigitalExchangesConfig() {
        DigitalExchangesConfig config = new DigitalExchangesConfig();
        config.setDigitalExchanges(exchanges);
        StringWriter sw = new StringWriter();
        JAXB.marshal(config, sw);
        String configString = sw.toString();

        try {
            configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES, configString);
        } catch (ApsSystemException ex) {
            throw new RuntimeException("Error updating DigitalExchanges configuration", ex);
        }
    }
}
