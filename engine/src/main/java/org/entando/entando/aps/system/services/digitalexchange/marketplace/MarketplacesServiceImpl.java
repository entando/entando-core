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
package org.entando.entando.aps.system.services.digitalexchange.marketplace;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.MarketplacesConfig;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import static org.entando.entando.web.digitalexchange.marketplace.MarketplaceValidator.*;

public class MarketplacesServiceImpl implements MarketplacesService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private ConfigInterface configManager;

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    @Override
    public List<Marketplace> getMarketplaces() {

        String marketplacesConfig = configManager.getConfigItem(SystemConstants.CONFIG_ITEM_MARKETPLACES);

        if (marketplacesConfig == null) {
            logger.warn("Marketplaces configuration not found");
            return new ArrayList<>();
        }

        try {
            MarketplacesConfig config = JAXB.unmarshal(new StringReader(marketplacesConfig), MarketplacesConfig.class);
            return config.getMarketplaces();
        } catch (DataBindingException ex) {
            logger.error("Unable to parse Marketplaces XML configuration", ex);
            return new ArrayList<>();
        }
    }

    @Override
    public Marketplace findByName(String name) {
        List<Marketplace> marketplaces = getMarketplaces();
        Optional<Marketplace> maybeMarketplace = getMarketplace(marketplaces, name);
        if (!maybeMarketplace.isPresent()) {
            throw new RestRourceNotFoundException(ERRCODE_MARKETPLACE_NOT_FOUND, "marketplace", name);
        }
        return maybeMarketplace.get();
    }

    @Override
    public Marketplace create(Marketplace marketplace) {

        List<Marketplace> marketplaces = getMarketplaces();

        BeanPropertyBindingResult validationResult = this.validateForCreate(marketplaces, marketplace);
        if (validationResult.hasErrors()) {
            throw new ValidationConflictException(validationResult);
        }

        marketplaces.add(marketplace);
        updateMarketplacesConfig(marketplaces);

        return marketplace;
    }

    private BeanPropertyBindingResult validateForCreate(List<Marketplace> marketplaces, Marketplace marketplace) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(marketplace, "marketplace");
        if (marketplaces.stream().anyMatch(m -> m.getName().equals(marketplace.getName()))) {
            errors.reject(ERRCODE_MARKETPLACE_ALREADY_EXISTS, null, "marketplace.exists");
        }
        return errors;
    }

    @Override
    public Marketplace update(Marketplace marketplace) {
        List<Marketplace> marketplaces = getMarketplaces();
        checkIfExists(marketplaces, marketplace.getName());
        marketplaces.replaceAll(m -> m.getName().equals(marketplace.getName()) ? marketplace : m);
        return marketplace;
    }

    @Override
    public void delete(String marketplaceName) {
        List<Marketplace> marketplaces = getMarketplaces();
        checkIfExists(marketplaces, marketplaceName);
        marketplaces.removeIf(m -> m.getName().equals(marketplaceName));
        updateMarketplacesConfig(marketplaces);
    }

    private void updateMarketplacesConfig(List<Marketplace> marketplaces) {
        MarketplacesConfig config = new MarketplacesConfig();
        config.setMarketplaces(marketplaces);
        StringWriter sw = new StringWriter();
        JAXB.marshal(config, sw);
        String configString = sw.toString();

        try {
            configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_MARKETPLACES, configString);
        } catch (ApsSystemException ex) {
            throw new RuntimeException("Error updating Marketplaces configuration", ex);
        }
    }

    private Optional<Marketplace> getMarketplace(List<Marketplace> marketplaces, String marketplaceName) {
        return marketplaces.stream().filter(m -> m.getName().equals(marketplaceName)).findFirst();
    }

    private void checkIfExists(List<Marketplace> marketplaces, String marketplaceName) {
        if (!getMarketplace(marketplaces, marketplaceName).isPresent()) {
            throw new RestRourceNotFoundException(ERRCODE_MARKETPLACE_NOT_FOUND, "marketplace", marketplaceName);
        }
    }
}
