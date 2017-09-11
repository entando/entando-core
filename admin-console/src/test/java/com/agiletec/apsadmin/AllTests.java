/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin;

import com.agiletec.apsadmin.admin.TestBaseAdminAction;
import com.agiletec.apsadmin.admin.TestSystemParamsUtils;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.agiletec.apsadmin.admin.lang.TestLangAction;
import com.agiletec.apsadmin.admin.lang.TestLangFinderAction;
import com.agiletec.apsadmin.admin.localestring.TestLocaleStringAction;
import com.agiletec.apsadmin.admin.localestring.TestLocaleStringFinderAction;
import com.agiletec.apsadmin.category.TestCategoryAction;
import com.agiletec.apsadmin.common.TestBaseCommonAction;
import com.agiletec.apsadmin.common.TestDispatchForward;
import com.agiletec.apsadmin.common.TestLoginAction;
import com.agiletec.apsadmin.common.TestShortcutConfigAction;
import com.agiletec.apsadmin.portal.TestPageAction;
import com.agiletec.apsadmin.portal.TestPageConfigAction;
import com.agiletec.apsadmin.portal.TestPageTreeAction;
import com.agiletec.apsadmin.portal.TestWidgetTypeAction;
import com.agiletec.apsadmin.portal.TestWidgetsViewerAction;
import com.agiletec.apsadmin.portal.specialwidget.TestSimpleWidgetConfigAction;
import com.agiletec.apsadmin.portal.specialwidget.navigator.TestNavigatorWidgetConfigAction;
import com.agiletec.apsadmin.system.entity.TestEntityManagersAction;
import com.agiletec.apsadmin.system.services.TestShortcutManager;
import com.agiletec.apsadmin.user.group.TestGroupAction;
import com.agiletec.apsadmin.user.group.TestGroupFinderAction;
import com.agiletec.apsadmin.user.role.TestRoleAction;
import com.agiletec.apsadmin.user.role.TestRoleFinderAction;
import org.entando.entando.apsadmin.api.TestApiServiceFinderAction;
import org.entando.entando.apsadmin.common.TestActivityStream;
import org.entando.entando.apsadmin.common.TestActivityStreamAction;
import org.entando.entando.apsadmin.common.currentuser.TestCurrentUserProfileAction;
import org.entando.entando.apsadmin.dataobject.model.TestDataObjectModelAction;
import org.entando.entando.apsadmin.dataobject.model.TestDataObjectModelFinderAction;
import org.entando.entando.apsadmin.filebrowser.TestFileBrowserAction;
import org.entando.entando.apsadmin.portal.guifragment.TestGuiFragmentAction;
import org.entando.entando.apsadmin.portal.model.TestPageModelAction;
import org.entando.entando.apsadmin.portal.model.TestPageModelFinderAction;
import org.entando.entando.apsadmin.system.services.activitystream.TestSocialActivityStreamDAO;
import org.entando.entando.apsadmin.user.TestUserAction;
import org.entando.entando.apsadmin.user.TestUserAuthorizationAction;
import org.entando.entando.apsadmin.user.TestUserFinderAction;
import org.entando.entando.apsadmin.user.TestUserProfileAction;
import org.entando.entando.apsadmin.user.TestUserProfileFinderAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for apsadmin");
		System.out.println("Test for apsadmin");

		// Lang
		suite.addTestSuite(TestLangAction.class);
		suite.addTestSuite(TestLangFinderAction.class);

		// LocalString
		suite.addTestSuite(TestLocaleStringAction.class);
		suite.addTestSuite(TestLocaleStringFinderAction.class);

		suite.addTestSuite(TestBaseAdminAction.class);
		suite.addTestSuite(TestSystemParamsUtils.class);

		// Common
		suite.addTestSuite(TestDispatchForward.class);
		suite.addTestSuite(TestLoginAction.class);
		suite.addTestSuite(TestBaseCommonAction.class);
		suite.addTestSuite(TestCurrentUserProfileAction.class);
		suite.addTestSuite(TestShortcutConfigAction.class);

		//API
		//suite.addTestSuite(TestApiMethodFinderAction.class);
		suite.addTestSuite(TestApiServiceFinderAction.class);

		//Category
		suite.addTestSuite(TestCategoryAction.class);

		// Portal
		suite.addTestSuite(TestPageAction.class);
		suite.addTestSuite(TestPageConfigAction.class);
		suite.addTestSuite(TestPageTreeAction.class);
		suite.addTestSuite(TestWidgetsViewerAction.class);
		suite.addTestSuite(TestWidgetTypeAction.class);
		suite.addTestSuite(TestSimpleWidgetConfigAction.class);
		suite.addTestSuite(TestNavigatorWidgetConfigAction.class);
		suite.addTestSuite(TestPageModelFinderAction.class);
		suite.addTestSuite(TestPageModelAction.class);

		//Entity
		suite.addTestSuite(TestEntityManagersAction.class);

		//Admin Area Manager
		suite.addTestSuite(TestShortcutManager.class);

		//User
		suite.addTestSuite(TestUserAction.class);
		suite.addTestSuite(TestUserAuthorizationAction.class);
		suite.addTestSuite(TestUserFinderAction.class);
		suite.addTestSuite(TestUserProfileAction.class);
		suite.addTestSuite(TestUserProfileFinderAction.class);

		//Group
		suite.addTestSuite(TestGroupAction.class);
		suite.addTestSuite(TestGroupFinderAction.class);

		//Role
		suite.addTestSuite(TestRoleAction.class);
		suite.addTestSuite(TestRoleFinderAction.class);

		//Activity Stream
		suite.addTestSuite(TestSocialActivityStreamDAO.class);
		suite.addTestSuite(TestActivityStream.class);
		suite.addTestSuite(TestActivityStreamAction.class);

		//File Browser
		suite.addTestSuite(TestFileBrowserAction.class);

		//Fragment
		suite.addTestSuite(TestGuiFragmentAction.class);

		// Data Object
		suite.addTestSuite(TestDataObjectModelFinderAction.class);
		suite.addTestSuite(TestDataObjectModelAction.class);

		return suite;
	}

}
