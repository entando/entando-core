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
