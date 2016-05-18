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
package com.agiletec.apsadmin.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletRequest;

/**
 * @author E.Santoboni
 */
public class ApsRequestParamsUtil {
	
	protected static String[] splitParam(String pname, String separator) {
        return purgeParameter(pname).split(separator);
	}
	
	protected static String purgeParameter(String pname) {
        if (pname.endsWith(".x") || pname.endsWith(".y")) {
        	return pname.substring(0, pname.length()-2);
        }
        return pname;
	}
	
	public static String[] getApsParams(String paramPrefix, String separator, ServletRequest request) {
		String[] apsParams = null;
		Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
        	String pname = (String) params.nextElement();
        	if (pname.startsWith(ACTION_PREFIX)) {
        		pname = pname.substring(ACTION_PREFIX.length());
        	}
			if (pname.startsWith(ENTANDO_ACTION_PREFIX)) {
        		pname = pname.substring(ENTANDO_ACTION_PREFIX.length());
        	}
        	if (pname.startsWith(paramPrefix)) {
        		apsParams = splitParam(pname, separator);
        		break;
        	}
        }
        return apsParams;
	}
	
	public static String createApsActionParam(String action, Properties params) {
		StringBuilder buffer = new StringBuilder(action);
		if (params.size()>0) {
			buffer.append("?");
			Iterator keys = params.keySet().iterator();
			boolean first = true;
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = params.getProperty(key);
				if (!first) buffer.append(";");
				buffer.append(key).append("=").append(value);
				first = false;
			}
		}
		return buffer.toString();
	}
	
	public static String extractEntandoActionName(ServletRequest request) {
		String entandoActionName = null;
		Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
        	String pname = (String) params.nextElement();
        	if (pname.startsWith(ACTION_PREFIX)) {
        		entandoActionName = pname.substring(ACTION_PREFIX.length());
        		break;
        	}
        	if (pname.startsWith(ENTANDO_ACTION_PREFIX)) {
        		entandoActionName = pname.substring(ENTANDO_ACTION_PREFIX.length());
        		break;
        	}
        }
		if (null != entandoActionName) {
			entandoActionName = purgeParameter(entandoActionName);
		}
		return entandoActionName;
	}
	
	public static Properties extractApsActionParameters(String entandoActionName) {
		Properties properties = new Properties();
		if (entandoActionName.contains("#")) {
			entandoActionName = entandoActionName.substring(0, entandoActionName.indexOf("#"));
		}
		String[] blocks = entandoActionName.split("[?]");
		if (blocks.length == 2) {
			String paramBlock = blocks[1];
			String[] params = paramBlock.split(";");
			for (int i=0; i<params.length; i++) {
				String[] parameter = params[i].split("=");
				if (parameter.length == 2) {
					properties.put(parameter[0], parameter[1]);
				}
			}
		}
		return properties;
	}
	
	public static final String ACTION_PREFIX = "action:";
	public static final String ENTANDO_ACTION_PREFIX = "entandoaction:";
	
}
