package org.entando.entando.aps.system.services.oauth2.model;


import java.util.Date;

public class ApiOAuth2ClientDetail {

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getClientId() {
        return _clientId;
    }

    public void setClientId(String clientId) {
        this._clientId = clientId;
    }

    public String getClientSecret() {
        return _clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this._clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return _redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this._redirectUri = redirectUri;
    }

    public String getClientUri() {
        return _clientUri;
    }

    public void setClientUri(String clientUri) {
        this._clientUri = clientUri;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getIconUri() {
        return _iconUri;
    }

    public void setIconUri(String iconUri) {
        this._iconUri = iconUri;
    }

    public Date getIssuedAt() {
        return _issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this._issuedAt = issuedAt;
    }

    public Date getExpiresIn() {
        return _expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this._expiresIn = expiresIn;
    }


    private int _id;
    private String _name;
    private String _clientId;
    private String _clientSecret;
    private String _redirectUri;
    private String _clientUri;
    private String _description;
    private String _iconUri;
    private Date _issuedAt;
    private Date _expiresIn;

}
