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
package org.entando.entando.apsadmin.common;

import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class UserAvatarAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserAvatarAction.class);
	
	public String returnAvatarStream() {
		try {
			IUserProfile profile = this.getUserProfile();
			if (null == profile) {
				return this.extractDefaultAvatarStream();
			}
			String email = (String) profile.getValueByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
			if (null == email) {
				return this.extractDefaultAvatarStream();
			}
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(this.createGravatarUri(email));
			HttpResponse response = httpclient.execute(httpGet);
			Integer i = response.getStatusLine().getStatusCode();
			if (i == 404) {
				return this.extractDefaultAvatarStream();
			}
			this.setInputStream(response.getEntity().getContent());
		} catch (Throwable t) {
			_logger.error("error in returnAvatarStream", t);
            //ApsSystemUtils.logThrowable(t, this, "returnAvatarStream");
			return this.extractDefaultAvatarStream();
        }
		return SUCCESS;
	}
	
	private String createGravatarUri(String emailAddress) throws Throwable {
		StringBuilder sb = new StringBuilder(this.getGravatarUrl());
		sb.append(this.createMd5Hex(emailAddress));
		sb.append(".").append(this.getGravatarExtension()).append("?d=404&s=").append(this.getGravatarSize());
		return sb.toString();
	}
	
	private String createMd5Hex(String emailAddress) throws Throwable {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(emailAddress.getBytes("CP1252"));
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));        
			}
		} catch (Throwable t) {
			_logger.error("Error creating Md5 Hex", t);
            //ApsSystemUtils.logThrowable(t, this, "createMd5Hex");
			throw new RuntimeException("Error creating Md5 Hex", t);
		}
		return sb.toString();
	}
	
	protected String extractDefaultAvatarStream() {
		try {
			Resource resource = new ClassPathResource("avatar-default.png");
			this.setInputStream(resource.getInputStream());
		} catch (Throwable t) {
			_logger.error("error in extractDefaultAvatarStream", t);
            //ApsSystemUtils.logThrowable(t, this, "extractDefaultAvatarStream");
            return FAILURE;
        }
		return SUCCESS;
	}
	
	protected IUserProfile getUserProfile() throws ApsSystemException {
		if (null == this.getUsername()) {
			return null;
		}
		return this.getUserProfileManager().getProfile(this.getUsername());
	}
	
	public String getMimeType() {
		return "image/png";
	}
	
	protected String getGravatarUrl() {
		return _gravatarUrl;
	}
	public void setGravatarUrl(String gravatarUrl) {
		this._gravatarUrl = gravatarUrl;
	}
	
	public String getGravatarExtension() {
		return _gravatarExtension;
	}
	public void setGravatarExtension(String gravatarExtension) {
		this._gravatarExtension = gravatarExtension;
	}
	
	public String getGravatarSize() {
		return _gravatarSize;
	}
	public void setGravatarSize(String gravatarSize) {
		this._gravatarSize = gravatarSize;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public InputStream getInputStream() {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private String _gravatarUrl;
	
	private String _gravatarExtension = "png";
	private String _gravatarSize = "56";
	
	private String _username;
	private InputStream _inputStream;
	
	private IUserProfileManager _userProfileManager;
	
}
