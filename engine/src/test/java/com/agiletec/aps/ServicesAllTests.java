package com.agiletec.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModelServiceTest;
import org.entando.entando.aps.system.services.entity.EntityManagerServiceIntegrationTest;
import org.entando.entando.aps.system.services.entity.EntityManagerServiceTest;
import org.entando.entando.aps.system.services.group.GroupServiceIntegrationTest;
import org.entando.entando.aps.system.services.group.GroupServiceTest;
import org.entando.entando.aps.system.services.group.RestListRequestTest;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentServiceTest;
import org.entando.entando.aps.system.services.page.PageServiceIntegrationTest;
import org.entando.entando.aps.system.services.page.PageService_WidgetIntegrationTest;
import org.entando.entando.aps.system.services.pagemodel.PageModelDtoTest;
import org.entando.entando.aps.system.services.pagemodel.PageModelServiceTest;
import org.entando.entando.aps.system.services.pagesettings.PageSettingsServiceIntegrationTest;

public class ServicesAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ServicesAllTests.class.getName());

        //
        suite.addTestSuite(GroupServiceIntegrationTest.class);
        suite.addTestSuite(PageSettingsServiceIntegrationTest.class);
        suite.addTestSuite(PageServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(RestListRequestTest.class));
        suite.addTest(new JUnit4TestAdapter(GroupServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DataObjectModelServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelDtoTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(EntityManagerServiceTest.class));
        suite.addTestSuite(EntityManagerServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(PageService_WidgetIntegrationTest.class));

        return suite;
    }

}
