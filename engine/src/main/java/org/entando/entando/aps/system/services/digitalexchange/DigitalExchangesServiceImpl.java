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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchangesConfig;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import static org.entando.entando.web.digitalexchange.DigitalExchangeValidator.*;

public class DigitalExchangesServiceImpl implements DigitalExchangesService {

    private static final String DIGITAL_EXCHANGE_LABEL = "digitalExchange";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ConfigInterface configManager;

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    @Override
    public List<DigitalExchange> getDigitalExchanges() {

        String digitalExchangesConfig = configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES);

        if (digitalExchangesConfig == null) {
            logger.warn("DigitalExchanges configuration not found");
            return new ArrayList<>();
        }

        try {
            DigitalExchangesConfig config = JAXB.unmarshal(new StringReader(digitalExchangesConfig), DigitalExchangesConfig.class);
            return config.getDigitalExchanges();
        } catch (DataBindingException ex) {
            logger.error("Unable to parse DigitalExchanges XML configuration", ex);
            return new ArrayList<>();
        }
    }

    @Override
    public DigitalExchange findByName(String name) {
        Optional<DigitalExchange> maybeDigitalExchange = getDigitalExchange(getDigitalExchanges(), name);
        return maybeDigitalExchange.orElseThrow(()
                -> new RestRourceNotFoundException(ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND, DIGITAL_EXCHANGE_LABEL, name));
    }

    @Override
    public DigitalExchange create(DigitalExchange digitalExchange) {

        List<DigitalExchange> digitalExchanges = getDigitalExchanges();

        BeanPropertyBindingResult validationResult = this.validateForCreate(digitalExchanges, digitalExchange);
        if (validationResult.hasErrors()) {
            throw new ValidationConflictException(validationResult);
        }

        digitalExchanges.add(digitalExchange);
        updateDigitalExchangesConfig(digitalExchanges);

        return digitalExchange;
    }

    private BeanPropertyBindingResult validateForCreate(List<DigitalExchange> digitalExchanges, DigitalExchange digitalExchange) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(digitalExchange, DIGITAL_EXCHANGE_LABEL);
        if (digitalExchanges.stream().anyMatch(m -> m.getName().equals(digitalExchange.getName()))) {
            errors.reject(ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS, null, "digitalExchange.exists");
        }
        return errors;
    }

    @Override
    public DigitalExchange update(DigitalExchange digitalExchange) {
        List<DigitalExchange> digitalExchanges = getDigitalExchanges();
        checkIfExists(digitalExchanges, digitalExchange.getName());
        digitalExchanges.replaceAll(m -> m.getName().equals(digitalExchange.getName()) ? digitalExchange : m);
        return digitalExchange;
    }

    @Override
    public void delete(String digitalExchangeName) {
        List<DigitalExchange> digitalExchanges = getDigitalExchanges();
        checkIfExists(digitalExchanges, digitalExchangeName);
        digitalExchanges.removeIf(m -> m.getName().equals(digitalExchangeName));
        updateDigitalExchangesConfig(digitalExchanges);
    }

    private void updateDigitalExchangesConfig(List<DigitalExchange> digitalExchanges) {
        DigitalExchangesConfig config = new DigitalExchangesConfig();
        config.setDigitalExchanges(digitalExchanges);
        StringWriter sw = new StringWriter();
        JAXB.marshal(config, sw);
        String configString = sw.toString();

        try {
            configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES, configString);
        } catch (ApsSystemException ex) {
            throw new RuntimeException("Error updating DigitalExchanges configuration", ex);
        }
    }

    private Optional<DigitalExchange> getDigitalExchange(List<DigitalExchange> digitalExchanges, String digitalExchangeName) {
        return digitalExchanges.stream().filter(m -> m.getName().equals(digitalExchangeName)).findFirst();
    }

    private void checkIfExists(List<DigitalExchange> digitalExchanges, String digitalExchangeName) {
        if (!getDigitalExchange(digitalExchanges, digitalExchangeName).isPresent()) {
            throw new RestRourceNotFoundException(ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND, DIGITAL_EXCHANGE_LABEL, digitalExchangeName);
        }
    }
}
