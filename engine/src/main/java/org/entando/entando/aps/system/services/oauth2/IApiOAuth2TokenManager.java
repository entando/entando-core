package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;

public interface IApiOAuth2TokenManager {

    String BEAN_NAME = "OAuth2TokenManager";

    void addApiOAuth2Token(final OAuth2Token accessToken,final boolean isLocalUser) throws ApsSystemException;

    OAuth2Token getApiOAuth2Token(final String accessToken) throws ApsSystemException;

    void updateToken(final String accessToken, long seconds) throws ApsSystemException;


}
