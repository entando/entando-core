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
package org.entando.entando.aps.system.services.datatypemodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object per gli oggetti datatype.
 *
 * @author M.Diana - C.Siddi - C.Sirigu
 */
public class DataModelDAO extends AbstractDAO implements IDataModelDAO {

	private static final Logger _logger = LoggerFactory.getLogger(DataModelDAO.class);

	@Override
	public Map<Long, DataModel> loadDataModels() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<Long, DataModel> models = new HashMap<Long, DataModel>();
		String query = ALL_CONTENTMODEL;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(query);
			while (res.next()) {
				DataModel contentModel = loadContentModel(res);
				//Hash map che contiene come chiave l'id e come valore l'oggetto stesso
				Long wrapLongId = new Long(contentModel.getId());
				models.put(wrapLongId, contentModel);
			}
		} catch (Throwable t) {
			_logger.error("Error loading datatype models", t);
			throw new RuntimeException("Error loading datatype models", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return models;
	}

	@Override
	public void addDataModel(DataModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_CONTENT_MODEL);
			stat.setLong(1, model.getId());
			stat.setString(2, model.getContentType());
			stat.setString(3, model.getDescription());
			stat.setString(4, model.getContentShape());
			stat.setString(5, model.getStylesheet());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding datatype model {}", model.getId(), t);
			throw new RuntimeException("Error adding datatype model " + model.getId(), t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void deleteDataModel(DataModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_CONTENT_MODEL);
			stat.setLong(1, model.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting datatype model {} ", model.getId(), t);
			throw new RuntimeException("Error deleting datatype model " + model.getId(), t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updateDataModel(DataModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_CONTENT_MODEL);
			stat.setString(1, model.getContentType());
			stat.setString(2, model.getDescription());
			stat.setString(3, model.getContentShape());
			stat.setString(4, model.getStylesheet());
			stat.setLong(5, model.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating datatype model {} ", model.getId(), t);
			throw new RuntimeException("Error updating datatype model " + model.getId(), t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	private DataModel loadContentModel(ResultSet res) throws SQLException {
		DataModel contentModel = new DataModel();
		contentModel.setId(res.getLong(1));
		contentModel.setContentType(res.getString(2));
		contentModel.setDescription(res.getString(3));
		contentModel.setContentShape(res.getString(4));
		contentModel.setStylesheet(res.getString(5));
		return contentModel;
	}

	private final String ALL_CONTENTMODEL
			= "SELECT modelid, datatype, descr, model, stylesheet FROM datatypemodels";

	private final String ADD_CONTENT_MODEL
			= "INSERT INTO datatypemodels (modelid, datatype, descr, model, stylesheet ) VALUES ( ? , ? , ? , ? , ? )";

	private static final String DELETE_CONTENT_MODEL
			= "DELETE FROM datatypemodels WHERE modelid = ? ";

	private final String UPDATE_CONTENT_MODEL
			= "UPDATE datatypemodels SET datatype = ? , descr = ? , model = ? , stylesheet = ? WHERE modelid = ? ";

}
