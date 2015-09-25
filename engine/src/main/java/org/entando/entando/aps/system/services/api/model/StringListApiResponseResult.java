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
package org.entando.entando.aps.system.services.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author E.Santoboni
 */
public class StringListApiResponseResult extends AbstractApiResponseResult {
    
	@Override
    @XmlElement(name = "items", required = false)
    public ListResponse<String> getResult() {
        if (null != this.getMainResult()) {
            List<String> strings = new ArrayList<String>();
            strings.addAll((Collection<String>) this.getMainResult());
            ListResponse<String> entity = new ListResponse<String>(strings) {};
            return entity;
        }
        return null;
    }
    
}