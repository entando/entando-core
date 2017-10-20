package org.entando.entando.aps.system.services.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledDeleteExpiredTokenThread extends Thread {

    private static Logger _logger = LoggerFactory.getLogger(ScheduledDeleteExpiredTokenThread.class);

    private IOAuth2TokenDAO tokenDAO;


    @Override
    public void run() {

        _logger.info("start delete expired access token");
        tokenDAO.deleteExpiredToken();
        _logger.info("end delete expired access token");

    }

    public void setTokenDAO(IOAuth2TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }


    public IOAuth2TokenDAO getTokenDAO() {
        return tokenDAO;
    }
}
