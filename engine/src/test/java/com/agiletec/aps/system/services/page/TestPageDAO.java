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
package com.agiletec.aps.system.services.page;

import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

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
            for (int i=0; i<pages.size(); i++) {
    			IPage page = pages.get(i);
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
		Page newPageForTest = this.createPageForTest("temp");
		IPage extractedPage = null;
		try {
			List<IPage> pages = this._pageDao.loadPages();
		    for (int i=0; i<pages.size(); i++) {
    			IPage page = pages.get(i);
				if (page.getCode().equals("temp")) {
					extractedPage = page;
					break;
				}
			}
			assertNull(extractedPage);
        	this._pageDao.addPage(newPageForTest);
        	pages = this._pageDao.loadPages();
		    for (int i=0; i<pages.size(); i++) {
    			IPage page = pages.get(i);
				if (page.getCode().equals("temp")) {
					extractedPage = page;
					break;
				}
			}
			assertNotNull(extractedPage);
			assertEquals(extractedPage.getCode(), "temp");
			assertEquals(extractedPage.getGroup(), "free");
			assertEquals(extractedPage.getTitle("it"), "pagina temporanea");
			assertEquals(extractedPage.getModel().getCode(), "service");
			assertTrue(extractedPage.isShowable());
			Widget[] showlets = extractedPage.getWidgets();
			boolean contains = showlets[0].getConfig().contains("temp");
			assertEquals(contains, true);
			assertEquals(showlets[0].getPublishedContent(), "ART1");
			assertEquals(showlets[0].getType().getCode(), "content_viewer");
			this.updatePage(extractedPage, this._pageDao);
		} catch (Throwable t) {
			throw t;
		} finally {
			Page pageToDelete = (null != extractedPage) ? (Page) extractedPage : newPageForTest;
			this.deletePage(pageToDelete, _pageDao);
		}
	}
	
	private void updatePage(IPage ipageToUpdate, PageDAO pageDAO) throws Throwable {
		Page pageToUpdate = (Page) ipageToUpdate;
		pageToUpdate.setTitle("it", "pagina temporanea1");
		pageToUpdate.setShowable(false);
		Widget widget = new Widget();
		ApsProperties config = new ApsProperties();
		config.setProperty("temp1", "temp1");		
		widget.setConfig(config);
		widget.setPublishedContent("ART11");
		WidgetType showletType = new WidgetType();
		showletType.setCode("content_viewer");
		widget.setType(showletType);
		Widget[] modifiesShowlets = {widget};
		pageToUpdate.setWidgets(modifiesShowlets);
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
			assertEquals(extractedPage.getTitle("it"), "pagina temporanea1");
			assertEquals(extractedPage.getModel().getCode(), "service");
			assertFalse(extractedPage.isShowable());
			Widget[] showlets = extractedPage.getWidgets();
			assertTrue(showlets[0].getConfig().contains("temp1"));
			assertEquals(showlets[0].getPublishedContent(), "ART11");
			assertEquals(showlets[0].getType().getCode(), "content_viewer");
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
		assertEquals(contains, false);        
	}
	
	private Page createPageForTest(String code) {
		Page page = new Page();
		page.setCode(code);
		IPage parentPage = this._pageManager.getPage("service");
		page.setParent(parentPage);
		page.setParentCode("service");
		PageModel pageModel = new PageModel();
		pageModel.setCode("service");
		page.setModel(pageModel);
		page.setGroup("free");
		page.setShowable(true);
		page.setTitle("it", "temptitle");
		ApsProperties titles = new ApsProperties();
		titles.setProperty("it", "pagina temporanea");
		page.setTitles(titles);
		Widget widget = new Widget();
		ApsProperties config = new ApsProperties();
		config.setProperty("temp", "temp");		
		widget.setConfig(config);
		widget.setPublishedContent("ART1");
		WidgetType showletType = new WidgetType();
		showletType.setCode("content_viewer");
		widget.setType(showletType);
		Widget[] showlets = {widget};
		page.setWidgets(showlets);
		return page;
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