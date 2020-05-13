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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.assertionhelper.PageAssertionHelper;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageRequest;

import java.util.*;
import java.util.stream.IntStream;

import org.entando.entando.web.page.model.PageSearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.springframework.context.ApplicationContext;

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

    @Mock
    private IPageTokenManager pageTokenManager;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private PageUtilizer pageUtilizer;

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
        when(dtoBuilder.convert(any(IPage.class))).thenReturn(dto);

//        PageModel pageModel = PageMockHelper.mockServicePageModel();

        Page page = PageMockHelper.mockTestPage();
        page.setExtraGroups(new HashSet<>(Arrays.asList(PageMockHelper.GROUP)));
        when(pageManager.getDraftPage(page.getCode())).thenReturn(page);
        when(pageModelManager.getPageModel(page.getModel().getCode())).thenReturn(page.getModel());

        PageRequest request = PageMockHelper.mockPageRequest(page);
        request.setJoinGroups(Arrays.asList(PageMockHelper.GROUP, "admin"));
        when(pageManager.getDraftPage(request.getParentCode())).thenReturn(new Page());
        PageDto pageDto = pageService.updatePage(page.getCode(), request);

        assertThat(pageDto.getJoinGroups()).containsExactlyInAnyOrder(PageMockHelper.GROUP, "admin");
    }

    @Test
    public void shouldRemoveExtraGroup() {
        PageDto dto = new PageDto();
        dto.addJoinGroup("free");
        when(dtoBuilder.convert(any(IPage.class))).thenReturn(dto);

        PageModel pageModel = PageMockHelper.mockServicePageModel();
        when(pageModelManager.getPageModel(pageModel.getCode())).thenReturn(pageModel);

        Page page = PageMockHelper.mockTestPage();
        page.setExtraGroups(new HashSet<>(Arrays.asList("free", "admin")));
        when(pageManager.getDraftPage(page.getCode())).thenReturn(page);

        PageRequest request = PageMockHelper.mockPageRequest(page);
        request.setJoinGroups(Arrays.asList("free"));
        when(pageManager.getDraftPage(request.getParentCode())).thenReturn(new Page());
        PageDto pageDto = pageService.updatePage(page.getCode(), request);

        assertThat(pageDto.getJoinGroups()).containsExactly("free");
    }


    @Test
    public void getPageUsageDetailsWithPublishedPageShouldAddItself() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();

        this.testSinglePageUsageDetails(pageDto);
    }

    @Test
    public void getPageUsageDetailsWithPaginationAndWithPublishedPageShouldAddItself() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();

        this.testPagedPageUsageDetails(pageDto);
    }


    @Test
    public void getPageUsageDetailsWithInvalidCodeShouldThrowResourceNotFoundException() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();
        mockForPageUsageTest(PageMockHelper.mockTestPage(), pageDto);

        Arrays.stream(new String[]{ "not existing", null, ""})
                .forEach(code -> {
                    try {
                        pageService.getComponentUsageDetails(code, new PageSearchRequest(PageMockHelper.PAGE_CODE));
                        fail("ResourceNotFoundException NOT thrown with code " + code);
                    } catch (Exception e) {
                        assertTrue(e instanceof ResourceNotFoundException);
                    }
                });
    }


    @Test
    public void getPageUsageDetailsWithPublishedPageShouldNOTAddItself() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();
        pageDto.setStatus(IPageService.STATUS_DRAFT);

        this.testSinglePageUsageDetails(pageDto);
    }


    @Test
    public void getPageUsageDetailsWithPaginationAndWithPublishedPageShouldNOTAddItself() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();
        pageDto.setStatus(IPageService.STATUS_DRAFT);

        this.testPagedPageUsageDetails(pageDto);
    }



    @Test
    public void getPageUsageDetailsWithNoChildrenShouldReturnItself() throws Exception {

        PageDto pageDto = PageMockHelper.mockPageDto();
        pageDto.setChildren(new ArrayList<>());

        mockForPageUsageTest(PageMockHelper.mockTestPage(), pageDto);

        PagedMetadata<ComponentUsageEntity> pageUsageDetails = pageService.getComponentUsageDetails(PageMockHelper.PAGE_CODE, new PageSearchRequest(PageMockHelper.PAGE_CODE));

        PageAssertionHelper.assertUsageDetails(pageUsageDetails, new String[0], 0, 1, pageDto.getStatus());
    }



    /**
     * contains generic code to test a single paged page usage details
     * @param pageDto
     * @throws Exception
     */
    private void testSinglePageUsageDetails(PageDto pageDto) throws Exception {

        mockForPageUsageTest(PageMockHelper.mockTestPage(), pageDto);

        PagedMetadata<ComponentUsageEntity> pageUsageDetails = pageService.getComponentUsageDetails(PageMockHelper.PAGE_CODE, new PageSearchRequest(PageMockHelper.PAGE_CODE));

        PageAssertionHelper.assertUsageDetails(pageUsageDetails, pageDto.getStatus());
    }


    /**
     * contains generic code to test a single paged page usage details
     * @param pageDto
     * @throws Exception
     */
    private void testPagedPageUsageDetails(PageDto pageDto) throws Exception {

        mockForPageUsageTest(PageMockHelper.mockTestPage(), pageDto);

        PageSearchRequest pageSearchRequest = new PageSearchRequest(PageMockHelper.PAGE_CODE);
        pageSearchRequest.setPageSize(3);

        // creates paged data
        List<Integer> pageList = Arrays.asList(1, 2);
        String[][] utilizers = {
                { PageMockHelper.UTILIZER_1, PageMockHelper.UTILIZER_2, PageMockHelper.UTILIZER_3 },
                { PageMockHelper.UTILIZER_4, PageMockHelper.UTILIZER_5 }
        };

        // does assertions
        IntStream.range(0, pageList.size())
                .forEach(i -> {

                    pageSearchRequest.setPage(pageList.get(i));

                    PagedMetadata<ComponentUsageEntity> pageUsageDetails = pageService.getComponentUsageDetails(PageMockHelper.PAGE_CODE, pageSearchRequest);

                    PageAssertionHelper.assertUsageDetails(pageUsageDetails,
                            utilizers[i],
                            PageMockHelper.UTILIZERS.length,
                            pageList.get(i),
                            pageDto.getStatus());
                });
    }



    /**
     *
     */
    private void mockForPageUsageTest(Page page, PageDto pageDto) throws ApsSystemException {

        when(pageManager.getDraftPage(page.getCode())).thenReturn(page);
        when(pageTokenManager.encrypt(page.getCode())).thenReturn(PageMockHelper.TOKEN);
        when(dtoBuilder.convert(any(IPage.class))).thenReturn(pageDto);
        when(applicationContext.getBeanNamesForType((Class<?>) any())).thenReturn(PageMockHelper.UTILIZERS);
        when(applicationContext.getBean(anyString())).thenReturn(pageUtilizer);
        when(pageUtilizer.getPageUtilizers(page.getCode())).thenReturn(Arrays.asList(PageMockHelper.UTILIZERS));
        when(pageUtilizer.getName())
                .thenReturn(PageMockHelper.UTILIZER_1)
                .thenReturn(PageMockHelper.UTILIZER_2);
    }
}
