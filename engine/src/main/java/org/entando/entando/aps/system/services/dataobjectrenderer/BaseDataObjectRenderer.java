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
package org.entando.entando.aps.system.services.dataobjectrenderer;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.renderer.BaseEntityRenderer;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import com.agiletec.aps.system.common.renderer.TextAttributeCharReplaceInfo;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.I18nManagerWrapper;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;

/**
 * Servizio di renderizzazione DataObject.
 *
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public class BaseDataObjectRenderer extends BaseEntityRenderer implements IDataObjectRenderer {

	private static final Logger _logger = LoggerFactory.getLogger(BaseDataObjectRenderer.class);

	@Override
	public String render(DataObject dataobject, long modelId, String langCode, RequestContext reqCtx) {
		String renderedEntity = null;
		List<TextAttributeCharReplaceInfo> conversions = null;
		try {
			conversions = this.convertSpecialCharacters(dataobject, langCode);
			String contentModel = this.getModelShape(modelId);
			Context velocityContext = new VelocityContext();
			DataObjectWrapper contentWrapper = (DataObjectWrapper) this.getEntityWrapper(dataobject);
			contentWrapper.setRenderingLang(langCode);
			contentWrapper.setReqCtx(reqCtx);
			velocityContext.put(this.getEntityWrapperContextName(), contentWrapper);
			I18nManagerWrapper i18nWrapper = new I18nManagerWrapper(langCode, this.getI18nManager());
			velocityContext.put("i18n", i18nWrapper);
			SystemInfoWrapper systemInfoWrapper = new SystemInfoWrapper(reqCtx);
			velocityContext.put("info", systemInfoWrapper);
			StringWriter stringWriter = new StringWriter();
			boolean isEvaluated = Velocity.evaluate(velocityContext, stringWriter, "render", contentModel);
			if (!isEvaluated) {
				throw new ApsSystemException("Error rendering DataObject");
			}
			stringWriter.flush();
			renderedEntity = stringWriter.toString();
		} catch (Throwable t) {
			_logger.error("Error rendering dataobject", t);
			renderedEntity = "";
		} finally {
			if (null != conversions) {
				this.replaceSpecialCharacters(conversions);
			}
		}
		return renderedEntity;
	}

	@Override
	protected EntityWrapper getEntityWrapper(IApsEntity entity) {
		return new DataObjectWrapper((DataObject) entity, this.getBeanFactory());
	}

	protected String getModelShape(long modelId) {
		DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelId);
		String shape = null;
		if (model != null) {
			shape = model.getShape();
		}
		if (shape == null) {
			shape = "DataObject model " + modelId + " undefined";
			_logger.error("DataObject model {} undefined", modelId);
		}
		return shape;
	}

	@Override
	protected String getEntityWrapperContextName() {
		return "data";
	}

	protected IDataObjectModelManager getDataObjectModelManager() {
		return _dataObjectModelManager;
	}

	public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
		this._dataObjectModelManager = dataObjectModelManager;
	}

	private IDataObjectModelManager _dataObjectModelManager;

}
