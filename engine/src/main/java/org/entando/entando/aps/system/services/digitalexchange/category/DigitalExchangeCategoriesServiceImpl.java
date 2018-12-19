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
package org.entando.entando.aps.system.services.digitalexchange.category;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.SimpleDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class DigitalExchangeCategoriesServiceImpl implements DigitalExchangeCategoriesService {

    public static final String ERRCODE_DE_CATEGORIES_CONFIG_NOT_FOUND = "1";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigInterface configManager;
    private final DigitalExchangesClient client;
    private final MessageSource messageSource;

    @Autowired
    public DigitalExchangeCategoriesServiceImpl(ConfigInterface configManager, MessageSource messageSource, DigitalExchangesClient client) {
        this.configManager = configManager;
        this.messageSource = messageSource;
        this.client = client;
    }

    @Override
    public ResilientListWrapper<String> getCategories() {

        String supportedCategoriesConfig = configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGE_CATEGORIES);

        ResilientListWrapper<String> result = new ResilientListWrapper<>();

        if (supportedCategoriesConfig == null) {
            logger.error("DigitalExchange categories configuration not found");
            result.getErrors().add(getCategoriesConfigNotFoundError());
            return result;
        }

        List<String> supportedCategories = Arrays.asList(supportedCategoriesConfig.trim().split(","));

        SimpleDigitalExchangeCall<List<String>> call = new SimpleDigitalExchangeCall<>(HttpMethod.GET, new ParameterizedTypeReference<SimpleRestResponse<List<String>>>() {
        }, "digitalExchange", "categories");

        ResilientListWrapper<List<String>> responses = client.getCombinedResult(call);

        // intersection between supported categories and provided categories
        List<String> categories = responses.getList().stream()
                .flatMap(List::stream)
                .distinct()
                .filter(c -> supportedCategories.contains(c))
                .collect(Collectors.toList());
        
        result.getList().addAll(categories);
        result.getErrors().addAll(responses.getErrors());
        
        return result;
    }

    private RestError getCategoriesConfigNotFoundError() {
        String errorMessage = messageSource.getMessage("digitalExchange.categories.config.notFound", new Object[]{}, null);
        return new RestError(ERRCODE_DE_CATEGORIES_CONFIG_NOT_FOUND, errorMessage);
    }
}
