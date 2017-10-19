package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;

public interface IApiOAuth2TokenManager {

    String BEAN_NAME = "OAuth2TokenManager";

    void addApiOAuth2Token(final OAuth2Token accessToken) throws ApsSystemException;

    OAuth2Token getApiOAuth2Token(final String accessToken) throws ApsSystemException;
}
