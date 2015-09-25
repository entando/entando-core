/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.system.dispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.inject.Container;

/**
 * @author E.Santoboni
 */
public class PrepareOperations extends org.apache.struts2.dispatcher.ng.PrepareOperations {
	
	public PrepareOperations(ServletContext servletContext, Dispatcher dispatcher) {
        super(servletContext, dispatcher);
		this._dispatcher = dispatcher;
		this._servletContext = servletContext;
    }
	
	@Override
	public ActionMapping findActionMapping(HttpServletRequest request, HttpServletResponse response, boolean forceLookup) {
        ActionMapping mapping = (ActionMapping) request.getAttribute(STRUTS_ACTION_MAPPING_KEY);
        if (mapping == null || forceLookup) {
            try {
				Container container = this._dispatcher.getContainer();
				ActionMapper mapper = container.getInstance(ActionMapper.class);
				String entandoActionName = EntandoActionUtils.extractEntandoActionName(request);
				mapping = mapper.getMapping(request, this._dispatcher.getConfigurationManager());
				if (null != entandoActionName) {
					mapping.setName(entandoActionName);
				}
                if (mapping != null) {
                    request.setAttribute(STRUTS_ACTION_MAPPING_KEY, mapping);
                }
            } catch (Exception ex) {
                this._dispatcher.sendError(request, response, this._servletContext, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex);
            }
        }
        return mapping;
    }
	
	public Dispatcher getDispatcher() {
		return this._dispatcher;
	}
	
	private Dispatcher _dispatcher;
	private ServletContext _servletContext;
	
	private static final String STRUTS_ACTION_MAPPING_KEY = "struts.actionMapping";
	
}
