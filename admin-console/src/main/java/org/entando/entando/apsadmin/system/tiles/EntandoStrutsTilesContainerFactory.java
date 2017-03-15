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
package org.entando.entando.apsadmin.system.tiles;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.struts2.tiles.StrutsTilesContainerFactory;
import org.apache.tiles.definition.DefinitionsFactory;

/**
 * @see org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener
 * @author zuanni G.Cocco - E.Santoboni
 */
public class EntandoStrutsTilesContainerFactory extends StrutsTilesContainerFactory {
	
	private ServletContext _servletContext;
	
	protected EntandoStrutsTilesContainerFactory(ServletContext servletContext) {
		this.setServletContext(servletContext);
	}
	
	@Override
	protected Set<String> getTilesDefinitions(Map<String, String> params) {
        if (params.containsKey(DefinitionsFactory.DEFINITIONS_CONFIG)) {
            return this.getResourceNames(params.get(DefinitionsFactory.DEFINITIONS_CONFIG));
        }
        return this.getResourceNames(TILES_DEFAULT_PATTERN);
    }
	
	protected Set<String> getResourceNames(String resourceString) {
		StringTokenizer tokenizer = new StringTokenizer(resourceString, ",");
		Set<String> filenames = new HashSet<String>();
		System.out.println(EntandoStrutsTilesContainerFactory.class.getName() + " Tiles Conf files: ");
		while (tokenizer.hasMoreTokens()) {
			String currentFilename = tokenizer.nextToken().trim();
			int index = currentFilename.indexOf(AXTER);
			if (-1 == index) {
				filenames.add(currentFilename);
				System.out.println(EntandoStrutsTilesContainerFactory.class.getName() + " " + currentFilename);
			} else {
				Set<String> confFiles = new HashSet<String>();
				String rootInspectionDir = currentFilename.substring(0, index);
				this.inspectResources(currentFilename, rootInspectionDir, confFiles);
				filenames.addAll(confFiles);
			}
		}
		return filenames;
	}
	
	private void inspectResources(String currentFilenamesConf, String rootInspectionDir, Set<String> confFiles) {
		Set<String> resourcesPath = this.getServletContext().getResourcePaths(rootInspectionDir);
		if (null != resourcesPath) {
			Iterator<String> it = resourcesPath.iterator();
			while (it.hasNext()) {
				String current = it.next();
				if (!current.endsWith("/") && this.isConfResource(current, currentFilenamesConf)) {
					confFiles.add(current);
					System.out.println(EntandoStrutsTilesContainerFactory.class.getName() + " " + current);
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
	
	protected void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	protected ServletContext getServletContext() {
		return _servletContext;
	}
	
	private final static String REG_EXP = "[\\\\w,\\\\-,_]*";
	private final static String AXTER = "**";
	private final static String AXTER_REG_EXP = "\\*\\*";
	
}
