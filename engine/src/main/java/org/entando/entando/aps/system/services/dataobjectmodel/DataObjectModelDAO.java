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
package org.entando.entando.aps.system.services.dataobjectmodel;

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
public class DataObjectModelDAO extends AbstractDAO implements IDataObjectModelDAO {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectModelDAO.class);

	@Override
	public Map<Long, DataObjectModel> loadDataModels() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<Long, DataObjectModel> models = new HashMap<Long, DataObjectModel>();
		String query = ALL_DATA_UX;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(query);
			while (res.next()) {
				DataObjectModel contentModel = loadContentModel(res);
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
	public void addDataModel(DataObjectModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (0 == model.getId()) {
				model.setId(this.extractNextId(conn));
			}
			stat = conn.prepareStatement(ADD_DATA_UX);
			stat.setLong(1, model.getId());
			stat.setString(2, model.getDataType());
			stat.setString(3, model.getDescription());
			stat.setString(4, model.getShape());
			stat.setString(5, model.getStylesheet());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding datatype ux {}", model.getId(), t);
			throw new RuntimeException("Error adding datatype ux " + model.getId(), t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void deleteDataModel(DataObjectModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_DATA_UX);
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
	public void updateDataModel(DataObjectModel model) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_DATA_UX);
			stat.setString(1, model.getDataType());
			stat.setString(2, model.getDescription());
			stat.setString(3, model.getShape());
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

	private DataObjectModel loadContentModel(ResultSet res) throws SQLException {
		DataObjectModel contentModel = new DataObjectModel();
		contentModel.setId(res.getLong(1));
		contentModel.setDataType(res.getString(2));
		contentModel.setDescription(res.getString(3));
		contentModel.setShape(res.getString(4));
		contentModel.setStylesheet(res.getString(5));
		return contentModel;
	}

	protected long extractNextId(Connection conn) {
		long id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(EXTRACT_NEXT_ID);
			res.next();
			id = res.getLong(1) + 1; // N.B.: funziona anche per il primo record
		} catch (Throwable t) {
			_logger.error("Error extracting next id", t);
			throw new RuntimeException("Error extracting next id", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}

	private final String ALL_DATA_UX
			= "SELECT modelid, datatype, descr, model, stylesheet FROM dataobjectmodels";

	private final String ADD_DATA_UX
			= "INSERT INTO dataobjectmodels (modelid, datatype, descr, model, stylesheet ) VALUES ( ? , ? , ? , ? , ? )";

	private static final String DELETE_DATA_UX
			= "DELETE FROM dataobjectmodels WHERE modelid = ? ";

	private final String UPDATE_DATA_UX
			= "UPDATE dataobjectmodels SET datatype = ? , descr = ? , model = ? , stylesheet = ? WHERE modelid = ? ";

	private final String EXTRACT_NEXT_ID
			= "SELECT MAX(modelid) FROM dataobjectmodels ";

}
