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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.HttpHeaders;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

public class WidgetControllerIntegrationTest extends AbstractControllerIntegrationTest {
    
    @Autowired
    private IPageManager pageManager;

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
        testCors("/widgets");
    }

    @Test
    public void testGetWidget_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = this.executeWidgetGet("1", accessToken, status().isNotFound());
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }
    
    @Test
    public void testGetWidget_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = this.executeWidgetGet("login_form", accessToken, status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("login_form")));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetUsage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "login_form";

        // @formatter:off
        executeWidgetUsage(code, accessToken, status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.payload.type", is(WidgetController.COMPONENT_ID)))
                .andExpect(jsonPath("$.payload.code", is(code)))
                .andExpect(jsonPath("$.payload.usage", is(4)))
                .andReturn();
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
    public void testAddUpdateWidget_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String pageCode = "test_add_delete_widget";
        String newWidgetCode = "test_new_type_3";
        Assert.assertNull(this.widgetTypeManager.getWidgetType(newWidgetCode));
        try {
            WidgetRequest request = getWidgetRequest(newWidgetCode);
            ResultActions result0 = this.executeWidgetPost(request, accessToken, status().isOk());
            result0.andExpect(jsonPath("$.payload.code", is(newWidgetCode)));
            Assert.assertNotNull(this.widgetTypeManager.getWidgetType(newWidgetCode));

            PageRequest pageRequest = new PageRequest();
            pageRequest.setCode(pageCode);
            pageRequest.setPageModel("home");
            pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
            Map<String, String> pageTitles = new HashMap<>();
            pageTitles.put("it", pageCode);
            pageTitles.put("en", pageCode);
            pageRequest.setTitles(pageTitles);
            pageRequest.setParentCode("service");
            this.addPage(accessToken, pageRequest);
            
            ResultActions result1 = this.executeWidgetGet(newWidgetCode, accessToken, status().isOk());
            result1.andExpect(jsonPath("$.payload.used", is(0)));
            
            WidgetConfigurationRequest wcr = new WidgetConfigurationRequest();
            wcr.setCode(newWidgetCode);
            ResultActions resultPutWidget = mockMvc
                .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 1})
                        .content(mapper.writeValueAsString(wcr))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
            resultPutWidget.andExpect(status().isOk());
            
            ResultActions result3 = this.executeWidgetGet(newWidgetCode, accessToken, status().isOk());
            result3.andExpect(jsonPath("$.payload.used", is(1)));
        } catch (Exception e) {
            throw e;
        } finally {
            this.pageManager.deletePage(pageCode);
            Assert.assertNull(this.pageManager.getDraftPage(pageCode));
            this.widgetTypeManager.deleteWidgetType(newWidgetCode);
            Assert.assertNull(this.widgetTypeManager.getWidgetType(newWidgetCode));
        }
    }

    @Test
    public void testMoveWidgetToAnotherFrame() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String pageCode = "test_move_widget_page";
        String newWidgetCode = "test_move_widget_1";
        Assert.assertNull(this.widgetTypeManager.getWidgetType(newWidgetCode));
        Assert.assertNull(this.pageManager.getDraftPage(pageCode));
        try {
            WidgetRequest request = getWidgetRequest(newWidgetCode);
            ResultActions result = this.executeWidgetPost(request, accessToken, status().isOk());
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.code", is(newWidgetCode)));
            Assert.assertNotNull(this.widgetTypeManager.getWidgetType(newWidgetCode));

            PageRequest pageRequest = getPageRequest(pageCode);

            result = mockMvc
                    .perform(post("/pages")
                            .content(mapper.writeValueAsString(pageRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk());

            result = this.executeWidgetGet(newWidgetCode, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.used", is(0)));

            WidgetConfigurationRequest wcr = new WidgetConfigurationRequest();
            wcr.setCode(newWidgetCode);
            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                            .content(mapper.writeValueAsString(wcr))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk());

            result = this.executeWidgetGet(newWidgetCode, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.used", is(1)));

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets", pageCode)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload[0].code", is(newWidgetCode)))
                    .andExpect(jsonPath("$.payload[1]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[2]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[3]", Matchers.isEmptyOrNullString()));

            result = mockMvc
                    .perform(delete("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk());

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets", pageCode)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload[0]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[1]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[2]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[3]", Matchers.isEmptyOrNullString()));

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 2})
                            .content(mapper.writeValueAsString(wcr))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk());

            result = this.executeWidgetGet(newWidgetCode, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.used", is(1)));

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets", pageCode)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload[0]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[1]", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[2].code", is(newWidgetCode)))
                    .andExpect(jsonPath("$.payload[3]", Matchers.isEmptyOrNullString()));

        } catch (Exception e) {
            throw e;
        } finally {
            this.pageManager.deletePage(pageCode);
            Assert.assertNull(this.pageManager.getDraftPage(pageCode));
            this.widgetTypeManager.deleteWidgetType(newWidgetCode);
            Assert.assertNull(this.widgetTypeManager.getWidgetType(newWidgetCode));
        }
    }

    private PageRequest getPageRequest(String pageCode) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCode(pageCode);
        pageRequest.setPageModel("home");
        pageRequest.setOwnerGroup(Group.FREE_GROUP_NAME);
        Map<String, String> pageTitles = new HashMap<>();
        pageTitles.put("it", pageCode);
        pageTitles.put("en", pageCode);
        pageRequest.setTitles(pageTitles);
        pageRequest.setParentCode("service");
        return pageRequest;
    }

    private WidgetRequest getWidgetRequest(String newWidgetCode) {
        WidgetRequest request = new WidgetRequest();
        request.setCode(newWidgetCode);
        request.setGroup(Group.FREE_GROUP_NAME);
        Map<String, String> titles = new HashMap<>();
        titles.put("it", "Titolo ITA 3");
        titles.put("en", "Title EN 3");
        request.setTitles(titles);
        request.setCustomUi("<h1>Test</h1>");
        request.setGroup(Group.FREE_GROUP_NAME);
        return request;
    }

    private void addPage(String accessToken, PageRequest pageRequest) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/pages")
                        .content(mapper.writeValueAsString(pageRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
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

    @Test
    public void testGetWidgetsWithAdminPermission() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/widgets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetWidgetsWithoutPermission() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("normal_user", "0x24").build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/widgets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isForbidden());
    }

    @Test
    public void testGetWidgetsWithManagePagesPermission() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("enter_backend_user", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "admin", Permission.MANAGE_PAGES).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/widgets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    private ResultActions executeWidgetGet(String widgetTypeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(get("/widgets/{code}", new Object[]{widgetTypeCode})
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeWidgetUsage(String widgetTypeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(get("/widgets/{code}/usage", new Object[]{widgetTypeCode})
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
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
