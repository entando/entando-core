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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class SystemInfoWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(SystemInfoWrapper.class);
	
	public SystemInfoWrapper(RequestContext reqCtx) {
		this.setReqCtx(reqCtx);
	}
	
    /**
	 * Return the value of a System parameter.
	 * @param paramName The name of parameters
	 * @return The value to return
	 */
    public String getConfigParameter(String paramName) {
		try {
            ConfigInterface configManager = 
					(ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.getReqCtx().getRequest());
            return configManager.getParam(paramName);
        } catch (Throwable t) {
        	
        	_logger.error("Error extracting config parameter - parameter ", paramName, t);
        	
            //ApsSystemUtils.logThrowable(t, this, "getConfigParameter", "Error extracting config parameter - parameter " + paramName);
			return null;
        }
    }
	
    public IPage getCurrentPage() {
		try {
            IPage page = (IPage) this.getReqCtx().getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            return page;
        } catch (Throwable t) {
        	_logger.error("Error getting current page", t);
            //ApsSystemUtils.logThrowable(t, this, "getCurrentPage", "Error current page");
			return null;
        }
    }

    public IPage getPageWithWidget(String widgetCode) {
    	IPage page = null;
    	try {
            IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.getReqCtx().getRequest());
    		List<IPage> pages = pageManager.getWidgetUtilizers(widgetCode);
    		if (null != pages && !pages.isEmpty()) {
    			page = pages.get(0);
    		}
    		return page;
    	} catch (Throwable t) {
    		_logger.error("Error getting page with widget: {}", widgetCode, t);
    		//ApsSystemUtils.logThrowable(t, this, "getPageWithWidget", "Error getting page with widget: " + widgetCode);
    		return null;
    	}
    }

    public String getPageURLWithWidget(String widgetCode) {
    	String url = null;
    	try {
    		IPage page = this.getPageWithWidget(widgetCode);
    		if (null == page) return url;
    		IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.getReqCtx().getRequest());
    		PageURL pageUrl = urlManager.createURL(this.getReqCtx());
    		pageUrl.setPage(page);
    		url = pageUrl.getURL();
    	} catch (Throwable t) {
    		_logger.error("Error getting pageUrl with widget: {}", widgetCode, t);
    		//ApsSystemUtils.logThrowable(t, this, "getPageURLWithWidget", "Error getting pageUrl with widget: " + widgetCode);
    		return null;
    	}
    	return url;
    }
    
    public Lang getCurrentLang() {
		try {
            return (Lang) this.getReqCtx().getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        } catch (Throwable t) {
        	_logger.error("Error getting current lang", t);
            //ApsSystemUtils.logThrowable(t, this, "getCurrentLang", "Error current lang");
			return null;
        }
    }
	
    
    /**
		 * @deprecated Use {@link #getCurrentWidget()} instead
		 */
		public Widget getCurrentShowlet() {
			return getCurrentWidget();
		}

		public Widget getCurrentWidget() {
			try {
				return (Widget) this.getReqCtx().getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			} catch (Throwable t) {
				_logger.error("Error getting current Widget", t);
				//ApsSystemUtils.logThrowable(t, this, "getCurrentWidget", "Error current Widget");
				return null;
			}
		}
	
    protected RequestContext getReqCtx() {
        return _reqCtx;
    }
    private void setReqCtx(RequestContext reqCtx) {
        this._reqCtx = reqCtx;
    }
    
	private RequestContext _reqCtx;
	
}