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
package com.agiletec.apsadmin.system.services.shortcut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.services.shortcut.model.MenuSection;
import com.agiletec.apsadmin.system.services.shortcut.model.Shortcut;
import com.agiletec.apsadmin.system.services.shortcut.model.UserConfigBean;

/**
 * Manager of Shortcut catalog and user config.
 * @author E.Santoboni
 */
public class ShortcutManager extends AbstractService implements IShortcutManager, ServletContextAware {

	private static final Logger _logger = LoggerFactory.getLogger(ShortcutManager.class);
	
	@Override
    public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	protected void release() {
		super.release();
		this.setMenuSections(null);
		this.setShortcuts(null);
	}
	
	protected void loadShortcuts() {
		try {
			ShortcutLoader loader = new ShortcutLoader(this.getLocationPatterns(), this.getServletContext());
			this.setMenuSections(loader.getManuSections());
			this.setShortcuts(loader.getShortcuts());
			_logger.debug("{} ready: initialized  Sections menu:{} - Shortcuts:{}",  this.getClass().getName(), this.getMenuSections().size(), this.getShortcuts().size());
		} catch (Throwable t) {
			this.setMenuSections(new HashMap<String, MenuSection>());
			this.setShortcuts(new HashMap<String, Shortcut>());
			_logger.error("Error loading Shortcut definitions", t);
			//ApsSystemUtils.logThrowable(t, this, "loadShortcuts", "Error loading Shortcut definitions");
		}
	}
	
	@Override
	public UserConfigBean saveUserConfigBean(UserDetails user, UserConfigBean userConfig) throws ApsSystemException {
		if (null == user || null == userConfig 
				|| userConfig.getUsername().equals(user.getUsername())) {
			_logger.info("Required operation for null user or invalid user config");
			return null;
		}
		String[] config = this.saveUserConfig(user, userConfig.getConfig());
		return new UserConfigBean(user.getUsername(), config);
	}
	
	@Override
	public String[] saveUserConfig(UserDetails user, String[] config) throws ApsSystemException {
		if (null == user) {
			_logger.info("Required operation for null user");
			return null;
		}
		try {
			config = this.checkShortcutConfig(user, config);
			String xml = UserShortcutConfigDOM.createUserConfigXml(config);
			this.getUserShortcutDAO().saveUserConfig(user.getUsername(), xml);
		} catch (Throwable t) {
			_logger.error("Error saving user config by user ", user.getUsername(), t);
			//ApsSystemUtils.logThrowable(t, this, "saveUserConfig");
			throw new ApsSystemException("Error saving user config by user " + user.getUsername(), t);
		}
		return config;
	}
	
	@Override
	public UserConfigBean getUserConfigBean(UserDetails user) throws ApsSystemException {
		String[] config = this.getUserConfig(user);
		if (null == config) return null;
		return new UserConfigBean(user.getUsername(), config);
	}
	
	@Override
	public String[] getUserConfig(UserDetails user) throws ApsSystemException {
		String[] config = null;
		if (null == user) {
			_logger.info("Required shortcut config for null user");
			return null;
		}
		try {
			String xml = this.getUserShortcutDAO().loadUserConfig(user.getUsername());
			config = UserShortcutConfigDOM.extractUserConfig(xml, this.getUserShortcutsMaxNumber());
			config = this.checkShortcutConfig(user, config);
		} catch (Throwable t) {
			_logger.error("Error loading user config", t);
			//ApsSystemUtils.logThrowable(t, this, "getUserConfig");
			throw new ApsSystemException("Error loading user config", t);
		}
		return config;
	}
	
	private String[] checkShortcutConfig(UserDetails user, String[] config) throws Throwable {
		if (null == config) {
			config = new String[this.getUserShortcutsMaxNumber()];
		}
		try {
			if (config.length != this.getUserShortcutsMaxNumber()) {
				String[] newConfig = new String[this.getUserShortcutsMaxNumber()];
				for (int i = 0; i < config.length; i++) {
					if (i>=newConfig.length) continue;
					newConfig[i] = config[i];
				}
				config = newConfig;
			}
			for (int i = 0; i < config.length; i++) {
				String code = config[i];
				Shortcut shortcut = this.getShortcuts().get(code);
				String reqPerm = (null == shortcut) ? null : shortcut.getRequiredPermission();
				if (null == shortcut || (null != reqPerm && !this.getAuthorizationManager().isAuthOnPermission(user, reqPerm))) {
					config[i] = null;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking Shortcut Config by user {}", user.getUsername(), t);
			//ApsSystemUtils.logThrowable(t, this, "checkShortcutConfig");
			throw new ApsSystemException("Error checking Shortcut Config by user " + user.getUsername(), t);
		}
		return config;
	}
	
	@Override
	public void deleteUserConfig(String username) throws ApsSystemException {
		try {
			this.getUserShortcutDAO().deleteUserConfig(username);
		} catch (Throwable t) {
			_logger.error("Error deleting user config by user {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "deleteUserConfig");
			throw new ApsSystemException("Error deleting user config by user " + username, t);
		}
	}
	
	@Override
	public List<Shortcut> getAllowedShortcuts(UserDetails user) throws ApsSystemException {
		List<Shortcut> allowedShortcuts = new ArrayList<Shortcut>();
		if (null == user) {
			_logger.info("Required allowed shortcut for null user");
			return allowedShortcuts;
		}
		try {
			Iterator<Shortcut> shorCutIter = this.getShortcuts().values().iterator();
			while (shorCutIter.hasNext()) {
				Shortcut shortcut = shorCutIter.next();
				String permissionName = shortcut.getRequiredPermission();
				if (null == permissionName || this.getAuthorizationManager().isAuthOnPermission(user, permissionName)) {
					allowedShortcuts.add(shortcut.clone());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting allowed shortcuts by user {}", user.getUsername(), t);
			//ApsSystemUtils.logThrowable(t, this, "getAllowedShortcuts");
			throw new ApsSystemException("Error extracting allowed shortcuts by user " + user.getUsername(), t);
		}
		BeanComparator comparator = new BeanComparator("source");
		Collections.sort(allowedShortcuts, comparator);
		return allowedShortcuts;
	}
	
	@Override
	public Shortcut getShortcut(String code){
		Shortcut shortcut = this.getShortcuts().get(code);
		if (null == shortcut) {
			return null;
		}
		return shortcut.clone();
	}
	
	@Override
	public Integer getUserShortcutsMaxNumber() {
		return _userShortcutsMaxNumber;
	}
	public void setUserShortcutsMaxNumber(Integer userShortcutsMaxNumber) {
		this._userShortcutsMaxNumber = userShortcutsMaxNumber;
	}
	
	protected Map<String, Shortcut> getShortcuts() {
		if (null == this._shortcuts) {
			this.loadShortcuts();
		}
		return _shortcuts;
	}
	protected void setShortcuts(Map<String, Shortcut> shortcuts) {
		this._shortcuts = shortcuts;
	}
	
	protected Map<String, MenuSection> getMenuSections() {
		if (null == this._menuSections) {
			this.loadShortcuts();
		}
		return _menuSections;
	}
	protected void setMenuSections(Map<String, MenuSection> menuSections) {
		this._menuSections = menuSections;
	}
	
	protected ServletContext getServletContext() {
		return this._servletContext;
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	
	protected String getLocationPatterns() {
		if (null == this._locationPatterns) {
			return DEFAULT_LOCATION_PATTERN;
		}
		return _locationPatterns;
	}
	public void setLocationPatterns(String locationPatterns) {
		this._locationPatterns = locationPatterns;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IUserShortcutDAO getUserShortcutDAO() {
		return _userShortcutDAO;
	}
	public void setUserShortcutDAO(IUserShortcutDAO userShortcutDAO) {
		this._userShortcutDAO = userShortcutDAO;
	}
	
	private Map<String, Shortcut> _shortcuts;
	private Map<String, MenuSection> _menuSections;
	
	private ServletContext _servletContext;
	private String _locationPatterns;
	
	private IAuthorizationManager _authorizationManager;
	private IUserShortcutDAO _userShortcutDAO;
	
	private Integer _userShortcutsMaxNumber;
	
	public static final String DEFAULT_LOCATION_PATTERN = "classpath*:/shortcuts/**/apsadmin/shortcuts.xml";
	
}