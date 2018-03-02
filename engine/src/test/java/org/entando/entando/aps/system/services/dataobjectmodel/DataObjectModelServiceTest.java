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

import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDtoBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DataObjectModelServiceTest {

    @InjectMocks
    private DataObjectModelService dataObjectModelService;

    @Mock
    private DataModelDtoBuilder dtoBuilder;

    @Mock
    private IDataObjectModelManager dataObjectModelManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void getNullObjectModel() {
        Mockito.when(dataObjectModelManager.getDefaultUtilizer(Mockito.any(Long.class))).thenReturn(null);
        this.dataObjectModelService.getDataObjectModel(4l);
    }

    /*
    @Test(expected = ValidationConflictException.class)
    public void should_raise_exception_on_delete_reserved_fragment() throws Throwable {
        GuiFragment reference = new GuiFragment();
        reference.setCode("referenced_code");
        reference.setGui("<p>Code</p>");
        GuiFragmentDto dto = new GuiFragmentDto();
        dto.setCode("master");
        dto.setCode("<p>Code of master</p>");
        dto.addFragmentRef(reference);
        when(this.dtoBuilder.convert(any(GuiFragment.class))).thenReturn(dto);
        GuiFragment fragment = new GuiFragment();
        fragment.setCode("test_code");
        when(guiFragmentManager.getGuiFragment("test_code")).thenReturn(fragment);
        this.guiFragmentService.removeGuiFragment(fragment.getCode());
    }
     */
}
