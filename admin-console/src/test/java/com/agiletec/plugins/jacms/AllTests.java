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
package com.agiletec.plugins.jacms;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.entando.entando.plugins.jacms.apsadmin.content.TestSaveBooleanAttributes;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateBooleanAttributes;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateDateAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateMonotextAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateNumberAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateResourceAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateTextAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.TestContentCategoryBulkAction;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.TestContentGroupBulkAction;

import com.agiletec.plugins.jacms.apsadmin.category.TestTrashReferencedCategory;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentAdminAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentGroupAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentInspectionAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestIntroNewContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestContentLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestExtendedResourceAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestExtendedResourceFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestHypertextAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestLinkAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestListAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestPageLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestResourceAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestUrlLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.model.TestContentModelAction;
import com.agiletec.plugins.jacms.apsadmin.content.model.TestContentModelFinderAction;
import com.agiletec.plugins.jacms.apsadmin.portal.TestPageAction;
import com.agiletec.plugins.jacms.apsadmin.portal.TestTrashReferencedPage;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestBaseFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestContentListViewerWidgetAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestDateAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestNumberAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestTextAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer.TestContentFinderViewerAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer.TestContentViewerWidgetAction;
import com.agiletec.plugins.jacms.apsadmin.resource.TestResourceAction;
import com.agiletec.plugins.jacms.apsadmin.resource.TestResourceFinderAction;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityAttributeConfigAction;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityManagersAction;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityTypeConfigAction;
import com.agiletec.plugins.jacms.apsadmin.user.group.TestTrashReferencedGroup;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jACMS");
		
		System.out.println("Test for jACMS plugin");
		
		suite.addTestSuite(TestTrashReferencedCategory.class);
		
		// Content
		suite.addTestSuite(TestListAttributeAction.class);
		suite.addTestSuite(TestResourceAttributeAction.class);
		suite.addTestSuite(TestExtendedResourceAction.class);
		suite.addTestSuite(TestExtendedResourceFinderAction.class);
		suite.addTestSuite(TestHypertextAttributeAction.class);
		suite.addTestSuite(TestLinkAttributeAction.class);
		suite.addTestSuite(TestPageLinkAction.class);
		suite.addTestSuite(TestContentLinkAction.class);
		suite.addTestSuite(TestUrlLinkAction.class);
		suite.addTestSuite(TestContentModelAction.class);
		suite.addTestSuite(TestContentModelFinderAction.class);
		suite.addTestSuite(TestContentAction.class);
		suite.addTestSuite(TestSaveBooleanAttributes.class);
		suite.addTestSuite(TestValidateBooleanAttributes.class);
		suite.addTestSuite(TestValidateDateAttribute.class);
		suite.addTestSuite(TestValidateMonotextAttribute.class);
		suite.addTestSuite(TestValidateNumberAttribute.class);
		suite.addTestSuite(TestValidateResourceAttribute.class);
		suite.addTestSuite(TestValidateTextAttribute.class);
		suite.addTestSuite(TestContentAdminAction.class);
		suite.addTestSuite(TestContentFinderAction.class);
		suite.addTestSuite(TestContentGroupAction.class);
		suite.addTestSuite(TestContentInspectionAction.class);
		suite.addTestSuite(TestIntroNewContentAction.class);
		suite.addTestSuite(TestContentGroupBulkAction.class);
		suite.addTestSuite(TestContentCategoryBulkAction.class);
		
		// Page
		suite.addTestSuite(TestContentListViewerWidgetAction.class);
		suite.addTestSuite(TestBaseFilterAction.class);
		suite.addTestSuite(TestDateAttributeFilterAction.class);
		suite.addTestSuite(TestNumberAttributeFilterAction.class);
		suite.addTestSuite(TestTextAttributeFilterAction.class);
		suite.addTestSuite(TestContentFinderViewerAction.class);
		suite.addTestSuite(TestContentViewerWidgetAction.class);
		suite.addTestSuite(TestPageAction.class);
		suite.addTestSuite(TestTrashReferencedPage.class);
		
		//Resource
		suite.addTestSuite(TestResourceAction.class);
		suite.addTestSuite(TestResourceFinderAction.class);
		
		//Entity
		suite.addTestSuite(TestJacmsEntityAttributeConfigAction.class);
		suite.addTestSuite(TestJacmsEntityTypeConfigAction.class);
		suite.addTestSuite(TestJacmsEntityManagersAction.class);
		
		//Group
		suite.addTestSuite(TestTrashReferencedGroup.class);
		
		return suite;
	}

}
