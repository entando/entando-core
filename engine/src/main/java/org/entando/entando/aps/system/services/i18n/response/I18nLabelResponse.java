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
package org.entando.entando.aps.system.services.i18n.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.userprofile.api.response.UserProfileResponseResult;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "response")
public class I18nLabelResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = true)
    public I18nLabelResponseResult getResult() {
        return (I18nLabelResponseResult) super.getResult();
    }
    
	public void setResult(I18nLabelResponseResult result) {
		super.setResult(result);
	}
	
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new I18nLabelResponseResult();
    }
    
}