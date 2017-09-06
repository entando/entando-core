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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Rappresenta un DataObject nella forma utilizzabile al servizio di
 * renderizzazione. La classe estende HashMap per un agevole accesso agli
 * attributi che popolano il dataobject.
 *
 * @author M.Diana - E.Santoboni
 */
public class DataObjectWrapper extends EntityWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectWrapper.class);

	/**
	 * Inizializzazione del Wrapper.
	 *
	 * @param dataobject Il dataobject da utilizzare dal servizio di
	 * renderizzazione.
	 */
	public DataObjectWrapper(DataObject dataobject) {
		super(dataobject);
	}

	public DataObjectWrapper(DataObject dataobject, BeanFactory beanFactory) {
		super(dataobject, beanFactory);
	}

	public boolean isUserAllowed(String permissionName) {
		try {
			IAuthorizationManager authManager
					= (IAuthorizationManager) this.getBeanFactory().getBean(SystemConstants.AUTHORIZATION_SERVICE);
			UserDetails currentUser = (UserDetails) this.getReqCtx().getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == currentUser) {
				return false;
			}
			if (!authManager.isAuthOnGroup(currentUser, this.getEntity().getMainGroup())) {
				return false;
			}
			if (null != permissionName && permissionName.trim().length() > 0 && !authManager.isAuthOnPermission(currentUser, permissionName)) {
				return false;
			}
		} catch (Throwable t) {
			_logger.error("Error checking authority - permission {}", permissionName, t);
			return false;
		}
		return true;
	}

	/**
	 * Return the value of a System parameter.
	 *
	 * @param paramName The name of parameters
	 * @return The value to return
	 * @deprecated this method has to be moved outside dataobject Wrapper
	 */
	public String getConfigParameter(String paramName) {
		try {
			ConfigInterface configManager = (ConfigInterface) this.getBeanFactory().getBean(SystemConstants.BASE_CONFIG_MANAGER);
			return configManager.getParam(paramName);
		} catch (Throwable t) {
			_logger.error("Error extracting config parameter - parameter ", paramName, t);
			return null;
		}
	}

	public String getLangCode() {
		return super.getRenderingLang();
	}

	public String getCreated(String pattern) {
		DataObject dataobject = (DataObject) super.getEntity();
		if (null != dataobject.getCreated()) {
			return DateConverter.getFormattedDate(dataobject.getCreated(), pattern, this.getRenderingLang());
		}
		return null;
	}

	public String getLastModified(String pattern) {
		DataObject dataobject = (DataObject) super.getEntity();
		if (null != dataobject.getLastModified()) {
			return DateConverter.getFormattedDate(dataobject.getLastModified(), pattern, this.getRenderingLang());
		}
		return null;
	}

	public String getVersion() {
		return ((DataObject) super.getEntity()).getVersion();
	}

	public String getLastEditor() {
		return ((DataObject) super.getEntity()).getLastEditor();
	}

	protected RequestContext getReqCtx() {
		return _reqCtx;
	}

	protected void setReqCtx(RequestContext reqCtx) {
		this._reqCtx = reqCtx;
	}

	private RequestContext _reqCtx;

}
