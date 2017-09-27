/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General  License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General  License for more
 * details.
 */
package org.entando.entando.aps.system.services.dataobject;

import com.agiletec.aps.system.ApsSystemUtils;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

import java.sql.Connection;

/**
 *
 * @author E.Santoboni
 */
public class DataObjectUpdaterDAO extends DataObjectDAO implements IDataObjectUpdaterDAO {

    @Override
    public void reloadDataObjectCategoryReferences(DataObject content) {
        ApsSystemUtils.getLogger().debug("reload PUBLIC references for content " + content.getId());
        if (content.isOnLine()) {
            Connection conn = null;
            try {
                conn = this.getConnection();
                conn.setAutoCommit(false);
                this.executeReloadDataObjectReferences(content, conn);
                conn.commit();
            } catch (Throwable t) {
                this.executeRollback(conn);
                ApsSystemUtils.logThrowable(t, this, "reloadDataObjectReferences");
                throw new RuntimeException("Error reloading references - DataObject " + content.getId(), t);
            } finally {
                this.closeConnection(conn);
            }
        }
    }

}
