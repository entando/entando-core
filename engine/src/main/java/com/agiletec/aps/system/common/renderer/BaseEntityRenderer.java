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
package com.agiletec.aps.system.common.renderer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.I18nManagerWrapper;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Entities rendering service.
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public abstract class BaseEntityRenderer extends DefaultVelocityRenderer implements IEntityRenderer {

	private static final Logger _logger = LoggerFactory.getLogger(BaseEntityRenderer.class);
	
	@Override
	public String render(IApsEntity entity, String velocityTemplate, String langCode, boolean convertSpecialCharacters) {
		String renderedEntity = null;
		List<TextAttributeCharReplaceInfo> conversions = null;
		try {
			if (convertSpecialCharacters) {
				conversions = this.convertSpecialCharacters(entity, langCode);
			}
			Context velocityContext = new VelocityContext();
			EntityWrapper entityWrapper = this.getEntityWrapper(entity);
			entityWrapper.setRenderingLang(langCode);
			velocityContext.put(this.getEntityWrapperContextName(), entityWrapper);

			I18nManagerWrapper i18nWrapper = new I18nManagerWrapper(langCode, this.getI18nManager());
			velocityContext.put("i18n", i18nWrapper);
			StringWriter stringWriter = new StringWriter();
			boolean isEvaluated = Velocity.evaluate(velocityContext, stringWriter, "render", velocityTemplate);
			if (!isEvaluated) {
				throw new ApsSystemException("Rendering error");
			}
			stringWriter.flush();
			renderedEntity = stringWriter.toString();
		} catch (Throwable t) {
			_logger.error("Rendering error. entity {}", entity.getTypeCode(), t);
			//ApsSystemUtils.logThrowable(t, this, "render", "Rendering error");
			renderedEntity = "";
		} finally {
			if (convertSpecialCharacters && null != conversions) {
				this.replaceSpecialCharacters(conversions);
			}
		}
		return renderedEntity;
	}

	protected abstract EntityWrapper getEntityWrapper(IApsEntity entity);
	
	protected List<TextAttributeCharReplaceInfo> convertSpecialCharacters(IApsEntity entity, String langCode) {
		List<TextAttributeCharReplaceInfo> conversions = new ArrayList<TextAttributeCharReplaceInfo>();
		Lang defaultLang = this.getLangManager().getDefaultLang();
		EntityAttributeIterator attributeIter = new EntityAttributeIterator(entity);
		while (attributeIter.hasNext()) {
			AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
			if (currAttribute instanceof ITextAttribute) {
				String attributeLangCode = langCode;
				ITextAttribute renderizable = (ITextAttribute) currAttribute;
				if (renderizable.needToConvertSpecialCharacter()) {
					String textToConvert = renderizable.getTextForLang(attributeLangCode);
					if (null == textToConvert || textToConvert.trim().length() == 0) {
						attributeLangCode = defaultLang.getCode();
						textToConvert = renderizable.getTextForLang(attributeLangCode);
					}
					if (null != textToConvert && textToConvert.trim().length() > 0) {
						conversions.add(new TextAttributeCharReplaceInfo(renderizable, textToConvert, attributeLangCode));
						String convertedText = StringEscapeUtils.escapeHtml4(textToConvert);
						renderizable.setText(convertedText, attributeLangCode);
					}
				}
			}
		}
		return conversions;
	}
	
	protected void replaceSpecialCharacters(List<TextAttributeCharReplaceInfo> conversions) {
		if (conversions == null) return;
		for (int i = 0; i < conversions.size(); i++) {
			TextAttributeCharReplaceInfo conversion = conversions.get(i);
			conversion.restore();
		}
	}
	
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected String getEntityWrapperContextName() {
		if (null == this._entityWrapperContextName) {
			return DEFAULT_ENTITY_WRAPPER_CTX_NAME;
		}
		return _entityWrapperContextName;
	}
	
	public void setEntityWrapperContextName(String entityWrapperContextName) {
		this._entityWrapperContextName = entityWrapperContextName;
	}
	
	private II18nManager _i18nManager;
	private ILangManager _langManager;

	private String _entityWrapperContextName;
	
	protected static final String DEFAULT_ENTITY_WRAPPER_CTX_NAME = "entity";
	
}