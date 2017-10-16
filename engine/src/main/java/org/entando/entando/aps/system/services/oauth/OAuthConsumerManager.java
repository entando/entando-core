/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.oauth;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;
import org.entando.entando.aps.system.services.oauth.model.EntandoOAuthAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager of consumers, access token (stored in database and in local cache)
 * and request tokens (stored in local cache). Special thanks to Praveen
 * Alavilli and OAuth examples.
 * http://oauth.googlecode.com/svn/code/java/example/oauth-provider/src/net/oauth/example/provider/core/SampleOAuthProvider.java
 *
 * @author Praveen Alavilli - E.Santoboni
 */
public class OAuthConsumerManager extends AbstractService implements IOAuthConsumerManager {

    private static final Logger _logger = LoggerFactory.getLogger(OAuthConsumerManager.class);

    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    protected void release() {
        super.release();
        //this.getConsumers().clear();
        //this.getUnauthorizedTokensCache().clear();
        //this.getAuthorizedTokensCache().clear();
    }

	/*
    public OAuthConsumer getConsumer(OAuthMessage requestMessage) throws IOException, OAuthProblemException {
        String consumerKey = requestMessage.getConsumerKey();
        OAuthConsumer consumer = null;
        try {
            consumer = this.getConsumers().get(consumerKey);
            if (consumer == null) {
                consumer = this.getConsumerDAO().getConsumer(consumerKey);
                if (null != consumer) {
                    this.getConsumers().put(consumerKey, consumer);
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting consumer by key '{}'", consumerKey, t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumer", "Error extracting consumer by key '" + consumerKey + "'");
            throw new RuntimeException("Error extracting consumer by key '" + consumerKey + "'");
        }
        if (consumer == null) {
            OAuthProblemException problem = new OAuthProblemException("Invalid Consumer - key '" + consumerKey + "'");
            throw problem;
        }
        return consumer;
    }

    public synchronized OAuthAccessor getAuthorizedAccessor(OAuthMessage requestMessage)
            throws IOException, OAuthProblemException {
        EntandoOAuthAccessor accessor = null;
        String consumerToken = requestMessage.getToken();
        try {
            OAuthConsumer consumer = this.getConsumer(requestMessage);
            accessor = this.getAuthorizedTokensCache().get(consumerToken);
            if (null == accessor) {
                accessor = this.getTokenDAO().getAccessor(consumerToken, consumer);
                if (null != accessor) {
                    this.getAuthorizedTokensCache().put(consumerToken, accessor);
                }
            }
			if (null != accessor) {
				accessor.setLastAccess(new Date());
				TokenUpdaterThread thread = new TokenUpdaterThread(consumerToken, this.getTokenTimeValidity(), this.getTokenDAO());
    			String threadName = "OauthTokenUpdater_" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
    			thread.setName(threadName);
    			thread.start();
			}
        } catch (OAuthProblemException t) {
            throw t;
        } catch (IOException io) {
            throw io;
        } catch (Throwable t) {
        	_logger.error("Error extracting access token", t);
            //ApsSystemUtils.logThrowable(t, this, "getAuthorizedAccessor", "Error extracting access token");
            throw new RuntimeException("Error extracting access token");
        }
        if (accessor == null) {
            throw new OAuthProblemException("token_expired");
        }
        return accessor;
    }
	 */
 /*
    public synchronized OAuthAccessor getAccessor(OAuthMessage requestMessage)
            throws IOException, OAuthProblemException {
        String consumerToken = requestMessage.getToken();
        OAuthAccessor accessor = this.getUnauthorizedTokensCache().get(consumerToken);
        if (accessor == null) {
            throw new OAuthProblemException("token_expired");
        }
        return accessor;
    }
	 */
 /*
     * Set the access token
	 */
 /*
    public synchronized void markAsAuthorized(OAuthAccessor accessor, String username) throws OAuthException {
        try {
            String requestToken = accessor.requestToken;
            OAuthAccessor unauthorizedAccessor = this.getUnauthorizedTokensCache().get(requestToken);
            if (null == unauthorizedAccessor) {
                throw new OAuthException("Invalid token for user '" + username + "'");
            }
            unauthorizedAccessor.setProperty("user", username);
            unauthorizedAccessor.setProperty("authorized", Boolean.TRUE);
        } catch (OAuthException oe) {
            throw oe;
        } catch (Throwable t) {
        	_logger.error("Error while mark As Authorized request token", t);
            //ApsSystemUtils.logThrowable(t, this, "markAsAuthorized", "Error while mark As Authorized request token");
        }
    }

    public synchronized void generateRequestToken(OAuthAccessor accessor) throws OAuthException {
        try {
            String consumerKey = (String) accessor.consumer.getProperty("name");
            String token_data = consumerKey + System.nanoTime();
            String token = DigestUtils.md5Hex(token_data);
            String secret_data = consumerKey + System.nanoTime() + token;
            String secret = DigestUtils.md5Hex(secret_data);
            accessor.requestToken = token;
            accessor.tokenSecret = secret;
            accessor.accessToken = null;
            this.getUnauthorizedTokensCache().put(token, accessor);
        } catch (Throwable t) {
        	_logger.error("Error generating request token", t);
            //ApsSystemUtils.logThrowable(t, this, "generateRequestToken", "Error generating request token");
        }
    }
	 */

    public synchronized void generateAccessToken(String clientId, String tokenType) {
        try {

            final OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            BasicOAuthToken basicOAuthToken = new BasicOAuthToken(accessToken, tokenType);
            this.getTokenDAO().addAccessToken(clientId, basicOAuthToken);
        } catch (Throwable t) {
            _logger.error("Error generating access token", t);
            //ApsSystemUtils.logThrowable(t, this, "generateAccessToken", "Error generating access token");
        }
    }

    /*
       public void handleException(Exception e, HttpServletRequest request,
               HttpServletResponse response, boolean sendBody)
               throws IOException, ServletException {
           String realm = (request.isSecure()) ? "https://" : "http://";
           realm += request.getLocalName();
           OAuthServlet.handleException(response, e, realm, sendBody);
       }

       public OAuthValidator getOAuthValidator() {
           return new SimpleOAuthValidator();
       }
        */
 /*
    public void deleteMyAccessToken(Properties properties) throws Throwable {
        try {
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                _logger.info("Unable to delete access token form null user");
            }
            String username = user.getUsername();
            String accessToken = properties.getProperty("accessToken");
            OAuthConsumer consumer = (OAuthConsumer) properties.get(SystemConstants.API_OAUTH_CONSUMER_PARAMETER);
            String consumerKey = (null != consumer) ? consumer.consumerKey : null;
            this.getTokenDAO().deleteAccessToken(username, accessToken, consumerKey);
        } catch (Throwable t) {
        	_logger.error("Error deleting access token", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteMyAccessToken", "Error deleting access token");
        }
    }
	 */
    public ConsumerRecordVO getConsumerRecord(String clientId) throws ApsSystemException {
        ConsumerRecordVO consumer = null;
        try {
            consumer = this.getConsumerDAO().getConsumerRecord(clientId);
        } catch (Throwable t) {
            _logger.error("Error extracting consumer record by key {}", clientId, t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerRecord", "Error extracting consumer record by key " + consumerKey);
            throw new ApsSystemException("Error extracting consumer record by key " + clientId, t);
        }
        return consumer;
    }

    public void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().addConsumer(consumer);
        } catch (Throwable t) {
            _logger.error("Error adding consumer", t);
            //ApsSystemUtils.logThrowable(t, this, "addConsumer", "Error adding consumer");
            throw new ApsSystemException("Error adding consumer", t);
        }
    }

    public void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().updateConsumer(consumer);
        } catch (Throwable t) {
            _logger.error("Error updating consumer", t);
            //ApsSystemUtils.logThrowable(t, this, "updateConsumer", "Error updating consumer");
            throw new ApsSystemException("Error updating consumer", t);
        }
    }

    public void deleteConsumer(String clientId) throws ApsSystemException {
        try {
            this.getConsumerDAO().deleteConsumer(clientId);
        } catch (Throwable t) {
            _logger.error("Error deleting consumer record by key {}", clientId, t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerRecord", "Error deleting consumer record by key " + consumerKey);
            throw new ApsSystemException("Error deleting consumer record by key " + clientId, t);
        }
    }

    public List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException {
        List<String> consumerKeys = null;
        try {
            consumerKeys = this.getConsumerDAO().getConsumerKeys(filters);
        } catch (Throwable t) {
            _logger.error("Error extracting consumer keys", t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerKeys", "Error extracting consumer keys");
            throw new ApsSystemException("Error extracting consumer keys", t);
        }
        return consumerKeys;
    }

    /*
    public Map<String, Integer> getTokenOccurrencesByConsumer() throws ApsSystemException {
        Map<String, Integer> occurrences = null;
        try {
            occurrences = this.getTokenDAO().getOccurrencesByConsumer();
        } catch (Throwable t) {
            _logger.error("Error extracting token occurrences", t);
            //ApsSystemUtils.logThrowable(t, this, "getTokenOccurrencesByConsumer", "Error extracting token occurrences");
            throw new ApsSystemException("Error extracting token occurrences", t);
        }
        return occurrences;
    }

    protected Map<String, OAuthConsumer> getConsumers() {
        return _consumers;
    }
    protected void setConsumers(Map<String, OAuthConsumer> consumers) {
        this._consumers = consumers;
    }

    protected Map<String, EntandoOAuthAccessor> getAuthorizedTokensCache() {
        return _authorizedTokensCache;
    }
    protected Map<String, OAuthAccessor> getUnauthorizedTokensCache() {
        return _unauthorizedTokensCache;
    }
     */
    protected Integer getTokenTimeValidity() {
        if (null == this._tokenTimeValidity) {
            return 365;
        }
        return _tokenTimeValidity;
    }

    public void setTokenTimeValidity(Integer tokenTimeValidity) {
        this._tokenTimeValidity = tokenTimeValidity;
    }

    protected IOAuthConsumerDAO getConsumerDAO() {
        return _consumerDAO;
    }

    public void setConsumerDAO(IOAuthConsumerDAO consumerDAO) {
        this._consumerDAO = consumerDAO;
    }

    protected IOAuthTokenDAO getTokenDAO() {
        return _tokenDAO;
    }

    public void setTokenDAO(IOAuthTokenDAO tokenDAO) {
        this._tokenDAO = tokenDAO;
    }

    //private Map<String, OAuthConsumer> _consumers = new HashMap<String, OAuthConsumer>();
    private Map<String, EntandoOAuthAccessor> _authorizedTokensCache = new HashMap<String, EntandoOAuthAccessor>();
    private Integer _tokenTimeValidity;
    private IOAuthConsumerDAO _consumerDAO;
    private IOAuthTokenDAO _tokenDAO;

}
