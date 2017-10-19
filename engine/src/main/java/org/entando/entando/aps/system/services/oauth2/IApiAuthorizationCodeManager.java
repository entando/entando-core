package org.entando.entando.aps.system.services.oauth2;

import org.entando.entando.aps.system.services.oauth.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.AuthorizationCode;

public interface IApiAuthorizationCodeManager {

    void addAuthorizationCode(final AuthorizationCode authCode);

    AuthorizationCode getAuthorizationCode(final String authCode);

    /**
     * Verify access before getting the access token
     * @param clientId
     * @param clientSecret
     * @param clientDetailManager
     * @return boolean
     * @throws Throwable
     */
    boolean verifyAccess(final String clientId, final String clientSecret, IApiOAuth2ClientDetailManager clientDetailManager) throws  Throwable;

    boolean verifyCode(final String authCode, final String source);
}
