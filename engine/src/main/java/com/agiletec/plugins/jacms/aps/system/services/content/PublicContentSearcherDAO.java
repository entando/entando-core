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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;

/**
 * @author E.Santoboni
 */
public class PublicContentSearcherDAO extends AbstractContentSearcherDAO implements IPublicContentSearcherDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(PublicContentSearcherDAO.class);
	
	@Override
	public List<String> loadPublicContentsId(String contentType, String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		return this.loadPublicContentsId(contentType, categories, false, filters, userGroupCodes);
	}
	
	@Override
	public List<String> loadPublicContentsId(String contentType, String[] categories, boolean orClauseCategoryFilter, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		if (contentType != null && contentType.trim().length()>0) {
			EntitySearchFilter typeFilter = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, contentType, false);
			filters = this.addFilter(filters, typeFilter);
		}
		return this.loadPublicContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
	}
	
	@Override
	public List<String> loadPublicContentsId(String[] categories, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		return this.loadPublicContentsId(categories, false, filters, userGroupCodes);
	}
	
	@Override
	public List<String> loadPublicContentsId(String[] categories, 
			boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		Set<String> groupCodes = new HashSet<String>();
		if (null != userGroupCodes) {
			groupCodes.addAll(userGroupCodes);
		}
		groupCodes.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter onLineFilter = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
		filters = this.addFilter(filters, onLineFilter);
		List<String> contentsId = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, categories, orClauseCategoryFilter, groupCodes, false, conn);
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
	protected PreparedStatement buildStatement(EntitySearchFilter[] filters, 
			String[] categories, boolean orClauseCategoryFilter, 
			Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		String query = this.createQueryString(filters, categories, orClauseCategoryFilter, groupsForSelect, selectAll);
		//System.out.println("QUERY : " + query);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = super.addAttributeFilterStatementBlock(filters, index, stat);
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
			if (groupsForSelect != null) {
				index = this.addGroupStatementBlock(groupsForSelect, index, stat);
			}
			if (categories != null) {
				for (int i=0; i<categories.length; i++) {
					stat.setString(++index, categories[i]);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating statement",  t);
			throw new RuntimeException("Error creating statement", t);
			//processDaoException(t, "Errore in fase di creazione statement", "buildStatement");
		}
		return stat;
	}
	
	@Override
	protected void addGroupsQueryBlock(StringBuffer query, Collection<String> userGroupCodes) {
		query.append(" ( ");
		int size = userGroupCodes.size();
		for (int i=0; i<size; i++) {
			if (i!=0) {
				query.append("OR ");
			}
			query.append("contents.maingroup = ? ");
		}
		query.append(" OR contents.contentid IN ( SELECT contentid FROM ")
				.append(this.getContentRelationsTableName()).append(" WHERE ");
		for (int i=0; i<size; i++) {
			if (i!=0) query.append("OR ");
			query.append(this.getContentRelationsTableName()).append(".refgroup = ? ");
		}
		query.append(") ");
		query.append(") ");
	}
	
	@Override
	protected int addGroupStatementBlock(Collection<String> groupCodes, int index, PreparedStatement stat) throws Throwable {
		List<String> groups = new ArrayList<String>(groupCodes);
		for (int i=0; i<groups.size(); i++) {
			String groupName = groups.get(i);
			stat.setString(++index, groupName);
		}
		for (int i=0; i<groups.size(); i++) {
			String groupName = groups.get(i);
			stat.setString(++index, groupName);
		}
		return index;
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "contentsearch";
	}
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "contentid";
	}
	@Override
	protected String getContentRelationsTableName() {
		return "contentrelations";
	}
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "contentattributeroles";
	}
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "contentid";
	}
	
}