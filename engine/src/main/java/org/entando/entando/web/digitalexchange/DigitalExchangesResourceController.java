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
package org.entando.entando.web.digitalexchange;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.model.RestError;

@RestController
public class DigitalExchangesResourceController implements DigitalExchangesResource {

    private final DigitalExchangesService digitalExchangeService;
    private final DigitalExchangeValidator digitalExchangeValidator;

    @Autowired
    public DigitalExchangesResourceController(DigitalExchangesService digitalExchangeService, DigitalExchangeValidator digitalExchangeValidator) {
        this.digitalExchangeService = digitalExchangeService;
        this.digitalExchangeValidator = digitalExchangeValidator;
    }

    @Override
    public ResponseEntity<SimpleRestResponse<List<String>>> list() {
        List<String> digitalExchangeNames = digitalExchangeService.getDigitalExchanges()
                .stream().map(m -> m.getName())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new SimpleRestResponse<>(digitalExchangeNames));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DigitalExchange>> create(@Valid @RequestBody DigitalExchange digitalExchange, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return ResponseEntity.ok(new SimpleRestResponse<>(digitalExchangeService.create(digitalExchange)));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DigitalExchange>> get(@PathVariable("name") String name) {
        return ResponseEntity.ok(new SimpleRestResponse<>(digitalExchangeService.findByName(name)));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DigitalExchange>> update(@PathVariable("name") String name, @Valid @RequestBody DigitalExchange digitalExchange, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        digitalExchangeValidator.validateBodyName(name, digitalExchange, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return ResponseEntity.ok(new SimpleRestResponse<>(digitalExchangeService.update(digitalExchange)));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("name") String name) {
        digitalExchangeService.delete(name);
        return ResponseEntity.ok(new SimpleRestResponse<>(name));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<String>> test(@PathVariable("name") String name) {

        List<RestError> errors = digitalExchangeService.test(name);

        SimpleRestResponse<String> response = new SimpleRestResponse<>(errors.isEmpty() ? "OK" : "");
        response.setErrors(errors);

        return ResponseEntity.ok(response);
    }
}
