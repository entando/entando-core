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
package org.entando.entando.aps.system.services.oauth.model;

/**
 * Thread Class delegate to update OAuth token.
 *
 * @author E.Santoboni
 */
public class TokenUpdaterThread extends Thread {
	/*
	private static final Logger _logger =  LoggerFactory.getLogger(TokenUpdaterThread.class);

	public TokenUpdaterThread(String accessToken, int tokenTimeValidity,
			IOAuthTokenDAO tokenDao) {
		this._accessToken = accessToken;
		this._tokenTimeValidity = tokenTimeValidity;
		this._tokenDao = tokenDao;
	}

	public void run() {
		try {
			this._tokenDao.refreshAccessTokens(this._accessToken, this._tokenTimeValidity);
		} catch (Throwable t) {
			_logger.error("error in run", t);
			//ApsSystemUtils.logThrowable(t, this, "run");
		}
	}

	private String _accessToken;
	private int _tokenTimeValidity;
	private IOAuthTokenDAO _tokenDao;
	 */
}
