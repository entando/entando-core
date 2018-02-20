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
package org.entando.entando.aps.system.services.guifragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class GuiFragmentDAO extends AbstractSearcherDAO implements IGuiFragmentDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GuiFragmentDAO.class);
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "guifragment";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "code";
	}
	
	@Override
	public List<String> searchGuiFragments(FieldSearchFilter[] filters) {
		List<String> guiFragmentsId = null;
		try {
			guiFragmentsId  = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchGuiFragments",  t);
			throw new RuntimeException("error in searchGuiFragments", t);
		}
		return guiFragmentsId;
	}
	
	@Override
	public void insertGuiFragment(GuiFragment guiFragment) {
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertGuiFragment(guiFragment, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert guiFragment",  t);
			throw new RuntimeException("Error on insert guiFragment", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void insertGuiFragment(GuiFragment guiFragment, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_GUIFRAGMENT);
			int index = 1;
 			stat.setString(index++, guiFragment.getCode());
 			if (StringUtils.isNotBlank(guiFragment.getWidgetTypeCode())) {
				stat.setString(index++, guiFragment.getWidgetTypeCode());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if (StringUtils.isNotBlank(guiFragment.getPluginCode())) {
				stat.setString(index++, guiFragment.getPluginCode());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, guiFragment.getGui());
 			stat.setInt(index++, 0);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert guiFragment",  t);
			throw new RuntimeException("Error on insert guiFragment", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}

	@Override
	public void updateGuiFragment(GuiFragment guiFragment) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateGuiFragment(guiFragment, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating guiFragment {}", guiFragment.getCode(),  t);
			throw new RuntimeException("Error updating guiFragment", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void updateGuiFragment(GuiFragment guiFragment, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_GUIFRAGMENT);
			int index = 1;
 			if(StringUtils.isNotBlank(guiFragment.getWidgetTypeCode())) {
				stat.setString(index++, guiFragment.getWidgetTypeCode());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(guiFragment.getPluginCode())) {
				stat.setString(index++, guiFragment.getPluginCode());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, guiFragment.getGui());
			stat.setString(index++, guiFragment.getCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating guiFragment {}", guiFragment.getCode(),  t);
			throw new RuntimeException("Error updating guiFragment", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void removeGuiFragment(String code) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeGuiFragment(code, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting guiFragment {}", code, t);
			throw new RuntimeException("Error deleting guiFragment", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	public void removeGuiFragment(String code, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_GUIFRAGMENT);
			int index = 1;
			stat.setString(index++, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting guiFragment {}", code, t);
			throw new RuntimeException("Error deleting guiFragment", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}
	
	@Override
	public GuiFragment loadGuiFragment(String code) {
		GuiFragment guiFragment = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			guiFragment = this.loadGuiFragment(code, conn);
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment with id {}", code, t);
			throw new RuntimeException("Error loading guiFragment with id " + code, t);
		} finally {
			this.closeConnection(conn);
		}
		return guiFragment;
	}
	
	protected GuiFragment loadGuiFragment(String code, Connection conn) {
		GuiFragment guiFragment = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_GUIFRAGMENT);
			int index = 1;
			stat.setString(index++, code);
			res = stat.executeQuery();
			if (res.next()) {
				guiFragment = this.buildGuiFragmentFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment with id {}", code, t);
			throw new RuntimeException("Error loading guiFragment with id " + code, t);
		} finally {
			closeDaoResources(res, stat);
		}
		return guiFragment;
	}

	protected GuiFragment buildGuiFragmentFromRes(ResultSet res) {
		GuiFragment guiFragment = null;
		try {
			guiFragment = new GuiFragment();				
			guiFragment.setCode(res.getString("code"));
			guiFragment.setWidgetTypeCode(res.getString("widgettypecode"));
			guiFragment.setPluginCode(res.getString("plugincode"));
			guiFragment.setGui(res.getString("gui"));
			guiFragment.setDefaultGui(res.getString("defaultgui"));
			Integer locked = res.getInt("locked");
			guiFragment.setLocked(null != locked && locked.intValue() == 1);
		} catch (Throwable t) {
			_logger.error("Error in buildGuiFragmentFromRes", t);
		}
		return guiFragment;
	}
	
	@Override
	public List<String> loadGuiFragmentPluginCodes() {
		List<String> codes = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_GUIFRAGMENT_PLUGIN_CODES);
			res = stat.executeQuery();
			while (res.next()) {
				String code = res.getString("plugincode");
				if (StringUtils.isNotEmpty(code) && !codes.contains(code)) {
					codes.add(code);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment plugin codes", t);
			throw new RuntimeException("Error loading guiFragment plugin codes", t);
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return codes;
	}
	
	private static final String ADD_GUIFRAGMENT = "INSERT INTO guifragment (code, widgettypecode, plugincode, gui, locked ) VALUES (? , ? , ? , ? , ?)";

	private static final String UPDATE_GUIFRAGMENT = "UPDATE guifragment SET widgettypecode = ?, plugincode = ? , gui = ? WHERE code = ? ";

	private static final String DELETE_GUIFRAGMENT = "DELETE FROM guifragment WHERE code = ?";
	
	private static final String LOAD_GUIFRAGMENT = "SELECT code, widgettypecode, plugincode, gui, defaultgui, locked FROM guifragment WHERE code = ?";
	
	private static final String LOAD_GUIFRAGMENT_PLUGIN_CODES = "SELECT plugincode FROM guifragment";
	
}