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
