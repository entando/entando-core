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
package org.entando.entando.aps.system.services.digitalexchange.component;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;

public class DigitalExchangeComponentBuilder {

    private final DigitalExchangeComponent component = new DigitalExchangeComponent();

    public DigitalExchangeComponentBuilder() {
    }

    public DigitalExchangeComponentBuilder(String name) {
        setName(name);
    }

    public final DigitalExchangeComponentBuilder setName(String name) {
        component.setName(name);
        component.setId(name);
        return this;
    }

    public DigitalExchangeComponentBuilder setLastUpdate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        try {
            component.setLastUpdate(sdf.parse(date));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public DigitalExchangeComponentBuilder setLastUpdate(Date date) {
        component.setLastUpdate(date);
        return this;
    }

    public DigitalExchangeComponent build() {
        return component;
    }
}
