package com.agiletec.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.web.group.GroupControllerTest;
import org.entando.entando.web.guifragment.GuiFragmentControllerTest;
import org.entando.entando.web.page.PageControllerTest;
import org.entando.entando.web.pagesettings.PageSettingsControllerTest;

public class ControllersAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ControllersAllTests.class.getName());

        suite.addTest(new JUnit4TestAdapter(GroupControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageSettingsControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerTest.class));
      	suite.addTest(new JUnit4TestAdapter(GuiFragmentControllerTest.class));
        return suite;
    }

}
