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
package org.entando.entando.aps.system.services.digitalexchange.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "digitalExchanges")
public class DigitalExchangesConfig {

    private List<DigitalExchange> digitalExchanges;

    public DigitalExchangesConfig() {
        this.digitalExchanges = new ArrayList<>();
    }

    @XmlElements({
        @XmlElement(name = "digitalExchange")
    })
    public List<DigitalExchange> getDigitalExchanges() {
        return digitalExchanges;
    }

    public void setDigitalExchanges(List<DigitalExchange> digitalExchanges) {
        this.digitalExchanges = digitalExchanges;
    }
}
