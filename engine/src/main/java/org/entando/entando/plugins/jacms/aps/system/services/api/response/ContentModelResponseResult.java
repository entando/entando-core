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
package org.entando.entando.plugins.jacms.aps.system.services.api.response;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;

/**
 * @author E.Santoboni
 */
public class ContentModelResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "contentModel", required = false)
    public ContentModel getResult() {
        return (ContentModel) this.getMainResult();
    }
	
}
