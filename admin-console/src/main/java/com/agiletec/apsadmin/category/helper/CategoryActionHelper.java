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
package com.agiletec.apsadmin.category.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.TreeNodeBaseActionHelper;

/**
 * This Helper class provides support for categories management.
 * @author E.Santoboni
 */
public class CategoryActionHelper extends TreeNodeBaseActionHelper implements ICategoryActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(CategoryActionHelper.class);
	
	@Override
	public Map getReferencingObjects(Category category, HttpServletRequest request) throws ApsSystemException {
    	Map<String, List> references = new HashMap<String, List>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(CategoryUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error checking Referencing Objects", t);
					service = null;
				}
				if (service != null) {
					CategoryUtilizer categoryUtilizer = (CategoryUtilizer) service;
					List utilizers = categoryUtilizer.getCategoryUtilizers(category.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(categoryUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
    		throw new ApsSystemException("Errore in hasReferencingObjects", t);
    	}
    	return references;
    }

	@Override
	public Map getReferencingObjectsForMove(Category category, HttpServletRequest request) throws ApsSystemException {
		Map<String, List> references = new HashMap<String, List>();
		try {
			String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(CategoryUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error checking Referencing Objects", t);
					service = null;
				}
				if (service != null) {
					CategoryUtilizer categoryUtilizer = (CategoryUtilizer) service;
					List utilizers = categoryUtilizer.getCategoryUtilizersForReloadReferences(category.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(categoryUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Error in getReferencingObjectsForMove", t);
		}
		return references;
	}
	
	@Override
	public Category buildNewCategory(String code, String parentCode, ApsProperties titles) throws ApsSystemException {
		Category category = new Category();
		try {
			category.setParentCode(parentCode);
			category.setTitles(titles);
			String newCategoryCode = code;
			if (null != newCategoryCode && newCategoryCode.trim().length() > 0) {
				category.setCode(newCategoryCode);
			} else {
				String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
				String defaultTitle = category.getTitles().getProperty(defaultLangCode);
				String categoryCode = this.buildCode(defaultTitle, "category", 25);
				category.setCode(categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("Error creating new category", t);
			throw new ApsSystemException("Error creating new category", t);
		}
		return category;
	}
	
	@Override
	protected ITreeNode getTreeNode(String code) {
		return this.getCategoryManager().getCategory(code);
	}
	
	@Override
	protected ITreeNode getRoot() {
		return (ITreeNode) this.getCategoryManager().getRoot();
	}
	
	@Override
	protected boolean isNodeAllowed(String code, Collection<String> groupCodes) {
		return true;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	private ICategoryManager _categoryManager;
	
}
