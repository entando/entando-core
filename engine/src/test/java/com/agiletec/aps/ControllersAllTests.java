/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.web.dataobject.DataTypeControllerIntegrationTest;
import org.entando.entando.web.dataobject.DataTypeControllerTest;
import org.entando.entando.web.dataobjectmodel.DataObjectModelControllerTest;
import org.entando.entando.web.entity.EntityManagerControllerTest;
import org.entando.entando.web.group.GroupControllerIntegrationTest;
import org.entando.entando.web.group.GroupControllerUnitTest;
import org.entando.entando.web.guifragment.GuiFragmentControllerTest;
import org.entando.entando.web.guifragment.validator.GuiFragmentValidatorTest;
import org.entando.entando.web.label.LabelControllerIntegrationTest;
import org.entando.entando.web.label.LabelControllerUnitTest;
import org.entando.entando.web.language.LanguageControllerIntegrationTest;
import org.entando.entando.web.language.LanguageControllerUnitTest;
import org.entando.entando.web.page.PageConfigurationControllerIntegrationTest;
import org.entando.entando.web.page.PageControllerTest;
import org.entando.entando.web.pagemodel.PageModelControllerTest;
import org.entando.entando.web.pagesettings.PageSettingsControllerTest;
import org.entando.entando.web.system.ReloadConfigurationControllerTest;
import org.entando.entando.web.userprofile.ProfileTypeControllerIntegrationTest;
import org.entando.entando.web.userprofile.ProfileTypeControllerTest;

public class ControllersAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ControllersAllTests.class.getName());

        suite.addTest(new JUnit4TestAdapter(GroupControllerUnitTest.class));
        suite.addTest(new JUnit4TestAdapter(GroupControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(PageSettingsControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentValidatorTest.class));
        suite.addTest(new JUnit4TestAdapter(DataObjectModelControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageConfigurationControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(EntityManagerControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(ReloadConfigurationControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(LanguageControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(LanguageControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(LabelControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(LabelControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(DataTypeControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DataTypeControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerTest.class));

        return suite;
    }

}
