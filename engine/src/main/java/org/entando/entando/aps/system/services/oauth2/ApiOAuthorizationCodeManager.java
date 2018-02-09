package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import org.entando.entando.aps.system.services.oauth2.model.AuthorizationCode;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApiOAuthorizationCodeManager extends AbstractService implements IApiOAuthorizationCodeManager {

    private final List<AuthorizationCode> authorizationCodes = new ArrayList<>();
    private static final Logger _logger = LoggerFactory.getLogger(ApiOAuthorizationCodeManager.class);
    @Override
    public void init() throws Exception {
        _logger.debug("{}  initialized ", this.getClass().getName());

    }

    public void addAuthorizationCode(final AuthorizationCode authCode) {
        for (Iterator<AuthorizationCode> iterator = authorizationCodes.iterator(); iterator.hasNext();) {
            AuthorizationCode authorizationCode = iterator.next();
            if (authorizationCode.getExpires() < System.currentTimeMillis()) {
                iterator.remove();
            }
        }

        authorizationCodes.add(authCode);
    }


    public AuthorizationCode getAuthorizationCode(final String authCode) {
        int i = 0;
        for (final AuthorizationCode authorizationCode : authorizationCodes) {
            if (authorizationCode.getAuthorizationCode().equals(authCode)) {
                return authorizationCodes.remove(i);
            }
            i++;
        }
        return null;

    }

    @Override
    public boolean verifyAccess(String clientId, String clientSecret, IOAuthConsumerManager consumerManager) throws Throwable {
        final ConsumerRecordVO record = consumerManager.getConsumerRecord(clientId);
        final Date now = new Date();

        if (null != record) {
            if (!record.getKey().equals(clientId)) {
                _logger.info("client id does not match");
                return false;
            } else if (!record.getSecret().equals(clientSecret)) {
                _logger.info("client secret does not match");
                return false;
            } else if (record.getExpirationDate().getTime() < now.getTime()) {
                _logger.info("client secret expired");
                return false;
            }
            // finally
            return true;
        } else {
            _logger.info("client ID not found");
        }
        return false;
    }

    @Override
    public boolean verifyCode(final String authCode, final String source) {
        final AuthorizationCode code = getAuthorizationCode(authCode);
        if (code == null || !code.getSource().equals(source)) {
            _logger.info("authcode does not match");
            return false;
        } else if (System.currentTimeMillis() > code.getExpires()) {
            _logger.warn("OAuth2 code '{}' is expired", code);
            return false;

        }
        return true;
    }
}
