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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author spuddu
 *
 */
@DatabaseTable(tableName = WidgetConfigDraft.TABLE_NAME)
public class WidgetConfigDraft implements ExtendedColumnDefinition {

    public WidgetConfigDraft() {
    }

    @DatabaseField(foreign = true, columnName = "pagecode",
            width = 30,
            canBeNull = false)
    private Page _page;

    @DatabaseField(columnName = "framepos",
            dataType = DataType.INTEGER,
            canBeNull = false)
    private int _framePos;

    @DatabaseField(foreign = true, columnName = "widgetcode",
            width = 40,
            canBeNull = false)
    private WidgetCatalog _widget;

    @DatabaseField(columnName = "config",
            dataType = DataType.LONG_STRING)
    private String _config;

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = TABLE_NAME;
        String pageTableName = Page.TABLE_NAME;
        String widgetCatalogTableName = WidgetCatalog.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            pageTableName = "`" + pageTableName + "`";
            widgetCatalogTableName = "`" + widgetCatalogTableName + "`";
        }
        String[] queries = new String[3];
        queries[0] = "ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT "
                + TABLE_NAME + "_pkey PRIMARY KEY(pagecode , framepos)";
        queries[1] = "ALTER TABLE " + tableName + " "
                + "ADD CONSTRAINT " + TABLE_NAME + "_pc_fkey FOREIGN KEY (pagecode) "
                + "REFERENCES " + pageTableName + " (code)";
        queries[2] = "ALTER TABLE " + tableName + " "
                + "ADD CONSTRAINT " + TABLE_NAME + "_wc_fkey FOREIGN KEY (widgetcode) "
                + "REFERENCES " + widgetCatalogTableName + " (code)";
        return queries;
    }

    public static final String TABLE_NAME = "widgetconfig_draft";

}
/*
CREATE TABLE widgetconfig_draft
(
  pagecode character varying(30) NOT NULL,
  framepos integer NOT NULL,
  widgetcode character varying(40) NOT NULL,
  config text,
  CONSTRAINT widgetconfig_draft_pkey PRIMARY KEY (pagecode, framepos),
  CONSTRAINT widgetconfig_draft_pagecode_fkey FOREIGN KEY (pagecode)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT widgetconfig_draft_widgetcode_fkey FOREIGN KEY (widgetcode)
      REFERENCES widgetcatalog (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
*/
