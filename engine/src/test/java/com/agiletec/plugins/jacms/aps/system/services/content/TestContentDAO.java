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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

/**
 * Test del Data Access Object per gli oggetti contenuto (Content).
 * @version 1.0
 * @author M.Morini - S.Didaci - E.Santoboni
 */
public class TestContentDAO extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		this.dispose();
	}
    
	public void testDeleteAddContent() throws Throwable {
    	try {
			Content mockContent = this.getMockContent();
			this.deleteContent(mockContent);
			this.addContent(mockContent);
		} catch (Throwable e) {
			throw e;
		}
    }
	
	private void deleteContent(Content content) throws ApsSystemException {
		this._contentDao.deleteEntity(content.getId());
		ContentRecordVO contentRecord = (ContentRecordVO) this._contentDao.loadEntityRecord(content.getId());
		assertNull(contentRecord);
	}
	
	private void addContent(Content mockContent) throws ApsSystemException {
		_contentDao.addEntity(mockContent);
		ContentRecordVO contentRecord = (ContentRecordVO) this._contentDao.loadEntityRecord(mockContent.getId());
		assertEquals(mockContent.getDescr(), contentRecord.getDescr());
		assertEquals(mockContent.getStatus(), contentRecord.getStatus());
		assertFalse(contentRecord.isOnLine());
	}
	
	public void testGetAllContentIds() throws Throwable {
		List<String> contentIds1 = this._contentDao.getAllEntityId();
		List<String> contentIds2 = this._contentManager.searchId(null);
		assertEquals(contentIds1.size(), contentIds2.size());
		for (int i = 0; i < contentIds1.size(); i++) {
			String contentId = contentIds1.get(i);
			assertTrue(contentIds2.contains(contentId));
		}
	}
	
	public void testInsertRemoveOnlineContent() throws Throwable {
    	try {
			Content mockContent = this.getMockContent();
			this.insertOnLineContent(mockContent);
			this.getAllContentsOnLine(mockContent);
	    	this.removeOnLineContent(mockContent);
		} catch (Throwable e) {
			throw e;
		}
    }
	
	private void insertOnLineContent(Content mockContent) throws ApsSystemException {
		this._contentDao.insertOnLineContent(mockContent);
		ContentRecordVO contentRecord = (ContentRecordVO) this._contentDao.loadEntityRecord(mockContent.getId());
		assertTrue(contentRecord.isOnLine());
	}
	
	private void getAllContentsOnLine(Content mockContent) throws ApsSystemException {  
		List<String> list = this._contentDao.getAllEntityId();
        assertTrue(list.contains(mockContent.getId()));
    }
    
	private void removeOnLineContent(Content content) throws ApsSystemException {
		this._contentDao.removeOnLineContent(content);
		ContentRecordVO contentRecord = (ContentRecordVO) this._contentDao.loadEntityRecord(content.getId());
		assertFalse(contentRecord.isOnLine());
	}
	
	public void testUpdateContent() throws Throwable {
    	try {
			Content mockContent = this.getMockContent();
			mockContent.setDescr("New Description");
			mockContent.setStatus(Content.STATUS_READY);
			this.updateContent(mockContent);
		} catch (Throwable t) {
			throw t;
		}
    }
	
	public void testGetPageUtilizers() throws Throwable {
		List<String> contentIds = _contentDao.getPageUtilizers("pagina_11");
		assertNotNull(contentIds);
		assertEquals(2, contentIds.size());
		String contentId = contentIds.get(0);
		assertEquals("EVN193", contentId);
    }
	
	public void testGetContentUtilizers() throws Throwable {
		List<String> contentIds = _contentDao.getContentUtilizers("ART1");
		assertNotNull(contentIds);
		assertEquals(2, contentIds.size());
		String contentId = contentIds.get(0);
		assertEquals("EVN193", contentId);
		contentId = contentIds.get(1);
		assertEquals("EVN194", contentId);
    }
	
	public void testGetGroupUtilizers() throws Throwable {
		List<String> contentIds = _contentDao.getGroupUtilizers("customers");
		assertNotNull(contentIds);
		assertEquals(5, contentIds.size());
		assertTrue(contentIds.contains("ART102"));
		assertTrue(contentIds.contains("ART111"));
		assertTrue(contentIds.contains("ART122"));
		assertTrue(contentIds.contains("RAH101"));
		assertTrue(contentIds.contains("ART112"));
    }
	
	public void testGetResourceUtilizers() throws Throwable {
		List<String> contentIds = _contentDao.getResourceUtilizers("44");
		assertNotNull(contentIds);
		assertEquals(2, contentIds.size());
		String contentId = contentIds.get(0);
		assertEquals("ART1", contentId);
		contentId = contentIds.get(1);
		assertEquals("ART180", contentId);
    }
	
	private void updateContent(Content mockContent) throws ApsSystemException {
		this._contentDao.updateEntity(mockContent);
		ContentRecordVO contentRecord = (ContentRecordVO) this._contentDao.loadEntityRecord(mockContent.getId());
		assertEquals(mockContent.getDescr(), contentRecord.getDescr());
		assertEquals(mockContent.getStatus(), contentRecord.getStatus());
		assertFalse(contentRecord.isOnLine());
	}
	
	private Content getMockContent() { 
        Content content = this._contentManager.createContentType("ART");
        
        content.setId("temp");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        
        content.addGroup("firstGroup");
        content.addGroup("secondGroup");
        content.addGroup("thirdGroup");
        
    	AttributeInterface attribute = new MonoTextAttribute();
    	attribute.setName("temp");
    	attribute.setDefaultLangCode("it");
    	attribute.setRenderingLang("it");
    	attribute.setSearchable(true);
    	attribute.setType("Monotext");
    	content.addAttribute(attribute);	
    	content.setDefaultLang("it");
    	content.setDefaultModel("content_viewer");
    	content.setDescr("temp");
    	content.setListModel("Monolist");
    	content.setRenderingLang("it");
    	content.setStatus("Bozza");
    	content.setTypeCode("ART");
    	content.setTypeDescr("Articolo rassegna stampa");
    	return content;
    }
    
    private void dispose() throws Exception {
		Content mockContent = this.getMockContent();
		try {
			this._contentDao.deleteEntity(mockContent.getId());
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private void init() throws Exception {
		this._contentDao = new ContentDAO();
		try {
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			Content mockContent = this.getMockContent();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			this._contentDao.setDataSource(dataSource);
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			this._contentDao.setLangManager(langManager);
			this._contentDao.addEntity(mockContent);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private ContentDAO _contentDao;
    
    private IContentManager _contentManager;
	
}
