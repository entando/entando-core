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
package org.entando.entando.aps.system.services.dataobject.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.List;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;

/**
 * @author E.Santoboni
 */
public class DataTypeDto extends EntityTypeFullDto {

    private String viewPage;
    private String listModel;
    private String defaultModel;

    public DataTypeDto() {
        super();
    }

    public DataTypeDto(DataObject dataObject, List<AttributeRole> roles) {
        super(dataObject, roles);
        this.setListModel(dataObject.getListModel());
        this.setDefaultModel(dataObject.getDefaultModel());
        this.setViewPage(dataObject.getViewPage());
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
