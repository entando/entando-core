/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import org.entando.entando.plugins.jacms.aps.system.services.cache.TestCacheInfoManager;
import org.entando.entando.plugins.jacms.aps.system.services.page.TestCmsPageManagerWrapper;

import com.agiletec.plugins.jacms.aps.system.TestApplicationContext;
import com.agiletec.plugins.jacms.aps.system.services.content.TestCategoryUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.TestContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.TestContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.TestGroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.TestPublicContentSearcherDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.TestValidateContent;
import com.agiletec.plugins.jacms.aps.system.services.content.authorization.TestContentAuthorization;
import com.agiletec.plugins.jacms.aps.system.services.content.entity.TestContentEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.content.parse.TestContentDOM;
import com.agiletec.plugins.jacms.aps.system.services.content.util.TestContentAttributeIterator;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.TestContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.TestContentViewerHelper;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.TestContentModelDAO;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.TestContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.TestContentPageMapperManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.TestContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.TestLinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.TestResourceDAO;
import com.agiletec.plugins.jacms.aps.system.services.resource.TestResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.TestResourceDOM;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.TestSearchEngineManager;
import org.entando.entando.plugins.jacms.aps.system.services.api.TestApiContentInterface;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jACMS");
		
		System.out.println("Test for jACMS plugin");
		
		// 
		suite.addTestSuite(TestContentAuthorization.class);
		suite.addTestSuite(TestContentEntityManager.class);
		suite.addTestSuite(TestContentDOM.class);
		suite.addTestSuite(TestContentListHelper.class);
		suite.addTestSuite(TestContentViewerHelper.class);
		suite.addTestSuite(TestContentAttributeIterator.class);
		suite.addTestSuite(TestContentDAO.class);
		suite.addTestSuite(TestContentManager.class);
		suite.addTestSuite(TestPublicContentSearcherDAO.class);
		suite.addTestSuite(TestValidateContent.class);
		//
		suite.addTestSuite(TestContentModelDAO.class);
		suite.addTestSuite(TestContentModelManager.class);
		//
		suite.addTestSuite(TestContentPageMapperManager.class);
		//
		suite.addTestSuite(TestContentDispenser.class);
		//
		suite.addTestSuite(TestLinkResolverManager.class);
		//
		suite.addTestSuite(TestCmsPageManagerWrapper.class);
		//
		suite.addTestSuite(TestResourceDOM.class);
		suite.addTestSuite(TestResourceDAO.class);
		suite.addTestSuite(TestResourceManager.class);
		//
		suite.addTestSuite(TestSearchEngineManager.class);
		suite.addTestSuite(TestApplicationContext.class);
		
		// Test cross utilizers
		suite.addTestSuite(TestCategoryUtilizer.class);
		suite.addTestSuite(TestGroupUtilizer.class);
		
		suite.addTestSuite(TestCacheInfoManager.class);
		//
		suite.addTestSuite(TestApiContentInterface.class);
		
		return suite;
	}

}
