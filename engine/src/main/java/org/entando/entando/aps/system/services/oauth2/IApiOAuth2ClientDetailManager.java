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

    OAuth2ClientDetail getApiOAuth2ClientDetail(String clientId) throws ApsSystemException;

    List<String> getApiOAuth2ClientDetails() throws ApsSystemException;

    List<String> searchApiOAuth2ClientDetails(FieldSearchFilter filters[]) throws ApsSystemException;

    void addApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void updateApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void deleteApiOAuth2ClientDetail(final String clientId) throws ApsSystemException;

}