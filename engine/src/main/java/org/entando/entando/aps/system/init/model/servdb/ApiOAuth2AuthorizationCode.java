package org.entando.entando.aps.system.init.model.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = ApiOAuth2AuthorizationCode.TABLE_NAME)
public class ApiOAuth2AuthorizationCode {

    public static final String TABLE_NAME = "api_oauth2authorization_code";

    @DatabaseField(columnName = "authorizationcode",
            dataType = DataType.LONG_STRING,
            canBeNull = false, id = true)
    private String _authorizationCode;

    @DatabaseField(columnName = "clientid",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String _clientId;

    @DatabaseField(columnName = "expires",
            dataType = DataType.DATE,
            canBeNull = false)
    private Date _expires;
}
