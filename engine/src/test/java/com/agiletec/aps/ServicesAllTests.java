package com.agiletec.aps;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.group.GroupServiceIntegrationTest;


public class ServicesAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ServicesAllTests.class.getName());

        //
        suite.addTestSuite(GroupServiceIntegrationTest.class);

        return suite;
    }

}
