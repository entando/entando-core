/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;

import java.util.List;

public interface IApiOAuth2ClientDetailDAO {

    List<String> searchApiOAuth2ClientDetails(FieldSearchFilter[] filters);

    List<String> loadApiOAuth2ClientDetails();

    OAuth2ClientDetail loadApiOAuth2ClientDetail(final String id);

    void removeApiOAuth2ClientDetail(final String clientId);

    void updateApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail);

    void insertApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail);


}