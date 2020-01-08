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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.init.model.portdb.PageMetadataDraft;
import org.entando.entando.aps.system.init.model.portdb.PageMetadataOnline;
import org.entando.entando.aps.system.init.model.portdb.WidgetConfig;
import org.entando.entando.aps.system.init.model.portdb.WidgetConfigDraft;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object for the 'page' objects
 *
 * @author M.Diana - E.Santoboni - E.Mezzano
 */
public class PageDAO extends AbstractDAO implements IPageDAO {

    private static final Logger _logger = LoggerFactory.getLogger(PageDAO.class);

    protected enum WidgetConfigDest {
        ON_LINE, DRAFT;
    }

    @Override
    public List<PageRecord> loadPageRecords() {
        Connection conn = null;
        List<PageRecord> pages = null;
        try {
            conn = this.getConnection();
            pages = this.createPageRecordList(conn);
            this.readPageWidgets(pages, true, conn);
            this.readPageWidgets(pages, false, conn);
        } catch (Throwable t) {
            _logger.error("Error loading pages", t);
            throw new RuntimeException("Error loading pages", t);
        } finally {
            closeConnection(conn);
        }
        return pages;
    }

    private List<PageRecord> createPageRecordList(Connection conn) {
        List<PageRecord> pages = new ArrayList<>();
        Statement stat = null;
        ResultSet res = null;
        try {
            stat = conn.createStatement();
            res = stat.executeQuery(ALL_PAGES);
            while (res.next()) {
                String code = res.getString(3);
                PageRecord page = this.createPageRecord(code, res);
                pages.add(page);
                int numFramesOnline = this.getWidgetArrayLength(page.getMetadataOnline());
                if (numFramesOnline >= 0) {
                    page.setWidgetsOnline(new Widget[numFramesOnline]);
                }
                int numFramesDraft = this.getWidgetArrayLength(page.getMetadataDraft());
                if (numFramesDraft >= 0) {
                    page.setWidgetsDraft(new Widget[numFramesDraft]);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error loading page records", t);
            throw new RuntimeException("Error loading page records", t);
        } finally {
            closeDaoResources(res, stat);
        }
        return pages;
    }

    private void readPageWidgets(List<PageRecord> pages, boolean online, Connection conn) {
        Statement stat = null;
        ResultSet res = null;
        try {
            stat = conn.createStatement();
            res = stat.executeQuery(online ? ALL_WIDGETS_ONLINE : ALL_WIDGETS_DRAFT);
            PageRecord currentPage = null;
            int currentWidgetNum = 0;
            Widget[] currentWidgets = null;
            Iterator<PageRecord> pagesIter = pages.iterator();
            while (res.next()) {
                String code = res.getString(1);
                while (currentPage == null || !currentPage.getCode().equals(code)) {
                    currentPage = pagesIter.next();
                    currentWidgetNum = this.getWidgetArrayLength(online ? currentPage.getMetadataOnline() : currentPage.getMetadataDraft());
                    currentWidgets = online ? currentPage.getWidgetsOnline() : currentPage.getWidgetsDraft();
                }
                this.readWidget(currentPage, currentWidgetNum, currentWidgets, 2, res);
            }
        } catch (Throwable t) {
            _logger.error("Error loading page widgets - online/draft: {}", online, t);
            throw new RuntimeException("Error loading page widgets - online/draft: " + online, t);
        } finally {
            closeDaoResources(res, stat);
        }
    }

    protected int getWidgetArrayLength(PageMetadata metadata) {
        int numFrames = -1;
        if (metadata != null) {
            PageModel model = metadata.getModel();
            if (model != null) {
                numFrames = model.getFrames().length;
            }
        }
        return numFrames;
    }

    protected void readWidget(PageRecord page, int numOfFrames, Widget widgets[],
            int startIndex, ResultSet res) throws ApsSystemException, SQLException {
        Object posObj = res.getObject(startIndex);
        if (posObj != null) {
            int pos = res.getInt(startIndex);
            if (pos >= 0 && pos < numOfFrames) {
                Widget widget = this.createWidget(page, pos, res, startIndex + 1);
                widgets[pos] = widget;
            } else {
                _logger.warn("The position read from the database exceeds the number of frames defined in the model of the page {}", page
                        .getCode());
            }
        }
    }

    protected PageRecord createPageRecord(String code, ResultSet res) throws Throwable {
        PageRecord page = new PageRecord();
        page.setCode(code);
        page.setParentCode(res.getString(1));
        page.setPosition(res.getInt(2));
        if (res.getString(4) != null) {
            page.setMetadataOnline(this.createPageMetadata(code, res, 4));
        }
        if (res.getString(10) != null) {
            page.setMetadataDraft(this.createPageMetadata(code, res, 10));
        }
        return page;
    }

    protected Widget createWidget(PageRecord page, int pos, ResultSet res, int startIndex) throws ApsSystemException, SQLException {
        String typeCode = res.getString(startIndex++);
        if (null == typeCode) {
            return null;
        }
        Widget widget = new Widget();
        WidgetType type = this.getWidgetTypeManager().getWidgetType(typeCode);
        widget.setType(type);
        ApsProperties config = new ApsProperties();
        String configText = res.getString(startIndex++);
        if (null != configText && configText.trim().length() > 0) {
            try {
                config.loadFromXml(configText);
            } catch (Throwable t) {
                _logger.error("IO error detected while parsing the configuration of the widget in position '{}' of the page '{}'", pos, page
                        .getCode(), t);
                String msg = "IO error detected while parsing the configuration of the widget in position " + pos + " of the page '" + page
                        .getCode() + "'";
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
     *
     * @param page The new page to insert.
     */
    @Override
    public void addPage(IPage page) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            String pageCode = page.getCode();
            this.addPageRecord(page, conn);
            PageMetadata draftMetadata = page.getMetadata();
            if (draftMetadata == null) {
                draftMetadata = new PageMetadata();
            }
            draftMetadata.setUpdatedAt(new Date());
            this.addDraftPageMetadata(pageCode, draftMetadata, conn);
            this.addWidgetForPage(page, WidgetConfigDest.DRAFT, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while adding a new page", t);
            throw new RuntimeException("Error while adding a new page", t);
        } finally {
            closeConnection(conn);
        }
    }

    protected void addPageRecord(IPage page, Connection conn) throws ApsSystemException {
        String parentCode = page.getParentCode();
        // a new page is always inserted in the last position,
        // to avoid changes of the position of the "sister" pages.
        int position = this.getLastPosition(parentCode, conn) + 1;
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_PAGE);
            stat.setString(1, page.getCode());
            stat.setString(2, parentCode);
            stat.setInt(3, position);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error adding a new page record", t);
            throw new RuntimeException("Error adding a new page record", t);
        } finally {
            closeDaoResources(null, stat);
        }
        if (page instanceof Page) {
            ((Page) page).setPosition(position);
        }
    }

    /**
     * Delete the page identified by the given code.
     *
     * @param page The page to delete.
     */
    @Override
    public void deletePage(IPage page) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            String pageCode = page.getCode();
            this.deleteOnlineWidgets(pageCode, conn);
            this.deleteDraftWidgets(pageCode, conn);
            this.deleteOnlinePageMetadata(pageCode, conn);
            this.deleteDraftPageMetadata(pageCode, conn);
            this.deletePageRecord(pageCode, conn);
            this.shiftPages(page.getParentCode(), page.getPosition(), conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting page", t);
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
            _logger.error("Error deleting a page record", t);
            throw new RuntimeException("Error deleting a page record", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Delete the widget associated to a page.
     *
     * @param pageCode The code of the page containing the widget to delete.
     * @param conn The database connection
     * @throws ApsSystemException In case of database error
     */
    protected void deleteOnlineWidgets(String pageCode, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_WIDGETS_FOR_PAGE_ONLINE);
            stat.setString(1, pageCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error while deleting widgets for page '{}'", pageCode, t);
            throw new RuntimeException("Error while deleting widgets", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Delete the widget associated to the draft version of a page.
     *
     * @param pageCode The code of the page containing the widget to delete.
     * @param conn The database connection
     * @throws ApsSystemException In case of database error
     */
    protected void deleteDraftWidgets(String pageCode, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_WIDGETS_FOR_PAGE_DRAFT);
            stat.setString(1, pageCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error while deleting  draft widgets for page '{}'", pageCode, t);
            throw new RuntimeException("Error while deleting draft widgets", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Decrement by one the position of a group of pages to compact the
     * positions after a deletion
     *
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
            _logger.error("Error moving page position", t);
            throw new RuntimeException("Error moving page position", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Updates the position for the page movement
     *
     * @param pageDown The page to move downwards
     * @param pageUp The page to move upwards
     */
    @Override
    public void updatePosition(String pageDown, String pageUp) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updatePosition(pageDown, MOVE_DOWN, conn);
            this.updatePosition(pageUp, MOVE_UP, conn);
            this.updatePageMetadataOnlineLastUpdate(pageDown, new Date(), conn);
            this.updatePageMetadataOnlineLastUpdate(pageUp, new Date(), conn);
            this.updatePageMetadataDraftLastUpdate(pageDown, new Date(), conn);
            this.updatePageMetadataDraftLastUpdate(pageUp, new Date(), conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error detected while updating positions", t);
            throw new RuntimeException("Error detected while updating positions", t);
        } finally {
            closeConnection(conn);
        }
    }

    private void updatePosition(String pageCode, String query, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, pageCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error detected while updating position for page {}", pageCode, t);
            throw new RuntimeException("Error detected while updating position for page " + pageCode, t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    @Override
    public void updateWidgetPosition(String pageCode, Integer frameToMove, Integer destFrame) {
        Connection conn = null;
        int TEMP_FRAME_POSITION = -9999;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateWidgetPosition(pageCode, frameToMove, TEMP_FRAME_POSITION, conn);
            this.updateWidgetPosition(pageCode, destFrame, frameToMove, conn);
            this.updateWidgetPosition(pageCode, TEMP_FRAME_POSITION, destFrame, conn);
            this.updatePageMetadataDraftLastUpdate(pageCode, new Date(), conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while updating WidgetPosition. page: {} from position: {} to position {}", pageCode, frameToMove,
                    destFrame, t);
            throw new RuntimeException("Error while updating widget position", t);
        } finally {
            closeConnection(conn);
        }
    }

    private void updateWidgetPosition(String pageCode, int frameToMove, int destFrame, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(MOVE_WIDGET);
            stat.setInt(1, destFrame);
            stat.setString(2, pageCode);
            stat.setInt(3, frameToMove);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error while updating WidgetPosition. page: {} from position: {} to position {}", pageCode, frameToMove,
                    destFrame, t);
            throw new RuntimeException("Error while updating widget position", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Updates a page record in the database.
     *
     * @param page The page to update
     */
    @Override
    public void updatePage(IPage page) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            String pageCode = page.getCode();
            this.deleteDraftWidgets(pageCode, conn);
            this.deleteDraftPageMetadata(pageCode, conn);
            this.updatePageRecord(page, conn);
            PageMetadata metadata = page.getMetadata();
            metadata.setUpdatedAt(new Date());
            this.addDraftPageMetadata(pageCode, page.getMetadata(), conn);
            this.addWidgetForPage(page, WidgetConfigDest.DRAFT, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while updating the page", t);
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
            stat.setString(2, page.getCode());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error while updating the page record", t);
            throw new RuntimeException("Error while updating the page record", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    @Override
    public void setPageOnline(String pageCode) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.deleteOnlineWidgets(pageCode, conn);
            this.deleteOnlinePageMetadata(pageCode, conn);
            this.executeQueryWithoutResultset(conn, SET_ONLINE_METADATA, pageCode);
            this.executeQueryWithoutResultset(conn, SET_ONLINE_WIDGETS, pageCode);
            Date now = new Date();
            this.updatePageMetadataDraftLastUpdate(pageCode, now, conn);
            this.updatePageMetadataOnlineLastUpdate(pageCode, now, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while setting page '{}' as online", pageCode, t);
            throw new RuntimeException("Error while setting page " + pageCode + " as online", t);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void setPageOffline(String pageCode) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.deleteOnlineWidgets(pageCode, conn);
            this.deleteOnlinePageMetadata(pageCode, conn);
            this.updatePageMetadataDraftLastUpdate(pageCode, new Date(), conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while setting page '{}' as online", pageCode, t);
            throw new RuntimeException("Error while setting page " + pageCode + " as online", t);
        } finally {
            closeConnection(conn);
        }
    }

    protected void addOnlinePageMetadata(String pageCode, PageMetadata pageMetadata, Connection conn) throws ApsSystemException {
        this.savePageMetadata(pageCode, pageMetadata, true, PageMetadataOnline.TABLE_NAME, conn);
    }

    protected void addDraftPageMetadata(String pageCode, PageMetadata pageMetadata, Connection conn) throws ApsSystemException {
        this.savePageMetadata(pageCode, pageMetadata, true, PageMetadataDraft.TABLE_NAME, conn);
    }

    protected void deleteOnlinePageMetadata(String pageCode, Connection conn) throws ApsSystemException {
        this.executeQueryWithoutResultset(conn, DELETE_ONLINE_PAGE_METADATA, pageCode);
    }

    protected void deleteDraftPageMetadata(String pageCode, Connection conn) throws ApsSystemException {
        this.executeQueryWithoutResultset(conn, DELETE_DRAFT_PAGE_METADATA, pageCode);
    }

    protected void savePageMetadata(String pageCode, PageMetadata pageMetadata, boolean isAdd, String tableName, Connection conn)
            throws ApsSystemException {
        if (pageMetadata != null) {
            PreparedStatement stat = null;
            try {
                StringBuilder query = new StringBuilder(isAdd ? ADD_PAGE_METADATA_START : UPDATE_PAGE_METADATA_START);
                query.append(tableName).append(isAdd ? ADD_PAGE_METADATA_END : UPDATE_PAGE_METADATA_END);
                stat = conn.prepareStatement(query.toString());
                int index = 1;
                if (isAdd) {
                    stat.setString(index++, pageCode);
                }
                stat.setString(index++, pageMetadata.getGroup());
                stat.setString(index++, pageMetadata.getTitles().toXml());
                stat.setString(index++, pageMetadata.getModel().getCode());
                if (pageMetadata.isShowable()) {
                    stat.setInt(index++, 1);
                } else {
                    stat.setInt(index++, 0);
                }
                String extraConfig = this.getExtraConfig(pageMetadata);
                stat.setString(index++, extraConfig);
                Date updatedAt = pageMetadata.getUpdatedAt() != null ? pageMetadata.getUpdatedAt() : new Date();
                stat.setTimestamp(index++, updatedAt != null ? new java.sql.Timestamp(updatedAt.getTime()) : null);
                if (!isAdd) {
                    stat.setString(index++, pageCode);
                }
                stat.executeUpdate();
            } catch (Throwable t) {
                _logger.error("Error while saving the page metadata record for table {}", tableName, t);
                throw new RuntimeException("Error while saving the page metadata record for table " + tableName, t);
            } finally {
                closeDaoResources(null, stat);
            }
        }
    }

    protected PageMetadata createPageMetadata(String code, ResultSet res, int startIndex) throws Throwable {
        PageMetadata pageMetadata = new PageMetadata();
        int index = startIndex;
        pageMetadata.setGroup(res.getString(index++));
        String titleText = res.getString(index++);
        ApsProperties titles = new ApsProperties();
        try {
            titles.loadFromXml(titleText);
        } catch (Throwable t) {
            _logger.error("IO error detected while parsing the titles of the page {}", code, t);
            String msg = "IO error detected while parsing the titles of the page '" + code + "'";
            throw new ApsSystemException(msg, t);
        }
        pageMetadata.setTitles(titles);
        pageMetadata.setModel(this.getPageModelManager().getPageModel(res.getString(index++)));
        Integer showable = res.getInt(index++);
        pageMetadata.setShowable(showable == 1);
        String extraConfig = res.getString(index++);
        if (null != extraConfig && extraConfig.trim().length() > 0) {
            try {
                PageExtraConfigDOM configDom = new PageExtraConfigDOM();
                configDom.addExtraConfig(pageMetadata, extraConfig);
            } catch (Throwable t) {
                _logger.error("IO error detected while parsing the extra config of the page {}", code, t);
                String msg = "IO error detected while parsing the extra config of the page '" + code + "'";
                throw new ApsSystemException(msg, t);
            }
        }
        Timestamp date = res.getTimestamp(index++);
        pageMetadata.setUpdatedAt(date != null ? new Date(date.getTime()) : null);
        return pageMetadata;
    }

    protected String getExtraConfig(PageMetadata pageMetadata) {
        PageExtraConfigDOM dom = this.getExtraConfigDOM();
        return dom.extractXml(pageMetadata);
    }

    protected PageExtraConfigDOM getExtraConfigDOM() {
        return new PageExtraConfigDOM();
    }

    protected void addWidgetForPage(IPage page, WidgetConfigDest dest, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            Widget[] widgets = null;
            String query = "";
            if (dest == WidgetConfigDest.ON_LINE) {
                query = ADD_WIDGET_FOR_PAGE;
                widgets = page.getWidgets();
            } else if (dest == WidgetConfigDest.DRAFT) {
                query = ADD_WIDGET_FOR_PAGE_DRAFT;
                widgets = page.getWidgets();
            }
            if (null == widgets) {
                return;
            }
            String pageCode = page.getCode();
            stat = conn.prepareStatement(query);
            for (int i = 0; i < widgets.length; i++) {
                Widget widget = widgets[i];
                if (widget != null) {
                    if (null == widget.getType()) {
                        _logger.error("Widget Type null when adding widget on frame '{}' of page '{}'", i, page.getCode());
                        continue;
                    }
                    this.valueAddWidgetStatement(pageCode, i, widget, stat);
                    stat.addBatch();
                    stat.clearParameters();
                }
            }
            stat.executeBatch();
        } catch (Throwable t) {
            _logger.error("Error while inserting the widgets in a page", t);
            throw new RuntimeException("Error while inserting the widgets in a page", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    @Override
    public void removeWidget(IPage page, int pos) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            super.executeQueryWithoutResultset(conn, DELETE_WIDGET_FOR_PAGE_DRAFT, page.getCode(), pos);
            Date now = new Date();
            this.updatePageMetadataDraftLastUpdate(page.getCode(), now, conn);
            page.getMetadata().setUpdatedAt(now);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error removing the widget from page '{}', frame {}", page.getCode(), pos, t);
            throw new RuntimeException("Error removing the widget from page '" + page.getCode() + "', frame " + pos, t);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void joinWidget(IPage page, Widget widget, int pos) {
        String pageCode = page.getCode();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            super.executeQueryWithoutResultset(conn, DELETE_WIDGET_FOR_PAGE_DRAFT, pageCode, pos);
            stat = conn.prepareStatement(ADD_WIDGET_FOR_PAGE_DRAFT);
            this.valueAddWidgetStatement(pageCode, pos, widget, stat);
            stat.executeUpdate();
            Date now = new Date();
            this.updatePageMetadataDraftLastUpdate(pageCode, now, conn);
            page.getMetadata().setUpdatedAt(now);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error adding a widget in the frame '{}' of the page '{}'", pos, pageCode, t);
            throw new RuntimeException("Error adding a widget in the frame " + pos + " of the page '" + pageCode + "'", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    private void valueAddWidgetStatement(String pageCode, int pos, Widget widget, PreparedStatement stat) throws Throwable {
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
            // a moved page is always inserted in the last position into the new parent,
            // to avoid changes of the position of the "sister" pages.
            int pos = this.getLastPosition(newParent.getCode(), conn) + 1;
            stat = conn.prepareStatement(UPDATE_PAGE_TREE_POSITION);
            stat.setString(1, newParent.getCode());
            stat.setInt(2, pos);
            stat.setString(3, currentPage.getCode());
            stat.executeUpdate();
            this.shiftPages(currentPage.getParentCode(), currentPage.getPosition(), conn);
            this.updatePageMetadataDraftLastUpdate(currentPage.getCode(), new Date(), conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while moving the page {} under {}" + newParent, currentPage, newParent, t);
            throw new RuntimeException("Error while moving the page " + currentPage + " under " + newParent, t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    private int getLastPosition(String parentPageCode, Connection conn) {
        int position = 0;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(GET_LAST_CHILDREN_POSITION);
            stat.setString(1, parentPageCode);
            res = stat.executeQuery();
            if (res.next()) {
                position = res.getInt(1);
            }
        } catch (Throwable t) {
            _logger.error("Error loading LastPosition", t);
            throw new RuntimeException("Error loading LastPosition", t);
        } finally {
            this.closeDaoResources(res, stat);
        }
        return position;
    }

    private void updatePageMetadataDraftLastUpdate(String pageCode, Date date, Connection conn) throws SQLException {
        this.updatePageMetatataUpdate(pageCode, date, PageMetadataDraft.TABLE_NAME, conn);
    }

    private void updatePageMetadataOnlineLastUpdate(String pageCode, Date date, Connection conn) throws SQLException {
        this.updatePageMetatataUpdate(pageCode, date, PageMetadataOnline.TABLE_NAME, conn);
    }

    private void updatePageMetatataUpdate(String pageCode, Date date, String tablename, Connection conn) throws SQLException {
        PreparedStatement stat = null;
        try {
            String query = "UPDATE " + tablename + " SET updatedat = ? WHERE code = ?";
            stat = conn.prepareStatement(query);
            stat.setTimestamp(1, new Timestamp(date.getTime()));
            stat.setString(2, pageCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error while updating the page metadata record for table {} and page {}", PageMetadataDraft.TABLE_NAME, pageCode,
                    t);
            throw new RuntimeException("Error while updating the page metadata record for table " + PageMetadataDraft.TABLE_NAME
                    + " and page " + pageCode, t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    @Override
    public List<String> loadLastUpdatedPages(int size) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        List<String> pages = new ArrayList<>();
        try {
            conn = this.getConnection();
            String query = LOAD_LAST_UPDATED_PAGES;
            stat = conn.prepareStatement(query);
            res = stat.executeQuery();
            int limit = 1;
            while (res.next() && limit++ <= size) {
                pages.add(res.getString("code"));
            }
        } catch (Throwable t) {
            _logger.error("Error loading lastUpdatedPages", t);
            throw new RuntimeException("Error loading lastUpdatedPages", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return pages;
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
    private static final String ALL_PAGES = "SELECT p.parentcode, p.pos, p.code, "
            + "onl.groupcode, onl.titles, onl.modelcode, onl.showinmenu, onl.extraconfig, onl.updatedat, "
            + "drf.groupcode, drf.titles, drf.modelcode, drf.showinmenu, drf.extraconfig, drf.updatedat FROM pages p LEFT JOIN "
            + PageMetadataOnline.TABLE_NAME + " onl ON p.code = onl.code LEFT JOIN " + PageMetadataDraft.TABLE_NAME
            + " drf ON p.code = drf.code ORDER BY p.parentcode, p.pos, p.code ";

    private static final String ALL_WIDGETS_START = "SELECT w.pagecode, w.framepos, w.widgetcode, w.config " + "FROM pages p JOIN ";
    private static final String ALL_WIDGETS_END = " w ON p.code = w.pagecode " + "ORDER BY p.parentcode, p.pos, p.code, w.framepos ";

    private static final String ALL_WIDGETS_ONLINE = ALL_WIDGETS_START + WidgetConfig.TABLE_NAME + ALL_WIDGETS_END;
    private static final String ALL_WIDGETS_DRAFT = ALL_WIDGETS_START + WidgetConfigDraft.TABLE_NAME + ALL_WIDGETS_END;

    private static final String ADD_PAGE = "INSERT INTO pages(code, parentcode, pos) VALUES ( ? , ? , ? )";

    private static final String DELETE_PAGE = "DELETE FROM pages WHERE code = ? ";

    private static final String DELETE_WIDGETS_FOR_PAGE_ONLINE = "DELETE FROM " + WidgetConfig.TABLE_NAME + " WHERE pagecode = ? ";

    private static final String DELETE_WIDGETS_FOR_PAGE_DRAFT = "DELETE FROM " + WidgetConfigDraft.TABLE_NAME + " WHERE pagecode = ? ";

    private static final String DELETE_WIDGET_FOR_PAGE_DRAFT = DELETE_WIDGETS_FOR_PAGE_DRAFT + " AND framepos = ? ";

    private static final String MOVE_UP = "UPDATE pages SET pos = (pos - 1) WHERE code = ? ";

    private static final String MOVE_DOWN = "UPDATE pages SET pos = (pos + 1) WHERE code = ? ";

    private static final String UPDATE_PAGE = "UPDATE pages SET parentcode = ? WHERE code = ? ";

    private static final String SHIFT_PAGE = "UPDATE pages SET pos = (pos - 1) WHERE parentcode = ? AND pos > ? ";

    private static final String ADD_WIDGET_FOR_PAGE = "INSERT INTO " + WidgetConfig.TABLE_NAME
            + " (pagecode, framepos, widgetcode, config) VALUES ( ? , ? , ? , ? )";

    private static final String ADD_WIDGET_FOR_PAGE_DRAFT = "INSERT INTO " + WidgetConfigDraft.TABLE_NAME
            + " (pagecode, framepos, widgetcode, config) VALUES ( ? , ? , ? , ? )";

    private static final String MOVE_WIDGET = "UPDATE " + WidgetConfigDraft.TABLE_NAME
            + " SET framepos = ? WHERE pagecode = ? and framepos = ? ";

    private static final String UPDATE_PAGE_TREE_POSITION = "UPDATE pages SET parentcode = ? , pos =?  WHERE code = ? ";

    private static final String PAGE_METADATA_WHERE_CODE = " WHERE code = ?";

    private static final String ADD_PAGE_METADATA_END = " (code, groupcode, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES (?, ?, ?, ?, ?, ?, ?) ";

    private static final String UPDATE_PAGE_METADATA_END = "SET groupcode = ? , titles = ?, modelcode = ?, showinmenu = ?, extraconfig = ?, updatedat = ? "
            + PAGE_METADATA_WHERE_CODE;

    private static final String ADD_PAGE_METADATA_START = "INSERT INTO ";

    private static final String UPDATE_PAGE_METADATA_START = "UPDATE ";

    private static final String DELETE_ONLINE_PAGE_METADATA = "DELETE FROM " + PageMetadataOnline.TABLE_NAME + PAGE_METADATA_WHERE_CODE;
    private static final String DELETE_DRAFT_PAGE_METADATA = "DELETE FROM " + PageMetadataDraft.TABLE_NAME + PAGE_METADATA_WHERE_CODE;

    private static final String SET_ONLINE_METADATA = "INSERT INTO " + PageMetadataOnline.TABLE_NAME
            + " (code, groupcode, titles, modelcode, showinmenu, extraconfig, updatedat) SELECT code, groupcode, titles, modelcode, showinmenu, extraconfig, updatedat FROM "
            + PageMetadataDraft.TABLE_NAME + " WHERE code = ?";

    private static final String SET_ONLINE_WIDGETS = "INSERT INTO " + WidgetConfig.TABLE_NAME
            + " (pagecode, framepos, widgetcode, config) SELECT pagecode, framepos, widgetcode, config FROM " + WidgetConfigDraft.TABLE_NAME
            + " WHERE pagecode = ?";

    private static final String LOAD_LAST_UPDATED_PAGES = "SELECT code FROM pages_metadata_draft ORDER BY updatedat DESC";

    private static final String GET_LAST_CHILDREN_POSITION = "SELECT pos FROM pages WHERE parentcode = ? ORDER BY pos DESC";

}
