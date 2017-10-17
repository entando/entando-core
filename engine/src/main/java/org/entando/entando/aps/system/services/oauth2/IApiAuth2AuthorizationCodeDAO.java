package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AuthorizationCode;

import java.util.List;

public interface IApiAuth2AuthorizationCodeDAO {

    List<String> searchOAuth2AuthorizationCodes(FieldSearchFilter[] filters);

    OAuth2AuthorizationCode loadOAuth2AuthorizationCode(final String authorizationCode);

    List<String> loadOAuth2AuthorizationCodes();

    void removeOAuth2AuthorizationCode(final String authorizationCode);

    void updateOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode);

    void insertOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode);

}
