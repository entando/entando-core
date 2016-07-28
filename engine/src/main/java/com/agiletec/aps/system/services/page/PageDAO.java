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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object for the 'page' objects
 * @author M.Diana - E.Santoboni
 */
public class PageDAO extends AbstractDAO implements IPageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(PageDAO.class);
	
	/**
	 * Load a sorted list of the pages and the configuration of the widgets 
	 * @return the list of pages
	 */
	@Override
	public List<IPage> loadPages() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		List<IPage> pages = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(ALL_PAGES);
			pages = this.createPages(res);
		} catch (Throwable t) {
			_logger.error("Error loading pages",  t);
			throw new RuntimeException("Error loading pages", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return pages;
	}

	/**
	 * Read & create in a single passage, for efficiency reasons, the pages and the 
	 * association of the associated widgets.
	 * @param res the result set where to extract pages information from.
	 * @return The list of the pages defined in the system
	 * @throws Throwable In case of error
	 */
	protected List<IPage> createPages(ResultSet res) throws Throwable {
		List<IPage> pages = new ArrayList<IPage>();
		Page page = null;
		Widget widgets[] = null;
		int numFrames = 0;
		String prevCode = "...no previous code...";
		while (res.next()) {
			String code = res.getString(3);
			if (!code.equals(prevCode)) {
				if (page != null) {
					pages.add(page);
				}
				page = this.createPage(code, res);
				numFrames = page.getModel().getFrames().length;
				widgets = new Widget[numFrames];
				page.setWidgets(widgets);
				prevCode = code;
			}
			int pos = res.getInt(9);
			if (pos >= 0 && pos < numFrames) {
				Widget widget = this.createWidget(page, pos, res);
				widgets[pos] = widget;
			} else {
				_logger.warn("The position read from the database exceeds the numer of frames defined in the model of the page {}", page.getCode());
			}
		}
		pages.add(page);
		return pages;
	}
	
	protected Page createPage(String code, ResultSet res) throws Throwable {
		Page page = new Page();
		page.setCode(code);
		page.setParentCode(res.getString(1));
		page.setPosition(res.getInt(2));
		Integer showable = new Integer (res.getInt(4));
		page.setShowable(showable.intValue() == 1);
		page.setModel(this.getPageModelManager().getPageModel(res.getString(5)));
		String titleText = res.getString(6);
		ApsProperties titles = new ApsProperties();
		try {
			titles.loadFromXml(titleText);
		} catch (Throwable t) {
			_logger.error("IO error detected while parsing the titles of the page {}", page.getCode(), t);
			String msg = "IO error detected while parsing the titles of the page '" + page.getCode()+"'";
			throw new ApsSystemException(msg, t);
		}
		page.setTitles(titles);
		page.setGroup(res.getString(7));
		String extraConfig = res.getString(8);
		if (null != extraConfig && extraConfig.trim().length() > 0) {
			try {
				PageExtraConfigDOM configDom = new PageExtraConfigDOM();
				configDom.addExtraConfig(page, extraConfig);
			} catch (Throwable t) {
				_logger.error("IO error detected while parsing the extra config of the page {}", page.getCode(), t);
				String msg = "IO error detected while parsing the extra config of the page '" + page.getCode()+"'";
				throw new ApsSystemException(msg, t);
			}
		}
		return page;
	}
	
	protected Widget createWidget(IPage page, int pos, ResultSet res) throws Throwable {
		String typeCode = res.getString(10);
		if (null == typeCode) {
			return null;
		}
		Widget widget = new Widget();
		WidgetType type = this.getWidgetTypeManager().getWidgetType(typeCode);
		widget.setType(type);
		ApsProperties config = new ApsProperties();
		String configText = res.getString(11);
		if (null != configText && configText.trim().length() > 0) {
			try {
				config.loadFromXml(configText);
			} catch (Throwable t) {
				_logger.error("IO error detected while parsing the configuration of the widget in position '{}' of the page '{}'", pos, page.getCode(), t);
				String msg = "IO error detected while parsing the configuration of the widget in position " +pos+ " of the page '"+ page.getCode()+"'";
				throw new ApsSystemException(msg, t);
			}
		} else {
			config = type.getConfig();
		}
		widget.setConfig(config);
		return widget;
	}

	/**
	 * Insert a new page.
	 * @param page The new page to insert.
	 */
	@Override
	public void addPage(IPage page) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.addPageRecord(page, conn);
			this.addWidgetForPage(page, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a new page",  t);
			throw new RuntimeException("Error while adding a new page", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	protected void addPageRecord(IPage page, Connection conn) throws ApsSystemException {
		int position = 1;
		IPage[] sisters = page.getParent().getChildren();
		if (null != sisters && sisters.length > 0) {
			IPage last = sisters[sisters.length - 1];
			if (null != last) {
				position = last.getPosition() + 1;
			} else {
				position = sisters.length + 1;
			}
		}
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_PAGE);
			stat.setString(1, page.getCode());
			stat.setString(2, page.getParent().getCode());
			if (page.isShowable()) {
				stat.setInt(3, 1);
			} else {
				stat.setInt(3, 0);
			}
			stat.setInt(4, position);
			stat.setString(5, page.getModel().getCode());
			stat.setString(6, page.getTitles().toXml());
			stat.setString(7, page.getGroup());
			String extraConfig = this.getExtraConfig(page);
			stat.setString(8, extraConfig);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error adding a new page record",  t);
			throw new RuntimeException("Error adding a new page record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Delete the page identified by the given code.
	 * @param page The page to delete.
	 */
	@Override
	public void deletePage(IPage page) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteWidgets(page.getCode(), conn);
			this.deletePageRecord(page.getCode(), conn);
			this.shiftPages(page.getParentCode(), page.getPosition(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting page",  t);
			throw new RuntimeException("Error deleting page", t);
		} finally {
			closeConnection(conn);
		}
	}

	protected void deletePageRecord(String pageCode, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_PAGE);
			stat.setString(1, pageCode);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting a page record",  t);
			throw new RuntimeException("Error deleting a page record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Delete the widget associated to a page.
	 * @param codePage The code of the page containing the widget to delete.
	 * @param conn The database connection
	 * @throws ApsSystemException In case of database error
	 */
	protected void deleteWidgets(String codePage, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_WIDGETS_FOR_PAGE);
			stat.setString(1, codePage);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error while deleting widgets for page '{}'", codePage,  t);
			throw new RuntimeException("Error while deleting widgets", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	/**
	 * Decrement by one the position of a group of pages to compact the positions after a deletion
	 * @param parentCode the code of the parent of the pages to compact.
	 * @param position The empty position which needs to be compacted.
	 * @param conn The connection to the database
	 * @throws ApsSystemException In case of database error
	 */
	protected void shiftPages(String parentCode, int position, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(SHIFT_PAGE);
			stat.setString(1, parentCode);
			stat.setInt(2, position);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error moving page position",  t);
			throw new RuntimeException("Error moving page position", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	/**
	 * Updates the position for the page movement
	 * @param pageDown The page to move downwards
	 * @param pageUp The page to move upwards
	 */
	@Override
	public void updatePosition(IPage pageDown, IPage pageUp) {
		Connection conn = null;
		PreparedStatement stat = null;
		PreparedStatement stat2 = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);

			stat = conn.prepareStatement(MOVE_DOWN);
			stat.setString(1, pageDown.getCode());
			stat.executeUpdate();

			stat2 = conn.prepareStatement(MOVE_UP);
			stat2.setString(1, pageUp.getCode());
			stat2.executeUpdate();

			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error detected while updating positions",  t);
			throw new RuntimeException("Error detected while updating positions", t);
		} finally {
			closeDaoResources(null, stat);
			closeDaoResources(null, stat2, conn);
		}
	}
	
	@Override
	public void updateWidgetPosition(String pageCode, Integer frameToMove, Integer destFrame) {
		Connection conn = null;
		PreparedStatement stat = null;
		PreparedStatement stat2 = null;
		PreparedStatement stat3 = null;
		int TEMP_FRAME_POSITION = -9999;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);

			stat = conn.prepareStatement(MOVE_WIDGET);
			stat.setInt(1, TEMP_FRAME_POSITION);
			stat.setString(2, pageCode);
			stat.setInt(3, frameToMove);
			stat.executeUpdate();

			stat2 = conn.prepareStatement(MOVE_WIDGET);
			stat2.setInt(1, frameToMove);
			stat2.setString(2, pageCode);
			stat2.setInt(3, destFrame);
			stat2.executeUpdate();

			stat3 = conn.prepareStatement(MOVE_WIDGET);
			stat3.setInt(1, destFrame);
			stat3.setString(2, pageCode);
			stat3.setInt(3, TEMP_FRAME_POSITION);
			stat3.executeUpdate();
			
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating WidgetPosition. page: {} from position: {} to position {}", pageCode, frameToMove, destFrame,  t);
			throw new RuntimeException("Error while updating widget position", t);
		} finally {
			closeDaoResources(null, stat);
			closeDaoResources(null, stat2);
			closeDaoResources(null, stat3, conn);
		}
	}

	/**
	 * Updates a page record in the database.
	 * @param page The page to update
	 */
	@Override
	public void updatePage(IPage page) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteWidgets(page.getCode(), conn);
			this.updatePageRecord(page, conn);
			this.addWidgetForPage(page, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating the page",  t);
			throw new RuntimeException("Error while updating the page", t);
		} finally {
			closeConnection(conn);
		}
	}

	protected void updatePageRecord(IPage page, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_PAGE);
			stat.setString(1, page.getParentCode());
			if (page.isShowable()) {
				stat.setInt(2, 1);
			} else {
				stat.setInt(2, 0);
			}
			stat.setString(3, page.getModel().getCode());
			stat.setString(4, page.getTitles().toXml());
			stat.setString(5, page.getGroup());
			String extraConfig = this.getExtraConfig(page);
			stat.setString(6, extraConfig);
			stat.setString(7, page.getCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error while updating the page record",  t);
			throw new RuntimeException("Error while updating the page record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected String getExtraConfig(IPage page) {
		PageExtraConfigDOM dom = this.getExtraConfigDOM();
		return dom.extractXml(page);
	}
	
	protected PageExtraConfigDOM getExtraConfigDOM() {
		return new PageExtraConfigDOM();
	}
	
	protected void addWidgetForPage(IPage page, Connection conn) throws ApsSystemException {
		if (null == page.getWidgets()) return;
		PreparedStatement stat = null;
		try {
			Widget[] widgets = page.getWidgets();
			stat = conn.prepareStatement(ADD_WIDGET_FOR_PAGE);
			for (int i = 0; i < widgets.length; i++) {
				Widget widget = widgets[i];
				if (widget != null) {
					if (null == widget.getType()) {
						_logger.error("Widget Type null when adding widget on frame '{}' of page '{}'", i, page.getCode());
						continue;
					}
					this.valueAddWidgetStatement(page.getCode(), i, widget, stat);
					stat.addBatch();
					stat.clearParameters();
				}
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Error while inserting the widgets in a page",  t);
			throw new RuntimeException("Error while inserting the widgets in a page", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * @deprecated Use {@link #removeWidget(String,int)} instead
	 */
	@Override
	public void removeShowlet(String pageCode, int pos) {
		removeWidget(pageCode, pos);
	}
	
	@Override
	public void removeWidget(String pageCode, int pos) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(REMOVE_WIDGET_FROM_FRAME);
			stat.setString(1, pageCode);
			stat.setInt(2, pos);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing the widget from page '{}', frame {}", pageCode, pos,  t);
			throw new RuntimeException("Error removing the widget from page '" +pageCode + "', frame " + pos, t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	/**
	 * @deprecated Use {@link #joinWidget(String,Widget,int)} instead
	 */
	@Override
	public void joinShowlet(String pageCode, Widget widget, int pos) {
		joinWidget(pageCode, widget, pos);
	}

	@Override
	public void joinWidget(String pageCode, Widget widget, int pos) {
		this.removeWidget(pageCode, pos);
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_WIDGET_FOR_PAGE);
			this.valueAddWidgetStatement(pageCode, pos, widget, stat);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding a widget in the frame '{}' of the page '{}'", pos, pageCode, t);
			throw new RuntimeException("Error adding a widget in the frame " +pos+" of the page '"+pageCode+"'", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private void valueAddWidgetStatement(String pageCode, 
			int pos, Widget widget, PreparedStatement stat) throws Throwable {
		stat.setString(1, pageCode);
		stat.setInt(2, pos);
		stat.setString(3, widget.getType().getCode());
		if (!widget.getType().isLogic()) {
			String config = null;
			if (null != widget.getConfig()) {
				config = widget.getConfig().toXml();
			}
			stat.setString(4, config);
		} else {
			stat.setNull(4, Types.VARCHAR);
		}
	}
	
	@Override
	public void movePage(IPage currentPage, IPage newParent) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			//position
			int pos = 1;
			IPage[] sisters = newParent.getChildren();
			if (null != sisters && sisters.length > 0) {
				IPage last = sisters[sisters.length - 1];
				if (null != last) {
					pos = last.getPosition() + 1;
				} else {
					pos = sisters.length + 1;
				}
			}
			stat = conn.prepareStatement(UPDATE_PAGE_TREE_POSITION);
			stat.setString(1, newParent.getCode());
			stat.setInt(2, pos);
			stat.setString(3, currentPage.getCode());
			stat.executeUpdate();
			this.shiftPages(currentPage.getParentCode(), currentPage.getPosition(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while moving the page {} under {}" + newParent, currentPage, newParent, t);
			throw new RuntimeException("Error while moving the page " + currentPage + " under " + newParent, t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	private IPageModelManager _pageModelManager;
	private IWidgetTypeManager _widgetTypeManager;

	// attenzione: l'ordinamento deve rispettare prima l'ordine delle pagine
	// figlie nelle pagine madri, e poi l'ordine dei widget nella pagina.
	private static final String ALL_PAGES = 
		"SELECT p.parentcode, p.pos, p.code, p.showinmenu, "
		+ "p.modelcode, p.titles, p.groupcode, p.extraconfig, "
		+ "s.framepos, s.widgetcode, s.config "
		+ "FROM pages p LEFT JOIN widgetconfig s ON p.code = s.pagecode "
		+ "ORDER BY p.parentcode, p.pos, p.code, s.framepos";
	
	private static final String ADD_PAGE = 
		"INSERT INTO pages(code, parentcode, showinmenu, pos, modelcode, titles, groupcode, extraconfig) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? )";

	private static final String DELETE_PAGE = 
		"DELETE FROM pages WHERE code = ? ";

	private static final String DELETE_WIDGETS_FOR_PAGE = 
		"DELETE FROM widgetconfig WHERE pagecode = ? ";

	private static final String REMOVE_WIDGET_FROM_FRAME = 
		DELETE_WIDGETS_FOR_PAGE + " AND framepos = ? ";

	private static final String MOVE_UP = 
		"UPDATE pages SET pos = (pos - 1) WHERE code = ? ";

	private static final String MOVE_DOWN = 
		"UPDATE pages SET pos = (pos + 1) WHERE code = ? ";

	private static final String UPDATE_PAGE = 
		"UPDATE pages SET parentcode = ? , showinmenu = ? , modelcode = ? , titles = ? , groupcode = ? , extraconfig = ? WHERE code = ? ";

	private static final String SHIFT_PAGE = 
		"UPDATE pages SET pos = (pos - 1) WHERE parentcode = ? AND pos > ? ";

	private static final String ADD_WIDGET_FOR_PAGE = 
		"INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ( ? , ? , ? , ? )";

	private static final String MOVE_WIDGET =
		"UPDATE widgetconfig SET framepos = ? WHERE pagecode = ? and framepos = ? ";
	
	private static final String UPDATE_PAGE_TREE_POSITION = 
			"UPDATE pages SET parentcode = ? , pos =?  WHERE code = ? ";
	
}