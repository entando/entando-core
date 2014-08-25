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
package com.agiletec.aps.system.services.pagemodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.apache.commons.lang3.StringUtils;

/**
 * Data Access Object for page model objects
 * @author M.Diana - E.Santoboni
 */
public class PageModelDAO extends AbstractDAO implements IPageModelDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(PageModelDAO.class);
	
	/**
	 * Carica e restituisce la mappa dei modelli di pagina.
	 * @return La mappa dei modelli.
	 */
	@Override
	public Map<String, PageModel> loadModels() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, PageModel> models = new HashMap<String, PageModel>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(ALL_PAGEMODEL);
			while (res.next()) {
				PageModel pageModel = this.getPageModelFromResultSet(res);
				models.put(pageModel.getCode(), pageModel);
			}
		} catch (Throwable t) {
			_logger.error("Error loading the page models",  t);
			throw new RuntimeException("Error loading the page models", t);
		} finally{
			closeDaoResources(res, stat, conn);
		}
		return models;
	}
	
	/**
	 * Build and return a page model by a resultset.
	 * @param res The resultset to read.
	 * @return The built page model.
	 * @throws ApsSystemException In case of error.
	 */
	protected PageModel getPageModelFromResultSet(ResultSet res) throws ApsSystemException {
		PageModel pageModel = new PageModel();
		String code = null;
		try {
			code = res.getString(1);
			pageModel.setCode(code);
			pageModel.setDescription(res.getString(2));
			String xmlFrames = res.getString(3);
			if (null != xmlFrames && xmlFrames.length() > 0) {
				PageModelDOM pageModelDOM = new PageModelDOM(xmlFrames, this.getWidgetTypeManager());
				pageModel.setMainFrame(pageModelDOM.getMainFrame());
				pageModel.setConfiguration(pageModelDOM.getConfiguration());
			}
			pageModel.setPluginCode(res.getString(4));
			pageModel.setTemplate(res.getString(5));
		} catch (Throwable t) {
			_logger.error("Error building the page model code '{}'", code, t);
			throw new RuntimeException("Error building the page model code '" + code + "'", t);
		}
		return pageModel;
	}
	
	@Override
	public void addModel(PageModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_PAGEMODEL);
			stat.setString(1, model.getCode());
			stat.setString(2, model.getDescription());
			PageModelDOM dom = new PageModelDOM(model);
			stat.setString(3, dom.getXMLDocument());
			String pluginCode = (StringUtils.isBlank(model.getPluginCode())) ? null : model.getPluginCode();
			stat.setString(4, pluginCode);
			String template = (StringUtils.isBlank(model.getTemplate())) ? null : model.getTemplate();
			stat.setString(5, template);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a model",  t);
			throw new RuntimeException("Error while adding a model", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateModel(PageModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_PAGEMODEL);
			stat.setString(1, model.getDescription());
			PageModelDOM dom = new PageModelDOM(model);
			stat.setString(2, dom.getXMLDocument());
			String pluginCode = (StringUtils.isBlank(model.getPluginCode())) ? null : model.getPluginCode();
			stat.setString(3, pluginCode);
			String template = (StringUtils.isBlank(model.getTemplate())) ? null : model.getTemplate();
			stat.setString(4, template);
			stat.setString(5, model.getCode());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a model",  t);
			throw new RuntimeException("Error while updating a model", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteModel(String code) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_PAGEMODEL);
			stat.setString(1, code);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting a model",  t);
			throw new RuntimeException("Error while deleting a model", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	private IWidgetTypeManager _widgetTypeManager;
	
	private final String ALL_PAGEMODEL = 
			"SELECT code, descr, frames, plugincode, templategui FROM pagemodels";
	
	private static final String ADD_PAGEMODEL =
			"INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ( ? , ? , ? , ? , ? )";
	
	private static final String UPDATE_PAGEMODEL =
			"UPDATE pagemodels SET descr = ? , frames = ? , plugincode = ? , templategui = ? WHERE code = ?";
	
	private static final String DELETE_PAGEMODEL =
			"DELETE FROM pagemodels WHERE code = ?";
	
}
