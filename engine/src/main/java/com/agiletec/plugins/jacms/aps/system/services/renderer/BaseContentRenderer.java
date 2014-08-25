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
package com.agiletec.plugins.jacms.aps.system.services.renderer;

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
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * Servizio di renderizzazione contenuti.
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public class BaseContentRenderer extends BaseEntityRenderer implements IContentRenderer {

	private static final Logger _logger = LoggerFactory.getLogger(BaseContentRenderer.class);
	
	@Override
	public String render(Content content, long modelId, String langCode, RequestContext reqCtx) {
		String renderedEntity = null;
		List<TextAttributeCharReplaceInfo> conversions = null;
		try {
			conversions = this.convertSpecialCharacters(content, langCode);
			String contentModel = this.getModelShape(modelId);
			Context velocityContext = new VelocityContext();
			ContentWrapper contentWrapper = (ContentWrapper) this.getEntityWrapper(content);
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
				throw new ApsSystemException("Error rendering content");
			}
			stringWriter.flush();
			renderedEntity = stringWriter.toString();
		} catch (Throwable t) {
			_logger.error("Error rendering content", t);
			//ApsSystemUtils.logThrowable(t, this, "render", "Error rendering content");
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
		return new ContentWrapper((Content)entity, this.getBeanFactory());
	}
	
	protected String getModelShape(long modelId) {
		ContentModel model = this.getContentModelManager().getContentModel(modelId);
		String shape = null;
		if (model != null) {
			shape = model.getContentShape();
		} 
		if (shape == null) {
			shape = "Content model " + modelId + " undefined";
			_logger.error("Content model {} undefined", modelId);
		}
		return shape;
	}
	
	@Override
	protected String getEntityWrapperContextName() {
		return "content";
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	private IContentModelManager _contentModelManager;
	
}
