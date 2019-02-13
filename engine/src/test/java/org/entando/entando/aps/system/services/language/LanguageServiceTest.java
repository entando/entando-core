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
package org.entando.entando.aps.system.services.language;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.google.common.collect.ImmutableList;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceTest {

    @Mock
    private ILangManager langManager;

    @InjectMocks
    private LanguageService languageService;

    @Before
    public void setUp() throws Exception {
        languageService.setUpDto();

        when(langManager.getLangs()).thenReturn(ImmutableList.of(getEn()));
        when(langManager.getAssignableLangs()).thenReturn(ImmutableList.of(getEn(), getIt()));
        when(langManager.getDefaultLang()).thenReturn(getEn());
    }

    @Test
    public void shouldReturnAll() {
        PagedMetadata<LanguageDto> result = languageService.getLanguages(new RestListRequest());
        assertThat(result.getBody()).hasSize(2);
    }

    @Test
    public void shouldFilterByCode() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("code");
        filter.setValue("it");
        requestList.addFilter(filter);

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("it");
    }

    @Test
    public void shouldFilterByCodeWithOrCondition() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("code");
        filter.setValue("en,it");
        requestList.addFilter(filter);

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(2);
    }

    @Test
    public void shouldFilterByDescription() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("description");
        filter.setValue("English");
        requestList.addFilter(filter);

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("en");
    }

    @Test
    public void shouldFilterByIsActive() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("isActive");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        filter.setValue("false");
        requestList.addFilter(filter);

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("it");
    }

    @Test
    public void shouldFilterByIsDefault() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("isDefault");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        filter.setValue("true");
        requestList.addFilter(filter);

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("en");
    }

    @Test
    public void shouldSortByCode() {
        RestListRequest requestList = new RestListRequest();
        requestList.setDirection("DESC");

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("it");
        assertThat(result.getBody().get(1).getCode()).isEqualTo("en");
    }

    @Test
    public void shouldSortByDescription() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("description");

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("en");
        assertThat(result.getBody().get(1).getCode()).isEqualTo("it");
    }

    @Test
    public void shouldSortByIsDefault() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("isDefault");
        requestList.setDirection("DESC");

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("en");
        assertThat(result.getBody().get(1).getCode()).isEqualTo("it");
    }

    @Test
    public void shouldSortByIsActive() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("isActive");
        requestList.setDirection("DESC");

        PagedMetadata<LanguageDto> result = languageService.getLanguages(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo("en");
        assertThat(result.getBody().get(1).getCode()).isEqualTo("it");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldFailDisabilingUnexistingLang() {
        languageService.disableLang("xx");
    }

    private Lang getEn() {
        Lang en = new Lang();
        en.setCode("en");
        en.setDescr("English");
        en.setDefault(true);
        return en;
    }

    private Lang getIt() {
        Lang it = new Lang();
        it.setCode("it");
        it.setDescr("Italiano");
        it.setDefault(false);
        return it;
    }
}
