package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AuthorizationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OAuth2AuthorizationCodeManager extends AbstractService implements IOAuth2AuthorizationCodeManager {

    private static final Logger _logger = LoggerFactory.getLogger(OAuth2AuthorizationCodeManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    public OAuth2AuthorizationCode getOAuth2AuthorizationCode(final String authorizationCode) throws ApsSystemException {
        OAuth2AuthorizationCode oAuth2AuthorizationCode = null;
        try {
            oAuth2AuthorizationCode = this.getOAuth2AuthorizationCodeDAO().loadOAuth2AuthorizationCode(authorizationCode);
        } catch (Throwable t) {
            _logger.error("Error loading oAuth2AuthorizationCode with authorizationCode '{}'", authorizationCode, t);
            throw new ApsSystemException("Error loading oAuth2AuthorizationCode with authorizationCode: " + authorizationCode, t);
        }
        return oAuth2AuthorizationCode;
    }

    @Override
    public List<String> getOAuth2AuthorizationCodes() throws ApsSystemException {
        List<String> oAuth2AuthorizationCodes = new ArrayList<String>();
        try {
            oAuth2AuthorizationCodes = this.getOAuth2AuthorizationCodeDAO().loadOAuth2AuthorizationCodes();
        } catch (Throwable t) {
            _logger.error("Error loading OAuth2AuthorizationCode list", t);
            throw new ApsSystemException("Error loading OAuth2AuthorizationCode ", t);
        }
        return oAuth2AuthorizationCodes;
    }

    @Override
    public List<String> searchOAuth2AuthorizationCodes(FieldSearchFilter filters[]) throws ApsSystemException {
        List<String> oAuth2AuthorizationCodes = new ArrayList<String>();
        try {
            oAuth2AuthorizationCodes = this.getOAuth2AuthorizationCodeDAO().searchOAuth2AuthorizationCodes(filters);
        } catch (Throwable t) {
            _logger.error("Error searching OAuth2AuthorizationCodes", t);
            throw new ApsSystemException("Error searching OAuth2AuthorizationCodes", t);
        }
        return oAuth2AuthorizationCodes;
    }

    @Override
    public void addOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) throws ApsSystemException {
        try {
            this.getOAuth2AuthorizationCodeDAO().insertOAuth2AuthorizationCode(oAuth2AuthorizationCode);
        } catch (Throwable t) {
            _logger.error("Error adding OAuth2AuthorizationCode", t);
            throw new ApsSystemException("Error adding OAuth2AuthorizationCode", t);
        }
    }

    @Override
    public void updateOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) throws ApsSystemException {
        try {
            this.getOAuth2AuthorizationCodeDAO().updateOAuth2AuthorizationCode(oAuth2AuthorizationCode);
        } catch (Throwable t) {
            _logger.error("Error updating OAuth2AuthorizationCode", t);
            throw new ApsSystemException("Error updating OAuth2AuthorizationCode " + oAuth2AuthorizationCode, t);
        }
    }

    @Override
    public void deleteOAuth2AuthorizationCode(final String authorizationCode) throws ApsSystemException {
        try {
            OAuth2AuthorizationCode oAuth2AuthorizationCode = this.getOAuth2AuthorizationCode(authorizationCode);
            this.getOAuth2AuthorizationCodeDAO().removeOAuth2AuthorizationCode(authorizationCode);
        } catch (Throwable t) {
            _logger.error("Error deleting OAuth2AuthorizationCode with authorizationCode {}", authorizationCode, t);
            throw new ApsSystemException("Error deleting OAuth2AuthorizationCode with authorizationCode:" + authorizationCode, t);
        }
    }


    public void setOAuth2AuthorizationCodeDAO(IOAuth2AuthorizationCodeDAO oAuth2AuthorizationCodeDAO) {
        this._apiOAuth2AuthorizationCodeDAO = oAuth2AuthorizationCodeDAO;
    }

    protected IOAuth2AuthorizationCodeDAO getOAuth2AuthorizationCodeDAO() {
        return _apiOAuth2AuthorizationCodeDAO;
    }

    private IOAuth2AuthorizationCodeDAO _apiOAuth2AuthorizationCodeDAO;


}
