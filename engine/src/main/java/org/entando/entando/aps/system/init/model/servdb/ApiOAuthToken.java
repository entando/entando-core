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
@DatabaseTable(tableName = ApiOAuthToken.TABLE_NAME)
public class ApiOAuthToken {


    @DatabaseField(columnName = "accesstoken",
            dataType = DataType.STRING,
            width = 100,
            canBeNull = false, id = true)
    private String _accessToken;

    @DatabaseField(columnName = "clientid",
            dataType = DataType.STRING,
            width = 100,
            canBeNull = false)
    private String _clientId;

    @DatabaseField(columnName = "expiresin",
            dataType = DataType.DATE,
            width = 100,
            canBeNull = false)
    private Date _expiresin;


    @DatabaseField(columnName = "refreshtoken",
            dataType = DataType.STRING,
            canBeNull = false)
    private String _refreshToken;


    @DatabaseField(columnName = "granttype",
            dataType = DataType.STRING,
            width = 100,
            canBeNull = false)
    private String _grantType;


    public static final String TABLE_NAME = "api_oauth_tokens";

}
