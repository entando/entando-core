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
package org.entando.entando.aps.system.services.dataobject.widget;

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
import org.entando.entando.aps.system.services.dataobject.helper.PublicDataTypeAuthorizationInfo;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectdispenser.DataObjectRenderizationInfo;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.dataobject.helper.IDataAuthorizationHelper;
import org.entando.entando.aps.system.services.dataobjectdispenser.IDataObjectDispenser;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * Classe helper per i Widget di erogazione dataobject singoli.
 *
 * @author E.Santoboni
 */
public class DataObjectViewerHelper implements IDataObjectViewerHelper {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectViewerHelper.class);

	@Override
	public String getRenderedContent(String dataobjectId, String modelId, RequestContext reqCtx) throws ApsSystemException {
		return this.getRenderedContent(dataobjectId, modelId, false, reqCtx);
	}

	/**
	 * Restituisce il dataObject da visualizzare nel widget.
	 *
	 * @param dataobjectId L'identificativo del dataObject ricavato dal tag.
	 * @param modelId Il modello del dataObject ricavato dal tag.
	 * @param publishExtraTitle
	 * @param reqCtx Il contesto della richiesta.
	 * @return Il dataObject da visualizzare nella widget.
	 * @throws ApsSystemException In caso di errore.
	 */
	@Override
	public String getRenderedContent(String dataobjectId, String modelId, boolean publishExtraTitle, RequestContext reqCtx)
			throws ApsSystemException {
		String renderedDataObject = null;
		DataObjectRenderizationInfo renderInfo = this.getRenderizationInfo(dataobjectId, modelId, publishExtraTitle, reqCtx);
		if (null != renderInfo) {
			renderedDataObject = renderInfo.getRenderedDataobject();
		}
		if (null == renderedDataObject) {
			renderedDataObject = "";
		}
		return renderedDataObject;
	}

	@Override
	public DataObjectRenderizationInfo getRenderizationInfo(String dataobjectId, String modelId, boolean publishExtraTitle, RequestContext reqCtx)
			throws ApsSystemException {
		DataObjectRenderizationInfo renderizationInfo = null;
		try {
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			String langCode = currentLang.getCode();
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			ApsProperties widgetConfig = widget.getConfig();
			dataobjectId = this.extractContentId(dataobjectId, widgetConfig, reqCtx);
			modelId = this.extractModelId(dataobjectId, modelId, widgetConfig, reqCtx);
			if (dataobjectId != null && modelId != null) {
				long longModelId = new Long(modelId).longValue();
				this.setStylesheet(longModelId, reqCtx);
				renderizationInfo = this.getDataObjectDispenser().getRenderizationInfo(dataobjectId, longModelId, langCode, reqCtx, true);
				if (null == renderizationInfo) {
					_logger.info("Null Renderization informations: dataobject={}", dataobjectId);
					return null;
				}
				this.manageAttributeValues(renderizationInfo, publishExtraTitle, reqCtx);
			} else {
				_logger.warn("Parametri visualizzazione dataobject incompleti: dataobject={} modello={}", dataobjectId, modelId);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting renderization info", t);
			throw new ApsSystemException("Error extracting renderization info", t);
		}
		return renderizationInfo;
	}

	@Override
	public PublicDataTypeAuthorizationInfo getAuthorizationInfo(String dataobjectId, RequestContext reqCtx) throws ApsSystemException {
		PublicDataTypeAuthorizationInfo authInfo = null;
		try {
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			dataobjectId = this.extractContentId(dataobjectId, widget.getConfig(), reqCtx);
			if (null == dataobjectId) {
				_logger.info("Null dataobjectId");
				return null;
			}
			authInfo = this.getDataAuthorizationHelper().getAuthorizationInfo(dataobjectId, true);
			if (null == authInfo) {
				_logger.info("Null authorization info by dataobject '" + dataobjectId + "'");
			}
		} catch (Throwable t) {
			_logger.error("Error extracting dataobject authorization info by dataobject {}", dataobjectId, t);
			throw new ApsSystemException("Error extracting dataobject authorization info by dataobject '" + dataobjectId + "'", t);
		}
		return authInfo;
	}

	protected void manageAttributeValues(DataObjectRenderizationInfo renderInfo, boolean publishExtraTitle, RequestContext reqCtx) {
		if (!publishExtraTitle) {
			return;
		}
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		if (!page.isUseExtraTitles()) {
			return;
		}
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		if (currentFrame == page.getMetadata().getModel().getMainFrame() && null != renderInfo) {
			Object extraTitle = renderInfo.getAttributeValues().get(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
			if (null != extraTitle) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES, extraTitle);
			}
		}
	}

	/**
	 * Metodo che determina con che ordine viene ricercato l'identificativo del
	 * dataobject. L'ordine con cui viene cercato è questo: 1) Nel parametro
	 * specificato all'interno del tag. 2) Tra i parametri di configurazione del
	 * widget 3) Nella Request.
	 *
	 * @param dataobjectId L'identificativo del dataobject specificato nel tag.
	 * Può essere null o una Stringa alfanumerica.
	 * @param widgetConfig I parametri di configurazione del widget corrente.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'identificativo del dataobject da erogare.
	 */
	protected String extractContentId(String dataobjectId, ApsProperties widgetConfig, RequestContext reqCtx) {
		if (null == dataobjectId) {
			if (null != widgetConfig) {
				dataobjectId = (String) widgetConfig.get("contentId");
			}
			if (null == dataobjectId) {
				dataobjectId = reqCtx.getRequest().getParameter(SystemConstants.K_CONTENT_ID_PARAM);
			}
		}
		if (null != dataobjectId && dataobjectId.trim().length() == 0) {
			dataobjectId = null;
		}
		return dataobjectId;
	}

	/**
	 * Restituisce l'identificativo del modello con il quale renderizzare il
	 * dataobject. Metodo che determina con che ordine viene ricercato
	 * l'identificativo del modello di dataobject. L'ordine con cui viene
	 * cercato è questo: 1) Nel parametro specificato all'interno del tag. 2)
	 * Tra i parametri di configurazione del widget Nel caso non venga trovato
	 * nessun identificativo, viene restituito l'identificativo del modello di
	 * default specificato nella configurazione del tipo di dataobject.
	 *
	 * @param dataobjectId L'identificativo del dataobject da erogare. Può
	 * essere null, un numero in forma di stringa, o un'identificativo della
	 * tipologia del modello 'list' (in tal caso viene restituito il modello per
	 * le liste definito nella configurazione del tipo di dataobject) o
	 * 'default' (in tal caso viene restituito il modello di default definito
	 * nella configurazione del tipo di dataobject).
	 * @param modelId L'identificativo del modello specificato nel tag. Può
	 * essere null.
	 * @param widgetConfig La configurazione del widget corrente nel qual è
	 * inserito il tag erogatore del dataobject.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'identificativo del modello con il quale renderizzare il
	 * dataobject.
	 */
	protected String extractModelId(String dataobjectId, String modelId, ApsProperties widgetConfig, RequestContext reqCtx) {
		modelId = this.extractConfiguredModelId(dataobjectId, modelId, widgetConfig);
		if (null == modelId) {
			modelId = reqCtx.getRequest().getParameter("modelId");
		}
		if (null == modelId && null != dataobjectId) {
			modelId = this.getDataObjectManager().getDefaultModel(dataobjectId);
		}
		return modelId;
	}

	protected String extractModelId(String dataobjectId, String modelId, ApsProperties widgetConfig) {
		modelId = this.extractConfiguredModelId(dataobjectId, modelId, widgetConfig);
		if (null == modelId && null != dataobjectId) {
			modelId = this.getDataObjectManager().getDefaultModel(dataobjectId);
		}
		return modelId;
	}

	private String extractConfiguredModelId(String dataobjectId, String modelId, ApsProperties widgetConfig) {
		if (null != modelId && null != dataobjectId) {
			if (modelId.equals("list")) {
				modelId = this.getDataObjectManager().getListModel(dataobjectId);
			}
			if (null != modelId && modelId.equals("default")) {
				modelId = this.getDataObjectManager().getDefaultModel(dataobjectId);
			}
		}
		if (null == modelId && null != widgetConfig) {
			modelId = (String) widgetConfig.get("modelId");
		}
		return modelId;
	}

	protected void setStylesheet(long modelId, RequestContext reqCtx) {
		DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelId);
		if (model != null) {
			String stylesheet = model.getStylesheet();
			if (null != stylesheet && stylesheet.trim().length() > 0) {
				HeadInfoContainer headInfo = (HeadInfoContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
				if (headInfo != null) {
					headInfo.addInfo("CSS", stylesheet);
				}
			}
		}
	}

	public IDataObjectModelManager getDataObjectModelManager() {
		return _dataObjectModelManager;
	}

	public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
		this._dataObjectModelManager = dataObjectModelManager;
	}

	public IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	public IDataObjectDispenser getDataObjectDispenser() {
		return _dataObjectDispenser;
	}

	public void setDataObjectDispenser(IDataObjectDispenser dataObjectDispenser) {
		this._dataObjectDispenser = dataObjectDispenser;
	}

	public IDataAuthorizationHelper getDataAuthorizationHelper() {
		return _dataAuthorizationHelper;
	}

	public void setDataAuthorizationHelper(IDataAuthorizationHelper dataAuthorizationHelper) {
		this._dataAuthorizationHelper = dataAuthorizationHelper;
	}

	private IDataObjectModelManager _dataObjectModelManager;
	private IDataObjectManager _dataObjectManager;
	private IDataObjectDispenser _dataObjectDispenser;

	private IDataAuthorizationHelper _dataAuthorizationHelper;

}
