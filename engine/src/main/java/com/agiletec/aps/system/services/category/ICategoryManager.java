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
package com.agiletec.aps.system.services.category;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Base interface for the manager class for handling categories,
 *
 * @author E.Santoboni
 */
public interface ICategoryManager extends ITreeNodeManager {

	/**
	 * Add a category.
	 *
	 * @param category The category to add
	 * @throws ApsSystemException In case of error.
	 */
	public void addCategory(Category category) throws ApsSystemException;

	/**
	 * Cancella una categoria.
	 *
	 * @param code Il codice della categoria da eliminare.
	 * @throws ApsSystemException In case of error.
	 */
	public void deleteCategory(String code) throws ApsSystemException;

	/**
	 * Update a category.
	 *
	 * @param category The category to add.
	 * @throws ApsSystemException In case of error.
	 */
	public void updateCategory(Category category) throws ApsSystemException;

	/**
	 * Return the root of the category tree.
	 *
	 * @return The category root.
	 */
	@Override
	public Category getRoot();

	/**
	 * Return a category by the code.
	 *
	 * @param categoryCode the code of the category to return.
	 * @return The required category
	 */
	public Category getCategory(String categoryCode);

	/**
	 * Returns a list of available categories, sorted according to the category
	 * tree hierarchy. The root category is not included in the list.
	 *
	 * @return The list of available categories.
	 */
	public List<Category> getCategoriesList();

	/**
	 * Search categories by a token of its code.
	 *
	 * @param categoryCodeToken The token containing to be looked up across the
	 * categories.
	 * @return A list of candidates containing the given token. If the
	 * categoryCodeToken is null then this method will return a set containing
	 * all the pages.
	 * @throws ApsSystemException in case of error.
	 */
	public List<Category> searchCategories(String categoryCodeToken) throws ApsSystemException;

	/**
	 * Moves a category under another node
	 *
	 * @param currentCategory the category to move
	 * @param newParent the new parent
	 * @return true if the the operation succeeds
	 */
	public boolean moveCategory(Category currentCategory, Category newParent) throws ApsSystemException;

	public int getMoveTreeStatus();

	public Map<String, Integer> getReloadStatus();

	public static final String RELOAD_CATEGORY_REFERENCES_THREAD_NAME_PREFIX = "RELOAD_CATEGORY_REFERENCES_";

	public static final int STATUS_RELOADING_REFERENCES_IN_PROGRESS = 1;

	public static final int STATUS_READY = 0;

}
