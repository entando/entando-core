/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.apsadmin.system.tiles.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.tiles.impl.BasicTilesContainer;

/**
 * @see org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener
 * @author zuanni G.Cocco
 */
public class EntandoBasicTilesContainer extends BasicTilesContainer {
	
	@Override
	public List<String> getResourceNames(String resourceString) {
		StringTokenizer tokenizer = new StringTokenizer(resourceString, ",");
		List<String> filenames = new ArrayList<String>();
		System.out.println(EntandoBasicTilesContainer.class.getName() + " Tiles Conf files: ");
		while (tokenizer.hasMoreTokens()) {
			String currentFilename = tokenizer.nextToken().trim();
			int index = currentFilename.indexOf(AXTER);
			if (-1 == index) {
				filenames.add(currentFilename);
				System.out.println(EntandoBasicTilesContainer.class.getName() + " " + currentFilename);
			} else {
				List<String> confFiles = new ArrayList<String>();
				String rootInspectionDir = currentFilename.substring(0, index);
				this.inspectResources(currentFilename, rootInspectionDir, confFiles);
				filenames.addAll(confFiles);
			}
		}
		return filenames;
	}
	
	private void inspectResources(String currentFilenamesConf, String rootInspectionDir, List<String> confFiles) {
		Set<String> resourcesPath = this.getServletContext().getResourcePaths(rootInspectionDir);
		if (null != resourcesPath) {
			Iterator<String> it = resourcesPath.iterator();
			while (it.hasNext()) {
				String current = it.next();
				if (!current.endsWith("/") && this.isConfResource(current, currentFilenamesConf)) {
					confFiles.add(current);
					System.out.println(EntandoBasicTilesContainer.class.getName() + " " + current);
				} else {
					this.inspectResources(currentFilenamesConf, current, confFiles);
				}
			}
		}
	}
	
	private boolean isConfResource(String current, String currentFilename) {
		String regExp = currentFilename.replaceAll(AXTER_REG_EXP, REG_EXP);
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(current);
		return m.matches();
	}
	
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	public ServletContext getServletContext() {
		return _servletContext;
	}
	
	private ServletContext _servletContext;
	
	private final static String REG_EXP = "[\\\\w,\\\\-,_]*";
	private final static String AXTER = "**";
	private final static String AXTER_REG_EXP = "\\*\\*";
	
}
