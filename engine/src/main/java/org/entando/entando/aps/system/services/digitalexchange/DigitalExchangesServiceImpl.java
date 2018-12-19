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

import java.util.List;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.entando.entando.web.digitalexchange.DigitalExchangeValidator.*;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DigitalExchangesServiceImpl implements DigitalExchangesService {

    private static final String DIGITAL_EXCHANGE_LABEL = "digitalExchange";

    private final DigitalExchangesManager manager;

    @Autowired
    public DigitalExchangesServiceImpl(DigitalExchangesManager manager) {
        this.manager = manager;
    }

    @Override
    public List<DigitalExchange> getDigitalExchanges() {
        return manager.getDigitalExchanges();
    }

    @Override
    public DigitalExchange findByName(String name) {
        return manager.findByName(name).orElseThrow(()
                -> new RestRourceNotFoundException(ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND, DIGITAL_EXCHANGE_LABEL, name));
    }

    @Override
    public DigitalExchange create(DigitalExchange digitalExchange) {

        manager.findByName(digitalExchange.getName())
                .ifPresent(de -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(digitalExchange, DIGITAL_EXCHANGE_LABEL);
                    errors.reject(ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS, null, "digitalExchange.exists");
                    throw new ValidationConflictException(errors);
                });

        validateURL(digitalExchange);

        return manager.create(digitalExchange);
    }

    @Override
    public DigitalExchange update(DigitalExchange digitalExchange) {
        findByName(digitalExchange.getName());
        validateURL(digitalExchange);
        return manager.update(digitalExchange);
    }

    @Override
    public void delete(String digitalExchangeName) {
        findByName(digitalExchangeName);
        manager.delete(digitalExchangeName);
    }

    private void validateURL(DigitalExchange digitalExchange) {
        try {
            UriComponentsBuilder.fromHttpUrl(digitalExchange.getUrl());
        } catch (IllegalArgumentException ex) {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(digitalExchange, DIGITAL_EXCHANGE_LABEL);
            errors.reject(ERRCODE_DIGITAL_EXCHANGE_INVALID_URL, null, "digitalExchange.url.invalid");
            throw new ValidationConflictException(errors);
        }
    }
}
