package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;

public class PageServiceTest extends BaseTestCase {

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

}
