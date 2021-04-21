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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entities rendering service.
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public class DefaultVelocityRenderer extends AbstractService implements IVelocityRenderer {

	private static final Logger _logger = LoggerFactory.getLogger(DefaultVelocityRenderer.class);
	
	@Override
	public void init() throws Exception {
        Velocity.setProperty("runtime.conversion.handler.class", "none");
        Velocity.setProperty("parser.space_gobbling", "bc");
        Velocity.setProperty("directive.if.empty_check", "false");
        Velocity.init();
		_logger.debug("{} ready.", this.getName());
	}
	
	@Override
	public String render(Object object, String velocityTemplate) {
		String renderedObject = null;
		try {
			Context velocityContext = new VelocityContext();
			velocityContext.put(this.getWrapperContextName(), object);
			StringWriter stringWriter = new StringWriter();
			boolean isEvaluated = Velocity.evaluate(velocityContext, stringWriter, "render", velocityTemplate);
			if (!isEvaluated) {
				throw new ApsSystemException("Rendering error");
			}
			stringWriter.flush();
			renderedObject = stringWriter.toString();
		} catch (Throwable t) {
			_logger.error("Rendering error, class: {} - template: {}", object.getClass().getSimpleName(), velocityTemplate, t);
			renderedObject = "";
		}
		return renderedObject;
	}
    
	protected String getWrapperContextName() {
		if (null == this._wrapperContextName) {
			return DEFAULT_WRAPPER_CTX_NAME;
		}
		return _wrapperContextName;
	}
	
	public void setWrapperContextName(String wrapperContextName) {
		this._wrapperContextName = wrapperContextName;
	}
	
	private String _wrapperContextName;
	
	protected static final String DEFAULT_WRAPPER_CTX_NAME = "object";
	
}
