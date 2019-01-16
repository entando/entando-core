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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.transform.Result;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.model.PageDtoBuilder;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author paddeo
 */
public class PageControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testPageTree() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newPageCode = "test_page";
        try {
            ResultActions result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(8)));
            result.andExpect(jsonPath("$.payload[7].code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload[7].status", is("unpublished")));
            result.andExpect(jsonPath("$.payload[7].titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload[7].titles.en", is("Title EN")));
            
            this.pageManager.setPageOnline(newPageCode);
            
            result = mockMvc
                .perform(get("/pages")
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            
            IPage newPage = this.createPage(newPageCode, null, this.pageManager.getDraftRoot().getCode());
            newPage.setTitle("it", "Title IT");
            newPage.setTitle("en", "Title EN");
            this.pageManager.addPage(newPage);
            
            result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("unpublished")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title EN")));
            
            this.pageManager.setPageOnline(newPageCode);
            
            result = mockMvc
                .perform(get("/pages/{code}", newPageCode)
                        .header("Authorization", "Bearer " + accessToken));
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
                        .header("Authorization", "Bearer " + accessToken));
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
    public void testPatchPage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newPageCode = "test_page";
        try {
            ResultActions result = mockMvc
                    .perform(get("/pages/{code}", newPageCode)
                                     .header("Authorization", "Bearer " + accessToken));
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
                                     .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newPageCode)));
            result.andExpect(jsonPath("$.payload.status", is("unpublished")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Title IT")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title EN")));

            String payload = "[\n" +
                    "{ \"op\": \"replace\", \"path\": \"/displayedInMenu\", \"value\": true },\n  " +
                    "{ \"op\": \"replace\", \"path\": \"/charset\", \"value\": \"utf8\" },\n  " +
                    "{ \"op\": \"replace\", \"path\": \"/contentType\", \"value\": \"text/html\" },\n  " +
                    "{ \"op\": \"replace\", \"path\": \"/titles\", \"value\": { \"en\": \"Title English\", \"it\": \"Titolo Italiano\" } }, \n " +
                    "{ \"op\": \"replace\", \"path\": \"/titles/it\", \"value\": \"Nuovo titolo italiano\" }, \n " +
                    "{ \"op\": \"replace\", \"path\": \"/joinGroups\", \"value\": [\"management\", \"customers\"] } \n  " +
//                    "{ \"op\": \"add\", \"path\": \"/joinGroups\", \"value\": [ \"coach\" ] } \n " +
                "\n]";

            result = mockMvc
                    .perform(patch("/pages/{code}", newPageCode)
                             .header("Authorization", "Bearer " + accessToken)
                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                             .accept(MediaType.APPLICATION_JSON)
                             .characterEncoding("UTF-8")
                             .content(payload));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.displayedInMenu", is(true)));
            result.andExpect(jsonPath("$.payload.charset", is("utf8")));
            result.andExpect(jsonPath("$.payload.contentType", is("text/html")));
            result.andExpect(jsonPath("$.payload.joinGroups", hasItems("management", "customers")));
//            result.andExpect(jsonPath("$.payload.joinGroups", hasItems("administration", "customers", "coach")));
            result.andExpect(jsonPath("$.payload.titles.en", is("Title English")));
            result.andExpect(jsonPath("$.payload.titles.it", is("Nuovo titolo italiano")));

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
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.totalItems", is(12)));
    }

    @Test
    public void testMove() throws Throwable {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        try {
            pageManager.addPage(createPage("page_root", null, null));
            pageManager.addPage(createPage("page_a", null, "page_root"));
            pageManager.addPage(createPage("page_b", null, "page_root"));
            pageManager.addPage(createPage("page_c", null, "page_root"));

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));

            PagePositionRequest request = new PagePositionRequest();
            request.setCode("page_a");
            request.setParentCode("page_root");
            request.setPosition(3);

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/position", "page_a")
                            .param("pageSize", "5")
                            .param("pageCodeToken", "pagin")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + accessToken));

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
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            assertThat(pageManager.getDraftPage("page_a").getPosition(), is(1));
            assertThat(pageManager.getDraftPage("page_b").getPosition(), is(2));
            assertThat(pageManager.getDraftPage("page_c").getPosition(), is(3));

        } finally {
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

            //put
            pageRequest.setParentCode("homepage");
            pageRequest.getTitles().put("it", code.toUpperCase());
            ResultActions result = mockMvc
                    .perform(put("/pages/{code}", code)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("3")));

            //put
            pageRequest.setParentCode("service");
            pageRequest.setOwnerGroup(Group.ADMINS_GROUP_NAME);
            pageRequest.getTitles().put("it", code.toUpperCase());
            result = mockMvc
                    .perform(put("/pages/{code}", code)
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
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
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));
            assertThat(page.getTitle("it"), is(code.toUpperCase()));

            //status
            PageStatusRequest pageStatusRequest = new PageStatusRequest();
            pageStatusRequest.setStatus("published");

            result = mockMvc
                    .perform(put("/pages/{code}/status", code)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
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
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));

            onlinePage = this.pageManager.getOnlinePage(code);
            assertThat(onlinePage, is(nullValue()));

            //delete
            result = mockMvc
                    .perform(delete("/pages/{code}", code)
                            //.content(payload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
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
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("3")));
            
            //put
            movementRequest.setParentCode(codeChild);
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("3")));
            
            //put
            movementRequest.setParentCode("coach_page");
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors.size()", is(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("2")));
            
            //put
            movementRequest.setParentCode("service");
            result = mockMvc
                    .perform(put("/pages/{code}/position", codeParent)
                            .content(mapper.writeValueAsString(movementRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
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
    
    private void addPage(String accessToken, PageRequest pageRequest) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/pages")
                        .content(mapper.writeValueAsString(pageRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    private ResultActions executeUpdatePageStatus(String pageCode,
            PageStatusRequest statusRequest, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/status", pageCode)
                            .content(mapper.writeValueAsString(statusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent) {
        if (null == parent) {
            parent = "service";
        }
        IPage parentPage = pageManager.getDraftPage(parent);
        if (null == pageModel) {
            pageModel = parentPage.getMetadata().getModel();
        }
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(), true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = new ApsProperties();
        config.put("actionPath", "/mypage.jsp");
        Widget widgetToAdd = PageTestUtil.createWidget("formAction", config, this.widgetTypeManager);
        Widget[] widgets = {widgetToAdd};
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage, "free", metadata, widgets);
        return pageToAdd;
    }

}
