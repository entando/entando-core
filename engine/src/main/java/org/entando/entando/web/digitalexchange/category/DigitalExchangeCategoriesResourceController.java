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
package org.entando.entando.web.digitalexchange.category;

import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.category.DigitalExchangeCategoriesService;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigitalExchangeCategoriesResourceController implements DigitalExchangeCategoriesResource {

    private final DigitalExchangeCategoriesService service;

    @Autowired
    public DigitalExchangeCategoriesResourceController(DigitalExchangeCategoriesService service) {
        this.service = service;
    }

    @Override
    public SimpleRestResponse<List<String>> getCategories() {
        ResilientListWrapper<String> categoriesResult = service.getCategories();

        SimpleRestResponse<List<String>> response = new SimpleRestResponse<>(categoriesResult.getList());
        response.setErrors(categoriesResult.getErrors());

        return response;
    }
}
