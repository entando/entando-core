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
package org.entando.entando.web.dataobject.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author E.Santoboni
 */
@Component
public class DataTypeValidator extends AbstractEntityTypeValidator {

    public static final String ERRCODE_INVALID_DEFAULT_MODEL = "31";
    public static final String ERRCODE_DEFAULT_MODEL_DOES_NOT_EXIST = "32";
    public static final String ERRCODE_DEFAULT_MODEL_MISMATCH = "33";

    public static final String ERRCODE_INVALID_LIST_MODEL = "34";
    public static final String ERRCODE_LIST_MODEL_DOES_NOT_EXIST = "35";
    public static final String ERRCODE_LIST_MODEL_MISMATCH = "36";

    public static final String ERRCODE_INVALID_VIEW_PAGE = "37";

    @Autowired
    private IDataObjectManager dataObjectManager;

    public IDataObjectManager getDataObjectManager() {
        return dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this.dataObjectManager = dataObjectManager;
    }

    @Override
    protected IEntityManager getEntityManager() {
        return this.dataObjectManager;
    }

}
