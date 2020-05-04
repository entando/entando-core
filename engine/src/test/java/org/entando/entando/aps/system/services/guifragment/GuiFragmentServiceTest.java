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
package org.entando.entando.aps.system.services.guifragment;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import org.entando.entando.aps.system.services.assertionhelper.GuiFragmentAssertionHelper;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDtoBuilder;
import org.entando.entando.aps.system.services.mockhelper.FragmentMockHelper;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.AssertionErrors;

import java.util.Arrays;
import java.util.Map;

public class GuiFragmentServiceTest {

    @InjectMocks
    private GuiFragmentService guiFragmentService;

    @Mock
    private GuiFragmentDtoBuilder dtoBuilder;

    @Mock
    private IGuiFragmentManager guiFragmentManager;

    @Mock
    private ILangManager langManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationGenericException.class)
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


    @Test
    public void getFragmentUsageTest() throws Exception {

        Lang lang = new Lang();
        lang.setCode("IT");
        when(langManager.getDefaultLang()).thenReturn(lang);

        GuiFragment fragment = FragmentMockHelper.mockGuiFragment();
        GuiFragmentDto fragmentDto = FragmentMockHelper.mockGuiFragmentDto(fragment, langManager);

        when(guiFragmentManager.getGuiFragment(anyString())).thenReturn(fragment);
        when(this.dtoBuilder.convert(any(GuiFragment.class))).thenReturn(fragmentDto);

        PagedMetadata<ComponentUsageEntity> componentUsageDetails = guiFragmentService.getComponentUsageDetails(fragment.getCode(), new PageSearchRequest(PageMockHelper.PAGE_CODE));

        GuiFragmentAssertionHelper.assertUsageDetails(componentUsageDetails);
    }


}
