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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author E.Santoboni
 */
public class EntandoActionUtils {
	
	public static String extractEntandoActionName(HttpServletRequest request) {
		String entandoActionName = null;
		Enumeration enumeration = request.getParameterNames();
		if (null != enumeration) {
			while (enumeration.hasMoreElements()) {
				String paramName = enumeration.nextElement().toString();
				if (paramName.startsWith("entandoaction:")) {
					entandoActionName = paramName.substring("entandoaction:".length());
					// Strip off the image button location info, if found
					if (entandoActionName.endsWith(".x") || entandoActionName.endsWith(".y")) {
						entandoActionName = entandoActionName.substring(0, entandoActionName.length() - 2);
					}
					break;
				}
			}
		}
		return entandoActionName;
	}
	
}
