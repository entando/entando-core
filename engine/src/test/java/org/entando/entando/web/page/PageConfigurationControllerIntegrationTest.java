package org.entando.entando.web.page;

import java.util.Map;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageConfigurationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPageConfiguration() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/pages/{pageCode}/configuration", "homepage")
                                                                                                 .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.payload.online", is(true)));

        /**
         * The response should have the correct CORS headers and the CORS
         * configuration should reflect the one set in
         * org.entando.entando.aps.servlet.CORSFilter class
         */
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    /**
     * Given:
     *  a page only in draft
     * When:
     *  the user request the configuration for status draft
     * Then
     *  the result is ok
     *  
     * Given:
     *  a page only in draft
     * When:
     *  the user request the configuration for status published
     * Then
     *  an error with status code 400 is raised
     *  
     * @throws Exception
     */
    @Test
    public void testGetPageConfigurationOnLineNotFound() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                                          .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                                                                                                                 .param("status", IPageService.STATUS_DRAFT)
                                                                                                                 .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());


            result = mockMvc
                            .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                                                                                                   .param("status", IPageService.STATUS_ONLINE)
                                                                                                   .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("3")));
        } finally {
            this.pageManager.deletePage(pageCode);
        }

    }

    @Test
    public void testPutPageConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                                          .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                                                                                                                      .param("status", IPageService.STATUS_DRAFT)
                                                                                                                      .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            String getResult = result.andReturn().getResponse().getContentAsString();

            String payloadWithInvalidModelId = "{\n" +
                                               "  \"code\": \"content_viewer\",\n" +
                                               "  \"config\": {\n" +
                                               " \"contentId\": \"EVN24\",\n" +
                                               " \"modelId\": \"default\"\n" +
                                               "  }\n" +
                                               "}";

            result = mockMvc
                            .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                                                                                                        .param("status", IPageService.STATUS_DRAFT)
                                                                                                        .content(payloadWithInvalidModelId)
                                                                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                        .header("Authorization", "Bearer " + accessToken));

            String putResult = result.andReturn().getResponse().getContentAsString();
            result.andExpect(status().isOk());
            assertThat(putResult, is(getResult));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testGetPageWidgetConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                                          .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 999})
                                                                                                                        .param("status", IPageService.STATUS_DRAFT)
                                                                                                                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is("3")));

            result = mockMvc
                            .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "ASD"})
                                                                                                            .param("status", IPageService.STATUS_DRAFT)
                                                                                                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("40")));


        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    /**
     * creates a page without configured frames than applies the default widgets 
     */
    @Test
    public void testApplyDefautWidgets() throws Exception {
        String pageCode = "draft_page_100";
        try {
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);

            mockPage.setWidgets(new Widget[mockPage.getWidgets().length]);

            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);


            ResultActions result = mockMvc
                                          .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                                                                                                                 .param("status", IPageService.STATUS_DRAFT)

                                                                                                                 .header("Authorization", "Bearer " + accessToken));

            Widget[] defaultWidgetConfiguration = pageModel.getDefaultWidget();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", Matchers.hasSize(pageModel.getConfiguration().length)));
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);
                result.andExpect(jsonPath(path, is(nullValue())));
            }
            

            result = mockMvc
                            .perform(put("/pages/{pageCode}/configuration/defaultWidgets", new Object[]{pageCode})
                                                                                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                                       .header("Authorization", "Bearer " + accessToken));


            result.andExpect(status().isOk());
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);

                if (null != defaultWidgetConfiguration[i]) {
                    WidgetConfigurationDto exp = new WidgetConfigurationDto(defaultWidgetConfiguration[i].getType().getCode(), defaultWidgetConfiguration[i].getConfig());
                    Map actual = mapper.convertValue(exp, Map.class); //jsonPath workaround
                    result.andExpect(jsonPath(path, is(actual)));
                } else {
                    result.andExpect(jsonPath(path, is(nullValue())));
                }
            }


        } finally {
            this.pageManager.deletePage(pageCode);
        }
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

}
