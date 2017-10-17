/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OAuth2ClientDetailManager extends AbstractService implements IApiOAuth2ClientDetailManager {

    private static final Logger _logger = LoggerFactory.getLogger(IApiOAuth2ClientDetailManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    public OAuth2ClientDetail getApiOAuth2ClientDetail(final String clientId) throws ApsSystemException {
        OAuth2ClientDetail apiOAuth2ClientDetail = null;
        try {
            apiOAuth2ClientDetail = this.getApiOAuth2ClientDetailDAO().loadApiOAuth2ClientDetail(clientId);
        } catch (Throwable t) {
            _logger.error("Error loading apiOAuth2ClientDetail with id '{}'", clientId, t);
            throw new ApsSystemException("Error loading apiOAuth2ClientDetail with clientId: " + clientId, t);
        }
        return apiOAuth2ClientDetail;
    }

    @Override
    public List<String> getApiOAuth2ClientDetails() throws ApsSystemException {
        List<String> apiOAuth2ClientDetails = new ArrayList<String>();
        try {
            apiOAuth2ClientDetails = this.getApiOAuth2ClientDetailDAO().loadApiOAuth2ClientDetails();
        } catch (Throwable t) {
            _logger.error("Error loading ApiOAuth2ClientDetail list", t);
            throw new ApsSystemException("Error loading ApiOAuth2ClientDetail ", t);
        }
        return apiOAuth2ClientDetails;
    }

    @Override
    public List<String> searchApiOAuth2ClientDetails(FieldSearchFilter filters[]) throws ApsSystemException {
        List<String> apiOAuth2ClientDetails = new ArrayList<String>();
        try {
            this.getApiOAuth2ClientDetailDAO().searchApiOAuth2ClientDetails(filters);
        } catch (Throwable t) {
            _logger.error("Error searching ApiOAuth2ClientDetails", t);
            throw new ApsSystemException("Error searching ApiOAuth2ClientDetails", t);
        }
        return apiOAuth2ClientDetails;
    }

    @Override
    public void addApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException {
        try {
            this.getApiOAuth2ClientDetailDAO().insertApiOAuth2ClientDetail(apiOAuth2ClientDetail);
        } catch (Throwable t) {
            _logger.error("Error adding ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error adding ApiOAuth2ClientDetail", t);
        }
    }

    @Override
    public void updateApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException {
        try {
            this.getApiOAuth2ClientDetailDAO().updateApiOAuth2ClientDetail(apiOAuth2ClientDetail);
        } catch (Throwable t) {
            _logger.error("Error updating ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error updating ApiOAuth2ClientDetail " + apiOAuth2ClientDetail, t);
        }
    }

    @Override
    public void deleteApiOAuth2ClientDetail(final String clientId) throws ApsSystemException {
        try {
            OAuth2ClientDetail apiOAuth2ClientDetail = this.getApiOAuth2ClientDetail(clientId);
            this.getApiOAuth2ClientDetailDAO().removeApiOAuth2ClientDetail(clientId);
        } catch (Throwable t) {
            _logger.error("Error deleting ApiOAuth2ClientDetail with clientId {}", clientId, t);
            throw new ApsSystemException("Error deleting ApiOAuth2ClientDetail with clientId:" + clientId, t);
        }
    }


    public void setApiOAuth2ClientDetailDAO(IApiOAuth2ClientDetailDAO apiOAuth2ClientDetailDAO) {
        this._apiOAuth2ClientDetailDAO = apiOAuth2ClientDetailDAO;
    }

    protected IApiOAuth2ClientDetailDAO getApiOAuth2ClientDetailDAO() {
        return _apiOAuth2ClientDetailDAO;
    }

    private IApiOAuth2ClientDetailDAO _apiOAuth2ClientDetailDAO;
}
