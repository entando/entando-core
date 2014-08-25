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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.util.HeadInfoContainer;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;

/**
 * Classe helper per i Widget di erogazione contenuti singoli.
 * @author W.Ambu - E.Santoboni
 */
public class ContentViewerHelper implements IContentViewerHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentViewerHelper.class);
	
	@Override
	public String getRenderedContent(String contentId, String modelId, RequestContext reqCtx) throws ApsSystemException {
		return this.getRenderedContent(contentId, modelId, false, reqCtx);
	}
    
	/**
     * Restituisce il contenuto da visualizzare nel widget.
     * @param contentId L'identificativo del contenuto ricavato dal tag.
     * @param modelId Il modello del contenuto ricavato dal tag.
     * @param publishExtraTitle 
     * @param reqCtx Il contesto della richiesta.
     * @return Il contenuto da visualizzare nella widget.
     * @throws ApsSystemException In caso di errore.
     */
	@Override
	public String getRenderedContent(String contentId, String modelId, boolean publishExtraTitle, RequestContext reqCtx) throws ApsSystemException {
		String renderedContent = null;
		ContentRenderizationInfo renderInfo = this.getRenderizationInfo(contentId, modelId, publishExtraTitle, reqCtx);
		if (null != renderInfo) {
			renderedContent = renderInfo.getRenderedContent();
		}
		if (null == renderedContent) {
			renderedContent = "";
		} 
        return renderedContent;
    }
	
	@Override
	public ContentRenderizationInfo getRenderizationInfo(String contentId,
			String modelId, boolean publishExtraTitle, RequestContext reqCtx) throws ApsSystemException {
		ContentRenderizationInfo renderizationInfo = null;
        try {
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            String langCode = currentLang.getCode();
            Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
            ApsProperties widgetConfig = widget.getConfig();
			contentId = this.extractContentId(contentId, widgetConfig, reqCtx);
			modelId = this.extractModelId(contentId, modelId, widgetConfig, reqCtx);
			if (contentId != null && modelId != null) {   
 	            long longModelId = new Long(modelId).longValue();
	            this.setStylesheet(longModelId, reqCtx);
	            renderizationInfo = this.getContentDispenser().getRenderizationInfo(contentId, longModelId, langCode, reqCtx);
	            if (null == renderizationInfo) {
	            	_logger.warn("Null Renderization informations: content={}", contentId);
	            }
				this.getContentDispenser().resolveLinks(renderizationInfo, reqCtx);
	            this.manageAttributeValues(renderizationInfo, publishExtraTitle, reqCtx);
            } else {
            	_logger.warn("Parametri visualizzazione contenuto incompleti: " +"contenuto={} modello={}", contentId, modelId);
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting renderization info", t);
    		throw new ApsSystemException("Error extracting renderization info", t);
    	}
        return renderizationInfo;
	}
	
	@Override
	public PublicContentAuthorizationInfo getAuthorizationInfo(String contentId, RequestContext reqCtx) throws ApsSystemException {
		PublicContentAuthorizationInfo authInfo = null;
		try {
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			contentId = this.extractContentId(contentId, widget.getConfig(), reqCtx);
			if (null == contentId) {
				_logger.info("Null contentId");
				return null;
			}
			authInfo = this.getContentAuthorizationHelper().getAuthorizationInfo(contentId);
			if (null == authInfo) {
				_logger.info("Null authorization info by content '" + contentId + "'");
			}
		} catch (Throwable t) {
			_logger.error("Error extracting content authorization info by content {}", contentId, t);
			//ApsSystemUtils.logThrowable(t, this, "getAuthorizationInfo");
			throw new ApsSystemException("Error extracting content authorization info by content '" + contentId + "'", t);
		}
		return authInfo;
	}
	
	protected void manageAttributeValues(ContentRenderizationInfo renderInfo, boolean publishExtraTitle, RequestContext reqCtx) {
		if (!publishExtraTitle) {
			return;
		}
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		if (!page.isUseExtraTitles()) {
			return;
		} 
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		if (currentFrame == page.getModel().getMainFrame() && null != renderInfo) {
			Object extraTitle = renderInfo.getAttributeValues().get(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
			if (null != extraTitle) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES, extraTitle);
			}
		}
	}
	
	/**
	 * Metodo che determina con che ordine viene ricercato l'identificativo del contenuto.
	 * L'ordine con cui viene cercato è questo:
	 * 1) Nel parametro specificato all'interno del tag.
	 * 2) Tra i parametri di configurazione del widget
	 * 3) Nella Request.
	 * @param contentId L'identificativo del contenuto specificato nel tag. 
	 * Può essere null o una Stringa alfanumerica.
	 * @param widgetConfig I parametri di configurazione del widget corrente.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'identificativo del contenuto da erogare.
	 */
	protected String extractContentId(String contentId, ApsProperties widgetConfig, RequestContext reqCtx) {
		if (null == contentId) {
			if (null != widgetConfig) {
				contentId = (String) widgetConfig.get("contentId");
			}
			if (null == contentId) {
				contentId = reqCtx.getRequest().getParameter(SystemConstants.K_CONTENT_ID_PARAM);
			}
		}
		if (null != contentId && contentId.trim().length() == 0) {
			contentId = null;
		}
		return contentId;
	}
	
	/**
	 * Restituisce l'identificativo del modello 
	 * con il quale renderizzare il contenuto.
	 * Metodo che determina con che ordine viene ricercato 
	 * l'identificativo del modello di contenuto.
	 * L'ordine con cui viene cercato è questo:
	 * 1) Nel parametro specificato all'interno del tag.
	 * 2) Tra i parametri di configurazione del widget
	 * Nel caso non venga trovato nessun ideentificativo, viene restituito l'identificativo 
	 * del modello di default specificato nella configurazione del tipo di contenuto.
	 * @param contentId L'identificativo del contenuto da erogare.
	 * Può essere null, un numero in forma di stringa, o un'identificativo della tipologia del modello 'list' 
	 * (in tal caso viene restituito il modello per le liste definito nella configurazione del tipo di contenuto) 
	 * o 'default' (in tal caso viene restituito il modello di default definito nella configurazione del tipo di contenuto).
	 * @param modelId L'identificativo del modello specificato nel tag. Può essere null.
	 * @param widgetConfig La configurazione del widget corrente 
	 * nel qual è inserito il tag erogatore del contenuti.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'identificativo del modello 
	 * con il quale renderizzare il contenuto.
	 */
	protected String extractModelId(String contentId, String modelId, ApsProperties widgetConfig, RequestContext reqCtx) {
		modelId = this.extractConfiguredModelId(contentId, modelId, widgetConfig);
		if (null == modelId) {
			modelId = reqCtx.getRequest().getParameter("modelId");
		}
		if (null == modelId && null != contentId) {
			modelId = this.getContentManager().getDefaultModel(contentId);
		}
		return modelId;
	}
	
	protected String extractModelId(String contentId, String modelId, ApsProperties widgetConfig) {
		modelId = this.extractConfiguredModelId(contentId, modelId, widgetConfig);
		if (null == modelId && null != contentId) {
			modelId = this.getContentManager().getDefaultModel(contentId);
		}
		return modelId;
	}
	
	private String extractConfiguredModelId(String contentId, String modelId, ApsProperties widgetConfig) {
		if (null != modelId && null != contentId) {
			if (modelId.equals("list")) {
				modelId = this.getContentManager().getListModel(contentId);
			}
			if (null != modelId && modelId.equals("default")) {
				modelId = this.getContentManager().getDefaultModel(contentId);
			}
		}
		if (null == modelId && null != widgetConfig) {
			modelId = (String) widgetConfig.get("modelId");
		}
		return modelId;
	}
    
	protected void setStylesheet(long modelId, RequestContext reqCtx) {
		ContentModel model = this.getContentModelManager().getContentModel(modelId);
	    if (model != null) {
	    	String stylesheet = model.getStylesheet();
	    	if (null != stylesheet && stylesheet.trim().length()>0) {
	    		HeadInfoContainer headInfo  = (HeadInfoContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
	    		if (headInfo != null) {
	    			headInfo.addInfo("CSS", stylesheet);
	    		}
	    	}
	    }
    }
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IContentDispenser getContentDispenser() {
		return _contentDispenser;
	}
	public void setContentDispenser(IContentDispenser contentDispenser) {
		this._contentDispenser = contentDispenser;
	}
	
	protected IContentAuthorizationHelper getContentAuthorizationHelper() {
		return _contentAuthorizationHelper;
	}
	public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
		this._contentAuthorizationHelper = contentAuthorizationHelper;
	}
	
	private IContentModelManager _contentModelManager;
    private IContentManager _contentManager;
    private IContentDispenser _contentDispenser;
	
	private IContentAuthorizationHelper _contentAuthorizationHelper;
	
}