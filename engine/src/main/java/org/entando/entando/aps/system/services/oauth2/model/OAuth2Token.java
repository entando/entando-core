package org.entando.entando.aps.system.services.oauth2.model;

import java.util.Date;

public class OAuth2Token {

    private String _accessToken;
    private String _clientId;
    private Date _expiresIn;
    private String _refreshToken;
    private String _grantType;

    public String getAccessToken() {
        return _accessToken;
    }

    public void setAccessToken(String _accessToken) {
        this._accessToken = _accessToken;
    }

    public String getClientId() {
        return _clientId;
    }

    public void setClientId(String _clientId) {
        this._clientId = _clientId;
    }

    public Date getExpiresIn() {
        return _expiresIn;
    }

    public void setExpiresIn(Date _expiresIn) {
        this._expiresIn = _expiresIn;
    }

    public String getRefreshToken() {
        return _refreshToken;
    }

    public void setRefreshToken(String _refreshToken) {
        this._refreshToken = _refreshToken;
    }

    public String getGrantType() {
        return _grantType;
    }

    public void setGrantType(String _grantType) {
        this._grantType = _grantType;
    }
}
