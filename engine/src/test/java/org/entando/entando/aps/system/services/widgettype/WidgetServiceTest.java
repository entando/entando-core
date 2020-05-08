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
package org.entando.entando.aps.system.services.widgettype;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.services.assertionhelper.PageAssertionHelper;
import org.entando.entando.aps.system.services.assertionhelper.WidgetAssertionHelper;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.mockhelper.WidgetMockHelper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDtoBuilder;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WidgetServiceTest {

    private static final String WIDGET_1_CODE = "widget1";
    private static final String WIDGET_2_CODE = "widget2";
    private static final String CUSTOM_ELEMENT_1 = "my-custom-element-1";
    private static final String CUSTOM_ELEMENT_2 = "my-custom-element-2";
    private static final List<String> RESOURCES_1 = Arrays.asList("/relative/path/to/script.js", "/relative/path/to/otherScript.js");
    private static final List<String> RESOURCES_2 = Arrays.asList("/relative/path/to/script2.js", "/relative/path/to/otherScript2.js");
    private static final String BUNDLE_1 = "bundle1";
    private static final String BUNDLE_2 = "bundle2";
    private static final String CUSTOM_ELEMENT_KEY = "customElement";
    private static final String RESOURCES_KEY = "resources";

    @Mock
    private IPageManager pageManager;

    @Mock
    private IComponentManager componentManager;

    @Mock
    private WidgetTypeManager widgetManager;

    @Mock
    private IGroupManager groupManager;

    @Mock
    private IGuiFragmentManager guiFragmentManager;

    @InjectMocks
    private WidgetService widgetService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {

        when(pageManager.getOnlineWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(new Page()));
        when(pageManager.getDraftWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(new Page()));

        WidgetDtoBuilder dtoBuilder = new WidgetDtoBuilder();
        dtoBuilder.setPageManager(pageManager);
        dtoBuilder.setComponentManager(componentManager);
        dtoBuilder.setStockWidgetCodes("");
        widgetService.setDtoBuilder(dtoBuilder);

        when(widgetManager.getWidgetTypes()).thenReturn(ImmutableList.of(getWidget1(), getWidget2()));
    }

    @Test
    public void shouldReturnAll() {
        PagedMetadata<WidgetDto> result = widgetService.getWidgets(new RestListRequest());

        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getBundleId()).isEqualTo(BUNDLE_1);
        assertThat(result.getBody().get(0).getConfigUi().get(CUSTOM_ELEMENT_KEY)).isEqualTo(CUSTOM_ELEMENT_1);
        assertThat(result.getBody().get(0).getConfigUi().get(RESOURCES_KEY)).isEqualTo(RESOURCES_1);
        assertThat(result.getBody().get(1).getBundleId()).isEqualTo(BUNDLE_2);
        assertThat(result.getBody().get(1).getConfigUi().get(CUSTOM_ELEMENT_KEY)).isEqualTo(CUSTOM_ELEMENT_2);
        assertThat(result.getBody().get(1).getConfigUi().get(RESOURCES_KEY)).isEqualTo(RESOURCES_2);
    }

    @Test
    public void shouldFilterByCode() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("code");
        filter.setValue(WIDGET_1_CODE);
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldFilterByUsed() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("used");
        filter.setValue("2");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldFilterByTypology() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("typology");
        filter.setValue("custom");
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldFilterByGroup() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("group");
        filter.setValue("group2");
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
    }

    @Test
    public void shouldSortByCode() {
        RestListRequest requestList = new RestListRequest();

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_2_CODE);
    }

    @Test
    public void shouldSortByUsed() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("used");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldSortByTypology() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("typology");
        requestList.setDirection("DESC");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldSortByGroup() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("typology");
        requestList.setDirection("DESC");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    public void shouldAddNewWidget() throws Exception {
        // Given
        WidgetRequest widgetRequest = getWidgetRequest1();
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));

        // When
        WidgetDto widgetDto = widgetService.addWidget(widgetRequest);

        // Then
        ArgumentCaptor<WidgetType> argumentCaptor = ArgumentCaptor.forClass(WidgetType.class);
        verify(widgetManager).addWidgetType(argumentCaptor.capture());
        WidgetType argument = argumentCaptor.getValue();
        assertThat(argument.getCode()).isEqualTo(widgetRequest.getCode());
        assertThat(argument.getConfigUi()).isEqualTo(objectMapper.writeValueAsString(widgetRequest.getConfigUi()));
        assertThat(argument.getBundleId()).isEqualTo(widgetRequest.getBundleId());
        assertThat(widgetDto.getCode()).isEqualTo(widgetRequest.getCode());
        assertThat(widgetDto.getConfigUi()).isEqualTo(widgetRequest.getConfigUi());
        assertThat(widgetDto.getBundleId()).isEqualTo(widgetRequest.getBundleId());
    }

    @Test
    public void shouldUpdateWidget() throws Exception {
        // Given
        WidgetRequest widgetRequest = getWidgetRequest1();
        when(widgetManager.getWidgetType(eq(widgetRequest.getCode()))).thenReturn(getWidget1());
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));

        // When
        WidgetDto widgetDto = widgetService.updateWidget(WIDGET_1_CODE, widgetRequest);

        // Then
        ArgumentCaptor<String> configUiCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bundleIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(widgetManager).updateWidgetType(anyString(), any(), any(), anyString(), configUiCaptor.capture(),
                bundleIdCaptor.capture());
        assertThat(configUiCaptor.getValue()).isEqualTo(objectMapper.writeValueAsString(widgetRequest.getConfigUi()));
        assertThat(bundleIdCaptor.getValue()).isEqualTo(widgetRequest.getBundleId());
        assertThat(widgetDto.getConfigUi()).isEqualTo(widgetRequest.getConfigUi());
        assertThat(widgetDto.getBundleId()).isEqualTo(widgetRequest.getBundleId());
    }

    private WidgetType getWidget1() throws JsonProcessingException {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_1_CODE);
        widgetType.setMainGroup("group1");
        widgetType.setLocked(true);
        widgetType.setBundleId(BUNDLE_1);
        widgetType.setConfigUi(objectMapper.writeValueAsString(
                ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_1, RESOURCES_KEY, RESOURCES_1)));
        return widgetType;
    }

    private WidgetType getWidget2() throws JsonProcessingException {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_2_CODE);
        widgetType.setMainGroup("group2");
        widgetType.setParentType(getWidget1());
        widgetType.setBundleId(BUNDLE_2);
        widgetType.setConfigUi(objectMapper.writeValueAsString(
                ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_2, RESOURCES_KEY, RESOURCES_2)));
        return widgetType;
    }

    private WidgetRequest getWidgetRequest1() {
        WidgetRequest widgetRequest = new WidgetRequest();
        widgetRequest.setCode(WIDGET_1_CODE);
        widgetRequest.setTitles(ImmutableMap.of("it", "Mio Titolo", "en", "My Title"));
        widgetRequest.setCustomUi("<div></div>");
        widgetRequest.setGroup("group");
        widgetRequest.setConfigUi(ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_1, RESOURCES_KEY, RESOURCES_1));
        widgetRequest.setBundleId(BUNDLE_1);
        return widgetRequest;
    }


    @Test
    public void getWidgetUsageDetails() throws Exception {

        Page onlinePage = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE, null);
        when(pageManager.getOnlineWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(onlinePage));

        Page draftPage = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE, null);
        when(pageManager.getDraftWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(draftPage));

        when(widgetManager.getWidgetType(anyString())).thenReturn(getWidget1());

        PagedMetadata<ComponentUsageEntity> usageDetails = widgetService.getComponentUsageDetails(WIDGET_1_CODE, new PageSearchRequest(WidgetMockHelper.WIDGET_1_CODE));

        WidgetAssertionHelper.assertUsageDetails(usageDetails);
    }


    @Test
    public void getWidgetUsageDetailsWithPagination() throws Exception {

        Page page1 = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE);
        Page page2 = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE);
        page2.setCode(PageMockHelper.PAGE_MISSION_CODE);

        List mockedPageList = Arrays.asList(page1, page2);

        when(pageManager.getOnlineWidgetUtilizers(anyString())).thenReturn(mockedPageList);
        when(pageManager.getDraftWidgetUtilizers(anyString())).thenReturn(mockedPageList);

        when(widgetManager.getWidgetType(anyString())).thenReturn(getWidget1());

        // creates paged data
        List<Integer> pageNumList = Arrays.asList(1, 2);
        Map<Integer, List<ComponentUsageEntity>> usageEntityMap = new HashMap<>();
        usageEntityMap.put(pageNumList.get(0), Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_MISSION_CODE, IPageService.STATUS_ONLINE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_MISSION_CODE, IPageService.STATUS_DRAFT),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_CODE, IPageService.STATUS_ONLINE)));
        usageEntityMap.put(pageNumList.get(1), Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_CODE, IPageService.STATUS_DRAFT)));

        PageSearchRequest pageSearchRequest = new PageSearchRequest(WidgetMockHelper.WIDGET_1_CODE);
        pageSearchRequest.setPageSize(3);

        // does assertions
        IntStream.range(0, pageNumList.size())
                .forEach(i -> {

                    pageSearchRequest.setPage(pageNumList.get(i));

                    PagedMetadata<ComponentUsageEntity> pageUsageDetails = widgetService.getComponentUsageDetails(WIDGET_1_CODE, pageSearchRequest);

                    WidgetAssertionHelper.assertUsageDetails(pageUsageDetails,
                            usageEntityMap.get(i+1),
                            4,
                            pageNumList.get(i));
                });
    }


    @Test
    public void getWidgetUsageDetailsWithInvalidCodeShouldThrowResourceNotFoundException() throws Exception {

        Page page1 = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE);
        Page page2 = PageMockHelper.mockTestPage(WidgetMockHelper.WIDGET_1_CODE);
        page2.setCode(PageMockHelper.PAGE_MISSION_CODE);

        List mockedPageList = Arrays.asList(page1, page2);

        when(pageManager.getOnlineWidgetUtilizers(anyString())).thenReturn(mockedPageList);
        when(pageManager.getDraftWidgetUtilizers(anyString())).thenReturn(mockedPageList);

        when(widgetManager.getWidgetType(anyString())).thenReturn(getWidget1());

        PageSearchRequest pageSearchRequest = new PageSearchRequest(PageMockHelper.PAGE_CODE);

        Arrays.stream(new String[]{ "not existing", null, ""})
                .forEach(code -> {
                    try {
                        widgetService.getComponentUsageDetails(code, pageSearchRequest);
                        fail("NoSuchElementException NOT thrown with code " + code);
                    } catch (Exception e) {
                        assertTrue(e instanceof NoSuchElementException || e instanceof NullPointerException);
                    }
                });
    }


}
