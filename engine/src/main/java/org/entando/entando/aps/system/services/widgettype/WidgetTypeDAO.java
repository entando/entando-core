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
package org.entando.entando.aps.system.services.widgettype;

import com.agiletec.aps.system.common.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object per i tipi di widget (WidgetType).
 *
 * @author M.Diana - E.Santoboni
 */
public class WidgetTypeDAO extends AbstractDAO implements IWidgetTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(WidgetTypeDAO.class);

    private ILangManager langManager;

    private final String ALL_WIDGET_TYPES
            = "SELECT code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup, configui, bundleid FROM widgetcatalog";

    private final String ADD_WIDGET_TYPE
            = "INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup, configui, bundleid) "
            + "VALUES ( ? , ? , ? , ? , ? , ? , ? , ?, ?, ?)";

    private final String DELETE_WIDGET_TYPE
            = "DELETE FROM widgetcatalog WHERE code = ? AND locked = ? ";

    private final String UPDATE_WIDGET_TYPE
            = "UPDATE widgetcatalog SET titles = ? , defaultconfig = ? , maingroup = ?, configui = ?, bundleid = ? WHERE code = ? ";

    @Override
    public Map<String, WidgetType> loadWidgetTypes() {
        Connection conn = null;
        Statement stat = null;
        ResultSet res = null;
        Map<String, WidgetType> widgetTypes = new HashMap<>();
        try {
            conn = this.getConnection();
            stat = conn.createStatement();
            res = stat.executeQuery(ALL_WIDGET_TYPES);
            while (res.next()) {
                WidgetType widgetType = this.createWidgetTypeFromResultSet(res);
                widgetTypes.put(widgetType.getCode(), widgetType);
            }
        } catch (Throwable t) {
            logger.error("Error loading widgets", t);
            throw new RuntimeException("Error loading widgets", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return widgetTypes;
    }

    protected WidgetType createWidgetTypeFromResultSet(ResultSet res) throws ApsSystemException {
        WidgetType widgetType = new WidgetType();
        String code = null;
        try {
            code = res.getString(1);
            widgetType.setCode(code);
            String xmlTitles = res.getString(2);
            ApsProperties titles = new ApsProperties();
            titles.loadFromXml(xmlTitles);
            widgetType.setTitles(titles);
            String xml = res.getString(3);
            if (null != xml && xml.trim().length() > 0) {
                WidgetTypeDOM showletTypeDom = new WidgetTypeDOM(xml, this.getLangManager().getLangs());
                widgetType.setTypeParameters(showletTypeDom.getParameters());
                widgetType.setAction(showletTypeDom.getAction());
            }
            widgetType.setPluginCode(res.getString(4));
            widgetType.setParentTypeCode(res.getString(5));
            String config = res.getString(6);
            if (null != config && config.trim().length() > 0) {
                ApsProperties defaultConfig = new ApsProperties();
                defaultConfig.loadFromXml(config);
                widgetType.setConfig(defaultConfig);
            }
            if ((null != widgetType.getConfig() && null == widgetType.getParentTypeCode())) {
                throw new ApsSystemException("Default configuration found in the type '"
                        + code + "' with no parent type assigned");
            }
            int isLocked = res.getInt(7);
            widgetType.setLocked(isLocked == 1);
            String mainGroup = res.getString(8);
            if (null != mainGroup && mainGroup.trim().length() > 0) {
                widgetType.setMainGroup(mainGroup.trim());
            }
            String configUi = res.getString(9);
            if (StringUtils.isNotEmpty(configUi)) {
                widgetType.setConfigUi(configUi);
            }
            String bundleId = res.getString(10);
            if (StringUtils.isNotEmpty(bundleId)) {
                widgetType.setBundleId(bundleId);
            }
        } catch (Throwable t) {
            logger.error("Error parsing the Widget Type '{}'", code, t);
            throw new ApsSystemException("Error in the parsing in the Widget Type '" + code + "'", t);
        }
        return widgetType;
    }

    @Override
    public void addWidgetType(WidgetType widgetType) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(ADD_WIDGET_TYPE);
            //(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
            stat.setString(1, widgetType.getCode());
            stat.setString(2, widgetType.getTitles().toXml());
            if (null != widgetType.getTypeParameters()) {
                WidgetTypeDOM showletTypeDom = new WidgetTypeDOM(widgetType.getTypeParameters(), widgetType.getAction());
                stat.setString(3, showletTypeDom.getXMLDocument());
            } else {
                stat.setNull(3, Types.VARCHAR);
            }
            stat.setString(4, widgetType.getPluginCode());
            stat.setString(5, widgetType.getParentTypeCode());
            if (null != widgetType.getConfig()) {
                stat.setString(6, widgetType.getConfig().toXml());
            } else {
                stat.setNull(6, Types.VARCHAR);
            }
            if (widgetType.isLocked()) {
                stat.setInt(7, 1);
            } else {
                stat.setInt(7, 0);
            }
            stat.setString(8, widgetType.getMainGroup());
            stat.setString(9, widgetType.getConfigUi());
            stat.setString(10, widgetType.getBundleId());
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error while adding a new widget type", t);
            throw new RuntimeException("Error while adding a new widget type", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public void deleteWidgetType(String widgetTypeCode) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_WIDGET_TYPE);
            stat.setString(1, widgetTypeCode);
            stat.setInt(2, 0);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error deleting widget type '{}'", widgetTypeCode, t);
            throw new RuntimeException("Error deleting widget type", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public void updateWidgetType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup,
                                 String configUi, String bundleId) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(UPDATE_WIDGET_TYPE);
            stat.setString(1, titles.toXml());
            if (null == defaultConfig || defaultConfig.isEmpty()) {
                stat.setNull(2, Types.VARCHAR);
            } else {
                stat.setString(2, defaultConfig.toXml());
            }
            stat.setString(3, mainGroup);
            stat.setString(4, configUi);
            stat.setString(5, bundleId);
            stat.setString(6, widgetTypeCode);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error updating widget type {}", widgetTypeCode, t);
            throw new RuntimeException("Error updating widget type", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

}
