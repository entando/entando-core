/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.api.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.aps.system.services.api.model.ServiceInfo;

/**
 * @author E.Santoboni
 */
@XmlSeeAlso({ServiceInfo.class})
public class ServicesResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "services", required = false)
    public ListResponse getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<ServiceInfo> services = new ArrayList<ServiceInfo>();
            services.addAll((Collection<ServiceInfo>) this.getMainResult());
            ListResponse listResponse = new ListResponse(services) {};
            return listResponse;
        }
        return null;
    }
    
}