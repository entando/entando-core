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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class WorkContentSearcherDAO extends AbstractContentSearcherDAO implements IWorkContentSearcherDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(WorkContentSearcherDAO.class);
	
	@Override
	public List<String> loadContentsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		return this.loadContentsId(null, false, filters, userGroupCodes);
	}
	
	@Override
	public List<String> loadContentsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		return this.loadContentsId(categories, false, filters, userGroupCodes);
	}
	
	@Override
	public List<String> loadContentsId(String[] categories, boolean orClauseCategoryFilter, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		List<String> contentsId = new ArrayList<String>();
		if (userGroupCodes == null || userGroupCodes.isEmpty()) {
			return contentsId;
		}
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, categories, orClauseCategoryFilter, userGroupCodes, false, conn);
			result = stat.executeQuery();
            while (result.next()) {
                String id = result.getString(this.getMasterTableIdFieldName());
                if (!contentsId.contains(id)) {
                    contentsId.add(id);
                }
            }
		} catch (Throwable t) {
			_logger.error("Error loading contents id list",  t);
			throw new RuntimeException("Error loading contents id list", t);
			//processDaoException(t, "Errore in caricamento lista id contenuti", "loadContentsId");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return contentsId;
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "workcontentsearch";
	}
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "contentid";
	}
	@Override
	protected String getContentRelationsTableName() {
		return "workcontentrelations";
	}
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "workcontentattributeroles";
	}
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "contentid";
	}
	
}