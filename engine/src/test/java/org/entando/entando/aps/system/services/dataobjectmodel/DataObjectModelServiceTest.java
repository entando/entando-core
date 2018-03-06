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
package org.entando.entando.aps.system.services.dataobjectmodel;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDtoBuilder;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class DataObjectModelServiceTest {

    @InjectMocks
    private DataObjectModelService dataObjectModelService;

    @Mock
    private IDataObjectModelManager dataObjectModelManager;

    @Mock
    private DataModelDtoBuilder dtoBuilder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationConflictException.class)
    public void should_raise_exception_on_delete_referenced_dataModel() throws Throwable {
        DataObjectModel mockDataModel = new DataObjectModel();
        mockDataModel.setId(234l);
        when(this.dataObjectModelManager.getDataObjectModel(any(Long.class))).thenReturn(mockDataModel);
        DataModelDto dto = new DataModelDto(mockDataModel);
        when(this.dtoBuilder.convert(mockDataModel)).thenReturn(dto);
        Map<String, List<IPage>> utilizers = new HashMap<>();
        List<IPage> pages = new ArrayList<>();
        Page referencedPage = new Page();
        referencedPage.setCode("referenced");
        pages.add(referencedPage);
        utilizers.put("ABC123", pages);
        when(this.dataObjectModelManager.getReferencingPages(any(Long.class))).thenReturn(utilizers);
        this.dataObjectModelService.removeDataObjectModel(34l);
    }
}
