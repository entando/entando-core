/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.oauth.model;

import org.entando.entando.aps.system.services.oauth.IOAuthTokenDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * Thread Class delegate to update OAuth token.
 * @author E.Santoboni
 */
public class TokenUpdaterThread extends Thread {

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
	
}