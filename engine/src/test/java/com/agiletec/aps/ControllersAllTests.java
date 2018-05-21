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
import org.entando.entando.web.activitystream.ActivityStreamControllerIntegrationTest;
import org.entando.entando.web.activitystream.StreamInterceptorIntegrationTest;
import org.entando.entando.web.category.CategoryControllerIntegrationTest;
import org.entando.entando.web.category.CategoryControllerTest;
import org.entando.entando.web.dashboard.DashboardControllerIntegrationTest;
import org.entando.entando.web.database.DatabaseControllerIntegrationTest;
import org.entando.entando.web.database.DatabaseControllerTest;
import org.entando.entando.web.dataobject.DataTypeControllerIntegrationTest;
import org.entando.entando.web.dataobject.DataTypeControllerTest;
import org.entando.entando.web.dataobjectmodel.DataObjectModelControllerIntegrationTest;
import org.entando.entando.web.dataobjectmodel.DataObjectModelControllerTest;
import org.entando.entando.web.entity.EntityManagerControllerTest;
import org.entando.entando.web.filebrowser.FileBrowserControllerIntegrationTest;
import org.entando.entando.web.filebrowser.FileBrowserControllerTest;
import org.entando.entando.web.group.GroupControllerIntegrationTest;
import org.entando.entando.web.group.GroupControllerUnitTest;
import org.entando.entando.web.guifragment.GuiFragmentControllerTest;
import org.entando.entando.web.guifragment.GuiFragmentSettingsControllerIntegrationTest;
import org.entando.entando.web.guifragment.GuiFragmentSettingsControllerTest;
import org.entando.entando.web.guifragment.validator.GuiFragmentValidatorTest;
import org.entando.entando.web.label.LabelControllerIntegrationTest;
import org.entando.entando.web.label.LabelControllerUnitTest;
import org.entando.entando.web.language.LanguageControllerIntegrationTest;
import org.entando.entando.web.language.LanguageControllerUnitTest;
import org.entando.entando.web.page.PageConfigurationControllerIntegrationTest;
import org.entando.entando.web.page.PageConfigurationControllerWidgetsIntegrationTest;
import org.entando.entando.web.page.PageControllerIntegrationTest;
import org.entando.entando.web.page.PageControllerTest;
import org.entando.entando.web.pagemodel.PageModelControllerIntegrationTest;
import org.entando.entando.web.pagemodel.PageModelControllerTest;
import org.entando.entando.web.pagesettings.PageSettingsControllerTest;
import org.entando.entando.web.permission.PermissionControllerIntegrationTest;
import org.entando.entando.web.plugins.jacms.contentmodel.ContentModelControllerIntegrationTest;
import org.entando.entando.web.plugins.jacms.contentmodel.ContentModelControllerUnitTest;
import org.entando.entando.web.role.RoleControllerIntegrationTest;
import org.entando.entando.web.role.RoleControllerUnitTest;
import org.entando.entando.web.system.ReloadConfigurationControllerTest;
import org.entando.entando.web.user.UserControllerDeleteAuthoritiesIntegrationTest;
import org.entando.entando.web.user.UserControllerIntegrationTest;
import org.entando.entando.web.user.UserControllerUnitTest;
import org.entando.entando.web.userprofile.ProfileTypeControllerIntegrationTest;
import org.entando.entando.web.userprofile.ProfileTypeControllerTest;
import org.entando.entando.web.userprofile.UserProfileControllerIntegrationTest;
import org.entando.entando.web.usersettings.UserSettingsControllerIntegrationTest;
import org.entando.entando.web.usersettings.UserSettingsControllerUnitTest;
import org.entando.entando.web.widget.WidgetControllerIntegrationTest;
import org.entando.entando.web.widget.WidgetControllerTest;

public class ControllersAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ControllersAllTests.class.getName());

        suite.addTest(new JUnit4TestAdapter(GroupControllerUnitTest.class));
        suite.addTest(new JUnit4TestAdapter(GroupControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(GuiFragmentControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentSettingsControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentSettingsControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentValidatorTest.class));

        suite.addTest(new JUnit4TestAdapter(DataObjectModelControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(DataObjectModelControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(PageModelControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(PageSettingsControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageConfigurationControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(PageConfigurationControllerWidgetsIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(WidgetControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(WidgetControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(EntityManagerControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(ReloadConfigurationControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(LanguageControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(LanguageControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(LabelControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(LabelControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(DataTypeControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DataTypeControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(UserProfileControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(UserControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(UserControllerDeleteAuthoritiesIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(UserControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(UserSettingsControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(UserSettingsControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(DatabaseControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DatabaseControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(CategoryControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(CategoryControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(RoleControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(RoleControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(PermissionControllerIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(ContentModelControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(ContentModelControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(FileBrowserControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(FileBrowserControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(ActivityStreamControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(StreamInterceptorIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(DashboardControllerIntegrationTest.class));

        return suite;
    }

}
