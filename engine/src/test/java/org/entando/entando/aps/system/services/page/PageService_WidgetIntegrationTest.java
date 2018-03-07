package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PageService_WidgetIntegrationTest extends BaseTestCase {

    private IPageService pageService;
    private IPageManager pageManager;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            pageService = (IPageService) this.getApplicationContext().getBean(IPageService.BEAN_NAME);
            pageManager = (IPageManager) this.getApplicationContext().getBean(SystemConstants.PAGE_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

    public void test_get_page_configuration() throws JsonProcessingException {
        IPage draftRoot = this.pageManager.getDraftRoot();
        PageConfigurationDto pageConfigurationDto = (PageConfigurationDto) this.pageService.getPageConfiguration(draftRoot.getCode(), IPageService.STATUS_DRAFT);
        ObjectMapper mapper = new ObjectMapper();
        String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pageConfigurationDto);
        System.out.println(out);
    }

    public void test_get_page_widget() throws JsonProcessingException {
        IPage draftRoot = this.pageManager.getDraftRoot();
        WidgetConfigurationDto widgetConfigurationDto = this.pageService.getWidgetConfiguration(draftRoot.getCode(), 0, IPageService.STATUS_DRAFT);
        assertThat(widgetConfigurationDto.getCode(), is("content_viewer_list"));
    }

    public void test_update_page_widget() throws JsonProcessingException, ApsSystemException {
        String pageCode = "temp001";
        IPage parentPage = pageManager.getDraftRoot();
        PageModel pageModel = parentPage.getMetadata().getModel();
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(),
                                                                true, pageCode, null, null, false, null, null);
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage, "free", metadata, null);
        try {
            pageManager.addPage(pageToAdd);
            WidgetConfigurationDto widgetConfigurationDto = this.pageService.getWidgetConfiguration(pageToAdd.getCode(), 0, IPageService.STATUS_DRAFT);
            assertThat(widgetConfigurationDto, is(nullValue()));

            WidgetConfigurationRequest widgetConfigurationRequest = new WidgetConfigurationRequest();
            widgetConfigurationRequest.setCode("login_form");
            widgetConfigurationRequest.setConfig(null);

            this.pageService.updateWidgetConfiguration(pageCode, 0, widgetConfigurationRequest);

            assertThat(this.pageService.getWidgetConfiguration(pageToAdd.getCode(), 0, IPageService.STATUS_DRAFT).getCode(), is("login_form"));

        } finally {
            pageManager.deletePage(pageCode);
        }

    }

    public void test_remove_page_widget() throws JsonProcessingException, ApsSystemException {
        String pageCode = "temp001";
        IPage parentPage = pageManager.getDraftRoot();
        PageModel pageModel = parentPage.getMetadata().getModel();
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(),
                                                                true, pageCode, null, null, false, null, null);
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage, "free", metadata, null);
        try {
            pageManager.addPage(pageToAdd);
            WidgetConfigurationDto widgetConfigurationDto = this.pageService.getWidgetConfiguration(pageToAdd.getCode(), 0, IPageService.STATUS_DRAFT);
            assertThat(widgetConfigurationDto, is(nullValue()));

            WidgetConfigurationRequest widgetConfigurationRequest = new WidgetConfigurationRequest();
            widgetConfigurationRequest.setCode("login_form");
            widgetConfigurationRequest.setConfig(null);

            this.pageService.updateWidgetConfiguration(pageCode, 0, widgetConfigurationRequest);
            assertThat(this.pageService.getWidgetConfiguration(pageToAdd.getCode(), 0, IPageService.STATUS_DRAFT).getCode(), is("login_form"));

            this.pageService.deleteWidgetConfiguration(pageToAdd.getCode(), 0);
            assertThat(widgetConfigurationDto, is(nullValue()));

        } finally {
            pageManager.deletePage(pageCode);
        }
    }



}
