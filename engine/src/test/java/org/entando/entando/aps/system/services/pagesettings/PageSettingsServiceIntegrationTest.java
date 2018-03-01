/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.pagesettings;

import com.agiletec.aps.BaseTestCase;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class PageSettingsServiceIntegrationTest extends BaseTestCase {

    private IPageSettingsService pageSettingsService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            pageSettingsService = (IPageSettingsService) this.getApplicationContext().getBean(IPageSettingsService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testGetSettings() {
        PageSettingsDto settings = pageSettingsService.getPageSettings();
        assertNotNull(settings);
        assertNotNull(settings.getParams());
        assertTrue(settings.getParams().size() > 0);
        boolean found = false;
        settings.getParams().forEach(param -> {
            if (param.getName().equals("baseUrl")) {
                found = true;
            }
        });
        assertEquals(settings.getParams()., this
    

);
    }

}
