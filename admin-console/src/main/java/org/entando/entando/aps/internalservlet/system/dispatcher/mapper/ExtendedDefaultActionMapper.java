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
package org.entando.entando.aps.internalservlet.system.dispatcher.mapper;

import com.opensymphony.xwork2.config.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;

/**
 * Extension of Default Action mapper for the Entando Servlet used by "Internal Servlet" functionality.
 * @author E.Santoboni
 */
public class ExtendedDefaultActionMapper extends DefaultActionMapper {
	
	@Override
	public ActionMapping getMapping(HttpServletRequest request, ConfigurationManager configManager) {
        ActionMapping mapping = new ActionMapping();
        String uri = this.getUri(request);
        int indexOfSemicolon = uri.indexOf(";");
        uri = (indexOfSemicolon > -1) ? uri.substring(0, indexOfSemicolon) : uri;
        uri = super.dropExtension(uri, mapping);
        if (uri == null) {
            return null;
        }
        super.parseNameAndNamespace(uri, mapping, configManager);
        super.handleSpecialParameters(request, mapping);
        return super.parseActionName(mapping);
    }
	
    /**
     * Gets the uri from the request
     * @param request The request
     * @return The uri
     */
    protected String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.
        //String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        if (uri == null || "".equals(uri)) {
        	uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        } else {
        	return uri.substring((request.getContextPath()+"/ExtStr2").length());
        }
        if (uri != null && !"".equals(uri)) {
            return uri;
        }
        uri = request.getRequestURI();
        uri = uri.substring(request.getContextPath().length());
        return uri;
    }
    
}
