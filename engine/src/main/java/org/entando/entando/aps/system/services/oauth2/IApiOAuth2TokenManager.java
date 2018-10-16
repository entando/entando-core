package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.springframework.security.oauth2.provider.token.TokenStore;

public interface IApiOAuth2TokenManager extends TokenStore {

    String BEAN_NAME = "OAuth2TokenManager";

    @Deprecated
    void addApiOAuth2Token(final OAuth2Token accessToken, final boolean isLocalUser) throws ApsSystemException;

    @Deprecated
    OAuth2Token getApiOAuth2Token(final String accessToken) throws ApsSystemException;

    void updateToken(final String accessToken, long seconds) throws ApsSystemException;

}
