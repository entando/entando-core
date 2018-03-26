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
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageConfigurationControllerWidgetsIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;


    @Test
    public void testConfigureListViewer() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode);
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


            String payloadWithInvalidContentType = "{\n" +
                                                   "  \"code\": \"content_viewer_list\",\n" +
                                                   "  \"config\": {\n" +
                                                   "      \"contentType\": \"LOL\",\n" +
                                                   "      \"maxElements\": \"15\"\n" +
                                                   "  }\n" +
                                                   "}";

            result = mockMvc
                            .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                                                                                                          .param("status", IPageService.STATUS_ONLINE)
                                                                                                          .content(payloadWithInvalidContentType)
                                                                                                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                          .header("Authorization", "Bearer " + accessToken));

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isConflict());

            //-------------------

            String payloadWithInvalidModelId = "{\n" +
                                               "  \"code\": \"content_viewer_list\",\n" +
                                               "  \"config\": {\n" +
                                               " \"contentType\": \"ART\",\n" +
                                               " \"modelId\": \"9999999999\",\n" +
                                               " \"maxElements\": \"15\"\n" +
                                               "  }\n" +
                                               "}";

            result = mockMvc
                            .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                                                                                                          .param("status", IPageService.STATUS_ONLINE)
                                                                                                          .content(payloadWithInvalidModelId)
                                                                                                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                          .header("Authorization", "Bearer " + accessToken));

            //System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isConflict());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testContentViewer() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode);
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

            String payloadWithInvalidContentId = "{\n" +
                                                   "  \"code\": \"content_viewer\",\n" +
                                                   "  \"config\": {\n" +
                                                   "      \"contentId\": \"ART1120000000\",\n" +
                                                   "      \"modelId\": \"default\"\n" +
                                                   "  }\n" +
                                                   "}";

            result = mockMvc
                            .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                                                                                                          .param("status", IPageService.STATUS_ONLINE)
                                                                                                          .content(payloadWithInvalidContentId)
                                                                                                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                          .header("Authorization", "Bearer " + accessToken));

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isConflict());

            String validContentIdWithIncompatibleGroup = "{\n" +
                                               "  \"code\": \"content_viewer\",\n" +
                                               "  \"config\": {\n" +
                                               "      \"contentId\": \"ART112\",\n" +
                                               "      \"modelId\": \"default\"\n" +
                                               "  }\n" +
                                               "}";

            result = mockMvc
                            .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                                                                                                          .param("status", IPageService.STATUS_ONLINE)
                                                                                                          .content(validContentIdWithIncompatibleGroup)
                                                                                                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                          .header("Authorization", "Bearer " + accessToken));

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isConflict());


        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }



    protected Page createPage(String pageCode) {
        IPage parentPage = pageManager.getDraftPage("service");
        PageModel pageModel = parentPage.getMetadata().getModel();
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(), true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = PageTestUtil.createProperties("temp", "tempValue", "contentId", "ART11");
        Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this.widgetTypeManager);
        Widget[] widgets = {widgetToAdd};
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage, "free", metadata, widgets);
        return pageToAdd;
    }

}
