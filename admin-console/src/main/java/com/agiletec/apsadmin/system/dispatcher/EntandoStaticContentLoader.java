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
package com.agiletec.apsadmin.system.dispatcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.DefaultStaticContentLoader;

/**
 * <b>Entando implementation of struts static content loader</b>
 * This class is used to serve common static content needed when using various parts of Struts, such as JavaScript
 * files, CSS files, etc. It works by looking for requests to /struts/* (or /static/*), and then mapping the value after "/struts/"
 * to common packages in Struts and, optionally, in your class path.
 * This class extends the Static loader of Struts2 to manage null path prefixes.
 * @author E.Santoboni
 */
public class EntandoStaticContentLoader extends DefaultStaticContentLoader {
	
	@Override
	public void findStaticResource(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (null == super.pathPrefixes) {
			String packages = super.getAdditionalPackages();
			super.pathPrefixes = super.parse(packages);
		}
		super.findStaticResource(path, request, response);
	}
	
}
