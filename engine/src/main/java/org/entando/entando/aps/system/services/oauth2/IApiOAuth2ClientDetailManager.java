/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.ApiOAuth2ClientDetail;

import java.util.List;

public interface IApiOAuth2ClientDetailManager {

    ApiOAuth2ClientDetail getApiOAuth2ClientDetail(int id) throws ApsSystemException;

    List<Integer> getApiOAuth2ClientDetails() throws ApsSystemException;

    List<Integer> searchApiOAuth2ClientDetails(FieldSearchFilter filters[]) throws ApsSystemException;

    void addApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void updateApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException;

    void deleteApiOAuth2ClientDetail(int id) throws ApsSystemException;

}