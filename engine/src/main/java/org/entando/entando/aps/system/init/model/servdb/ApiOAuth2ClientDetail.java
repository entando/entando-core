package org.entando.entando.aps.system.init.model.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = ApiOAuth2ClientDetail.TABLE_NAME)
public class ApiOAuth2ClientDetail {

    @DatabaseField(columnName = "clientid",
            dataType = DataType.STRING,
            canBeNull = false, id=true)
    private String _clientId;

    @DatabaseField(columnName = "name",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _name;


    @DatabaseField(columnName = "clientsecret",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _clientSecret;


    @DatabaseField(columnName = "scope",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _scope;

    @DatabaseField(columnName = "authorizedgranttypes",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _authorizedGrantTypes;

    @DatabaseField(columnName = "redirecturi",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _redirectUri;

    @DatabaseField(columnName = "clienturi",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _clientUri;

    @DatabaseField(columnName = "description",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _description;

    @DatabaseField(columnName = "iconuri",
            dataType = DataType.STRING,
            canBeNull = true)
    private String _iconUri;

    @DatabaseField(columnName = "issuedat",
            dataType = DataType.DATE,
            canBeNull = true)
    private Date _issuedAt;

    @DatabaseField(columnName = "expiresin",
            dataType = DataType.DATE,
            canBeNull = true)
    private Date _expiresIn;

    public static final String TABLE_NAME = "api_oauth2client_detail";
}
