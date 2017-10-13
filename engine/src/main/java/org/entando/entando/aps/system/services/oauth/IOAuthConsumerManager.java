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

import java.util.List;

import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface IOAuthConsumerManager {

	public ConsumerRecordVO getConsumerRecord(String consumerKey) throws ApsSystemException;

	public void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

	public void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

	public void deleteConsumer(String consumerKey) throws ApsSystemException;

	//public Map<String, Integer> getTokenOccurrencesByConsumer() throws ApsSystemException;
	public List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException;
	/*
    public OAuthConsumer getConsumer(OAuthMessage requestMessage) throws IOException, OAuthProblemException;

    public OAuthAccessor getAuthorizedAccessor(OAuthMessage requestMessage) throws IOException, OAuthProblemException;

    public OAuthAccessor getAccessor(OAuthMessage requestMessage) throws IOException, OAuthProblemException;

    public OAuthValidator getOAuthValidator();
	 */
 /*
     * Generate a fresh request token and secret for a consumer.
     * @param accessor The consumer
     * @throws OAuthException In case of error
	 */
	//public void generateRequestToken(OAuthAccessor accessor) throws OAuthException;

	/*
     * Generate an access token and secret for a consumer.
     * @param accessor The consumer
     * @throws OAuthException In case of error
	 */
	//public void generateAccessToken(OAuthAccessor accessor) throws OAuthException;
	//public void markAsAuthorized(OAuthAccessor accessor, String username) throws OAuthException;
	//public void handleException(Exception e, HttpServletRequest request,
	//        HttpServletResponse response, boolean sendBody) throws IOException, ServletException;
	public static final String CONSUMER_KEY_FILTER_KEY = "consumerkey";
	public static final String CONSUMER_SECRET_FILTER_KEY = "consumersecret";
	public static final String CONSUMER_DESCRIPTION_FILTER_KEY = "description";
	public static final String CONSUMER_CALLBACKURL_FILTER_KEY = "callbackurl";
	public static final String CONSUMER_EXPIRATIONDATE_FILTER_KEY = "expirationdate";

}
