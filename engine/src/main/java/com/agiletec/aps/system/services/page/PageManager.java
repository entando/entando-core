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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.events.LangsChangedEvent;
import com.agiletec.aps.system.services.lang.events.LangsChangedObserver;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedEvent;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedObserver;

/**
 * This is the page manager service class. Pages are held in a tree-like
 * structure, to allow a hierarchical access, and stored in a map, to allow a
 * key-value type access. In the tree, the father points the son and vice versa;
 * the order between the pages in the same level is always kept.
 *
 * @author M.Diana - E.Santoboni
 */
public class PageManager extends AbstractService implements IPageManager, GroupUtilizer, LangsChangedObserver, PageModelUtilizer, PageModelChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(PageManager.class);

	@Override
	public void init() throws Exception {
		this.loadPageTree();
		_logger.debug("{} ready. : Initialized {} pages", this.getClass().getName(), _pages.size());
	}

	/**
	 * Load the page and organize them in a tree structure
	 *
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	private void loadPageTree() throws ApsSystemException {
		PagesStatus status = new PagesStatus();
		IPage newRoot = null;
		IPage newRootOnline = null;
		List<IPage> pageList = null;
		List<IPage> pageListO = null;
		List<PageRecord> pageRecordList = null;
		try {
			pageRecordList = this.getPageDAO().loadPageRecords();
			Map<String, IPage> newFullMap = new HashMap<String, IPage>(pageRecordList.size());
			Map<String, IPage> newOnlineMap = new HashMap<String, IPage>();
			pageListO = new ArrayList<>();
			pageList = new ArrayList<>();
			for (int i = 0; i < pageRecordList.size(); i++) {
				PageRecord pageRecord = pageRecordList.get(i);
				IPage pageD = pageRecord.createDraftPage();
				IPage pageO = pageRecord.createOnlinePage();
				pageList.add(pageD);
				newFullMap.put(pageD.getCode(), pageD);
				if (pageD.getCode().equals(pageD.getParentCode())) {
					newRoot = pageD;
					newRootOnline = pageO;
				}
				this.buildPagesStatus(status, pageD);
				if (pageD.isOnline()) {
					newOnlineMap.put(pageO.getCode(), pageO);
					pageListO.add(pageO);
				}
			}
			for (int i = 0; i < pageList.size(); i++) {
				this.buildTreeHierarchy(pageList, newRoot, newFullMap, true, i);
			}
			for (int i = 0; i < pageListO.size(); i++) {
				this.buildTreeHierarchy(pageListO, newRootOnline, newOnlineMap, false, i);
			}
			if (newRoot == null) {
				throw new ApsSystemException("Error in the page tree: root page undefined");
			}
			this._root = newRoot;
			this._onlineRoot = newRootOnline;
			this._pages = newFullMap;
			this._onlinePages = newOnlineMap;
			this._pagesStatus = status;
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error while building the tree of pages", t);
			throw new ApsSystemException("Error while building the tree of pages", t);
		}
	}

	@Override
	public void updateFromLangsChanged(LangsChangedEvent event) {
		try {
			this.init();
		} catch (Throwable t) {
			_logger.error("Error on init method", t);
		}
	}

	/**
	 * Delete a page and eventually the association with the widgets.
	 *
	 * @param pageCode
	 * the code of the page to delete
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	@Override
	public void deletePage(String pageCode) throws ApsSystemException {
		IPage page = this.getDraftPage(pageCode);
		if (null != page && page.getChildren().length <= 0) {
			try {
				this.getPageDAO().deletePage(page);
			} catch (Throwable t) {
				_logger.error("Error detected while deleting page {}", pageCode, t);
				throw new ApsSystemException("Error detected while deleting a page", t);
			}
		}
		this.loadPageTree();
		this.notifyPageChangedEvent(page, PageChangedEvent.REMOVE_OPERATION_CODE, null);
	}

	/**
	 * Add a new page to the database.
	 *
	 * @param page
	 * The page to add
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	@Override
	public void addPage(IPage page) throws ApsSystemException {
		try {
			this.getPageDAO().addPage(page);
		} catch (Throwable t) {
			_logger.error("Error adding a page", t);
			throw new ApsSystemException("Error adding a page", t);
		}
		this.loadPageTree();
		this.notifyPageChangedEvent(this.getDraftPage(page.getCode()), PageChangedEvent.INSERT_OPERATION_CODE, null);
	}

	/**
	 * Update a page record in the database.
	 *
	 * @param page
	 * The modified page.
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	@Override
	public void updatePage(IPage page) throws ApsSystemException {
		try {
			this.getPageDAO().updatePage(page);
		} catch (Throwable t) {
			_logger.error("Error updating a page", t);
			throw new ApsSystemException("Error updating a page", t);
		}
		this.loadPageTree();
		this.notifyPageChangedEvent(page, PageChangedEvent.UPDATE_OPERATION_CODE, null);
	}

	@Override
	public void setPageOnline(String pageCode) throws ApsSystemException {
		try {
			this.getPageDAO().setPageOnline(pageCode);
		} catch (Throwable t) {
			_logger.error("Error updating a page as online", t);
			throw new ApsSystemException("Error updating a page as online", t);
		}
		this.loadPageTree();
		this.notifyPageChangedEvent(this.getDraftPage(pageCode), PageChangedEvent.UPDATE_OPERATION_CODE, null, PageChangedEvent.EVENT_TYPE_SET_PAGE_ONLINE);
	}

	@Override
	public void setPageOffline(String pageCode) throws ApsSystemException {
		try {
			this.getPageDAO().setPageOffline(pageCode);
		} catch (Throwable t) {
			_logger.error("Error updating a page as offline", t);
			throw new ApsSystemException("Error updating a page as offline", t);
		}
		this.loadPageTree();
		this.notifyPageChangedEvent(this.getDraftPage(pageCode), PageChangedEvent.UPDATE_OPERATION_CODE, null, PageChangedEvent.EVENT_TYPE_SET_PAGE_OFFLINE);
	}

	private void notifyPageChangedEvent(IPage page, int operationCode, Integer framePos) {
		PageChangedEvent event = buildEvent(page, operationCode, framePos);
		this.notifyEvent(event);
	}

	private void notifyPageChangedEvent(IPage page, int operationCode, Integer framesPos, Integer destFramePos, String eventType) {
		PageChangedEvent event = buildEvent(page, operationCode, framesPos);
		event.setDestFrame(destFramePos);
		event.setEventType(eventType);
		this.notifyEvent(event);
	}

	private void notifyPageChangedEvent(IPage page, int operationCode, Integer framePos, String eventType) {
		PageChangedEvent event = buildEvent(page, operationCode, framePos);
		event.setEventType(eventType);
		this.notifyEvent(event);
	}

	private PageChangedEvent buildEvent(IPage page, int operationCode, Integer framePos) {
		PageChangedEvent event = new PageChangedEvent();
		event.setPage(page);
		event.setOperationCode(operationCode);
		if (null != framePos) {
			event.setFramePosition(framePos);
		}
		return event;
	}

	/**
	 * Move a page.
	 *
	 * @param pageCode
	 * The code of the page to move.
	 * @param moveUp
	 * When true the page is moved to a higher level of the tree, otherwise to a
	 * lower level.
	 * @return The result of the operation: false if the move request could not
	 * be satisfied, true otherwise.
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	@Override
	public boolean movePage(String pageCode, boolean moveUp) throws ApsSystemException {
		boolean resultOperation = true;
		try {
			IPage currentPage = this.getDraftPage(pageCode);
			if (null == currentPage) {
				throw new ApsSystemException("The page '" + pageCode + "' does not exist!");
			}
			IPage parent = currentPage.getParent();
			IPage[] sisterPages = parent.getChildren();
			for (int i = 0; i < sisterPages.length; i++) {
				IPage sisterPage = sisterPages[i];
				if (sisterPage.getCode().equals(pageCode)) {
					if (!verifyRequiredMovement(i, moveUp, sisterPages.length)) {
						return false;
					} else if (moveUp) {
						IPage pageDown = sisterPages[i - 1];
						this.moveUpDown(pageDown, currentPage);
					} else {
						IPage pageUp = sisterPages[i + 1];
						this.moveUpDown(currentPage, pageUp);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while moving  page {}", pageCode, t);
			throw new ApsSystemException("Error while moving a page", t);
		}
		this.loadPageTree();
		return resultOperation;
	}

	@Override
	public boolean moveWidget(String pageCode, Integer frameToMove, Integer destFrame) throws ApsSystemException {
		boolean resultOperation = true;
		try {
			IPage currentPage = this.getDraftPage(pageCode);
			if (null == currentPage) {
				throw new ApsSystemException("The page '" + pageCode + "' does not exist!");
			}
			Widget[] widgets = currentPage.getWidgets();
			Widget currentWidget = widgets[frameToMove];
			if (null == currentWidget) {
				throw new ApsSystemException("No widget found in frame '" + frameToMove + "' and page '" + pageCode + "'");
			}
			boolean movementEnabled = isMovementEnabled(frameToMove, destFrame, widgets.length);
			if (!movementEnabled) {
				return false;
			} else {
				this.getPageDAO().updateWidgetPosition(pageCode, frameToMove, destFrame);
			}
			this.notifyPageChangedEvent(currentPage, PageChangedEvent.EDIT_FRAME_OPERATION_CODE, frameToMove, destFrame, PageChangedEvent.EVENT_TYPE_MOVE_WIDGET);
		} catch (Throwable t) {
			_logger.error("Error while moving widget. page {} from position {} to position {}", pageCode, frameToMove, destFrame, t);
			throw new ApsSystemException("Error while moving a widget", t);
		}
		this.loadPageTree();
		return resultOperation;
	}

	private boolean isMovementEnabled(Integer frameToMove, Integer destFrame, int dimension) {
		boolean isEnabled = true;
		if (frameToMove.intValue() == destFrame.intValue()) {
			return false;
		}
		if (frameToMove > dimension) {
			return false;
		}
		if (frameToMove < 0) {
			return false;
		}
		if (destFrame > dimension - 1) {
			return false;
		}
		if (destFrame < 0) {
			return false;
		}
		return isEnabled;
	}

	/**
	 * Verify the possibility of the page to be moved elsewhere.
	 *
	 * @param position
	 * The position of the page to move
	 * @param moveUp
	 * When true the page is moved to a higher level of the tree, otherwise to a
	 * lower level.
	 * @param dimension
	 * The number the number of the pages of the parent of the page to move.
	 * @return if true then the requested movement is possible (but not
	 * performed) false otherwise.
	 */
	private boolean verifyRequiredMovement(int position, boolean moveUp, int dimension) {
		boolean result = true;
		if (moveUp) {
			if (position == 0) {
				result = false;
			}
		} else if (position == (dimension - 1)) {
			result = false;
		}
		return result;
	}

	/**
	 * Perform the movement of a page
	 *
	 * @param pageDown
	 * @param pageUp
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	private void moveUpDown(IPage pageDown, IPage pageUp) throws ApsSystemException {
		try {
			this.getPageDAO().updatePosition(pageDown, pageUp);
		} catch (Throwable t) {
			_logger.error("Error while moving a page", t);
			throw new ApsSystemException("Error while moving a page", t);
		}
	}

	/**
	 * Remove a widgets from the given page.
	 *
	 * @param pageCode
	 * the code of the page
	 * @param pos
	 * The position in the page to free
	 * @throws ApsSystemException
	 * In case of error
	 * @deprecated Use {@link #removeWidget(String,int)} instead
	 */
	@Override
	public void removeShowlet(String pageCode, int pos) throws ApsSystemException {
		this.removeWidget(pageCode, pos);
	}

	/**
	 * Remove a widget from the given page.
	 *
	 * @param pageCode
	 * the code of the page
	 * @param pos
	 * The position in the page to free
	 * @throws ApsSystemException
	 * In case of error
	 */
	@Override
	public void removeWidget(String pageCode, int pos) throws ApsSystemException {
		this.checkPagePos(pageCode, pos);
		try {
			IPage currentPage = this.getDraftPage(pageCode);
			this.getPageDAO().removeWidget(currentPage, pos);
			currentPage.getWidgets()[pos] = null;
			if (currentPage.isOnline()) {
				boolean widgetEquals = Arrays.deepEquals(currentPage.getWidgets(), this.getOnlinePage(pageCode).getWidgets());
				((Page) currentPage).setChanged(!widgetEquals);
			}
			this.notifyPageChangedEvent(currentPage, PageChangedEvent.EDIT_FRAME_OPERATION_CODE, pos, PageChangedEvent.EVENT_TYPE_REMOVE_WIDGET);
		} catch (Throwable t) {
			String message = "Error removing the widget from the page '" + pageCode + "' in the frame " + pos;
			_logger.error("Error removing the widget from the page '{}' in the frame {}", pageCode, pos, t);
			throw new ApsSystemException(message, t);
		}
	}

	/**
	 * @throws ApsSystemException
	 * In case of error.
	 * @deprecated Use {@link #joinWidget(String,Widget,int)} instead
	 */
	@Override
	public void joinShowlet(String pageCode, Widget widget, int pos) throws ApsSystemException {
		this.joinWidget(pageCode, widget, pos);
	}

	/**
	 * Set the widget -including its configuration- in the given page in the
	 * desired position. If the position is already occupied by another widget
	 * this will be substituted with the new one.
	 *
	 * @param pageCode
	 * the code of the page where to set the widget
	 * @param widget
	 * The widget to set
	 * @param pos
	 * The position where to place the widget in
	 * @throws ApsSystemException
	 * In case of error.
	 */
	@Override
	public void joinWidget(String pageCode, Widget widget, int pos) throws ApsSystemException {
		this.checkPagePos(pageCode, pos);
		if (null == widget || null == widget.getType()) {
			throw new ApsSystemException("Invalid null value found in either the Widget or the widgetType");
		}
		try {
			IPage currentPage = this.getDraftPage(pageCode);
			this.getPageDAO().joinWidget(currentPage, widget, pos);
			currentPage.getWidgets()[pos] = widget;
			if (currentPage.isOnline()) {
				boolean widgetEquals = Arrays.deepEquals(currentPage.getWidgets(), this.getOnlinePage(pageCode).getWidgets());
				((Page) currentPage).setChanged(!widgetEquals);
			}
			this.notifyPageChangedEvent(currentPage, PageChangedEvent.EDIT_FRAME_OPERATION_CODE, pos, PageChangedEvent.EVENT_TYPE_JOIN_WIDGET);
		} catch (Throwable t) {
			String message = "Error during the assignation of a widget to the frame " + pos + " in the page code " + pageCode;
			_logger.error("Error during the assignation of a widget to the frame {} in the page code {}", pos, pageCode, t);
			throw new ApsSystemException(message, t);
		}
	}

	/**
	 * Utility method which perform checks on the parameters submitted when
	 * editing the page.
	 *
	 * @param pageCode
	 * The code of the page
	 * @param pos
	 * The given position
	 * @throws ApsSystemException
	 * In case of database access error.
	 */
	private void checkPagePos(String pageCode, int pos) throws ApsSystemException {
		IPage currentPage = this.getDraftPage(pageCode);
		if (null == currentPage) {
			throw new ApsSystemException("The page '" + pageCode + "' does not exist!");
		}
		PageModel model = currentPage.getMetadata().getModel();
		if (pos < 0 || pos >= model.getFrames().length) {
			throw new ApsSystemException("The Position '" + pos + "' is not defined in the model '" + model.getDescription() + "' of the page '" + pageCode + "'!");
		}
	}

	/**
	 * Set the root page.
	 *
	 * @param root
	 * the Page to be set as root
	 */
	protected void setRoot(IPage root) {
		this._root = root;
	}

	/**
	 * Return the root of the pages tree.
	 *
	 * @return the root page
	 */
	@Override
	public IPage getRoot() {
		throw new UnsupportedOperationException("METODO NON SUPPORTATO: public IPage getRoot()");
	}

	@Override
	public IPage getOnlineRoot() {
		return this._onlineRoot;
	}

	@Override
	public IPage getDraftRoot() {
		return this._root;
	}

	@Override
	public IPage getOnlinePage(String pageCode) {
		return this._onlinePages.get(pageCode);
	}

	@Override
	public IPage getDraftPage(String pageCode) {
		return this._pages.get(pageCode);
	}

	@Override
	public List<IPage> searchPages(String pageCodeToken, List<String> allowedGroups) throws ApsSystemException {
		List<IPage> searchResult = new ArrayList<IPage>();
		try {
			if (null == this._pages || this._pages.isEmpty() || null == allowedGroups || allowedGroups.isEmpty()) {
				return searchResult;
			}
			IPage root = this.getDraftRoot();
			this.searchPages(root, pageCodeToken, allowedGroups, searchResult);
		} catch (Throwable t) {
			String message = "Error during searching pages with token " + pageCodeToken;
			_logger.error("Error during searching pages with token {}", pageCodeToken, t);
			throw new ApsSystemException(message, t);
		}
		return searchResult;
	}

	private void searchPages(IPage currentTarget, String pageCodeToken, List<String> allowedGroups, List<IPage> searchResult) {
		if ((null == pageCodeToken || currentTarget.getCode().toLowerCase().contains(pageCodeToken.toLowerCase())) && (allowedGroups.contains(currentTarget.getGroup())
				|| allowedGroups.contains(Group.ADMINS_GROUP_NAME))) {
			searchResult.add(currentTarget);
		}
		IPage[] children = currentTarget.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.searchPages(children[i], pageCodeToken, allowedGroups, searchResult);
		}
	}

	@Override
	public ITreeNode getNode(String code) {
		return this.getOnlinePage(code);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<IPage> utilizers = new ArrayList<IPage>();
		try {
			IPage root = this.getDraftRoot();
			this.searchUtilizers(groupName, utilizers, root);
			List<IPage> utilizersOnline = new ArrayList<IPage>();
			root = this.getOnlineRoot();
			this.searchUtilizers(groupName, utilizersOnline, root);
			for (IPage page : utilizersOnline) {
				if (!utilizers.contains(page)) {
					utilizers.add(page);
				}
			}
		} catch (Throwable t) {
			String message = "Error during searching page utilizers of group " + groupName;
			_logger.error("Error during searching page utilizers of group {}", groupName, t);
			throw new ApsSystemException(message, t);
		}
		return utilizers;
	}

	private void searchUtilizers(String groupName, List<IPage> utilizers, IPage page) {
		if (page.getGroup().equals(groupName) && !page.isOnlineInstance()) {
			utilizers.add(page);
		} else {
			Collection<String> extraGroups = page.getMetadata().getExtraGroups();
			boolean inUse = extraGroups != null && extraGroups.contains(groupName);
			if (inUse) {
				utilizers.add(page);
			}
		}
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.searchUtilizers(groupName, utilizers, children[i]);
		}
	}

	@Override
	public List<IPage> getOnlineWidgetUtilizers(String widgetTypeCode) throws ApsSystemException {
		return this.getWidgetUtilizers(widgetTypeCode, false);
	}

	@Override
	public List<IPage> getDraftWidgetUtilizers(String widgetTypeCode) throws ApsSystemException {
		return this.getWidgetUtilizers(widgetTypeCode, true);
	}

	private List<IPage> getWidgetUtilizers(String widgetTypeCode, boolean draft) throws ApsSystemException {
		List<IPage> pages = new ArrayList<IPage>();
		try {
			if (null == widgetTypeCode) {
				return pages;
			}
			IPage root = (draft) ? this.getDraftRoot() : this.getOnlineRoot();
			this.getWidgetUtilizers(root, widgetTypeCode, pages);
		} catch (Throwable t) {
			String message = "Error during searching draft page utilizers of widget with code " + widgetTypeCode;
			_logger.error("Error during searching draft page utilizers of widget with code {}", widgetTypeCode, t);
			throw new ApsSystemException(message, t);
		}
		return pages;
	}

	private void getWidgetUtilizers(IPage page, String widgetTypeCode, List<IPage> widgetUtilizers) {
		Widget[] widgets = page.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			Widget widget = widgets[i];
			if (null != widget && null != widget.getType() && widgetTypeCode.equals(widget.getType().getCode())) {
				widgetUtilizers.add(page);
				break;
			}
		}
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			IPage child = children[i];
			this.getWidgetUtilizers(child, widgetTypeCode, widgetUtilizers);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPageModelUtilizers(String pageModelCode) throws ApsSystemException {
		List<IPage> pages = new ArrayList<IPage>();
		try {
			if (null == this._pages || this._pages.isEmpty() || null == pageModelCode) {
				return pages;
			}
			IPage root = this.getDraftRoot();
			this.getPageModelUtilizers(root, pageModelCode, pages);

			root = this.getOnlineRoot();
			this.getPageModelUtilizers(root, pageModelCode, pages);

		} catch (Throwable t) {
			String message = "Error during searching page utilizers of page model with code " + pageModelCode;
			_logger.error("Error during searching page utilizers of page model with code {}", pageModelCode, t);
			throw new ApsSystemException(message, t);
		}
		return pages;
	}

	private void getPageModelUtilizers(IPage page, String pageModelCode, List<IPage> pageModelUtilizers) {
		PageMetadata pageMetadata = page.getMetadata();
		boolean usingModel = pageMetadata != null && pageMetadata.getModel() != null && pageModelCode.equals(pageMetadata.getModel().getCode());
		if (!usingModel) {
			pageMetadata = page.getMetadata();
			usingModel = pageMetadata != null && pageMetadata.getModel() != null && pageModelCode.equals(pageMetadata.getModel().getCode());
		}
		if (usingModel) {
			pageModelUtilizers.add(page);
		}
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			IPage child = children[i];
			this.getPageModelUtilizers(child, pageModelCode, pageModelUtilizers);
		}
	}

	@Override
	public void updateFromPageModelChanged(PageModelChangedEvent event) {
		try {
			if (event.getOperationCode() != PageModelChangedEvent.UPDATE_OPERATION_CODE) {
				return;
			}
			PageModel model = event.getPageModel();
			String pageModelCode = (null != model) ? model.getCode() : null;
			if (null != pageModelCode) {
				List<?> utilizers = this.getPageModelUtilizers(pageModelCode);
				if (null != utilizers && utilizers.size() > 0) {
					this.init();
				}
			}
		} catch (Throwable t) {
			_logger.error("Error during refres pages", t);
		}
	}

	@Override
	public boolean movePage(IPage currentPage, IPage newParent) throws ApsSystemException {
		boolean resultOperation = false;
		_logger.debug("start move page " + currentPage + "under " + newParent);
		try {
			this.getPageDAO().movePage(currentPage, newParent);
			resultOperation = true;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "movePage");
			throw new ApsSystemException("Error while moving a page under a root node", t);
		}
		this.loadPageTree();
		return resultOperation;
	}

	@Override
	public List<IPage> loadLastUpdatedPages(int size) throws ApsSystemException {
		List<IPage> pages = new ArrayList<IPage>();
		try {
			List<String> paceCodes = this.getPageDAO().loadLastUpdatedPages(size);
			if (null == paceCodes || paceCodes.isEmpty()) {
				return pages;
			}
			for (String pageCode : paceCodes) {
				IPage page = this.getDraftPage(pageCode);
				pages.add(page);
			}

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadLastUpdatedPages");
			throw new ApsSystemException("Error loading loadLastUpdatedPages", t);
		}
		return pages;
	}

	protected void buildTreeHierarchy(List<IPage> pageList, IPage root, Map<String, IPage> pagesMap, boolean includeDraft, int i) {
		Page page = (Page) pagesMap.get(pageList.get(i).getCode());
		Page parent = (Page) pagesMap.get(page.getParentCode());
		page.setParent(parent);
		if (page != root) {
			parent.addChild(page);
		}
	}

	protected void buildPagesStatus(PagesStatus status, IPage pageD) {
		Date currentDate = pageD.getMetadata().getUpdatedAt();
		if (pageD.isOnline()) {
			if (pageD.isChanged()) {
				status.setOnlineWithChanges(status.getOnlineWithChanges() + 1);
			} else {
				status.setOnline(status.getOnline() + 1);
			}
		} else {
			status.setDraft(status.getDraft() + 1);
		}

		if (null != currentDate) {
			if (null == status.getLastUpdate() || status.getLastUpdate().before(currentDate)) {
				status.setLastUpdate(currentDate);
			}
		}
	}

	@Override
	public PagesStatus getPagesStatus() {
		return this._pagesStatus;
	}

	protected IPageDAO getPageDAO() {
		return _pageDao;
	}

	public void setPageDAO(IPageDAO pageDao) {
		this._pageDao = pageDao;
	}

	/**
	 * The root of the pages tree.
	 */
	private IPage _root;
	private IPage _onlineRoot;

	/**
	 * The map of pages, indexed by code.
	 */
	private Map<String, IPage> _pages;
	private Map<String, IPage> _onlinePages;
	private PagesStatus _pagesStatus = new PagesStatus();

	private IPageDAO _pageDao;
}
