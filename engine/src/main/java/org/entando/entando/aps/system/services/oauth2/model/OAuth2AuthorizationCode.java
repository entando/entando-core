package org.entando.entando.aps.system.services.oauth2.model;

import java.util.Date;

public class OAuth2AuthorizationCode {

    private String authorizationCode;
    private String clientId;
    private Date expires;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
