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
package org.entando.entando.web.widget;

import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import javax.ws.rs.core.HttpHeaders;
import org.entando.entando.aps.servlet.security.CORSFilter;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WidgetControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetCategories() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/widgets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", CORSFilter.ALLOWED_METHODS));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetWidget_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets/{code}", "1").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidget_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets/{code}", "login_form").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("login_form")));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetInfo() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets/login_form/info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
        result.andExpect(jsonPath("$.payload.publishedUtilizers", Matchers.hasSize(2)));
    }

    @Test
    public void testGetWidgetList_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "100")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(6)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(6)));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "5")
                        .param("sort", "code").param("direction", "DESC")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(5)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(6)));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.payload[0].code", is("messages_system")));
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "5")
                        .param("sort", "code").param("direction", "DESC")
                        .param("filters[0].attribute", "typology").param("filters[0].value", "oc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(4)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(4)));
        result.andExpect(jsonPath("$.payload[0].code", is("messages_system")));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }
    
    @Test
    public void testAddUpdateWidget_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newCode = "test_new_type_1";
        Assert.assertNull(this.widgetTypeManager.getWidgetType(newCode));
        try {
            WidgetRequest request = new WidgetRequest();
            request.setCode(newCode);
            request.setGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", "Titolo ITA");
            titles.put("en", "Title EN");
            request.setTitles(titles);
            request.setCustomUi("<h1>Custom UI</h1>");
            request.setGroup(Group.FREE_GROUP_NAME);
            ResultActions result = this.executeWidgetPost(request, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newCode)));
            WidgetType widgetType = this.widgetTypeManager.getWidgetType(newCode);
            Assert.assertNotNull(widgetType);
            Assert.assertEquals("Title EN", widgetType.getTitles().getProperty("en"));
            
            request.setGroup("invalid");
            titles.put("en", "Title EN modified");
            result = this.executeWidgetPut(request, newCode, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_WIDGET_GROUP_INVALID)));
            
            request.setGroup("helpdesk");
            request.setCustomUi("");
            result = this.executeWidgetPut(request, newCode, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_NOT_BLANK)));
            
            titles.put("en", "Title EN modified");
            request.setCustomUi("New Custom Ui");
            result = this.executeWidgetPut(request, newCode, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.group", is("helpdesk")));
            widgetType = this.widgetTypeManager.getWidgetType(newCode);
            Assert.assertNotNull(widgetType);
            Assert.assertEquals("Title EN modified", widgetType.getTitles().getProperty("en"));
            Assert.assertEquals("helpdesk", widgetType.getMainGroup());
            
            result = this.executeWidgetDelete(newCode, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is(newCode)));
            widgetType = this.widgetTypeManager.getWidgetType(newCode);
            Assert.assertNull(widgetType);
        } catch (Exception e) {
            throw e;
        } finally {
            this.widgetTypeManager.deleteWidgetType(newCode);
        }
    }
    
    @Test
    public void testAddUpdateWidget_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newCode = "test_new_type_2";
        Assert.assertNull(this.widgetTypeManager.getWidgetType(newCode));
        try {
            WidgetRequest request = new WidgetRequest();
            request.setCode(newCode);
            request.setGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", "Titolo ITA 2");
            titles.put("en", "Title EN 2");
            request.setTitles(titles);
            request.setCustomUi("");
            request.setGroup(Group.FREE_GROUP_NAME);
            ResultActions result = this.executeWidgetPost(request, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_NOT_BLANK)));
            
            titles.put("en", "");
            request.setCustomUi("Custom UI");
            result = this.executeWidgetPost(request, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_MISSING_TITLE)));
            
            titles.put("en", "Title EN 2 bis");
            result = this.executeWidgetPut(request, newCode, accessToken, status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_WIDGET_NOT_FOUND)));
            
            result = this.executeWidgetPost(request, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.group", is(Group.FREE_GROUP_NAME)));
            WidgetType widgetType = this.widgetTypeManager.getWidgetType(newCode);
            Assert.assertNotNull(widgetType);
            Assert.assertEquals("Title EN 2 bis", widgetType.getTitles().getProperty("en"));
            
            titles.put("it", "");
            result = this.executeWidgetPut(request, newCode, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_MISSING_TITLE)));
        } catch (Exception e) {
            throw e;
        } finally {
            this.widgetTypeManager.deleteWidgetType(newCode);
            Assert.assertNull(this.widgetTypeManager.getWidgetType(newCode));
        }
    }

    @Test
    public void testAddDefaultUI() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String newCode = "test_new_type_3";
        Assert.assertNull(this.widgetTypeManager.getWidgetType(newCode));
        try {
            WidgetRequest request = new WidgetRequest();
            request.setCode(newCode);
            request.setGroup(Group.FREE_GROUP_NAME);
            Map<String, String> titles = new HashMap<>();
            titles.put("it", "Titolo ITA 3");
            titles.put("en", "Title EN 3");
            request.setTitles(titles);
            request.setCustomUi("<h1>This is a test</h1>");
            request.setGroup(Group.FREE_GROUP_NAME);
            this.executeWidgetPost(request, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.guiFragments[0].defaultUi").value(is("<h1>This is a test</h1>")));
        } catch (Exception e) {
            throw e;
        } finally {
            this.widgetTypeManager.deleteWidgetType(newCode);
            Assert.assertNull(this.widgetTypeManager.getWidgetType(newCode));
        }

    }
    
    @Test
    public void testUpdateStockLocked() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "login_form";
        WidgetType widgetType = this.widgetTypeManager.getWidgetType(code);
        WidgetRequest request = new WidgetRequest();
        request.setCode(code);
        request.setGroup(Group.FREE_GROUP_NAME);
        request.setTitles((Map) widgetType.getTitles());
        ResultActions result = this.executeWidgetPut(request, code, accessToken, status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("login_form")));
    }
    
    @Test
    public void testDeleteWidgetLocked() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "login_form";
        ResultActions result = this.executeWidgetDelete(code, accessToken, status().isBadRequest());
        result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_OPERATION_FORBIDDEN_LOCKED)));
    }
    
    private ResultActions executeWidgetPost(WidgetRequest request, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/widgets")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }
    
    private ResultActions executeWidgetPut(WidgetRequest request, String widgetTypeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(put("/widgets/{code}", new Object[]{widgetTypeCode})
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }
    
    private ResultActions executeWidgetDelete(String widgetTypeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/widgets/{code}", new Object[]{widgetTypeCode})
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }
    
}
