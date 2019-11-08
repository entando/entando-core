/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;
import java.util.Arrays;
import java.util.HashSet;
import org.entando.entando.aps.system.services.page.model.PageDtoBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.mockito.Mockito;

@RunWith(MockitoJUnitRunner.class)
public class PageServiceTest {

    @Mock
    private IPageManager pageManager;

    @Mock
    private IPageModelManager pageModelManager;

    @Mock
    private IGroupManager groupManager;
    
    @Mock
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    @InjectMocks
    private PageService pageService;

    @Before
    public void setUp() {
        when(groupManager.getGroup("free")).thenReturn(new Group());
        when(groupManager.getGroup("admin")).thenReturn(new Group());
    }

    @Test
    public void shouldAddExtraGroup() {
        PageDto dto = new PageDto();
        dto.addJoinGroup("free");
        dto.addJoinGroup("admin");
        when(dtoBuilder.convert(Mockito.any(IPage.class))).thenReturn(dto);
        
        PageModel pageModel = getServicePageModel();
        when(pageModelManager.getPageModel(pageModel.getCode())).thenReturn(pageModel);

        Page page = getTestPage();
        page.setExtraGroups(new HashSet<>(Arrays.asList("free")));
        when(pageManager.getDraftPage(page.getCode())).thenReturn(page);

        PageRequest request = getPageRequest(page);
        request.setJoinGroups(Arrays.asList("free", "admin"));
        when(pageManager.getDraftPage(request.getParentCode())).thenReturn(new Page());
        PageDto pageDto = pageService.updatePage(page.getCode(), request);

        assertThat(pageDto.getJoinGroups()).containsExactlyInAnyOrder("free", "admin");
    }

    @Test
    public void shouldRemoveExtraGroup() {
        PageDto dto = new PageDto();
        dto.addJoinGroup("free");
        when(dtoBuilder.convert(Mockito.any(IPage.class))).thenReturn(dto);
        
        PageModel pageModel = getServicePageModel();
        when(pageModelManager.getPageModel(pageModel.getCode())).thenReturn(pageModel);

        Page page = getTestPage();
        page.setExtraGroups(new HashSet<>(Arrays.asList("free", "admin")));
        when(pageManager.getDraftPage(page.getCode())).thenReturn(page);

        PageRequest request = getPageRequest(page);
        request.setJoinGroups(Arrays.asList("free"));
        when(pageManager.getDraftPage(request.getParentCode())).thenReturn(new Page());
        PageDto pageDto = pageService.updatePage(page.getCode(), request);

        assertThat(pageDto.getJoinGroups()).containsExactly("free");
    }

    private Page getTestPage() {
        Page page = new Page();
        page.setCode("test");
        page.setParentCode("homepage");
        page.setGroup("free");
        page.setModel(getServicePageModel());
        return page;
    }

    private PageRequest getPageRequest(Page page) {
        PageRequest request = new PageRequest();
        request.setPageModel(page.getModel().getCode());
        request.setParentCode(page.getParentCode());
        request.setOwnerGroup(page.getGroup());
        return request;
    }

    private PageModel getServicePageModel() {
        PageModel pageModel = new PageModel();
        pageModel.setCode("service");
        return pageModel;
    }
}
