package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;

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
        // every 30 min start the scheduler for delete expired access token
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        ScheduledDeleteExpiredTokenThread expiredTokenThread = new ScheduledDeleteExpiredTokenThread();
        expiredTokenThread.setTokenDAO(this.getOAuth2TokenDAO());
        scheduledThreadPool.scheduleAtFixedRate(expiredTokenThread,0, 30, TimeUnit.MINUTES);


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
