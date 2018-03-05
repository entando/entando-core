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
package com.agiletec.aps.system.services.pagemodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for page model objects
 * @author M.Diana - E.Santoboni
 */
public class PageModelDAO extends AbstractSearcherDAO implements IPageModelDAO {
	
    private static final Logger logger = LoggerFactory.getLogger(PageModelDAO.class);
	
    @Override
    public int count(FieldSearchFilter[] filters) {
        Integer count = null;
        try {
            count = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count pagemodels", t);
            throw new RuntimeException("error in count pagemodels", t);
        }
        return count;
    }

    @Override
    public List<String> search(FieldSearchFilter[] filters) {
        List<String> entityIdList = null;
        try {
            entityIdList = super.searchId(filters);
        } catch (Throwable t) {
            logger.error("error in search pagemodels", t);
            throw new RuntimeException("error in search pagemodels", t);
        }
        return entityIdList;
    }

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
            logger.error("Error loading the page models", t);
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
            logger.error("Error building the page model code '{}'", code, t);
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
            logger.error("Error while adding a model", t);
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
            logger.error("Error while updating a model", t);
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
            logger.error("Error while deleting a model", t);
			throw new RuntimeException("Error while deleting a model", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return " pagemodels";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "code";
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
