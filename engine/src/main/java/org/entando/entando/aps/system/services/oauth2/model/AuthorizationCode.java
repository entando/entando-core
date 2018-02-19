package org.entando.entando.aps.system.services.oauth2.model;

import java.io.Serializable;

public class AuthorizationCode implements Comparable<AuthorizationCode> , Serializable{

    private String authorizationCode;
    private Long expires;
    private String clientId;
    private String source;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(final String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    @Override
    public int compareTo(AuthorizationCode o) {
        return this.expires.compareTo(o.expires);
    }
}
