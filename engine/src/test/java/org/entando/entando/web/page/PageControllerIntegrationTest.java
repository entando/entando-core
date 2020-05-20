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
package org.entando.entando.web.page;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.*;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.FileTextReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.JsonPatchBuilder;
import org.entando.entando.web.assertionhelper.PageAssertionHelper;
import org.entando.entando.web.assertionhelper.PageRestResponseAssertionHelper;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.mockhelper.PageRequestMockHelper;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author paddeo
 */
public class PageControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private IPageModelManager pageModelManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPageTree() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newPageCode = "test_page";
        try {
            ResultActions result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(7)));
            result.andExpect(jsonPath("$.payload[0].code", is("service")));
            result.andExpect(jsonPath("$.metaData.parentCode", is("homepage")));

            IPage newPage = this.createPage(newPageCode, null, this.pageManager.getDraftRoot().getCode());
            newPage.setTitle("it", "Title IT");
            newPage.setTitle("en", "Title EN");
            this.pageManager.addPage(newPage);

            result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(8)));
            result.andExpect(jsonPath("$.payload[7].code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload[7].status", is("unpublished")));
            result.andExpect(jsonPath("$.payload[7].titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload[7].titles.en", is("Title EN")));

            this.pageManager.setPageOnline(newPageCode);

            result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(8)));
            result.andExpect(jsonPath("$.payload[7].code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload[7].status", is("published")));
            result.andExpect(jsonPath("$.payload[7].titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload[7].titles.en", is("Title EN")));
            
            IPage extracted = this.pageManager.getDraftPage(newPageCode);
            extracted.setTitle("it", "DRAFT title IT");
            extracted.setTitle("en", "DRAFT title EN");
            this.pageManager.updatePage(extracted);
            
            result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(8)));
            result.andExpect(jsonPath("$.payload[7].code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload[7].status", is("draft")));
            result.andExpect(jsonPath("$.payload[7].titles.it", is("DRAFT title IT")));
            result.andExpect(jsonPath("$.payload[7].titles.en", is("DRAFT title EN")));
        } finally {
            this.pageManager.deletePage(newPageCode);
        }
    }
    
    @Test
    public void testPageSearch() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/search")
                        .param("pageSize", "5")
                        .param("pageCodeToken", "pagin")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.totalItems", is(6)));
        result.andExpect(jsonPath("$.payload[0].code", is("pagina_1")));
    }

    @Test
    public void testGetPage_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/{code}", "pagina_11")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("pagina_11")));
        result.andExpect(jsonPath("$.payload.references.length()", is(0)));
    }

    @Test
    public void testGetPage_2() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newPageCode = "test_page";
        try {
            ResultActions result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.errors.size()", is(1)));

            IPage newPage = this.createPage(newPageCode, null, this.pageManager.getDraftRoot().getCode());
            newPage.setTitle("it", "Title IT");
            newPage.setTitle("en", "Title EN");
            this.pageManager.addPage(newPage);

            result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("unpublished")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title EN")));

            this.pageManager.setPageOnline(newPageCode);

            result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("published")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title EN")));
            
            IPage extracted = this.pageManager.getDraftPage(newPageCode);
            extracted.setTitle("it", "DRAFT title IT");
            extracted.setTitle("en", "DRAFT title EN");
            this.pageManager.updatePage(extracted);
            
            result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("draft")));
            result.andExpect(jsonPath("$.payload.titles.it", is("DRAFT title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("DRAFT title EN")));
        } finally {
            this.pageManager.deletePage(newPageCode);
        }
    }

    @Test
    public void testGetPageUsage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "pagina_11";

        mockMvc.perform(get("/pages/{code}/usage", code)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.type", is(PageController.COMPONENT_ID)))
                .andExpect(jsonPath("$.payload.code", is(code)))
                .andExpect(jsonPath("$.payload.usage", is(1)))
                .andReturn();
    }

    @Test
    public void testPatchPage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newPageCode = "test_page";
        try {
            ResultActions result = mockMvc
                    .perform(get("/pages/{code}", newPageCode)
                                     .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.errors.size()", is(1)));

            Page newPage = this.createPage(newPageCode, null, this.pageManager.getDraftRoot().getCode());
            newPage.setTitle("it", "Title IT");
            newPage.setTitle("en", "Title EN");
            newPage.setShowable(false);
            newPage.setCharset("ascii");
            newPage.setMimeType("application/json");
            newPage.setExtraGroups(Stream.of("administration", "customers").collect(Collectors.toSet()));
            this.pageManager.addPage(newPage);

            result = mockMvc
                    .perform(get("/pages/{code}", newPageCode)
                                     .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("unpublished")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title EN")));

            String payload = new JsonPatchBuilder()
                    .withReplace("/displayedInMenu", true)
                    .withReplace("/charset", "utf8")
                    .withReplace("/contentType", "text/html")
                    .withReplace("/titles", ImmutableMap.of("en", "Title English", "it", "Titolo Italiano"))
                    .withReplace("/joinGroups", ImmutableList.of("management", "customers"))
                    .getJsonPatchAsString();

            result = mockMvc
                    .perform(patch("/pages/{code}", newPageCode)
                             .header("Authorization", "Bearer " + accessToken)
                             .with(csrf())
                             .contentType(RestMediaTypes.JSON_PATCH_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .characterEncoding("UTF-8")
                             .content(payload));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.displayedInMenu", is(true)));
            result.andExpect(jsonPath("$.payload.charset", is("utf8")));
            result.andExpect(jsonPath("$.payload.contentType", is("text/html")));
            result.andExpect(jsonPath("$.payload.joinGroups", hasItems("management", "customers")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title English")));
        } finally {
            this.pageManager.deletePage(newPageCode);
        }
    }

    @Test
    public void testPageSearchFreeOnlinePages() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/search/group/free")
                        .param("pageSize", "50")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.totalItems", is(12)));
    }

    @Test
    public void testMove() throws Throwable {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        try {
            PagePositionRequest request;

            pageManager.addPage(createPage("page_root", null, null));
            pageManager.addPage(createPage("page_a", null, "page_root"));
            pageManager.addPage(createPage("page_b", null, "page_root"));
            pageManager.addPage(createPage("page_c", null, "page_root"));
            pageManager.addPage(createPage("published_page", null, "page_root"));
            pageManager.addPage(createPage("unpublished_page", null, "page_root"));
            pageManager.addPage(createPage("free_group_page", null, "page_root", Group.FREE_GROUP_NAME));
            pageManager.addPage(createPage("admin_group_page", null, "page_root", Group.ADMINS_GROUP_NAME));
            pageManager.addPage(createPage("test_group_page", null, "page_root", "test"));

            //move a page with test group under a different group page is not allowed

            assertThat(pageManager.getDraftPage("admin_group_page").getPosition(), is(7));
            assertThat(pageManager.getDraftPage("test_group_page").getPosition(), is(8));

            request = new PagePositionRequest();
            request.setCode("test_group_page");
            request.setParentCode("admin_group_page");
            request.setPosition(1);

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "test_group_page")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

            //move a free group page under a reserved  group page is not allowed

            assertThat(pageManager.getDraftPage("free_group_page").getPosition(), is(6));
            assertThat(pageManager.getDraftPage("admin_group_page").getPosition(), is(7));

            request = new PagePositionRequest();
            request.setCode("free_group_page");
            request.setParentCode("admin_group_page");
            request.setPosition(1);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "free_group_page")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

            //move a published page under an unpublished_page is not allowed

            assertThat(pageManager.getDraftPage("published_page").getPosition(), is(4));
            assertThat(pageManager.getDraftPage("unpublished_page").getPosition(), is(5));

            pageManager.setPageOnline("published_page");

            request = new PagePositionRequest();
            request.setCode("published_page");
            request.setParentCode("unpublished_page");
            request.setPosition(1);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "published_page")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

            //move a page to an invalid position is not allowed

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));

            request = new PagePositionRequest();
            request.setCode("page_a");
            request.setParentCode("page_root");
            request.setPosition(0);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "page_a")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

            //move a page with an invalid PagePositionRequest is not allowed

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));

            request = new PagePositionRequest();
            request.setCode("page_b");
            request.setParentCode("page_a");
            request.setPosition(1);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "page_a")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

            //-----------

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));
            request = new PagePositionRequest();
            request.setCode("page_a");
            request.setParentCode("page_root");
            request.setPosition(3);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "page_a")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(3));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(2));

            //--
            request = new PagePositionRequest();
            request.setCode("page_a");
            request.setParentCode("page_root");
            request.setPosition(1);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "page_a")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));

        } finally {
            this.pageManager.deletePage("published_page");
            this.pageManager.deletePage("unpublished_page");
            this.pageManager.deletePage("admin_group_page");
            this.pageManager.deletePage("free_group_page");
            this.pageManager.deletePage("test_group_page");
            this.pageManager.deletePage("page_c");
            this.pageManager.deletePage("page_b");
            this.pageManager.deletePage("page_a");
            this.pageManager.deletePage("page_root");
        }
    }

    @Test
    public void testAddPublishUnpublishDelete() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "testAddDelete";
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setCode(code);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", code);
            titles.put("en", code);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode("service");
            this.addPage(accessToken, pageRequest);

            IPage page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));
            Assert.assertEquals(6, page.getWidgets().length);

            //put (move the page changing parent from service to homepage)
            String newParentCode = "homepage";
            pageRequest.setParentCode(newParentCode);
            pageRequest.getTitles().put("it", code.toUpperCase());
            ResultActions result = mockMvc
                    .perform(put("/pages/{code}", code)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            page = this.pageManager.getDraftPage(code);
            assertThat(page.getParentCode(), is(newParentCode));

            //put
            pageRequest.setParentCode("service");
            pageRequest.setOwnerGroup(Group.ADMINS_GROUP_NAME);
            pageRequest.getTitles().put("it", code.toUpperCase());
            result = mockMvc
                    .perform(put("/pages/{code}", code)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

            //put
            pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
            pageRequest.getTitles().put("it", code.toUpperCase());
            result = mockMvc
                    .perform(put("/pages/{code}", code)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());

            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));
            assertThat(page.getTitle("it"), is(code.toUpperCase()));

            //put
            pageRequest.setPageModel("service");
            pageRequest.getTitles().put("it", "new Italian title");
            result = mockMvc.perform(put("/pages/{code}", code)
                    .content(mapper.writeValueAsString(pageRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            page = this.pageManager.getDraftPage(code);
            Assert.assertEquals(4, page.getWidgets().length);
            Assert.assertEquals("new Italian title", page.getTitle("it"));

            //status
            PageStatusRequest pageStatusRequest = new PageStatusRequest();
            pageStatusRequest.setStatus("published");

            result = mockMvc
                    .perform(put("/pages/{code}/status", code)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());

            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));

            IPage onlinePage = this.pageManager.getOnlinePage(code);
            assertThat(onlinePage, is(not(nullValue())));

            pageStatusRequest.setStatus("draft");
            result = mockMvc
                    .perform(put("/pages/{code}/status", code)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());

            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));

            onlinePage = this.pageManager.getOnlinePage(code);
            assertThat(onlinePage, is(nullValue()));

            //delete
            result = mockMvc
                    .perform(delete("/pages/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(nullValue()));
        } finally {
            this.pageManager.deletePage(code);
        }
    }

    @Test
    public void testMovePage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String codeParent = "testToMoveParent";
        String codeChild = "testToMoveChild";
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setCode(codeParent);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup("customers");
            Map<String, String> titles = new HashMap<>();
            titles.put("it", codeParent);
            titles.put("en", codeParent);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode("customers_page");
            this.addPage(accessToken, pageRequest);

            pageRequest.setCode(codeChild);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup("customers");
            titles = new HashMap<>();
            titles.put("it", codeChild);
            titles.put("en", codeChild);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode(codeParent);
            this.addPage(accessToken, pageRequest);

            PagePositionRequest movementRequest = new PagePositionRequest();
            movementRequest.setCode(codeParent);
            movementRequest.setParentCode(codeParent);
            movementRequest.setPosition(1);

            //put
            ResultActions result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("3")));

            //put
            movementRequest.setParentCode(codeChild);
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("3")));

            //put
            movementRequest.setParentCode("coach_page");
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

            //put
            movementRequest.setParentCode("service");
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
        } finally {
            this.pageManager.deletePage(codeChild);
            this.pageManager.deletePage(codeParent);
        }
    }

    @Test
    public void testPageStatus() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String codeParent = "testStatusParent";
        String codeChild = "testStatusChild";
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setCode(codeParent);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", codeParent);
            titles.put("en", codeParent);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode("homepage");
            this.addPage(accessToken, pageRequest);

            pageRequest.setCode(codeChild);
            titles = new HashMap<>();
            titles.put("it", codeChild);
            titles.put("en", codeChild);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode(codeParent);
            this.addPage(accessToken, pageRequest);

            PageStatusRequest statusRequest = new PageStatusRequest();

            //put
            ResultActions result = this.executeUpdatePageStatus(codeParent,
                    statusRequest, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("53")));

            statusRequest.setStatus("xxxxxxx");
            result = this.executeUpdatePageStatus(codeParent,
                    statusRequest, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("57")));

            statusRequest.setStatus("published");
            result = this.executeUpdatePageStatus(codeChild,
                    statusRequest, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("9")));

            statusRequest.setStatus("published");
            result = this.executeUpdatePageStatus("not_existing",
                    statusRequest, accessToken, status().isNotFound());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("1")));

            statusRequest.setStatus("published");
            result = this.executeUpdatePageStatus(codeParent,
                    statusRequest, accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors.size()", is(0)));
            result.andExpect(jsonPath("$.payload.code", is(codeParent)));

            statusRequest.setStatus("published");
            result = this.executeUpdatePageStatus(codeChild,
                    statusRequest, accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors.size()", is(0)));
            result.andExpect(jsonPath("$.payload.code", is(codeChild)));

            statusRequest.setStatus("draft");
            result = this.executeUpdatePageStatus(codeParent,
                    statusRequest, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("8")));

            statusRequest.setStatus("draft");
            result = this.executeUpdatePageStatus(codeChild,
                    statusRequest, accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors.size()", is(0)));
            result.andExpect(jsonPath("$.payload.code", is(codeChild)));

            statusRequest.setStatus("draft");
            result = this.executeUpdatePageStatus(codeParent,
                    statusRequest, accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors.size()", is(0)));
            result.andExpect(jsonPath("$.payload.code", is(codeParent)));

        } finally {
            this.pageManager.deletePage(codeChild);
            this.pageManager.deletePage(codeParent);
        }
    }

    @Test
    public void testUpdatePageModel() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String pageCode = "testUpdateModelPage";
        try {

            ResultActions result = mockMvc
                    .perform(post("/pages")
                            .content(getPageJson("1_POST_valid_page.json", pageCode))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.pageModel", is("service")));

            IPage page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNotNull(page);

            result = mockMvc
                    .perform(put("/pages/{pageCode}", pageCode)
                            .content(getPageJson("1_PUT_valid_page.json", pageCode))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.pageModel", is("home")));

            page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNotNull(page);

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testRecreatePage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String pageCode = "testUpdateModelPage";
        try {

            IPage page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNull(page);

            page = this.pageManager.getOnlinePage(pageCode);
            Assert.assertNull(page);

            ResultActions result = mockMvc
                    .perform(post("/pages")
                            .content(getPageJson("1_POST_valid_page.json", pageCode))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)));

            result = mockMvc
                    .perform(get("/pages/{pageCode}", pageCode)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.status", is("unpublished")));

            PageStatusRequest pageStatusRequest = new PageStatusRequest();
            pageStatusRequest.setStatus("published");

            result = mockMvc
                    .perform(put("/pages/{code}/status", pageCode)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.status", is("published")));

            page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNotNull(page);

            page = this.pageManager.getOnlinePage(pageCode);
            Assert.assertNotNull(page);

            result = mockMvc
                    .perform(delete("/pages/{code}", pageCode)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.errors[0].message", is("Online pages can not be deleted")));

            page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNotNull(page);

            page = this.pageManager.getOnlinePage(pageCode);
            Assert.assertNotNull(page);

            pageStatusRequest.setStatus("draft");

            result = mockMvc
                    .perform(put("/pages/{code}/status", pageCode)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.status", is("unpublished")));

            page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNotNull(page);

            page = this.pageManager.getOnlinePage(pageCode);
            Assert.assertNull(page);

            result = mockMvc
                    .perform(delete("/pages/{code}", pageCode)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk());

            page = this.pageManager.getDraftPage(pageCode);
            Assert.assertNull(page);

            page = this.pageManager.getOnlinePage(pageCode);
            Assert.assertNull(page);

            result = mockMvc
                    .perform(get("/pages/{pageCode}", pageCode)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isNotFound());

            result = mockMvc
                    .perform(post("/pages")
                            .content(getPageJson("1_POST_valid_page.json", pageCode))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testListViewPages() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String newPageCode1 = "view_page_1";
        String newPageCode2 = "view_page_2";

        try {
            IPage newPage1 = this.createPage(newPageCode1, null, this.pageManager.getDraftRoot().getCode(), true);
            newPage1.setTitle("it", "Title1 IT");
            newPage1.setTitle("en", "Title1 EN");
            pageManager.addPage(newPage1);
            pageManager.setPageOnline(newPageCode1);

            IPage test = pageManager.getOnlinePage(newPageCode1);

            IPage newPage2 = this.createPage(newPageCode2, null, this.pageManager.getDraftRoot().getCode(), false);
            newPage2.setTitle("it", "Title2 IT");
            newPage2.setTitle("en", "Title2 EN");
            this.pageManager.addPage(newPage2);
            pageManager.setPageOnline(newPageCode2);

            ResultActions result = performListViewPages(accessToken);

            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].code", is(newPageCode1)));
        } finally {
            pageManager.deletePage(newPageCode1);
            pageManager.deletePage(newPageCode2);
        }
    }

    @Test
    public void testPutOnPageWithChildren() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String parentPageCode = "pageWithChildren";
        String childrenPageCode = "childrenPage";

        try {
            //Posting parent page
            PageRequest pageRequest = new PageRequest();
            pageRequest.setCode(parentPageCode);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", parentPageCode);
            titles.put("en", parentPageCode);
            pageRequest.setTitles(titles);
            pageRequest.setParentCode("homepage");
            this.addPage(accessToken, pageRequest);

            IPage page = this.pageManager.getDraftPage(parentPageCode);
            assertThat(page, is(not(nullValue())));
            Assert.assertEquals(0, page.getChildrenCodes().length);

            //Adding children
            pageRequest.setCode(childrenPageCode);
            pageRequest.setParentCode(parentPageCode);
            this.addPage(accessToken, pageRequest);

            page = this.pageManager.getDraftPage(childrenPageCode);
            assertThat(page, is(not(nullValue())));
            Assert.assertEquals(0, page.getChildrenCodes().length);

            page = this.pageManager.getDraftPage(parentPageCode);
            assertThat(page, is(not(nullValue())));
            Assert.assertEquals(1, page.getChildrenCodes().length);

            //Updating parentPage
            pageRequest.setCode(parentPageCode);
            pageRequest.setParentCode("homepage");
            pageRequest.getTitles().put("it", parentPageCode.toUpperCase());
            ResultActions result = mockMvc
                    .perform(put("/pages/{code}", parentPageCode)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk()).andDo(print());

            page = this.pageManager.getDraftPage(parentPageCode);
            assertThat(page, is(not(nullValue())));
            Assert.assertEquals(1, page.getChildrenCodes().length);

        } finally {
            this.pageManager.deletePage(childrenPageCode);
            this.pageManager.deletePage(parentPageCode);
        }
    }

    @Test
    public void testPageAddUpdateDelete() throws Exception {
        String pageCode = "page_update_test";
        String widgetCode = "login_form";

        PageRequest pageRequest = new PageRequest();
        pageRequest.setCode(pageCode);
        pageRequest.setPageModel("home");
        pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
        Map<String, String> titles = new HashMap<>();
        titles.put("it", pageCode);
        titles.put("en", pageCode);
        pageRequest.setTitles(titles);
        pageRequest.setParentCode("homepage");

        WidgetConfigurationRequest widgetRequest = new WidgetConfigurationRequest();
        widgetRequest.setCode(widgetCode);
        widgetRequest.setConfig(new HashMap<>());

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        try {
            mockMvc.perform(post("/pages", pageCode)
                    .header("Authorization", "Bearer " + accessToken)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(pageRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.numWidget", is(0)));

            mockMvc.perform(put("/pages/{code}/widgets/{frame}", pageCode, 0)
                    .header("Authorization", "Bearer " + accessToken)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(widgetRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());

            mockMvc.perform(put("/pages/{code}", pageCode)
                    .header("Authorization", "Bearer " + accessToken)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(pageRequest)))
                    .andDo(print()).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.numWidget", is(1)));
        } finally {
            mockMvc.perform(delete("/pages/{code}", pageCode)
                    .header("Authorization", "Bearer " + accessToken).with(csrf()))
                    .andExpect(status().isOk());
        }
    }

    private ResultActions performListViewPages(String accessToken) throws Exception {
        return mockMvc.perform(get("/pages/viewpages")
                .header("Authorization", "Bearer " + accessToken).with(csrf()));
    }

    private Widget createWidget() {
        return null;
    }

    private void addPage(String accessToken, PageRequest pageRequest) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/pages")
                        .content(mapper.writeValueAsString(pageRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
    }

    private ResultActions executeUpdatePageStatus(String pageCode,
            PageStatusRequest statusRequest, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/status", pageCode)
                            .content(mapper.writeValueAsString(statusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent) {
        return createPage(pageCode, pageModel, parent, false, "free");
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent, boolean viewPage) {
        return createPage(pageCode, pageModel, parent, viewPage, "free");
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent, String group) {
        return createPage(pageCode, pageModel, parent, false, group);
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent, boolean viewPage, String group) {
        if (null == parent) {
            parent = "service";
        }
        IPage parentPage = pageManager.getDraftPage(parent);
        if (null == pageModel) {
            pageModel = parentPage.getMetadata().getModel();
        }
        PageMetadata metadata = PageTestUtil
                .createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = new ApsProperties();
        config.put("actionPath", "/mypage.jsp");
        Widget widgetToAdd = PageTestUtil.createWidget("formAction", config, this.widgetTypeManager);
        if (viewPage) {
            pageModel.setMainFrame(0);
            widgetToAdd.setConfig(null);
        }
        Widget[] widgets = new Widget[pageModel.getFrames().length];
        if (pageModel.getMainFrame() >= 0) {
            widgets[pageModel.getMainFrame()] = widgetToAdd;
        } else {
            widgets[0] = widgetToAdd;
        }
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), group, metadata, widgets);
        return pageToAdd;
    }

    protected String getPageJson(String filename, String pageCode) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(filename);
        String result = FileTextReader.getText(isJsonPostValid);

        if (pageCode != null) {
            result = result.replace("pageCodePlaceHolder", pageCode);
        }

        return result;
    }


    @Test
    public void testPageUsageDetailsWithoutPermissionWillResultIn401() throws Exception {

        UserDetails admin = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String adminAccessToken = mockOAuthInterceptor(admin);

        try {
            PageRequest pageRequest = PageRequestMockHelper.mockPageRequest();
            pageRequest.setOwnerGroup(Group.ADMINS_GROUP_NAME);
            this.addPage(adminAccessToken, pageRequest);

            UserDetails user = new OAuth2TestUtils.UserBuilder("John Lackland", "0x246")
                    .withAuthorization(Group.FREE_GROUP_NAME, "admin", Permission.SUPERUSER).build();
            String userAccessToken = mockOAuthInterceptor(user);

            ResultActions resultActions = mockMvc
                    .perform(get("/pages/{code}/usage/details", PageRequestMockHelper.ADD_PAGE_CODE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userAccessToken));


            resultActions.andExpect(status().isUnauthorized());

        } catch (Exception e) {
            Assert.fail();
        } finally {
            this.pageManager.deletePage(PageRequestMockHelper.ADD_PAGE_CODE);
        }
    }


    @Test
    public void testPageUsageDetailsWithPublishedPageShouldBeIncluded() throws Exception {

        List<ComponentUsageEntity> expectedResult = Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageRequestMockHelper.ADD_FIRST_CHILD_PAGE_CODE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageRequestMockHelper.ADD_PAGE_CODE));

        this.execPageUsageDetailsTest(true, expectedResult);
    }


    @Test
    public void testPageUsageDetailsWithUnpublishedPageShouldNOTBeIncluded() throws Exception {

        List<ComponentUsageEntity> expectedResult = Arrays.asList(new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageRequestMockHelper.ADD_FIRST_CHILD_PAGE_CODE));

        this.execPageUsageDetailsTest(false, expectedResult);
    }


    /**
     * executes a test of page usage details
     *
     * @param publishParentPage
     */
    private void execPageUsageDetailsTest(boolean publishParentPage, List<ComponentUsageEntity> expectedResult) throws Exception {

        UserDetails admin = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String adminAccessToken = mockOAuthInterceptor(admin);
        this.deletePagesForUsageDetailsTest();

        try {
            this.addPagesForUsageDetailsTest(publishParentPage, adminAccessToken, false);

            ResultActions resultActions = mockMvc.perform(get("/pages/{code}/usage/details", PageRequestMockHelper.ADD_PAGE_CODE)
                    .params(getFilteredParams())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + adminAccessToken))
                    .andDo(print());

            PageRestResponseAssertionHelper.assertNoFilters(resultActions);
            PageAssertionHelper.assertUsagePageDetails(resultActions, expectedResult);

        } catch (Exception e) {
            Assert.fail();
        } finally {
            this.deletePagesForUsageDetailsTest();
        }
    }


    /**
     * creates and returns a LinkedMultiValueMap containing one filter
     * @return
     */
    private LinkedMultiValueMap<String, String> getFilteredParams() {

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("filters[0].attribute", "status");
        requestParams.add("filters[0].operator", "eq");
        requestParams.add("filters[0].value", "unpublished");
        return requestParams;
    }


    /**
     * insert some pages useful to test
     */
    private void addPagesForUsageDetailsTest(boolean publishParentPage, String adminAccessToken, boolean addSecondChildPage) throws Exception {

        // add base page
        PageRequest pageRequest = PageRequestMockHelper.mockPageRequest();
        this.addPage(adminAccessToken, pageRequest);
        if (publishParentPage) {
            this.pageManager.setPageOnline(PageRequestMockHelper.ADD_PAGE_CODE);
        }

        // add first child page
        PageRequest firstChildPageRequest = PageRequestMockHelper.mockPageRequest();
        firstChildPageRequest.setCode(PageRequestMockHelper.ADD_FIRST_CHILD_PAGE_CODE);
        firstChildPageRequest.setParentCode(PageRequestMockHelper.ADD_PAGE_CODE);
        this.addPage(adminAccessToken, firstChildPageRequest);
    }


    /**
     * insert some pages useful to test
     */
    private void deletePagesForUsageDetailsTest() throws Exception {

        this.pageManager.deletePage(PageRequestMockHelper.ADD_FIRST_CHILD_PAGE_CODE);
        this.pageManager.deletePage(PageRequestMockHelper.ADD_PAGE_CODE);
    }
}
