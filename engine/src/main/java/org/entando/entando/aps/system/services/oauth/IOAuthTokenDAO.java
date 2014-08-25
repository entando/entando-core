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
package org.entando.entando.aps.system.services.oauth;

import java.util.Map;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;

import org.entando.entando.aps.system.services.oauth.model.EntandoOAuthAccessor;

/**
 * @author E.Santoboni
 */
public interface IOAuthTokenDAO {
    
    public void addAccessToken(OAuthAccessor accessor);
    
    public void refreshAccessTokens(String tokenToUpdate, int tokenTimeValidity);
    
    public EntandoOAuthAccessor getAccessor(String accessToken, OAuthConsumer consumer);
    
    public void deleteAccessToken(String username, String accessToken, String consumerKey);
    
    public Map<String, Integer> getOccurrencesByConsumer();
    
}
