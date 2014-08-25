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
package com.agiletec.apsadmin.system.services.shortcut;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.apsadmin.system.services.shortcut.model.MenuSection;
import com.agiletec.apsadmin.system.services.shortcut.model.Shortcut;

/**
 * Shortcut Loader Class.
 * @author E.Santoboni
 */
public class ShortcutLoader {

	private static final Logger _logger = LoggerFactory.getLogger(ShortcutLoader.class);
	
	protected ShortcutLoader(String locationPatterns, ServletContext servletContext) throws ApsSystemException {
		this.setSectionMenus(new HashMap<String, MenuSection>());
		this.setShortcuts(new HashMap<String, Shortcut>());
		try {
			StringTokenizer tokenizer = new StringTokenizer(locationPatterns, ",");
			while (tokenizer.hasMoreTokens()) {
				String locationPattern = tokenizer.nextToken().trim();
				this.loadShortcutObjects(locationPattern, servletContext);
			}
			this.completeLoading();
		} catch (Throwable t) {
			_logger.error("Error loading Shortcut definitions", t);
			//ApsSystemUtils.logThrowable(t, this, "ShortcutLoader", "Error loading Shortcut definitions");
			throw new ApsSystemException("Error loading Shortcut definitions", t);
		}
	}
	
	private void loadShortcutObjects(String locationPattern, ServletContext servletContext) throws Exception {
		Resource[] resources = ApsWebApplicationUtils.getResources(locationPattern, servletContext);
		ShortcutDefDOM dom = null;
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			InputStream is = null;
			try {
				String path = resource.getFilename();
				is = resource.getInputStream();
				String xml = FileTextReader.getText(is);
				dom = new ShortcutDefDOM(xml, path);
				this.getManuSections().putAll(dom.getSectionMenus());
				this.getShortcuts().putAll(dom.getShortcuts());
				_logger.info("Loaded Shortcut definition by file {}", path);
			} catch (Throwable t) {
				_logger.error("Error loading Shortcut definition by file {}", locationPattern, t);
				//ApsSystemUtils.logThrowable(t, this, "loadShortcuts", "Error loading Shortcut definition by file " + locationPattern);
			} finally {
				if (null != is) {
					is.close();
				}
			}
		}
	}
	
	private void completeLoading() {
		Iterator<Shortcut> shorCutIter = this.getShortcuts().values().iterator();
		while (shorCutIter.hasNext()) {
			Shortcut shortcut = shorCutIter.next();
			String menuSectionCode = shortcut.getMenuSectionCode();
			MenuSection section = this.getManuSections().get(menuSectionCode);
			shortcut.setMenuSection(section);
		}
	}
	
	protected Map<String, Shortcut> getShortcuts() {
		return _shortcuts;
	}
	private void setShortcuts(Map<String, Shortcut> shortcuts) {
		this._shortcuts = shortcuts;
	}
	
	protected Map<String, MenuSection> getManuSections() {
		return _sectionMenus;
	}
	private void setSectionMenus(Map<String, MenuSection> sectionMenus) {
		this._sectionMenus = sectionMenus;
	}
	
	private Map<String, Shortcut> _shortcuts;
	private Map<String, MenuSection> _sectionMenus;
	
}
