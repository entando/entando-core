package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiOAuth2TokenManager extends AbstractService implements IApiOAuth2TokenManager {

    private Logger _logger = LoggerFactory.getLogger(ApiOAuth2TokenManager.class);

    private OAuth2TokenDAO oAuth2TokenDAO;

    public OAuth2TokenDAO getOAuth2TokenDAO() {

        return oAuth2TokenDAO;
    }

    public void setOAuth2TokenDAO(OAuth2TokenDAO oAuth2TokenDAO) {
        this.oAuth2TokenDAO = oAuth2TokenDAO;
    }

    @Override
    public void init() throws Exception {
        _logger.info(this.getClass().getCanonicalName() + " initialized");
    }

    @Override
    public void addApiOAuth2Token(OAuth2Token accessToken) throws ApsSystemException {

        try {
            this.getOAuth2TokenDAO().addAccessToken(accessToken);
        } catch (Throwable t) {
            _logger.error("Error adding ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error adding OAuth2Token", t);
        }

    }

    @Override
    public OAuth2Token getApiOAuth2Token(final String accessToken) throws ApsSystemException {
        try {
            return this.getOAuth2TokenDAO().getAccessToken(accessToken);
        } catch (Throwable t) {
            _logger.error("Error adding ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error adding OAuth2Token", t);
        }
    }


}
