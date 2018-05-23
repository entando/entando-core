/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.activitystream;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.sql.Timestamp;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.activitystream.ISocialActivityStreamManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import org.hamcrest.Matchers;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActivityStreamControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private IActionLogManager actionLogManager;

    @Autowired
    private ISocialActivityStreamManager socialActivityStreamManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetActivityStream() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/activityStream")
                        .param("sort", "createdAt")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetActivityStreamDate() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String start = new Timestamp(DateConverter.parseDate("2017/01/01", "yyyy/MM/dd").getTime()).toString();
        String end = new Timestamp(DateConverter.parseDate("2017/01/01", "yyyy/MM/dd").getTime()).toString();
        ResultActions result = mockMvc
                .perform(get("/activityStream")
                        .param("sort", "createdAt")
                        .param("filters[0].attribute", "createdAt")
                        .param("filters[0].value", String.format("[%s TO %s]", start, end))
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetActivityStreamDate_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String start = new Timestamp(DateConverter.parseDate("2018/03/01", "yyyy/MM/dd").getTime()).toString();
        String end = new Timestamp(DateConverter.parseDate("2018/05/01", "yyyy/MM/dd").getTime()).toString();
        ResultActions result = mockMvc
                .perform(get("/activityStream")
                        .param("sort", "createdAt")
                        .param("filters[0].attribute", "createdAt")
                        .param("filters[0].value", String.format("[%s TO %s]", start, end))
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testActionLogRecordCRUD() throws Exception {
        String pageCode1 = "draft_page_100";
        String pageCode2 = "draft_page_200";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            Integer startSize = this.extractCurrentSize(accessToken);

            this.initTestObjects(accessToken, pageCode1, pageCode2);

            //assert record is present
            Integer secondSize = this.extractCurrentSize(accessToken);
            Assert.assertEquals(2, (secondSize - startSize));

            //add like
            int recordId = this.actionLogManager.getActionRecords(null).stream().findFirst().get();
            ResultActions result = mockMvc
                    .perform(post("/activityStream/{recordId}/like", recordId)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.likes.size()", is(1)));

            //remove like
            result = mockMvc
                    .perform(delete("/activityStream/{recordId}/like", recordId)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.likes.size()", is(0)));

            //add comment
            String comment = "this_is_a_comment";
            ActivityStreamCommentRequest req = new ActivityStreamCommentRequest();
            req.setComment(comment);
            req.setRecordId(recordId);

            result = mockMvc
                    .perform(post("/activityStream/{recordId}/comments", recordId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(req))
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.comments.size()", is(1)));

            result = mockMvc
                    .perform(get("/activityStream")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(req))
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            //remove comment
            result = mockMvc
                    .perform(delete("/activityStream/{recordId}/like", recordId)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.comments.size()", is(1)));

            //add invalid comment
            req = new ActivityStreamCommentRequest();
            req.setComment(comment);
            req.setRecordId(0);

            result = mockMvc
                    .perform(post("/activityStream/{recordId}/comments", recordId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(req))
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());

            //add invalid comment
            req = new ActivityStreamCommentRequest();
            // req.setComment(comment);
            req.setRecordId(recordId);

            result = mockMvc
                    .perform(post("/activityStream/{recordId}/comments", recordId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(req))
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isBadRequest());
        } finally {
            this.destroyLogs(pageCode1, pageCode2);
        }
    }

    @Test
    public void testOrderLogRecord() throws Exception {
        String pageCode1 = "draft_page_100";
        String pageCode2 = "draft_page_200";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            Integer startSize = this.extractCurrentSize(accessToken);
            this.initTestObjects(accessToken, pageCode1, pageCode2);

            //assert record is present
            Integer actualSize = this.extractCurrentSize(accessToken);
            Assert.assertEquals(2, (actualSize - startSize));
            ResultActions result = mockMvc
                    .perform(get("/activityStream")
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            String bodyResult = result.andReturn().getResponse().getContentAsString();
            Integer firstId = JsonPath.read(bodyResult, "$.payload[0].id");
            Integer secondId = JsonPath.read(bodyResult, "$.payload[1].id");

            result = mockMvc
                    .perform(get("/activityStream")
                            .param("direction", "DESC")
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(actualSize)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(1)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(actualSize)));
            bodyResult = result.andReturn().getResponse().getContentAsString();
            Integer firstIdInNewPos = JsonPath.read(bodyResult, "$.payload[" + (actualSize - 1) + "].id");
            Integer secondIdInNewPos = JsonPath.read(bodyResult, "$.payload[" + (actualSize - 2) + "].id");

            Assert.assertEquals(firstId, firstIdInNewPos);
            Assert.assertEquals(secondId, secondIdInNewPos);

            result = mockMvc
                    .perform(get("/activityStream")
                            .param("pageSize", "1").param("direction", "DESC")
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(1)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(1)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(actualSize)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(actualSize)));

        } finally {
            this.destroyLogs(pageCode1, pageCode2);
        }
    }

    private void initTestObjects(String accessToken, String... pageCodes) throws Exception {
        for (String pageCode : pageCodes) {
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);
            mockPage.setWidgets(new Widget[mockPage.getWidgets().length]);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));
            //execute and action
            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/configuration/defaultWidgets", new Object[]{pageCode})
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            synchronized (this) {
                this.wait(1000);
            }
        }
    }

    private void destroyLogs(String... pageCodes) {
        try {
            for (String pageCode : pageCodes) {
                this.pageManager.deletePage(pageCode);
                this.pageManager.deletePage(pageCode);
            }
            List<Integer> list = this.actionLogManager.getActionRecords(null);
            list.stream().forEach(i -> {
                try {
                    this.deleteCommentsByRecordId(i);
                    this.socialActivityStreamManager.editActionLikeRecord(i, "jack_bauer", false);
                    this.actionLogManager.deleteActionRecord(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCommentsByRecordId(Integer i) throws ApsSystemException {
        this.socialActivityStreamManager.getActionCommentRecords(i).forEach(k -> {
            try {
                this.socialActivityStreamManager.deleteActionCommentRecord(k.getId(), i);
            } catch (ApsSystemException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    protected Page createPage(String pageCode, PageModel pageModel) {
        IPage parentPage = pageManager.getDraftPage("service");
        if (null == pageModel) {
            pageModel = parentPage.getMetadata().getModel();
        }
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(), true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = PageTestUtil.createProperties("modelId", "default", "contentId", "EVN24");
        Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this.widgetTypeManager);
        Widget[] widgets = {widgetToAdd};
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage, "free", metadata, widgets);
        return pageToAdd;
    }

    private int extractCurrentSize(String accessToken) throws Exception {
        ResultActions result = mockMvc
                .perform(get("/activityStream")
                        .param("sort", "createdAt")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        return JsonPath.read(bodyResult, "$.payload.size()");
    }

}
