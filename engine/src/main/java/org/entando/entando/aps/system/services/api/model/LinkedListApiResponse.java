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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "response")
@XmlSeeAlso({LinkedListItem.class})
public class LinkedListApiResponse extends AbstractApiResponse {
    
	@Override
    @XmlElement(name = "result", required = true)
    public LinkedListApiResponseResult getResult() {
        return (LinkedListApiResponseResult) super.getResult();
    }
	
	public void setResult(LinkedListApiResponseResult result) {
		super.setResult(result);
	}
    
	@Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new LinkedListApiResponseResult();
    }
    
}
