/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;

import java.util.List;

public interface IApiOAuth2ClientDetailManager {
    String NAME_FILTER_KEY = "name";
    String CLIENT_ID_FILTER_KEY = "clientid";
    String CLIENT_SECRET_FILTER_KEY = "clientSecret";
    String CLIENT_REDIRECT_URI_FILTER_KEY = "redirecturi";
    String CLIENT_URI_FILTER_KEY = "clienturi";
    String DESCRIPTION_FILTER_KEY = "description";
    String ICON_URI_FILTER_KEY = "iconuri";
    String ISSUED_AT_FILTER_KEY = "issuedat";
    String EXPIRES_IN_FILTER_KEY = "expiresin";
    String SCOPE_FILTER_KEY = "scope";
    String AUTHORIZED_GRANT_TYPE_FILTER_KEY =  "authorizedgranttypes";


    OAuth2ClientDetail getApiOAuth2ClientDetail(String clientId) throws ApsSystemException;

    List<String> getApiOAuth2ClientDetails() throws ApsSystemException;

    List<String> searchApiOAuth2ClientDetails(FieldSearchFilter filters[]) throws ApsSystemException;

    void addApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void updateApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void deleteApiOAuth2ClientDetail(final String clientId) throws ApsSystemException;

}