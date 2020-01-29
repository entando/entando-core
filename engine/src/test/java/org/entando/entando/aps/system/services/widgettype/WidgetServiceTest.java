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

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDtoBuilder;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WidgetServiceTest {

    private static final String WIDGET_1_CODE = "widget1";
    private static final String WIDGET_2_CODE = "widget2";

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
        WidgetRequest widgetRequest = new WidgetRequest();
        widgetRequest.setCode(WIDGET_1_CODE);
        widgetRequest.setTitles(ImmutableMap.of("it", "Mio Titolo", "en", "My Title"));
        widgetRequest.setCustomUi("<div></div>");
        widgetRequest.setGroup("group");
        widgetRequest.setConfigUi(ImmutableMap.of("customElement", "my-custom-element-name", "resources",
                Arrays.asList("/relative/path/to/script.js", "/relative/path/to/otherScript.js")));
        widgetRequest.setBundleId("test-bundle");
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

    private WidgetType getWidget1() {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_1_CODE);
        widgetType.setMainGroup("group1");
        widgetType.setLocked(true);
        return widgetType;
    }

    private WidgetType getWidget2() {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_2_CODE);
        widgetType.setMainGroup("group2");
        widgetType.setParentType(getWidget1());
        return widgetType;
    }
}
