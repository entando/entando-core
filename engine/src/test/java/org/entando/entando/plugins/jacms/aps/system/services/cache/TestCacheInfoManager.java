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
package org.entando.entando.plugins.jacms.aps.system.services.cache;

import java.util.List;

import org.entando.entando.aps.system.services.cache.CacheInfoManager;
import org.entando.entando.plugins.jacms.aps.system.services.MockContentListBean;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.BaseContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;

/**
 * @author E.Santoboni
 */
public class TestCacheInfoManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.init();
    }
	
	//---------------------------------------------- ContentManager
	
	public void testPublicContent_1() throws Throwable {
		String contentId = null;
		try {
			contentId = this.createMockContent();
			String cacheKey = JacmsSystemConstants.CONTENT_CACHE_PREFIX + contentId;
			assertNull(this._cacheInfoManager.getFromCache(cacheKey));
			Content extractedContent = this._contentManager.loadContent(contentId, true);
			assertNotNull(extractedContent);
			Content cachedContent = (Content) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContent);
			assertEquals(cachedContent.hashCode(), extractedContent.hashCode());
			assertEquals(cachedContent.getStatus(), extractedContent.getStatus());
			assertEquals(cachedContent.getDescr(), extractedContent.getDescr());
			assertEquals(cachedContent.getLastModified(), extractedContent.getLastModified());
			assertEquals(cachedContent.getLastEditor(), extractedContent.getLastEditor());
			assertEquals(cachedContent.getAttributeList().size(), extractedContent.getAttributeList().size());
			this._contentManager.insertOnLineContent(extractedContent);
			cachedContent = (Content) this._cacheInfoManager.getFromCache(cacheKey);
			assertNull(cachedContent);
			Content extractedWorkContent = this._contentManager.loadContent(contentId, false);
			assertNotNull(extractedWorkContent);
			cachedContent = (Content) this._cacheInfoManager.getFromCache(cacheKey);
			assertNull(cachedContent);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContent(contentId);
		}
	}
	
	public void testPublicContent_2() throws Throwable {
		String contentId = null;
		try {
			contentId = this.createMockContent();
			String cacheKey = JacmsSystemConstants.CONTENT_CACHE_PREFIX + contentId;
			assertNull(this._cacheInfoManager.getFromCache(cacheKey));
			Content extractedContent = this._contentManager.loadContent(contentId, true);
			assertNotNull(extractedContent);
			assertNotNull(this._cacheInfoManager.getFromCache(cacheKey));
			String group1 = JacmsSystemConstants.CONTENT_CACHE_GROUP_PREFIX + contentId;
			this._cacheInfoManager.flushGroup(group1);
			assertNull(this._cacheInfoManager.getFromCache(cacheKey));
			extractedContent = this._contentManager.loadContent(contentId, true);
			assertNotNull(extractedContent);
			assertNotNull(this._cacheInfoManager.getFromCache(cacheKey));
			String group2 = JacmsSystemConstants.CONTENT_TYPE_CACHE_GROUP_PREFIX + extractedContent.getTypeCode();
			this._cacheInfoManager.flushGroup(group2);
			assertNull(this._cacheInfoManager.getFromCache(cacheKey));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContent(contentId);
		}
	}
	
	//---------------------------------------------- Dispenser
	
    public void testGetRenderedContent_1() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
		String contentId = null;
		String langCode = "en";
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String renderInfoCacheKey = BaseContentDispenser.getRenderizationInfoCacheKey(contentId, modelId, langCode, reqCtx);
			assertNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			ContentRenderizationInfo cachedOutputInfo = (ContentRenderizationInfo) this._cacheInfoManager.getFromCache(renderInfoCacheKey);
			assertNotNull(cachedOutputInfo);
			assertEquals(outputInfo.hashCode(), cachedOutputInfo.hashCode());
			assertEquals(outputInfo.getContentId(), cachedOutputInfo.getContentId());
			assertEquals(outputInfo.getModelId(), cachedOutputInfo.getModelId());
			assertEquals(outputInfo.getCachedRenderedContent(), cachedOutputInfo.getCachedRenderedContent());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
    }
    
	public void testGetRenderedContent_2() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
		String contentId = null;
		String langCode = "en";
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String renderInfoCacheKey = BaseContentDispenser.getRenderizationInfoCacheKey(contentId, modelId, langCode, reqCtx);
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			Content content = this._contentManager.loadContent(contentId, true);
			content.setDescr("Modified content description");
			this._contentManager.insertOnLineContent(content);
			super.waitNotifyingThread();
			assertNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
    }
    
	public void testGetRenderedContent_3() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
		String contentId = null;
		String langCode = "en";
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String renderInfoCacheKey = BaseContentDispenser.getRenderizationInfoCacheKey(contentId, modelId, langCode, reqCtx);
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			ContentModel contentModel = this._contentModelManager.getContentModel(modelId);
			contentModel.setDescription("Modified model description");
			this._contentModelManager.updateContentModel(contentModel);
			super.waitNotifyingThread();
			assertNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
    }
	
	public void testGetRenderedContentsGroup_1() throws Throwable {
		String contentId = null;
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String cacheGroupId = JacmsSystemConstants.CONTENT_CACHE_GROUP_PREFIX + contentId;
			this.testGetRenderedContentsGroup(contentId, modelId, cacheGroupId);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
	}
	
	public void testGetRenderedContentsGroup_2() throws Throwable {
		String contentId = null;
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String cacheGroupId = JacmsSystemConstants.CONTENT_MODEL_CACHE_GROUP_PREFIX + modelId;
			this.testGetRenderedContentsGroup(contentId, modelId, cacheGroupId);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
	}
	
	public void testGetRenderedContentsGroup_3() throws Throwable {
		String contentId = null;
		long modelId = -1;
		try {
			modelId = this.createMockContentModel();
			contentId = this.createMockContent();
			String cacheGroupId = JacmsSystemConstants.CONTENT_TYPE_CACHE_GROUP_PREFIX + contentId.substring(0, 3);
			this.testGetRenderedContentsGroup(contentId, modelId, cacheGroupId);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContentObject(contentId, modelId);
		}
	}
	
	protected void testGetRenderedContentsGroup(String contentId, long modelId, String cacheGroupId) throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
		String langCode = "en";
		try {
			String groupsCsv = BaseContentDispenser.getRenderizationInfoCacheGroupsCsv(contentId, modelId);
			String renderInfoCacheKey = BaseContentDispenser.getRenderizationInfoCacheKey(contentId, modelId, langCode, reqCtx);
			assertNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			assertTrue(groupsCsv.indexOf(cacheGroupId) > -1);
			this._cacheInfoManager.flushGroup(cacheGroupId);
			assertNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			
			outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
			//-----------
			outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
			assertNotNull(outputInfo);
			assertNotNull(this._cacheInfoManager.getFromCache(renderInfoCacheKey));
		} catch (Throwable t) {
			throw t;
		}
    }
	
	//---------------------------------------------- ContentList
	
	public void testGetContents_1() throws Throwable {
		try {
			UserDetails guestUser = super.getUser(SystemConstants.GUEST_USER_NAME);
			MockContentListBean bean = new MockContentListBean();
			bean.setContentType("ART");
			assertTrue(bean.isCacheable());
			String cacheKey = BaseContentListHelper.buildCacheKey(bean, guestUser);
			assertNull(this._cacheInfoManager.getFromCache(cacheKey));
			List<String> contents = this._contentListHelper.getContentsId(bean, guestUser);
			assertTrue(contents.size() > 0);
			List<String> cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContents);
			assertEquals(contents.size(), cachedContents.size());
			for (int i = 0; i < cachedContents.size(); i++) {
				assertEquals(contents.get(i), cachedContents.get(i));
			}
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testGetContents_2() throws Throwable {
		String contentId = null;
		try {
			UserDetails guestUser = super.getUser(SystemConstants.GUEST_USER_NAME);
			MockContentListBean bean = new MockContentListBean();
			bean.setContentType("ART");
			assertTrue(bean.isCacheable());
			String cacheKey = BaseContentListHelper.buildCacheKey(bean, guestUser);
			List<String> extractedContents = this._contentListHelper.getContentsId(bean, guestUser);
			List<String> cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContents);
			assertEquals(extractedContents.size(), cachedContents.size());
			
			contentId = this.createMockContent();
			super.waitNotifyingThread();
			cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNull(cachedContents);
			List<String> newExtractedContents = this._contentListHelper.getContentsId(bean, guestUser);
			cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContents);
			assertEquals(newExtractedContents.size(), cachedContents.size());
			assertEquals(newExtractedContents.size(), extractedContents.size() + 1);
			for (int i = 0; i < cachedContents.size(); i++) {
				assertEquals(newExtractedContents.get(i), cachedContents.get(i));
			}
			assertTrue(newExtractedContents.contains(contentId));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteMockContent(contentId);
		}
	}
	
	public void testGetContentsGroup() throws Throwable {
		try {
			UserDetails guestUser = super.getUser(SystemConstants.GUEST_USER_NAME);
			MockContentListBean bean = new MockContentListBean();
			bean.setContentType("ART");
			assertTrue(bean.isCacheable());
			String cacheKey = BaseContentListHelper.buildCacheKey(bean, guestUser);
			List<String> extractedContents = this._contentListHelper.getContentsId(bean, guestUser);
			List<String> cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContents);
			assertEquals(extractedContents.size(), cachedContents.size());
			
			String cacheGroupId = JacmsSystemConstants.CONTENTS_ID_CACHE_GROUP_PREFIX + "ART";
			this._cacheInfoManager.flushGroup(cacheGroupId);
			cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNull(cachedContents);
			
			extractedContents = this._contentListHelper.getContentsId(bean, guestUser);
			cachedContents = (List<String>) this._cacheInfoManager.getFromCache(cacheKey);
			assertNotNull(cachedContents);
			assertEquals(extractedContents.size(), cachedContents.size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	//----------------------------------------------
	
	protected String createMockContent() throws Throwable {
		Content content = this._contentManager.loadContent("ART1", false);
		content.setId(null);
		this._contentManager.insertOnLineContent(content);
		String id = content.getId();
		assertNotNull(id);
		return id;
	}
	
	protected void deleteMockContent(String contentId) throws Throwable {
		Content content = this._contentManager.loadContent(contentId, true);
		if (null != content) {
			this._contentManager.removeOnLineContent(content);
			this._contentManager.deleteContent(content);
			assertNull(this._contentManager.loadContent(contentId, false));
		}
	}
	
	protected long createMockContentModel() throws Throwable {
		ContentModel contentModel = this._contentModelManager.getContentModel(2);
		contentModel.setId(200);
		contentModel.setDescription("Test Content Model");
		this._contentModelManager.addContentModel(contentModel);
		return contentModel.getId();
	}
	
	protected void deleteMockContentObject(String contentId, long modelId) throws Throwable {
		this.deleteMockContent(contentId);
		ContentModel contentModel = this._contentModelManager.getContentModel(modelId);
		if (null != contentModel) {
			this._contentModelManager.removeContentModel(contentModel);
		}
	}
	
    private void init() throws Exception {
    	try {
    		this._contentDispenser = (IContentDispenser) this.getService(JacmsSystemConstants.CONTENT_DISPENSER_MANAGER);
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			this._contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
			this._cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
			this._contentListHelper = (IContentListHelper) this.getApplicationContext().getBean(JacmsSystemConstants.BASE_CONTENT_LIST_HELPER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IContentDispenser _contentDispenser;
    private IContentManager _contentManager;
    private IContentModelManager _contentModelManager;
	private CacheInfoManager _cacheInfoManager;
	private IContentListHelper _contentListHelper;
	
}
