package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledDeleteExpiredTokenThread implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ScheduledDeleteExpiredTokenThread.class);

    private IOAuth2TokenDAO tokenDAO;

    public ScheduledDeleteExpiredTokenThread(IOAuth2TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Override
    public void run() {

        logger.debug("start delete expired access token");
        try {
            if (tokenDAO != null) {

                tokenDAO.deleteExpiredToken();
            }
        } catch (ApsSystemException e) {
            logger.error("Error in deleteExpiredToken {}", e);
        }
        logger.debug("end delete expired access token");


    }

}
