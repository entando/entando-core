/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.metaData.totalItems", is(6)));
        result.andExpect(jsonPath("$.payload[0].code", is("pagina_1")));
    }

    @Test
    public void testMove() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
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

    protected Page createPage(String pageCode, PageModel pageModel, String parent) {
        if (null == parent) {
            parent = "service";
        }
        IPage parentPage = pageManager.getDraftPage(parent);
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

}
