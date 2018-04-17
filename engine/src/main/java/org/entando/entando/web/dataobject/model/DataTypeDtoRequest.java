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
package org.entando.entando.web.dataobject.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.List;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;

/**
 * @author E.Santoboni
 */
public class DataTypeDtoRequest extends EntityTypeDtoRequest {

    private String viewPage;
    private String listModel;
    private String defaultModel;

    public DataTypeDtoRequest() {
        super();
    }

    public DataTypeDtoRequest(IApsEntity entityType) {
        super(entityType);
        DataObject dataObject = (DataObject) entityType;
        this.setListModel(dataObject.getListModel());
        this.setDefaultModel(dataObject.getDefaultModel());
        this.setViewPage(dataObject.getViewPage());
    }

    public DataTypeDtoRequest(IApsEntity entityType, List<AttributeRole> roles) {
        this(entityType);
        List<AttributeInterface> entityAttributes = entityType.getAttributeList();
        for (AttributeInterface attribute : entityAttributes) {
            EntityTypeAttributeFullDto attributeDto = new EntityTypeAttributeFullDto(attribute, roles);
            this.getAttributes().add(attributeDto);
        }
    }

    public String getViewPage() {
        return this.viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getListModel() {
        return this.listModel;
    }

    public void setListModel(String listModel) {
        this.listModel = listModel;
    }

    public String getDefaultModel() {
        return this.defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

}
