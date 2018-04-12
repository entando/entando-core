package org.entando.entando.web.activitystream;

import java.sql.Timestamp;
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
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.activitystream.ISocialActivityStreamManager;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                                      .perform(get("/activitystream")
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
                                      .perform(get("/activitystream")
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
                                      .perform(get("/activitystream")
                                                                     .param("sort", "createdAt")
                                                                     .param("filters[0].attribute", "createdAt")
                                                                     .param("filters[0].value", String.format("[%s TO %s]", start, end))
                                                                     .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testActionLogRecordCRUD() throws Exception {
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
    
            //execute and action
            ResultActions result = mockMvc
                                          .perform(put("/pages/{pageCode}/configuration/defaultWidgets", new Object[]{pageCode})
                                                                                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                                                .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
    
            Thread.sleep(500);
            //assert record is present
            result = mockMvc
                            .perform(get("/activitystream")
                                                           .param("sort", "createdAt")
                                                           .header("Authorization", "Bearer " + accessToken));
            // result.andExpect(jsonPath("$.payload.length()", is(1)));
    
            //add like
            int recordId = this.actionLogManager.getActionRecords(null).stream().findFirst().get();
            result = mockMvc
                            .perform(post("/activitystream/{recordId}/like", recordId)
                                                                                      .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            // result.andExpect(jsonPath("$.payload[0].likes.length()", is(1)));
    
            //remove like
            result = mockMvc
                            .perform(delete("/activitystream/{recordId}/like", recordId)
                                                                                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            // result.andExpect(jsonPath("$.payload[0].likes.length()", is(0)));
    
            //add comment
            String comment = "this_is_a_comment";
            ActivityStreamCommentRequest req = new ActivityStreamCommentRequest();
            req.setComment(comment);
            req.setRecordId(recordId);
    
            result = mockMvc
                            .perform(post("/activitystream/{recordId}/comments", recordId)
                                                                                          .contentType(MediaType.APPLICATION_JSON)
                                                                                          .content(mapper.writeValueAsString(req))
                                                                                          .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            // result.andExpect(jsonPath("$.payload[0].comments.length()", is(1)));
            //result.andExpect(jsonPath("$.payload[0].comments[0].commentText", Matchers.is(comment)));
    
            //remove comment
            result = mockMvc
                            .perform(delete("/activitystream/{recordId}/like", recordId)
                                                                                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            //result.andExpect(jsonPath("$.payload[0].comments", Matchers.hasSize(0)));
    
            //add invalid comment
            req = new ActivityStreamCommentRequest();
            req.setComment(comment);
            req.setRecordId(0);
    
            result = mockMvc
                            .perform(post("/activitystream/{recordId}/comments", recordId)
                                                                                          .contentType(MediaType.APPLICATION_JSON)
                                                                                          .content(mapper.writeValueAsString(req))
                                                                                          .header("Authorization", "Bearer " + accessToken));
    
            result.andExpect(status().isBadRequest());
    
            //add invalid comment
    
            req = new ActivityStreamCommentRequest();
            // req.setComment(comment);
            req.setRecordId(recordId);
    
            result = mockMvc
                            .perform(post("/activitystream/{recordId}/comments", recordId)
                                                                                          .contentType(MediaType.APPLICATION_JSON)
                                                                                          .content(mapper.writeValueAsString(req))
                                                                                          .header("Authorization", "Bearer " + accessToken));
    
            result.andExpect(status().isBadRequest());
    
        } finally {
            this.pageManager.deletePage(pageCode);
            List<Integer> list = this.actionLogManager.getActionRecords(null);
            list.stream().forEach(i -> {
                try {
                    this.deleteCommentsByRecordId(i);
                    this.socialActivityStreamManager.editActionLikeRecord(i, "jack_bauer", false);
                    this.actionLogManager.deleteActionRecord(i);
                } catch (ApsSystemException e) {
    
                    e.printStackTrace();
                }
            });
    
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


}
