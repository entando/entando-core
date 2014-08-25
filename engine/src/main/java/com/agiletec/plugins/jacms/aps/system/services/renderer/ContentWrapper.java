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
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * Rappresenta un contenuto nella forma utilizzabile al servizio di renderizzazione. 
 * La classe estende HashMap per un agevole accesso agli attributi che
 * popolano il contenuto.
 * @author M.Diana - E.Santoboni
 */
public class ContentWrapper extends EntityWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentWrapper.class);
	
	/**
	 * Inizializzazione del Wrapper. 
	 * @param content Il contenuto da utilizzare 
	 * dal servizio di renderizzazione. 
	 */
	public ContentWrapper(Content content) {
		super(content);
	}
	
	public ContentWrapper(Content content, BeanFactory beanFactory) {
		super(content, beanFactory);
	}
	
    public boolean isUserAllowed(String permissionName) {
        try {
            IAuthorizationManager authManager = 
                (IAuthorizationManager) this.getBeanFactory().getBean(SystemConstants.AUTHORIZATION_SERVICE);
            UserDetails currentUser = (UserDetails) this.getReqCtx().getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            if (null == currentUser) return false;
            if (!authManager.isAuthOnGroup(currentUser, this.getEntity().getMainGroup())) return false;
            if (null != permissionName && permissionName.trim().length() > 0 && !authManager.isAuthOnPermission(currentUser, permissionName)) return false;
        } catch (Throwable t) {
        	_logger.error("Error checking authority - permission {}", permissionName, t);
            //ApsSystemUtils.logThrowable(t, this, "isUserAllowed", "Error checking authority - permission " + permissionName);
			return false;
        }
        return true;
    }
	
    /**
	 * Return the value of a System parameter.
	 * @param paramName The name of parameters
	 * @return The value to return
	 * @deprecated this method has to be moved outside Content Wrapper
	 */
    public String getConfigParameter(String paramName) {
		try {
            ConfigInterface configManager = (ConfigInterface) this.getBeanFactory().getBean(SystemConstants.BASE_CONFIG_MANAGER);
            return configManager.getParam(paramName);
        } catch (Throwable t) {
        	_logger.error("Error extracting config parameter - parameter ", paramName, t);
            //ApsSystemUtils.logThrowable(t, this, "getConfigParameter", "Error extracting config parameter - parameter " + paramName);
			return null;
        }
    }
	
	public String getLangCode() {
        return super.getRenderingLang();
    }
    
	/**
	 * Restituisce un URL simbolico che punta al contenuto stesso (link di 
	 * tipo SymbolicLink.CONTENT_TYPE).
	 * @return Un URL simbolico da utilizzare come href in un tag &lt;a&gt;
	 */
	public String getContentLink() {
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContent(this.getId());
		return link.getSymbolicDestination();
	}
	
	/**
	 * Restituisce un URL simbolico che punta al contenuto stesso su una pagina specficata 
	 * (link di tipo SymbolicLink.CONTENT_ON_PAGE_TYPE).
	 * @param pageCode Il codice della pagina su cui visualizzare il contenuto.
	 * @return Un URL simbolico da utilizzare come href in un tag &lt;a&gt;
	 */
	public String getContentOnPageLink(String pageCode) {
		SymbolicLink symbLink = new SymbolicLink();
		symbLink.setDestinationToContentOnPage(this.getId(), pageCode);
		return symbLink.getSymbolicDestination();
	}
	
	public String getCreated(String pattern) {
		Content content = (Content) super.getEntity();
		if (null != content.getCreated()) {
			return DateConverter.getFormattedDate(content.getCreated(), pattern, this.getRenderingLang());
		}
		return null;
	}
	
	public String getLastModified(String pattern) {
		Content content = (Content) super.getEntity();
		if (null != content.getLastModified()) {
			return DateConverter.getFormattedDate(content.getLastModified(), pattern, this.getRenderingLang());
		}
		return null;
	}
	
	public String getVersion() {
		return ((Content) super.getEntity()).getVersion();
	}
	
	public String getLastEditor() {
		return ((Content) super.getEntity()).getLastEditor();
	}
	
    protected RequestContext getReqCtx() {
        return _reqCtx;
    }
    protected void setReqCtx(RequestContext reqCtx) {
        this._reqCtx = reqCtx;
    }
    
	private RequestContext _reqCtx;
	
}
