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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

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
			this.flowResult(contentsId, filters, result);
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