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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModelReference;
import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

/**
 * @author E.Santoboni
 */
public class TestContentModelAction  extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
        
        @Override
        protected void tearDown() throws Exception {
            this.deleteReferencingPage();
            super.tearDown();
        }
	
	public void testNewModel() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "new");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testEdit() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "edit");
		ContentModelAction action = (ContentModelAction)this.getAction();
		this.addParameter("modelId", Long.valueOf(modelId).toString());
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		ContentModel currentModel = _contentModelManager.getContentModel(modelId);
		assertEquals(currentModel.getId(), new Long(action.getModelId()).longValue());
		assertEquals(currentModel.getContentShape(), action.getContentShape());
		assertEquals(currentModel.getContentType(), action.getContentType());
		assertEquals(currentModel.getDescription(), action.getDescription());
		assertEquals(currentModel.getStylesheet(), action.getStylesheet());
	}
	
	public void testSaveWithErrors_1() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertNotNull (fieldErrors);
		assertEquals(4, fieldErrors.size());
	}
	
	public void testSaveWithErrors_2() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("contentType", "EVN");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		addParameter("description", "contentModel description");
		addParameter("contentShape", "contentShape field value");
		addParameter("modelId", "2");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();		
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("modelId").size());//duplicate modelId
		
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("contentType", "EVN");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		addParameter("description", "contentModel description");
		addParameter("contentShape", "contentShape field value");
		addParameter("modelId", "khtds");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();		
		assertEquals(1, fieldErrors.size());
		assertEquals(2, fieldErrors.get("modelId").size());//wrong format
	}
	
	public void testSaveWithErrors_3() throws Throwable {
		String veryLongDescription = "Very but very very very long description (upper than 50 characters) for invoke description's length validation";
		int negativeModelId = 0;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jacms/ContentModel", "save");
			addParameter("contentType", "EVN");
			addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
			addParameter("description", veryLongDescription);
			addParameter("contentShape", "contentShape field value");
			addParameter("modelId", String.valueOf(negativeModelId));
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertEquals(1, fieldErrors.get("modelId").size());
			assertEquals(1, fieldErrors.get("description").size());
		} catch (Throwable t) {
			throw t;
		} finally {
			ContentModel model = this._contentModelManager.getContentModel(negativeModelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
		}
	}
	
	public void testAddNewModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelIdToAdd = 99;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jacms/ContentModel", "save");
			addParameter("contentType", "EVN");
			addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
			addParameter("description", "contentModel description");
			addParameter("contentShape", "contentShape field value\r\n");
			addParameter("modelId", String.valueOf(modelIdToAdd));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(1, eventModels.size());
			
			ContentModel model = eventModels.get(0);
			assertEquals("contentShape field value\r\n", model.getContentShape());
		} catch (Throwable t) {
			throw t;
		} finally {
			ContentModel model = this._contentModelManager.getContentModel(modelIdToAdd);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		}
	}
	
	public void testUpdateModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(1, eventModels.size());
		ContentModel model = (ContentModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");

			this.initAction("/do/jacms/ContentModel", "save");
			this.addParameter("strutsAction", new Integer(ApsAdminSystemConstants.EDIT).toString());
			this.addParameter("modelId", new Long(modelId).toString());
			this.addParameter("description", "updated description");
			this.addParameter("contentType", model.getContentType());
			this.addParameter("contentShape", model.getContentShape());
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(1, eventModels.size());

			model = this._contentModelManager.getContentModel(modelId);
			assertEquals("updated description", model.getDescription());
		} catch (Throwable t) {
			throw t;
		} finally {
			model = this._contentModelManager.getContentModel(modelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		}
	}
	
	public void testTrashModel() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");

		this.initAction("/do/jacms/ContentModel", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testTrashReferencedModel() throws Throwable {
		long modelId = 2;
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals("references", result);
	}
	
	public void testDeleteModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(1, eventModels.size());
		ContentModel model = (ContentModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");

			this.initAction("/do/jacms/ContentModel", "delete");
			this.addParameter("modelId", String.valueOf(modelId));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			model = this._contentModelManager.getContentModel(modelId);
			assertNull(model);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		} catch (Throwable t) {
			model = this._contentModelManager.getContentModel(modelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
			throw t;
		}
	}

        public void testDeleteReferencedModel() throws Throwable {
            this.setUserOnSession("admin");
            this.initAction("/do/jacms/ContentModel", "trash");
            this.addParameter("modelId", "2");
            String result = this.executeAction();
            assertEquals("references", result);
            ContentModelAction action = (ContentModelAction) this.getAction();
            
            List<ContentModelReference> references = action.getContentModelReferences();
            assertEquals(5, references.size());
            
            ContentModelReference ref0 = references.get(0);
            assertEquals(1, ref0.getContentsId().size());
            assertEquals("ART1", ref0.getContentsId().get(0));
            assertEquals("homepage", ref0.getPageCode());
            assertEquals(2, ref0.getWidgetPosition());
            assertEquals(false, ref0.isOnline());
            
            ContentModelReference ref1 = references.get(1);
            assertEquals(1, ref1.getContentsId().size());
            assertEquals("ART1", ref1.getContentsId().get(0));
            assertEquals("homepage", ref1.getPageCode());
            assertEquals(2, ref1.getWidgetPosition());
            assertEquals(true, ref1.isOnline());
            
            ContentModelReference ref2 = references.get(2);
            assertEquals("ART1", ref2.getContentsId().get(0));
            assertEquals("referencing_page", ref2.getPageCode());
            assertEquals(0, ref2.getWidgetPosition());
            assertEquals(false, ref2.isOnline());

            ContentModelReference ref3 = references.get(3);
            assertEquals(2, ref3.getContentsId().size());
            assertEquals("referencing_page", ref3.getPageCode());
            assertEquals(1, ref3.getWidgetPosition());
            assertEquals(false, ref3.isOnline());

            ContentModelReference ref4 = references.get(4);
            assertFalse(ref4.getContentsId().isEmpty());
            assertEquals("referencing_page", ref4.getPageCode());
            assertEquals(2, ref4.getWidgetPosition());
            assertEquals(false, ref4.isOnline());
        }
            
	private void addModelForTest(long id, String contentType) throws Throwable {
		ContentModel model = new ContentModel();
		model.setId(id);
		model.setContentType(contentType);
		model.setDescription("contentModel description");
		model.setContentShape("contentShape field value");
		this._contentModelManager.addContentModel(model);
	}

        /**
         * Sets up a page referencing some content models.
         */
        private void addReferencingPage() throws Exception {
            this.deleteReferencingPage();
            IPage root = this._pageManager.getDraftRoot();
            Page page = new Page();
            page.setCode("referencing_page");
            page.setTitle("en", "Test");
            page.setTitle("it", "Test");
            page.setParent(root);
            page.setParentCode(root.getCode());
            page.setGroup(root.getGroup());
            PageMetadata pageMetadata = new PageMetadata();
            pageMetadata.setMimeType("text/html");
            pageMetadata.setModel(root.getModel());
            pageMetadata.setTitles(page.getTitles());
            pageMetadata.setGroup(page.getGroup());
            page.setMetadata(pageMetadata);
            this._pageManager.addPage(page);
            this._pageManager.joinWidget("referencing_page", getSingleContentWidget(), 0);
            this._pageManager.joinWidget("referencing_page", getMultipleContentWidget(), 1);
            this._pageManager.joinWidget("referencing_page", getListOfContentsWidget(), 2);
        }

        private Widget getSingleContentWidget() {
            Widget widget = new Widget();
            WidgetType widgetType = this._widgetTypeManager.getWidgetType("content_viewer");
            widget.setType(widgetType);
            ApsProperties widgetConfig = new ApsProperties();
            widgetConfig.put("contentId", "ART1");
            widgetConfig.put("modelId", "2");
            widget.setConfig(widgetConfig);
            return widget;
        }

        private Widget getMultipleContentWidget() {
            Widget widget = new Widget();
            WidgetType widgetType = this._widgetTypeManager.getWidgetType("row_content_viewer_list");
            widget.setType(widgetType);
            ApsProperties widgetConfig = new ApsProperties();
            widgetConfig.put("contents", "[{contentId=ART1, modelId=2}, {modelId=2, contentId=ART187}]");
            widgetConfig.put("modelId", "2");
            widget.setConfig(widgetConfig);
            return widget;
        }

        private Widget getListOfContentsWidget() {
            Widget widget = new Widget();
            WidgetType widgetType = this._widgetTypeManager.getWidgetType("content_viewer_list");
            widget.setType(widgetType);
            ApsProperties widgetConfig = new ApsProperties();
            widgetConfig.put("maxElemForItem", "5");
            widgetConfig.put("contentType", "ART");
            widgetConfig.put("modelId", "2");
            widget.setConfig(widgetConfig);
            return widget;
        }

        private void deleteReferencingPage() throws Exception {
            this._pageManager.deletePage("referencing_page");
        }
	
	private void init() throws Exception {
                this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
                this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		this._contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
                this.addReferencingPage();
	}

	private IContentModelManager _contentModelManager;
        private IPageManager _pageManager;
        private IWidgetTypeManager _widgetTypeManager;
}
