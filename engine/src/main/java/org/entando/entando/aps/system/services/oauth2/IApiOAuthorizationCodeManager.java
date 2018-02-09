package org.entando.entando.aps.system.services.oauth2;

import org.entando.entando.aps.system.services.oauth2.model.AuthorizationCode;

public interface IApiOAuthorizationCodeManager {

    void addAuthorizationCode(final AuthorizationCode authCode);

    AuthorizationCode getAuthorizationCode(final String authCode);

    /**
     * Verify access before getting the access token
     * @param clientId
     * @param clientSecret
     * @param consumerManager
     * @return boolean
     * @throws Throwable
     */
    boolean verifyAccess(final String clientId, final String clientSecret, IOAuthConsumerManager consumerManager) throws  Throwable;

    boolean verifyCode(final String authCode, final String source);
}
