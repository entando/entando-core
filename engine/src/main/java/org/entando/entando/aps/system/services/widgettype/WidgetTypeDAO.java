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
package org.entando.entando.aps.system.services.widgettype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object per i tipi di widget (WidgetType).
 * @author M.Diana - E.Santoboni
 */
public class WidgetTypeDAO extends AbstractDAO implements IWidgetTypeDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(WidgetTypeDAO.class);
	
	/**
	 * Return the map of the widget types
	 * @return The map of the widget types
	 * @deprecated Use {@link #loadWidgetTypes()} instead
	 */
	@Override
	public Map<String, WidgetType> loadShowletTypes() {
		return loadWidgetTypes();
	}
	
	/**
	 * Return the map of the widget types
	 * @return The map of the widget types
	 */
	@Override
	public Map<String, WidgetType> loadWidgetTypes() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, WidgetType> widgetTypes = new HashMap<String, WidgetType>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(ALL_WIDGET_TYPES);
			while (res.next()) {
				WidgetType widgetType = this.createWidgetTypeFromResultSet(res);
				widgetTypes.put(widgetType.getCode(), widgetType);
			}
		} catch (Throwable t) {
			_logger.error("Error loading widgets",  t);
			throw new RuntimeException("Error loading widgets", t);
		} finally{
			closeDaoResources(res, stat, conn);
		}
		return widgetTypes;
	}
	
	@Deprecated
	protected WidgetType showletTypeFromResultSet(ResultSet res) throws ApsSystemException {
		return this.createWidgetTypeFromResultSet(res);
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
				throw new ApsSystemException("Default configuration found in the type '" +
						code + "' with no parent type assigned");
			}
			int isLocked = res.getInt(7);
			widgetType.setLocked(isLocked == 1);
			String mainGroup = res.getString(8);
			if (null != mainGroup && mainGroup.trim().length() > 0) {
				widgetType.setMainGroup(mainGroup.trim());
			}
		} catch (Throwable t) {
			_logger.error("Error parsing the Widget Type '{}'", code, t);
			throw new ApsSystemException("Error in the parsing in the Widget Type '" + code + "'", t);
		}
		return widgetType;
	}
	
	@Override
	@Deprecated
	public void addShowletType(WidgetType widgetType) {
		this.addWidgetType(widgetType);
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
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a new widget type",  t);
			throw new RuntimeException("Error while adding a new widget type", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void deleteShowletType(String showletTypeCode) {
		deleteWidgetType(showletTypeCode);
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
			_logger.error("Error deleting widget type '{}'", widgetTypeCode, t);
			throw new RuntimeException("Error deleting widget type", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletTypeTitles(String widgetTypeCode, ApsProperties titles) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_SHOWLET_TYPE_TITLES);
			stat.setString(1, titles.toXml());
			stat.setString(2, widgetTypeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating titles for showlet type {}", widgetTypeCode,  t);
			throw new RuntimeException("Error updating showlet type titles", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_SHOWLET_TYPE_DEPRECATED);
			stat.setString(1, titles.toXml());
			if (null == defaultConfig || defaultConfig.size() == 0) {
				stat.setNull(2, Types.VARCHAR);
			} else {
				stat.setString(2, defaultConfig.toXml());
			}
			stat.setString(3, widgetTypeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating widget type",  t);
			throw new RuntimeException("Error updating widget type", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletType(String showletTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) {
		updateWidgetType(showletTypeCode, titles, defaultConfig, mainGroup);
	}

	@Override
	public void updateWidgetType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_WIDGET_TYPE);
			stat.setString(1, titles.toXml());
			if (null == defaultConfig || defaultConfig.size() == 0) {
				stat.setNull(2, Types.VARCHAR);
			} else {
				stat.setString(2, defaultConfig.toXml());
			}
			stat.setString(3, mainGroup);
			stat.setString(4, widgetTypeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating widget type {}", widgetTypeCode,  t);
			throw new RuntimeException("Error updating widget type", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	private ILangManager _langManager;
	
	private final String ALL_WIDGET_TYPES = 
		"SELECT code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup FROM widgetcatalog";
	
	private final String ADD_WIDGET_TYPE = 
		"INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) " +
		"VALUES ( ? , ? , ? , ? , ? , ? , ? , ?)";
	
	private final String DELETE_WIDGET_TYPE = 
		"DELETE FROM widgetcatalog WHERE code = ? AND locked = ? ";
	
	@Deprecated
	private final String UPDATE_SHOWLET_TYPE_DEPRECATED = 
		"UPDATE widgetcatalog SET titles = ? , defaultconfig = ? WHERE code = ? ";
	
	private final String UPDATE_WIDGET_TYPE = 
		"UPDATE widgetcatalog SET titles = ? , defaultconfig = ? , maingroup = ? WHERE code = ? ";
	
	@Deprecated
	private final String UPDATE_SHOWLET_TYPE_TITLES = 
		"UPDATE widgetcatalog SET titles = ? WHERE code = ? ";
	
}