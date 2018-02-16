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
package org.entando.entando.aps.system.init.model.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ApiOAuthConsumer.TABLE_NAME)
public class ApiOAuthConsumer {

    @DatabaseField(columnName = "consumerkey",
            dataType = DataType.STRING,
            width = 100,
            canBeNull = false)
    private String consumerKey;

    @DatabaseField(columnName = "consumersecret",
            dataType = DataType.STRING,
            width = 100,
            canBeNull = false)
    private String consumerSecret;

    @DatabaseField(columnName = "name",
            dataType = DataType.STRING,
            canBeNull = true)
    private String name;

    @DatabaseField(columnName = "description",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String description;


    @DatabaseField(columnName = "callbackurl",
            dataType = DataType.LONG_STRING)
    private String callbackUrl;


    @DatabaseField(columnName = "scope",
            dataType = DataType.STRING,
            canBeNull = true)
    private String scope;

    @DatabaseField(columnName = "authorizedgranttypes",
            dataType = DataType.STRING,
            canBeNull = true)
    private String authorizedGrantTypes;

    @DatabaseField(columnName = "expirationdate",
            dataType = DataType.DATE)
    private Date expirationDate;

    @DatabaseField(columnName = "issueddate",
            dataType = DataType.DATE)
    private Date issuedDate;


    public static final String TABLE_NAME = "api_oauth_consumers";

}
