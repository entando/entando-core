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
package com.agiletec.apsadmin.user.group;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.apsadmin.user.group.helper.IGroupActionHelper;

/**
 * Classi action della gestione Gruppi.
 * @author E.Santoboni - E.Mezzano
 */
public class GroupAction extends BaseAction implements IGroupAction {

	private static final Logger _logger = LoggerFactory.getLogger(GroupAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			this.checkDuplicatedGroup();
		} else if (!this.existsGroup()) {
			this.addActionError(this.getText("error.group.notExist"));
		}
	}
	
	/**
	 * Esegue in fase di aggiunta la verifica sulla duplicazione del gruppo.<br />
	 * Nel caso la verifica risulti negativa aggiunge un fieldError.
	 */
	protected void checkDuplicatedGroup() {
		if (this.existsGroup()) {
			String[] args = {this.getName()};
			this.addFieldError("name", this.getText("error.group.duplicated", args));
		}
	}
	
	@Override
	public String newGroup() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	@Override
	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		return this.extractGroupFormValues();
	}
	
	@Override
	public String showDetail() {
		String result = this.extractGroupFormValues();
		if (!result.equals(SUCCESS)) return result;
		this.extractReferencingObjects(this.getName());
		return result;
	}
	
	protected String extractGroupFormValues() {
		try {
			if (!this.existsGroup()) {
				this.addActionError(this.getText("error.group.notExist"));
				return "groupList";
			}
			Group group = this.getGroupManager().getGroup(this.getName());
			this.setName(group.getName());
			this.setDescription(group.getDescr());
		} catch (Throwable t) {
			_logger.error("error in extractGroupFormValues", t);
			//ApsSystemUtils.logThrowable(t, this, "extractGroupFormValues");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			Group group = new Group();
			group.setName(this.getName());
			group.setDescr(this.getDescription());
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getGroupManager().addGroup(group);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getGroupManager().updateGroup(group);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String trash() {
		try {
			String check = this.checkGroupForDelete();
			if (null != check) return check;
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			//ApsSystemUtils.logThrowable(t, this, "trash");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			String check = this.checkGroupForDelete();
			if (null != check) return check;
			IGroupManager groupManager = this.getGroupManager();
			Group group = groupManager.getGroup(this.getName());
			this.getGroupManager().removeGroup(group);
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Verifica l'esistenza del gruppo.
	 * @return true in caso positivo, false nel caso il gruppo non esista.
	 */
	protected boolean existsGroup() {
		String name = this.getName();
		boolean exists = (name!=null && name.trim().length()>=0 && this.getGroupManager().getGroup(name)!=null);
		return exists;
	}
	
	/**
	 * Esegue i controlli necessari per la cancellazione di un gruppo. Imposta gli opportuni messaggi di errore come actionMessages.
	 * Restituisce l'esito del controllo.
	 * @return true in caso di cancellazione consentita, false in caso contrario.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected String checkGroupForDelete() throws ApsSystemException {
		if (!this.existsGroup()) {
			this.addActionError(this.getText("error.group.notExist"));
			return "groupList";
		}
		String name = this.getName();
		if (Group.FREE_GROUP_NAME.equals(name) || Group.ADMINS_GROUP_NAME.equals(name)) {
			this.addActionError(this.getText("error.group.undeletable"));
			return "groupList";
		}
		this.extractReferencingObjects(name);
		if (null != this.getReferences() && this.getReferences().size() > 0) {
	        return "references";
		}
		return null;
	}
	
	protected void extractReferencingObjects(String groupCode) {
		try {
			Group group = this.getGroupManager().getGroup(groupCode);
			if (null != group) {
				Map references = this.getHelper().getReferencingObjects(group, this.getRequest());
				if (references.size() > 0) {
					this.setReferences(references);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting referenced objects by group '{}'", groupCode, t);
		}
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	protected IGroupActionHelper getHelper() {
		return _helper;
	}
	public void setHelper(IGroupActionHelper helper) {
		this._helper = helper;
	}
	
	public Map<String, List<Object>> getReferences() {
		return _references;
	}
	protected void setReferences(Map<String, List<Object>> references) {
		this._references = references;
	}
	
	private int _strutsAction;
	private String _name;
	private String _description;
	
	private Map<String, List<Object>> _references;
	
	private IGroupManager _groupManager;
	
	private IGroupActionHelper _helper;
	
}