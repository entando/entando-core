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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.api.model.JAXBUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;

/**
 * @author E.Santoboni
 */
public class ApiUserProfileInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiUserProfileInterface.class);
	
    public List<String> getUserProfiles(Properties properties) throws Throwable {
        List<String> usernames = null;
        try {
            String userProfileType = properties.getProperty("typeCode");
            IUserProfile prototype = (IUserProfile) this.getUserProfileManager().getEntityPrototype(userProfileType);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Profile Type '" + userProfileType + "' does not exist", Response.Status.CONFLICT);
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            String filtersParam = properties.getProperty("filters");
            BaseFilterUtils filterUtils = new BaseFilterUtils();
            EntitySearchFilter[] filters = filterUtils.getFilters(prototype, filtersParam, langCode);
            usernames = this.getUserProfileManager().searchId(userProfileType, filters);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error searching usernames", t);
            //ApsSystemUtils.logThrowable(t, this, "getUserProfiles");
            throw new ApsSystemException("Error searching usernames", t);
        }
        return usernames;
    }
    
    public JAXBUserProfile getUserProfile(Properties properties) throws ApiException, Throwable {
        JAXBUserProfile jaxbUserProfile = null;
        try {
            String username = properties.getProperty("username");
            IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Profile of user '" + username + "' does not exist", Response.Status.CONFLICT);
            }
            jaxbUserProfile = new JAXBUserProfile(userProfile, null);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting user profile", t);
            //ApsSystemUtils.logThrowable(t, this, "getUserProfile");
            throw new ApsSystemException("Error extracting user profile", t);
        }
        return jaxbUserProfile;
    }
    
    public StringApiResponse addUserProfile(JAXBUserProfile jaxbUserProfile) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String username = jaxbUserProfile.getId();
            if (null != this.getUserProfileManager().getProfile(username)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Profile of user '" + username + "' already exist", Response.Status.CONFLICT);
            }
            IApsEntity profilePrototype = this.getUserProfileManager().getEntityPrototype(jaxbUserProfile.getTypeCode());
            if (null == profilePrototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"User Profile type with code '" + jaxbUserProfile.getTypeCode() + "' does not exist", Response.Status.CONFLICT);
            }
            IUserProfile userProfile = (IUserProfile) jaxbUserProfile.buildEntity(profilePrototype, null);
            List<ApiError> errors = this.validate(userProfile);
            if (errors.size() > 0) {
                response.addErrors(errors);
                response.setResult(IResponseBuilder.FAILURE, null);
                return response;
            }
            this.getUserProfileManager().addProfile(username, userProfile);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error adding user profile", t);
            //ApsSystemUtils.logThrowable(t, this, "addUserProfile");
            throw new ApsSystemException("Error adding user profile", t);
        }
        return response;
    }
    
    public StringApiResponse updateUserProfile(JAXBUserProfile jaxbUserProfile) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String username = jaxbUserProfile.getId();
            if (null == this.getUserProfileManager().getProfile(username)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Profile of user '" + username + "' does not exist", Response.Status.CONFLICT);
            }
            IApsEntity profilePrototype = this.getUserProfileManager().getEntityPrototype(jaxbUserProfile.getTypeCode());
            if (null == profilePrototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"User Profile type with code '" + jaxbUserProfile.getTypeCode() + "' does not exist", Response.Status.CONFLICT);
            }
            IUserProfile userProfile = (IUserProfile) jaxbUserProfile.buildEntity(profilePrototype, null);
            List<ApiError> errors = this.validate(userProfile);
            if (errors.size() > 0) {
                response.addErrors(errors);
                response.setResult(IResponseBuilder.FAILURE, null);
                return response;
            }
            this.getUserProfileManager().updateProfile(username, userProfile);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error updating user profile", t);
            //ApsSystemUtils.logThrowable(t, this, "updateUserProfile");
            throw new ApsSystemException("Error updating user profile", t);
        }
        return response;
    }
    
    private List<ApiError> validate(IUserProfile userProfile) throws ApsSystemException {
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            List<FieldError> fieldErrors = userProfile.validate(this.getGroupManager());
            if (null != fieldErrors) {
                for (int i = 0; i < fieldErrors.size(); i++) {
                    FieldError fieldError = fieldErrors.get(i);
                    if (fieldError instanceof AttributeFieldError) {
                        AttributeFieldError attributeError = (AttributeFieldError) fieldError;
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
								attributeError.getFullMessage(), Response.Status.CONFLICT));
                    } else {
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
								fieldError.getMessage(), Response.Status.CONFLICT));
                    }
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error validating profile", t);
            //ApsSystemUtils.logThrowable(t, this, "validate");
            throw new ApsSystemException("Error validating profile", t);
        }
        return errors;
    }
    
    public void deleteUserProfile(Properties properties) throws ApiException, Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String username = properties.getProperty("username");
            IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Profile of user '" + username + "' does not exist", Response.Status.CONFLICT);
            }
            this.getUserProfileManager().deleteProfile(username);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error deleting user Profile", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteUserProfile");
            throw new ApsSystemException("Error deleting user Profile", t);
        }
    }
    
    protected IUserProfileManager getUserProfileManager() {
        return _userProfileManager;
    }
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this._userProfileManager = userProfileManager;
    }
    
    protected IGroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }
    
    private IUserProfileManager _userProfileManager;
    private IGroupManager _groupManager;
    
}