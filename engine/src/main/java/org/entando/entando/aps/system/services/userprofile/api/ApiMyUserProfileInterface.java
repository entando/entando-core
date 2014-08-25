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
package org.entando.entando.aps.system.services.userprofile.api;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.userprofile.api.model.JAXBUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class ApiMyUserProfileInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiMyUserProfileInterface.class);
	
    public JAXBUserDetails getMyUserProfile(Properties properties) throws ApiException, Throwable {
        try {
            UserDetails userDetail = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == userDetail) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Null user", Response.Status.UNAUTHORIZED);
            }
            return new JAXBUserDetails(userDetail);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting userprofile", t);
            //ApsSystemUtils.logThrowable(t, this, "getMyUserProfile");
            throw new ApsSystemException("Error extracting userprofile", t);
        }
    }
    
}
