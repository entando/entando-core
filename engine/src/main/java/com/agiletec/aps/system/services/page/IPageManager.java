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
package com.agiletec.aps.system.services.page;

import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for the page manager services.
 * @author M.Diana
 */
public interface IPageManager extends ITreeNodeManager {

	/**
	 * Delete a page and eventually the association with the showlets. 
	 * @param pageCode the code of the page to delete
	 * @throws ApsSystemException In case of database access error.
	 */
	public void deletePage(String pageCode) throws ApsSystemException;

	/**
	 * Add a new page to the database.
	 * @param page The page to add
	 * @throws ApsSystemException In case of database access error.
	 */
	public void addPage(IPage page) throws ApsSystemException;

	/**
	 * Update a page record in the database.
	 * @param page The modified page.
	 * @throws ApsSystemException In case of database access error.
	 */
	public void updatePage(IPage page) throws ApsSystemException;

	/**
	 * Move a page.
	 * @param pageCode The code of the page to move.
	 * @param moveUp When true the page is moved to a higher level of the tree, otherwise to a lower level.
	 * @return The result of the operation: false if the move request could not be satisfied, true otherwise. 
	 * @throws ApsSystemException In case of database access error.
	 */
	public boolean movePage(String pageCode, boolean moveUp) throws ApsSystemException;

	/**
	 * Move a widget.
	 * @param pageCode The code of the page to configure.
	 * @param frameToMove the frame position to move .
	 * @param destFrame the frame final position .
	 * @return The result of the operation: false if the move request could not be satisfied, true otherwise.
	 * @throws ApsSystemException In case of database access error.
	 */
	public boolean moveWidget(String pageCode, Integer frameToMove, Integer destFrame) throws ApsSystemException;

	/**
	 * @deprecated Use {@link #joinWidget(String,Widget,int)} instead
	 */
	public void joinShowlet(String pageCode, Widget widget, int pos) throws ApsSystemException;

	/**
	 * Set the showlet -including its configuration- in the given page in the desidered position.
	 * If the position is already occupied by another showlet this will be substituted with the
	 * new one.
	 * @param pageCode the code of the page where to set the showlet
	 * @param widget The showlet to set
	 * @param pos The position where to place the showlet in
	 * @throws ApsSystemException In case of error.
	 */
	public void joinWidget(String pageCode, Widget widget, int pos) throws ApsSystemException;

	/**
	 * @deprecated Use {@link #removeWidget(String,int)} instead
	 */
	public void removeShowlet(String pageCode, int pos) throws ApsSystemException;

	/**
	 * Remove a widget from the given page.
	 * @param pageCode the code of the widget to remove from the page
	 * @param pos The position in the page to free
	 * @throws ApsSystemException In case of error
	 */
	public void removeWidget(String pageCode, int pos) throws ApsSystemException;

	/**
	 * Return the root of the pages tree.
	 * @return the root page.
	 */
	public IPage getRoot();

	/**
	 * Return a page given the name.
	 * @param pageCode The code of the page
	 * @return the requested page.
	 */
	public IPage getPage(String pageCode);

	/**
	 * Search pages by a token of its code.
	 * @param pageCodeToken The token containing to be looked up across the pages.
	 * @param allowedGroups The codes of allowed page groups.
	 * @return A list of candidates containing the given token. If the pageCodeToken is null then
	 * this method will return the full list of pages.
	 * @throws ApsSystemException in case of error.
	 */
	public List<IPage> searchPages(String pageCodeToken, List<String> allowedGroups) throws ApsSystemException;
	
	/**
	 * @param showletTypeCode the showlet type code
	 * @return The list of pages
	 * @deprecated Use {@link #getWidgetUtilizers(String)} instead
	 */
	public List<IPage> getShowletUtilizers(String showletTypeCode) throws ApsSystemException;

	public List<IPage> getWidgetUtilizers(String widgetTypeCode) throws ApsSystemException;
	
	public boolean movePage(IPage currentPage, IPage newParent) throws ApsSystemException;
	
}