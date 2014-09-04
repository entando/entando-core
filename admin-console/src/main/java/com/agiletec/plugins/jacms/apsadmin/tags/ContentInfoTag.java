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
package com.agiletec.plugins.jacms.apsadmin.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.tags.AbstractObjectInfoTag;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Returns a content (or one of its property) through the code.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "Content" (Entity Object): "id", "descr", "typeCode", "typeDescr", 
 * "mainGroup" (code), "groups" (extra group codes), "categories" (list of categories),
 * "attributeMap" (map of attributes indexed by the name), "attributeList" (list of attributes), "status" (code), 
 * "viewPage" (page code), "listModel", "defaultModel", "version", "lastEditor" (username of last editor).
 * The names of the available property of "Content" (Record Object): "id", "typeCode", "descr", "status" (code), "create" (Creation Date), 
 * "modify" (last modify date), "mainGroupCode", "version", "lastEditor" (username of last editor).
 * @author E.Santoboni
 */
public class ContentInfoTag extends AbstractObjectInfoTag {

	private static final Logger _logger = LoggerFactory.getLogger(ContentInfoTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		int result = super.doStartTag();
		try {
			if (null != this.getMasterObject()) {
				HttpSession session = this.pageContext.getSession();
				UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
				IContentAuthorizationHelper contentAuthHelper = (IContentAuthorizationHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_AUTHORIZATION_HELPER, this.pageContext);
				boolean isAuthOnEdit = false;
				if (this.isRecord()) {
					//PENSARE ALL'OPPORTUNITA'... meno prestante nel caso di oggetto contenuto!
					String keyValue = (String) super.findValue(this.getKey(), String.class);
					isAuthOnEdit = contentAuthHelper.isAuthToEdit(currentUser, keyValue, false);
				} else {
					isAuthOnEdit = contentAuthHelper.isAuthToEdit(currentUser, (Content) this.getMasterObject());
				}
				if (isAuthOnEdit) {
					if (null != this.getAuthToEditVar()) {
						ValueStack stack = this.getStack();
						stack.getContext().put(this.getAuthToEditVar(), isAuthOnEdit);
						stack.setValue("#attr['" + this.getAuthToEditVar() + "']", isAuthOnEdit, false);
					}
					result = EVAL_BODY_INCLUDE;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag", "Error on doStartTag");
			throw new JspException("Error on doStartTag", t);
		}
		return result;
	}
	
	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		try {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
			if (this.isRecord()) {
				this.setMasterObject(contentManager.loadContentVO(keyValue));
			} else {
				this.setMasterObject(contentManager.loadContent(keyValue, !this.isWorkVersion()));
			}
		} catch (Throwable t) {
			_logger.error("Error extracting content : id '{}' - record {} - work version {}", keyValue, this.isRecord(), this.isWorkVersion(), t);
			String message = "Error extracting content : id '" + keyValue + "' - " + "record " + this.isRecord() + "' - work version " + this.isWorkVersion();
			//ApsSystemUtils.logThrowable(t, this, "getMasterObject", message);
			throw new ApsSystemException(message, t);
		}
		return this.getMasterObject();
	}
	
	public void setContentId(String contentId) {
		super.setKey(contentId);
	}
	
	/**
	 * Indicates if the record of the entity object must be returned.
	 * @return true if the record of the entity object must be returned
	 */
	protected boolean isRecord() {
		return _record;
	}
	/**
	 * Decide if the record of the entity object must be returned.
	 * @param record true if the record of the entity object must be returned
	 */
	public void setRecord(boolean record) {
		this._record = record;
	}
	
	/**
	 * Determines the version to be returned: the current version or the published one.
	 * @return The boolean value
	 */
	public boolean isWorkVersion() {
		return _workVersion;
	}
	
	/**
	 * Decides the version to be returned: the current version or the published one.
	 * @param workVersion The boolean value
	 */
	public void setWorkVersion(boolean workVersion) {
		this._workVersion = workVersion;
	}
	
	protected Object getMasterObject() {
		return _masterObject;
	}
	protected void setMasterObject(Object masterObject) {
		this._masterObject = masterObject;
	}
	
	public String getAuthToEditVar() {
		return _authToEditVar;
	}
	public void setAuthToEditVar(String authToEditVar) {
		this._authToEditVar = authToEditVar;
	}
	
	private boolean _record;
	private boolean _workVersion;
	
	private Object _masterObject;
	
	private String _authToEditVar;
	
}