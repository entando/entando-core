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
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageConfigurationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

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
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type"));
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


            result = mockMvc
                            .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                                                                                                   .param("status", IPageService.STATUS_ONLINE)
                                                                                                   .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
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
