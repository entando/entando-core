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
package org.entando.entando.web.digitalexchange.marketplace;

import com.agiletec.aps.system.services.role.Permission;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.MarketplacesService;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketplacesControllerResource implements MarketplacesResource {

    private final MarketplacesService marketplacesService;
    private final MarketplaceValidator marketplaceValidator;

    @Autowired
    public MarketplacesControllerResource(MarketplacesService marketplacesService, MarketplaceValidator marketplaceValidator) {
        this.marketplacesService = marketplacesService;
        this.marketplaceValidator = marketplaceValidator;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<List<String>>> list() {
        List<String> marketplaceNames = marketplacesService.getMarketplaces()
                .stream().map(m -> m.getName())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new SimpleRestResponse<>(marketplaceNames));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<SimpleRestResponse<Marketplace>> create(@Valid @RequestBody Marketplace marketplace, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return ResponseEntity.ok(new SimpleRestResponse<>(marketplacesService.create(marketplace)));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimpleRestResponse<Marketplace>> get(@PathVariable("name") String name) {
        return ResponseEntity.ok(new SimpleRestResponse<>(marketplacesService.findByName(name)));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<SimpleRestResponse<Marketplace>> update(@PathVariable("name") String name, @Valid @RequestBody Marketplace marketplace, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        marketplaceValidator.validateBodyName(name, marketplace, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return ResponseEntity.ok(new SimpleRestResponse<>(marketplacesService.update(marketplace)));
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("name") String name) {
        marketplacesService.delete(name);
        return ResponseEntity.ok(new SimpleRestResponse<>(name));
    }
}
