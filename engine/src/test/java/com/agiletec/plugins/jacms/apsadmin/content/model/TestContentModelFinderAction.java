/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jacms.apsadmin.content.model;

import java.util.List;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentModelFinderAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testList() throws Throwable {
		this.initAction("/do/jacms/ContentModel", "list");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		IContentModelFinderAction contentModelFinderAction =(IContentModelFinderAction) this.getAction();
		List<ContentModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(4, contentModels.size());
	}

	public void testSearch_1() throws Throwable {
		String result = this.executeSearch("ART");
		assertEquals(Action.SUCCESS, result);
		IContentModelFinderAction contentModelFinderAction = (IContentModelFinderAction) this.getAction();
		List<ContentModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(4, contentModels.size());

		result = this.executeSearch("CNG");
		assertEquals(Action.SUCCESS, result);
		contentModelFinderAction = (IContentModelFinderAction) this.getAction();
		contentModels = contentModelFinderAction.getContentModels();
		assertEquals(0, contentModels.size());
	}

	public void testSearch_2() throws Throwable {
		ContentModel contentModel = this.createContentModel(MODEL_ID, "EVN");
		this._contentModelManager.addContentModel(contentModel);

		String result = this.executeSearch("");
		assertEquals(Action.SUCCESS, result);
		IContentModelFinderAction contentModelFinderAction = (IContentModelFinderAction) this.getAction();
		List<ContentModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(5, contentModels.size());

		result = this.executeSearch("EVN");
		assertEquals(Action.SUCCESS, result);
		contentModelFinderAction = (IContentModelFinderAction) this.getAction();
		contentModels = contentModelFinderAction.getContentModels();
		assertEquals(1, contentModels.size());
	}

	private String executeSearch(String contentType) throws Throwable {
		this.initAction("/do/jacms/ContentModel", "search");
		this.setUserOnSession("admin");
		this.addParameter("contentType", contentType);
		return this.executeAction();
	}

	private ContentModel createContentModel(int id, String contentType) {
		ContentModel model = new ContentModel();
		model.setId(id) ;
		model.setContentType(contentType);
		model.setDescription("Event test Model");
		model.setContentShape("test shape");
		return model;
	}

	private void init() {
		_contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		ContentModel model = this._contentModelManager.getContentModel(MODEL_ID);
		if (null != model) {
			this._contentModelManager.removeContentModel(model);
		}
	}

	private IContentModelManager _contentModelManager;
	private static final int MODEL_ID = 99;

}
