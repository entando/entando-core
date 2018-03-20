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
package org.entando.entando.aps.system.services.dataobject;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class DataObjectServiceTest {

    @InjectMocks
    private DataObjectService dataObjectService;

    @Mock
    private IDataObjectManager dataObjectManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<IEntityManager> managers = new ArrayList<>();
        managers.add(dataObjectManager);
        this.dataObjectService.setEntityManagers(managers);
        when(this.dataObjectManager.getName()).thenReturn(SystemConstants.DATA_OBJECT_MANAGER);
    }

    @Test(expected = ValidationConflictException.class)
    public void deleteReferencedDataType() throws Throwable {
        when(this.dataObjectManager.getName()).thenReturn(SystemConstants.DATA_OBJECT_MANAGER);
        DataObject dataObject = new DataObject();
        dataObject.setTypeCode("ABC");
        when(this.dataObjectManager.getEntityPrototype("ABC")).thenReturn(dataObject);
        List<String> list = new ArrayList<>();
        list.add("ABC123");
        when(this.dataObjectManager.searchId("ABC", null)).thenReturn(list);
        this.dataObjectService.deleteDataType("ABC");
    }
}
