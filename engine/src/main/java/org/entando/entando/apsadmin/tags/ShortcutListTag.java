/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts2.util.TextProviderHelper;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.services.shortcut.IShortcutManager;
import com.agiletec.apsadmin.system.services.shortcut.model.Shortcut;
import com.opensymphony.xwork2.util.ValueStack;

public class ShortcutListTag extends StrutsBodyTagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(ShortcutListTag.class);

	@Override
	public int doStartTag() throws JspException {
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			Object retval = null;
			IShortcutManager shortcutManager = (IShortcutManager) ApsWebApplicationUtils.getBean(ApsAdminSystemConstants.SHORTCUT_MANAGER, this.pageContext);
			List<Shortcut> myShortcuts = shortcutManager.getAllowedShortcuts(currentUser);
			if (this.getType().equalsIgnoreCase(TYPE_LIST_OBJECT)) {
				retval = (Object) myShortcuts;
			} else if (this.getType().equalsIgnoreCase(TYPE_LIST_ITEMS)) {
				retval = (Object) this.getAllowedShortcutSelectItems(myShortcuts, currentUser);
			} else {
				_logger.warn("Invalid param for attribute 'value'. Expected '{}' or '{}' but was {}", TYPE_LIST_ITEMS, TYPE_LIST_OBJECT, this.getType());
			}
			ValueStack stack = super.getStack();
			stack.getContext().put(this.getVar(), retval);
			stack.setValue("#attr['" + this.getVar() + "']", retval, false);
		} catch (Throwable t) {
			_logger.error("Error extracting shortcuts for user '{}'", currentUser.getUsername() , t);
			throw new JspException("Error extracting shortcuts", t);
		}
		return super.doStartTag();
	}


	private String getText(String key) {
		String msg = TextProviderHelper.getText(key, key, super.getStack());
		return msg;
	}

	public List<SelectItem> getAllowedShortcutSelectItems(List<Shortcut> myShortcuts, UserDetails currentUser) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		if (null == myShortcuts || myShortcuts.isEmpty()) {
			_logger.debug("shortcut list null");
			return items;
		}
		try {
			Map<String, List<SelectItem>> groups = new HashMap<String, List<SelectItem>>();
			for (int i = 0; i < myShortcuts.size(); i++) {
				Shortcut shortcut = myShortcuts.get(i);
				String groupCode = shortcut.getSource();
				String optgroup = shortcut.getSource();
				if (groupCode.equals("core")) {
					groupCode += " - " + shortcut.getMenuSection().getId();
					String sectDescrKey = shortcut.getMenuSection().getDescriptionKey();
					String sectDescr = this.getText(sectDescrKey);
					if (null == sectDescrKey || sectDescrKey.equals(sectDescr)) {
						sectDescr = shortcut.getMenuSection().getDescription();
					}
					optgroup += " - " + sectDescr;
				} else {
					String labelCode = optgroup + ".name";
					String optgroupDescr = this.getText(labelCode);
					if (!optgroupDescr.equals(labelCode)) {
						optgroup = optgroupDescr;
					}
				}
				String descrKey = shortcut.getDescriptionKey();
				String descr = this.getText(descrKey);
				if (null == descrKey || descrKey.equals(descr)) {
					descr = shortcut.getDescription();
				}
				List<SelectItem> itemsByGroup = groups.get(groupCode);
				if (null == itemsByGroup) {
					itemsByGroup = new ArrayList<SelectItem>();
					groups.put(groupCode, itemsByGroup);
				}
				SelectItem selectItem = new SelectItem(shortcut.getId(), descr, optgroup);
				itemsByGroup.add(selectItem);
			}
			List<String> keys = new ArrayList<String>(groups.keySet());
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++) {
				List<SelectItem> itemsByGroup = groups.get(keys.get(i));
				BeanComparator comparator = new BeanComparator("value");
				Collections.sort(itemsByGroup, comparator);
				items.addAll(itemsByGroup);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting allowed shortcut items by user {}", currentUser.getUsername(), t);
			throw new RuntimeException("Error extracting allowed shortcut items by user " + currentUser.getUsername(), t);
		}
		return items;
	}


	public String getType() {
		return _type;
	}
	public void setType(String type) {
		this._type = type;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _type;
	private String _var;

	public static final String TYPE_LIST_ITEMS = "list_items";
	public static final String TYPE_LIST_OBJECT = "list_object";

}