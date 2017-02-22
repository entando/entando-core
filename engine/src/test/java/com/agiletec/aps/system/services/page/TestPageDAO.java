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
package com.agiletec.aps.system.services.page;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;

/**
 * @author M.Diana
 */
public class TestPageDAO extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
    public void testLoadPageList() throws Throwable {
    	try {
			List<IPage> pages = _pageDao.loadPages();
            String value = null;
            boolean contains = false;
            for (IPage page : pages) {
    			value = page.getCode();
    			if (value.equals("homepage")) {
    				contains = true;
    			}
    		}
    		assertTrue(contains);
        } catch (Throwable t) {
            throw t;
        }
	}
    
	public void testAddUpdateDeletePage() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);
		IPage extractedPage = null;
		try {
			List<IPage> pages = this._pageDao.loadPages();
			extractedPage = this.getPageByCode(pages, pageCode);
			assertNull(extractedPage);
			
	    	this._pageDao.addPage(newPageForTest);
	    	pages = this._pageDao.loadPages();
			extractedPage = this.getPageByCode(pages, pageCode);
			
			this.comparePageMetadata(newPageForTest.getDraftMetadata(), extractedPage.getDraftMetadata(), 0);
			this.comparePageMetadata(newPageForTest.getOnlineMetadata(), extractedPage.getOnlineMetadata(), 0);
			
			this.compareWidgets(extractedPage.getDraftWidgets(), extractedPage.getOnlineWidgets());
			
			this.checkAddedPage(pageCode, extractedPage, this._pageDao);
			
			
			this.updatePage(extractedPage, this._pageDao);
		} catch (Throwable t) {
			throw t;
		} finally {
			Page pageToDelete = (null != extractedPage) ? (Page) extractedPage : newPageForTest;
			this.deletePage(pageToDelete, _pageDao);
		}
	}
	
	/**
	 * @param expected The expected PageMetadata to check
	 * @param actual The actual PageMetadata to check
	 * @param dateExpectation The result of the comparison between the 2 updatedAt dates. 1 if expected is greater, 0 if equals, -1 if lower
	 */
	private void comparePageMetadata(PageMetadata expected, PageMetadata actual, int dateExpectation) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected.getTitles(), actual.getTitles());
			assertEquals(expected.getExtraGroups(), actual.getExtraGroups());
			if (expected.getModel() == null) {
				assertNull(actual.getModel());
			} else {
				assertEquals(expected.getModel().getCode(), actual.getModel().getCode());
			}
			assertEquals(expected.isShowable(), actual.isShowable());
			assertEquals(expected.isUseExtraTitles(), actual.isUseExtraTitles());
			assertEquals(expected.getMimeType(), actual.getMimeType());
			assertEquals(expected.getCharset(), actual.getCharset());
			
			if (expected.getUpdatedAt() == null) {
				if (dateExpectation < 0) {
					assertNotNull(actual.getUpdatedAt());
				} else {
					assertNull(actual.getUpdatedAt());
				}
			} else {
				assertEquals(dateExpectation, DateUtils.truncate(expected.getUpdatedAt(), Calendar.MILLISECOND)
						.compareTo(DateUtils.truncate(actual.getUpdatedAt(), Calendar.MILLISECOND)));
			}
		}
	}
	
	private void compareWidgets(Widget[] expected, Widget[] actual) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected.length, actual.length);
			for (int i = expected.length; i < expected.length; i++) {
				assertEquals(expected[i], actual[i]);
			}
		}
	}
	
	private void checkAddedPage(String pageCode, IPage extractedPage, PageDAO pageDAO) throws Throwable {
		assertNotNull(extractedPage);
		assertEquals(extractedPage.getCode(), pageCode);
		assertEquals(extractedPage.getGroup(), "free");
	}
	
	private IPage getPageByCode(List<IPage> pages, String code) {
		IPage extractedPage = null;
	    for (int i=0; i<pages.size(); i++) {
			IPage page = pages.get(i);
			if (page.getCode().equals(code)) {
				extractedPage = page;
				break;
			}
		}
		return extractedPage;
	}
	
	private void updatePage(IPage ipageToUpdate, PageDAO pageDAO) throws Throwable {
		Page pageToUpdate = (Page) ipageToUpdate;
		PageMetadata metadata = pageToUpdate.getDraftMetadata();
		metadata.setTitle("it", "pagina temporanea1");
		metadata.setShowable(false);
		pageToUpdate.setOnlineMetadata(metadata);
		Widget widget = new Widget();
		ApsProperties config = new ApsProperties();
		config.setProperty("temp1", "temp1");
		config.setProperty("contentId", "ART11");
		widget.setConfig(config);
		WidgetType widgetType = new WidgetType();
		widgetType.setCode("content_viewer");
		widget.setType(widgetType);
		Widget[] modifiesWidgets = {widget};
		pageToUpdate.setOnlineWidgets(modifiesWidgets);
		pageToUpdate.setDraftWidgets(modifiesWidgets);
		try {
			pageDAO.updatePage(pageToUpdate);
			List<IPage> pages = pageDAO.loadPages();
	        IPage extractedPage = null;
	        for (int i=0; i<pages.size(); i++) {
    			IPage page = pages.get(i);
				if (page.getCode().equals("temp")) {
					extractedPage = page;
				}
			}
			assertNotNull(extractedPage);
			assertEquals(extractedPage.getCode(), "temp");
			assertEquals(extractedPage.getGroup(), "free");
			
			PageMetadata onlineMetadata = extractedPage.getOnlineMetadata();
			assertEquals(onlineMetadata.getTitle("it"), "pagina temporanea1");
			assertEquals(onlineMetadata.getModel().getCode(), "service");
			assertFalse(onlineMetadata.isShowable());
			
			PageMetadata draftMetadata = extractedPage.getDraftMetadata();
			assertEquals(draftMetadata.getTitle("it"), "pagina temporanea1");
			assertEquals(draftMetadata.getModel().getCode(), "service");
			assertFalse(draftMetadata.isShowable());
			
			// TODO Verificare Widget online/draft
			Widget[] widgets = extractedPage.getWidgets();
			ApsProperties extractedConfig = widgets[0].getConfig();
			assertTrue(extractedConfig.containsKey("temp1"));
			assertTrue(extractedConfig.containsKey("contentId"));
			assertEquals(widgets[0].getType().getCode(), "content_viewer");
        } catch (Throwable t) {
            throw t;
        }
	}
	
	private void deletePage(Page page, PageDAO pageDAO) throws Throwable {
		try {
        	pageDAO.deletePage(page);
        } catch (Throwable e) {
        	throw e;
        }
        List<IPage> pages = null;
        try {
        	pages = pageDAO.loadPages();
        } catch (Throwable t) {
        	throw t;
        }
        IPage currentPage = null;
        String value = null;
        boolean contains = false;
        for (int i=0; i<pages.size(); i++) {
        	currentPage = pages.get(i);
			value = currentPage.getCode();
			if (value.equals("temp")) {
				contains = true;
			}
		}
		assertFalse(contains);        
	}
	
	private Page createPageForTest(String code) {
		Page page = new Page();
		page.setCode(code);
		IPage parentPage = this._pageManager.getPage("service");
		page.setParent(parentPage);
		page.setParentCode("service");
		page.setGroup("free");
		
		PageMetadata metadata = this.createPageMetadata("service", true, "pagina temporanea", 
				null, null, false, null, DateConverter.parseDate("19700101", "yyyyMMdd"));
		page.setOnlineMetadata(metadata);
		page.setDraftMetadata(metadata);
		
		Widget widget = new Widget();
		ApsProperties config = new ApsProperties();
		config.setProperty("temp", "temp");		
		config.setProperty("contentId", "ART1");		
		widget.setConfig(config);
		WidgetType widgetType = new WidgetType();
		widgetType.setCode("content_viewer");
		widget.setType(widgetType);
		Widget[] widgets = new Widget[4];
		widgets[1] = widget;
		
		page.setOnlineWidgets(widgets);
		page.setDraftWidgets(widgets);
		return page;
	}
	
	private PageMetadata createPageMetadata(String pageModelCode, boolean showable, String defaultTitle) {
		return this.createPageMetadata(pageModelCode, showable, defaultTitle, null, null, false, null, null);
	}
	
	private PageMetadata createPageMetadata(String pageModelCode, boolean showable, String defaultTitle, 
			String mimeType, String charset, boolean useExtraTitles, Set<String> extraGroups, Date updatedAt) {
		PageMetadata metadata = new PageMetadata();
		PageModel pageModel = new PageModel();
		pageModel.setCode(pageModelCode);
		metadata.setModel(pageModel);
		
		metadata.setShowable(showable);
		metadata.setTitle("it", defaultTitle);
		if (extraGroups != null) {
			metadata.setExtraGroups(extraGroups);
		}
		metadata.setMimeType(mimeType);
		metadata.setCharset(charset);
		metadata.setUseExtraTitles(useExtraTitles);
		metadata.setExtraGroups(extraGroups);
		metadata.setUpdatedAt(updatedAt);
		return metadata;
	}
	
	private void init() throws Exception {
    	try {
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
    		this._pageDao = new PageDAO();
    		this._pageDao.setDataSource(dataSource);
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
    		IPageModelManager pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
    		IWidgetTypeManager showletTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
    		this._pageDao.setPageModelManager(pageModelManager);
    		this._pageDao.setWidgetTypeManager(showletTypeManager);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private PageDAO _pageDao;
	private IPageManager _pageManager;
	
}