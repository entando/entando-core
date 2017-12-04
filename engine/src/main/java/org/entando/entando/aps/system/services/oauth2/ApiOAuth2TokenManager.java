package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApiOAuth2TokenManager extends AbstractService implements IApiOAuth2TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiOAuth2TokenManager.class);
    private static final String ERROR_ADDING_TOKEN = "Error adding OAuth2Token";
    private transient final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private OAuth2TokenDAO oAuth2TokenDAO;

    public OAuth2TokenDAO getOAuth2TokenDAO() {

        return oAuth2TokenDAO;
    }

    public void setOAuth2TokenDAO(OAuth2TokenDAO oAuth2TokenDAO) {

        this.oAuth2TokenDAO = oAuth2TokenDAO;
    }

    @Override
    public void init() throws Exception {
        logger.debug("{}  initialized ", this.getClass().getName());
        // every 1 hour start the scheduler for delete expired access token
        scheduler.scheduleAtFixedRate(new ScheduledDeleteExpiredTokenThread(oAuth2TokenDAO), 0, 1, TimeUnit.HOURS);


    }

    @Override
    public void addApiOAuth2Token(final OAuth2Token accessToken, final boolean isLocalUser) throws ApsSystemException {
        try {
            this.getOAuth2TokenDAO().addAccessToken(accessToken, isLocalUser);
        } catch (ApsSystemException t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }

    }

    @Override
    public OAuth2Token getApiOAuth2Token(final String accessToken) throws ApsSystemException {
        try {
            return this.getOAuth2TokenDAO().getAccessToken(accessToken);
        } catch (ApsSystemException t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }
    }

    public void updateToken(final String accessToken, final long seconds) throws ApsSystemException {
        try {
            this.getOAuth2TokenDAO().updateAccessToken(accessToken, seconds);
        } catch (ApsSystemException t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }
    }


}
