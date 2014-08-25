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
package com.agiletec.plugins.jacms.aps.system.services.dispenser;

import org.entando.entando.aps.system.services.cache.CacheInfoManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import static junit.framework.Assert.assertEquals;

/**
 * @author W.Ambu - E.Santoboni
 */
public class TestContentDispenser extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testGetRenderedContent_1() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
    	
    	ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo("ART1", 2, "en", reqCtx);
		assertEquals(this.replaceNewLine(_attendedEnART1_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	this._contentDispenser.resolveLinks(outputInfo, reqCtx);
		assertEquals(this.replaceNewLine(_attendedEnART1.trim()), this.replaceNewLine(outputInfo.getRenderedContent().trim()));
    	
    	this.setUserOnSession("admin");
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART1", 2, "en", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedEnART1_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	this._contentDispenser.resolveLinks(outputInfo, reqCtx);
		assertEquals(this.replaceNewLine(_attendedEnART1.trim()), this.replaceNewLine(outputInfo.getRenderedContent().trim()));
    	
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	
    	this.setUserOnSession("editorCoach");
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	
    	this.setUserOnSession("pageManagerCoach");
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    }
    
    public void testGetRenderedContent_2() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
    	this.setUserOnSession("admin");
    	
    	ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo("ART120", 2, "it", reqCtx);
		assertEquals(this.replaceNewLine(_attendedItART120_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	this._contentDispenser.resolveLinks(outputInfo, reqCtx);
		assertEquals(this.replaceNewLine(_attendedItART120.trim()), this.replaceNewLine(outputInfo.getRenderedContent().trim()));
    	
		outputInfo = this._contentDispenser.getRenderizationInfo("ART120", 2, "en", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedEnART120_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	this._contentDispenser.resolveLinks(outputInfo, reqCtx);
		assertEquals(this.replaceNewLine(_attendedEnART120.trim()), this.replaceNewLine(outputInfo.getRenderedContent().trim()));
    	
		
		
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART121", 2, "it", reqCtx);
		assertEquals(this.replaceNewLine(_attendedItART121_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	
		outputInfo = this._contentDispenser.getRenderizationInfo("ART121", 2, "en", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedEnART121_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    	
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART122", 2, "en", reqCtx);
    	assertEquals(this.replaceNewLine(_attendedEnART122_cached.trim()), this.replaceNewLine(outputInfo.getCachedRenderedContent().trim()));
    }
	
	public void testGetRenderedContent_3() throws Throwable {
		Content content = this._contentManager.loadContent("ART120", true);
		content.setId(null);
		try {
			RequestContext reqCtx = this.getRequestContext();
			this.setUserOnSession("admin");
			this._contentManager.insertOnLineContent(content);
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(content.getId(), 2, "it", reqCtx);
			assertNotNull(outputInfo);
			
			assertNotNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_CACHE_PREFIX+content.getId()));
			assertNotNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX+content.getId()));
			
			this._contentManager.insertOnLineContent(content);
			this.waitNotifyingThread();
			
			assertNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_CACHE_PREFIX+content.getId()));
			assertNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX+content.getId()));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != content.getId()) {
				this._contentManager.deleteContent(content);
			}
		}
	}
    
	public void testGetRenderedContent_4() throws Throwable {
		String contentId = "ART120";
		String contentShapeModel = "title (Text): testo=$content.Titolo.getText()";
		int modelId = 1972;
		try {
			this.addNewContentModel(modelId, contentShapeModel, "ART");
			RequestContext reqCtx = this.getRequestContext();
			this.setUserOnSession("admin");
			ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, "en", reqCtx);
			assertEquals("title (Text): testo=Title of Administrator's Content", outputInfo.getCachedRenderedContent());
			
			ContentModel model = this._contentModelManager.getContentModel(modelId);
			String newContentShapeModel = "title: testo=$content.Titolo.getText()";
			model.setContentShape(newContentShapeModel);
			this._contentModelManager.updateContentModel(model);
			this.waitNotifyingThread();
			
			outputInfo = this._contentDispenser.getRenderizationInfo(contentId, modelId, "en", reqCtx);
			assertEquals("title: testo=Title of Administrator's Content", outputInfo.getCachedRenderedContent());
		} catch (Throwable t) {
			throw t;
		} finally {
			ContentModel model = this._contentModelManager.getContentModel(modelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
		}
	}
	
	public void addNewContentModel(int id, String shape, String contentTypeCode) throws Throwable {
		ContentModel model = new ContentModel();
		model.setContentType(contentTypeCode);
		model.setDescription("test");
		model.setId(id);
		model.setContentShape(shape);
		this._contentModelManager.addContentModel(model);
	}
    
    public void testGetUnauthorizedContent() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
    	
    	ContentRenderizationInfo outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals("Current user 'guest' can't view this content", outputInfo.getCachedRenderedContent().trim());
    	
    	this.setUserOnSession("editorCustomers");
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals("Current user 'editorCustomers' can't view this content", outputInfo.getCachedRenderedContent().trim());
    	
    	this.setUserOnSession("supervisorCustomers");
    	outputInfo = this._contentDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
    	assertEquals("Current user 'supervisorCustomers' can't view this content", outputInfo.getCachedRenderedContent().trim());
    }
    
    public void testGetRenderedContentWithWrongModel() throws Throwable {
    	RequestContext reqCtx = this.getRequestContext();
    	String output = _contentDispenser.getRenderedContent("ART1", 67, "en", reqCtx);
    	assertEquals("Content model 67 undefined", output.trim());
    }
    
    private String replaceNewLine(String input) {
    	input = input.replaceAll("\\n", "");
    	input = input.replaceAll("\\r", "");
        return input;
    }
    
    private void init() throws Exception {
    	try {
    		this._contentDispenser = (IContentDispenser) this.getService(JacmsSystemConstants.CONTENT_DISPENSER_MANAGER);
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			this._contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
			this._cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
    	}
    }
    
    private IContentDispenser _contentDispenser = null;
    private IContentManager _contentManager = null;
    private IContentModelManager _contentModelManager = null;
	private CacheInfoManager _cacheInfoManager;
    
    private String _attendedEnART1_cached = 
		"ART1;\n" 
    	+ "Pippo;\n"
    	+ "Paperino;\n"
    	+ "Pluto;\n"
    	+ "The title;\n"
    	+ "Spiderman,#!U;http://www.spiderman.org!#;\n"
    	+ "Image description,/Entando/resources/cms/images/lvback_d1.jpg;\n"
    	+ "Mar 10, 2004;";
    
    private String _attendedEnART1 = 
		"ART1;\n" 
    	+ "Pippo;\n"
    	+ "Paperino;\n"
    	+ "Pluto;\n"
    	+ "The title;\n"
    	+ "Spiderman,http://www.spiderman.org;\n"
    	+ "Image description,/Entando/resources/cms/images/lvback_d1.jpg;\n"
    	+ "Mar 10, 2004;";
    
    private String _attendedItART104_cached = 
		"ART104;\n" 
    	+ "Walter;\n"
    	+ "Marco;\n"
    	+ "Eugenio;\n"
    	+ "William;\n"
    	+ "Titolo Contenuto 2 Coach;\n"
    	+ "Home jAPS,#!U;http://www.japsportal.org!#;\n"
    	+ ",;\n"
    	+ "4-gen-2007;";
    
    private String _attendedItART104 = 
		"ART104;\n" 
    	+ "Walter;\n"
    	+ "Marco;\n"
    	+ "Eugenio;\n"
    	+ "William;\n"
    	+ "Titolo Contenuto 2 Coach;\n"
    	+ "Home jAPS,http://www.japsportal.org;\n"
    	+ ",;\n"
    	+ "4-gen-2007;";
    
    private String _attendedItART120_cached = 
    	"ART120;\n" +
    	"Titolo Contenuto degli &quot;Amministratori&quot;;\n" +
    	"Pagina Iniziale jAPSPortal,#!U;http://www.japsportal.org!#;\n,;\n" +
    	"28-mar-2009;";
    
    private String _attendedItART120 = 
    	"ART120;\n" +
    	"Titolo Contenuto degli &quot;Amministratori&quot;;\n" +
    	"Pagina Iniziale jAPSPortal,http://www.japsportal.org;\n,;\n" +
    	"28-mar-2009;";
    
    private String _attendedEnART120_cached = 
    	"ART120;\n" +
    	"Title of Administrator's Content;\n" +
    	"jAPSPortal HomePage,#!U;http://www.japsportal.org!#;\n,;\n" +
    	"Mar 28, 2009;";
	
    private String _attendedEnART120 = 
    	"ART120;\n" +
    	"Title of Administrator's Content;\n" +
    	"jAPSPortal HomePage,http://www.japsportal.org;\n,;\n" +
    	"Mar 28, 2009;";
	
	private String _attendedItART121_cached = 
    	"ART121;\n" +
    	"Titolo Contenuto degli &quot;Amministratori&quot; 2;\n" +
    	"Pagina Iniziale W3C,#!U;http://www.w3.org/!#;\n,;\n" +
    	"30-mar-2009;";
	
	private String _attendedEnART121_cached = 
    	"ART121;\n" +
    	"Title of Administrator's Content &lt;2&gt;;\n" +
    	"World Wide Web Consortium - Web Standards,#!U;http://www.w3.org/!#;\n,;\n" +
    	"Mar 30, 2009;";
	
	private String _attendedEnART122_cached = 
    	"ART122;\n" +
    	"Titolo Contenuto degli &quot;Amministratori&quot; 3;\n,;\n,;\n;";
    
}