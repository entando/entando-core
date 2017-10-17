package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AuthorizationCode;

import java.util.List;

public interface IApiOAuth2AuthorizationCodeManager {
    OAuth2AuthorizationCode getOAuth2AuthorizationCode(final String authorizationCode) throws ApsSystemException;

    List<String> getOAuth2AuthorizationCodes() throws ApsSystemException;

    List<String> searchOAuth2AuthorizationCodes(FieldSearchFilter filters[]) throws ApsSystemException;

    void addOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) throws ApsSystemException;

    void updateOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) throws ApsSystemException;

    void deleteOAuth2AuthorizationCode(final String authorizationCode) throws ApsSystemException;
}
