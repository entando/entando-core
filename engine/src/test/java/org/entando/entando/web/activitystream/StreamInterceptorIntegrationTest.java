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
package org.entando.entando.web.activitystream;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author E.Santoboni
 */
public class StreamInterceptorIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IActionLogManager actionLogManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddPublishUnpublishDelete() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "testAddDelete";
        try {
            IActionLogRecordSearchBean searchBean = null;
            List<Integer> logs1 = actionLogManager.getActionRecords(searchBean);
            int size1 = logs1.size();
            pageManager.addPage(createPage(code, null, null));
            //status
            PageStatusRequest pageStatusRequest = new PageStatusRequest();
            pageStatusRequest.setStatus("published");
            IPage page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));
            ResultActions result = mockMvc
                    .perform(put("/pages/{code}/status", code)
                            .content(mapper.writeValueAsString(pageStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            this.waitActionLoggerThread();
            List<Integer> logs2 = actionLogManager.getActionRecords(searchBean);
            int size2 = logs2.size();
            Assert.assertEquals(1, (size2 - size1));
            page = this.pageManager.getDraftPage(code);
            assertThat(page, is(not(nullValue())));
            IPage onlinePage = this.pageManager.getOnlinePage(code);
            assertThat(onlinePage, is(not(nullValue())));
        } finally {
            this.pageManager.deletePage(code);
        }
    }

    protected void waitActionLoggerThread() throws InterruptedException {
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        String threadNamePrefix = IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX;
        for (int i = 0; i < threads.length; i++) {
            Thread currentThread = threads[i];
            if (currentThread != null
                    && currentThread.getName().startsWith(threadNamePrefix)) {
                currentThread.join();
            }
        }
    }

    protected Page createPage(String pageCode, PageModel pageModel, String parent) {
        if (null == parent) {
            parent = "service";
        }
        IPage parentPage = pageManager.getDraftPage(parent);
        if (null == pageModel) {
            pageModel = parentPage.getMetadata().getModel();
        }
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        Widget[] widgets = {};
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), "free", metadata, widgets);
        return pageToAdd;
    }

}
