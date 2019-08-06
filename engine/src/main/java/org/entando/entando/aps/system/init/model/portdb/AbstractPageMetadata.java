/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.model.portdb;

import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @author E.Santoboni, E.Mezzano
 */
public abstract class AbstractPageMetadata implements ExtendedColumnDefinition {

    public AbstractPageMetadata() {
    }

    @DatabaseField(columnName = "code", dataType = DataType.STRING, width = 30, canBeNull = false, id = true)
    protected String _code;

    @DatabaseField(columnName = "groupcode", dataType = DataType.STRING, width = 30, canBeNull = false)
    private String _groupCode;

    @DatabaseField(columnName = "titles", dataType = DataType.LONG_STRING, canBeNull = false)
    protected String _titles;

    @DatabaseField(foreign = true, columnName = "modelcode", width = 40, canBeNull = false)
    protected PageModel _model;

    @DatabaseField(columnName = "showinmenu", dataType = DataType.SHORT, canBeNull = false)
    protected short _showInMenu;

    @DatabaseField(columnName = "extraconfig", dataType = DataType.LONG_STRING)
    protected String _extraConfig;

    @DatabaseField(columnName = "updatedat", dataType = DataType.DATE, canBeNull = true)
    protected Date _updatedAt;

    protected abstract String getTableName();

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = this.getTableName();
        String pageTableName = Page.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            pageTableName = "`" + pageTableName + "`";
        }
        return new String[]{
            "ALTER TABLE " + tableName + " " + "ADD CONSTRAINT "
            + this.getTableName() + "_cd_fk FOREIGN KEY (code) " + "REFERENCES " + pageTableName + " (code) ",
            "ALTER TABLE " + tableName + " " + "ADD CONSTRAINT "
            + this.getTableName() + "_mc_fk FOREIGN KEY (modelcode) " + "REFERENCES " + PageModel.TABLE_NAME + " (code)"};
    }

}
