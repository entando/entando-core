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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

import java.sql.Connection;

/**
 * EVOLUZIONE DEL CORE - AGGIUNTA FIRST EDITOR e funzioni aggiornamento referenze
 * @author E.Santoboni
 */
public class ContentUpdaterDAO extends ContentDAO implements IContentUpdaterDAO {
	
	@Override
	public void reloadWorkContentCategoryReferences(Content content) {
		ApsSystemUtils.getLogger().debug("reload WORK facet references for content " + content.getId());
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeReloadWorkContentReferences(content, conn);;
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			ApsSystemUtils.logThrowable(t, this, "reloadWorkContentFacetReferences");
			throw new RuntimeException("Error reloading references - Content " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	@Override
	public void reloadPublicContentCategoryReferences(Content content) {
		ApsSystemUtils.getLogger().debug("reload PUBLIC facet references for content " + content.getId());
		if (content.isOnLine()) {
			Connection conn = null;
			try {
				conn = this.getConnection();
				conn.setAutoCommit(false);
				this.executeReloadPublicContentReferences(content, conn);
				conn.commit();
			} catch (Throwable t) {
				this.executeRollback(conn);
				ApsSystemUtils.logThrowable(t, this, "reloadPublicContentFacetReferences");
				throw new RuntimeException("Error reloading references - Content " + content.getId(), t);
			} finally {
				this.closeConnection(conn);
			}
		}
	}
	
}