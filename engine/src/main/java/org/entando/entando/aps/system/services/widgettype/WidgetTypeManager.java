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
package org.entando.entando.aps.system.services.widgettype;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.events.LangsChangedEvent;
import com.agiletec.aps.system.services.lang.events.LangsChangedObserver;
import com.agiletec.aps.util.ApsProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;

import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio di gestione dei tipi di widget (WidgetType) definiti
 * nel sistema. (Questo servizio non riguarda la configurazione delle
 * istanze di widget nelle pagine)
 * @author M.Diana - E.Santoboni
 */
public class WidgetTypeManager extends AbstractService 
		implements IWidgetTypeManager, LangsChangedObserver, GroupUtilizer, GuiFragmentUtilizer {
	
	private static final Logger _logger =  LoggerFactory.getLogger(WidgetTypeManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadWidgetTypes();
		_logger.debug("{} ready. Initialized {} widget types", this.getClass().getName(), this._widgetTypes.size());
	}
	
	/**
	 * Caricamento da db del catalogo dei tipi di widget.
	 * @throws ApsSystemException In caso di errori di lettura da db.
	 */
	private void loadWidgetTypes() throws ApsSystemException {
		try {
			this._widgetTypes = this.getWidgetTypeDAO().loadWidgetTypes();
			Iterator<WidgetType> iter = this._widgetTypes.values().iterator();
			while (iter.hasNext()) {
				WidgetType type = iter.next();
				String mainTypeCode = type.getParentTypeCode();
				if (null != mainTypeCode) {
					type.setParentType(this._widgetTypes.get(mainTypeCode));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading widgets types", t);
			throw new ApsSystemException("Error loading widgets types", t);
		}
	}
	
	@Override
	public void updateFromLangsChanged(LangsChangedEvent event) {
		try {
			this.init();
		} catch (Throwable t) {
			_logger.error("Error on init method", t);
		}
	}
	
	@Override
	@Deprecated
	public WidgetType getShowletType(String widgetTypeCode) {
		return this.getWidgetType(widgetTypeCode);
	}

	@Override
	public WidgetType getWidgetType(String code) {
		return this._widgetTypes.get(code);
	}
	
	@Override
	@Deprecated
	public List<WidgetType> getShowletTypes() {
		return this.getWidgetTypes();
	}

	@Override
	public List<WidgetType> getWidgetTypes() {
		List<WidgetType> types = new ArrayList<WidgetType>();
		Iterator<WidgetType> masterTypesIter = this._widgetTypes.values().iterator();
		while (masterTypesIter.hasNext()) {
			WidgetType widgetType = masterTypesIter.next();
			types.add(widgetType.clone());
		}
		BeanComparator comparator = new BeanComparator("code");
		Collections.sort(types, comparator);
		return types;
	}
	
	@Override
	@Deprecated
	public void addShowletType(WidgetType widgetType) throws ApsSystemException {
		this.addWidgetType(widgetType);
	}

	@Override
	public void addWidgetType(WidgetType widgetType) throws ApsSystemException {
		try {
			WidgetType type = this._widgetTypes.get(widgetType.getCode());
			if (null != type) {
				_logger.error("Type already exists : type code {}", widgetType.getCode());
				return;
			}
			String parentTypeCode = widgetType.getParentTypeCode();
			if (null != parentTypeCode && null == this._widgetTypes.get(parentTypeCode)) {
				throw new ApsSystemException("ERROR : Parent type '" + parentTypeCode + "' doesn't exists");
			}
			if (null == parentTypeCode && null != widgetType.getConfig()) {
				throw new ApsSystemException("ERROR : Parent type null and default config not null");
			}
			if (null != widgetType.getTypeParameters() && null != widgetType.getConfig()) {
				throw new ApsSystemException("ERROR : Params not null and config not null");
			}
			this.getWidgetTypeDAO().addWidgetType(widgetType);
			this._widgetTypes.put(widgetType.getCode(), widgetType);
			this.notifyWidgetTypeChanging(widgetType.getCode(), WidgetTypeChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding a Widget Type", t);
			throw new ApsSystemException("Error adding a Widget Type", t);
		}
	}
	
	@Override
	@Deprecated
	public void deleteShowletType(String widgetTypeCode) throws ApsSystemException {
		this.deleteWidgetType(widgetTypeCode);
	}
	
	@Override
	public void deleteWidgetType(String widgetTypeCode) throws ApsSystemException {
		List<GuiFragment> deletedFragments = new ArrayList<GuiFragment>();
		try {
			WidgetType type = this._widgetTypes.get(widgetTypeCode);
			if (null == type) {
				_logger.error("Type not exists : type code {}", widgetTypeCode);
				return;
			}
			if (type.isLocked()) {
				_logger.error("A locked widget can't be deleted - type {}", widgetTypeCode);
				return;
			}
			List<String> fragmentCodes = this.getGuiFragmentManager().getGuiFragmentCodesByWidgetType(widgetTypeCode);
			if (null != fragmentCodes) {
				for (int i = 0; i < fragmentCodes.size(); i++) {
					String fragmentCode = fragmentCodes.get(i);
					GuiFragment fragmentToDelete = this.getGuiFragmentManager().getGuiFragment(fragmentCode);
					deletedFragments.add(fragmentToDelete);
					this.getGuiFragmentManager().deleteGuiFragment(fragmentCode);
				}
			}
			this.getWidgetTypeDAO().deleteWidgetType(widgetTypeCode);
			this._widgetTypes.remove(widgetTypeCode);
			this.notifyWidgetTypeChanging(widgetTypeCode, WidgetTypeChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			for (int i = 0; i < deletedFragments.size(); i++) {
				GuiFragment guiFragment = deletedFragments.get(i);
				if (null == this.getGuiFragmentManager().getGuiFragment(guiFragment.getCode())) {
					this.getGuiFragmentManager().addGuiFragment(guiFragment);
				}
			}
			_logger.error("Error deleting widget type", t);
			throw new ApsSystemException("Error deleting widget type", t);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig) throws ApsSystemException {
		try {
			WidgetType type = this._widgetTypes.get(widgetTypeCode);
			if (null == type) {
				_logger.error("Type not exists : type code {}", widgetTypeCode);
				return;
			}
			this.updateWidgetType(widgetTypeCode, titles, defaultConfig, Group.FREE_GROUP_NAME);
		} catch (Throwable t) {
			_logger.error("Error updating Widget type titles : type code {}", widgetTypeCode, t);
			throw new ApsSystemException("Error updating Widget type titles : type code" + widgetTypeCode, t);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) throws ApsSystemException {
		this.updateWidgetType(widgetTypeCode, titles, defaultConfig, mainGroup);
	}
	
	@Override
	public void updateWidgetType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) throws ApsSystemException {
		try {
			WidgetType type = this._widgetTypes.get(widgetTypeCode);
			if (null == type) {
				_logger.error("Type not exists : type code {}", widgetTypeCode);
				return;
			}
			if (type.isLocked() || !type.isLogic() || !type.isUserType()) {
				defaultConfig = type.getConfig();
			}
			this.getWidgetTypeDAO().updateWidgetType(widgetTypeCode, titles, defaultConfig, mainGroup);
			type.setTitles(titles);
			type.setConfig(defaultConfig);
			type.setMainGroup(mainGroup);
			this.notifyWidgetTypeChanging(widgetTypeCode, WidgetTypeChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Widget type titles : type code {}", widgetTypeCode, t);
			throw new ApsSystemException("Error updating Widget type titles : type code" + widgetTypeCode, t);
		}
	}
	
	@Override
	@Deprecated
	public void updateShowletTypeTitles(String widgetTypeCode, ApsProperties titles) throws ApsSystemException {
		try {
			WidgetType type = this._widgetTypes.get(widgetTypeCode);
			if (null == type) {
				_logger.error("Type not exists : type code {}", widgetTypeCode);
				return;
			}
			this.getWidgetTypeDAO().updateShowletTypeTitles(widgetTypeCode, titles);
			type.setTitles(titles);
			this.notifyWidgetTypeChanging(widgetTypeCode, WidgetTypeChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Widget type titles : type code {}", widgetTypeCode, t);
			throw new ApsSystemException("Error updating Widget type titles : type code" + widgetTypeCode, t);
		}
	}
	
	@Override
	public List<WidgetType> getGroupUtilizers(String groupName) throws ApsSystemException {
		List<WidgetType> utilizers = null;
		try {
			boolean freeTypes = (null == groupName || groupName.equals(Group.FREE_GROUP_NAME));
			List<WidgetType> allTypes = this.getWidgetTypes();
			for (int i = 0; i < allTypes.size(); i++) {
				WidgetType type = allTypes.get(i);
				String typeGroup = type.getMainGroup();
				if ((freeTypes && null == typeGroup) || groupName.equals(typeGroup)) {
					if (null == utilizers) {
						utilizers = new ArrayList<WidgetType>();
					}
					utilizers.add(type);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting utilizers", t);
			throw new ApsSystemException("Error extracting utilizers", t);
		}
		return utilizers;
	}
	
	@Override
	public List getGuiFragmentUtilizers(String guiFragmentCode) throws ApsSystemException {
		List<WidgetType> utilizers = new ArrayList<WidgetType>();
		try {
			FieldSearchFilter codeFilter = new FieldSearchFilter("code", guiFragmentCode, false);
			FieldSearchFilter widgetTypeFilter = new FieldSearchFilter("widgettypecode");
			FieldSearchFilter[] filters = {codeFilter, widgetTypeFilter};
			List<String> widgetUtilizers = this.getGuiFragmentManager().searchGuiFragments(filters);
			if (null != widgetUtilizers && !widgetUtilizers.isEmpty()) {
				for (int i = 0; i < widgetUtilizers.size(); i++) {
					String code = widgetUtilizers.get(i);
					GuiFragment fragment = this.getGuiFragmentManager().getGuiFragment(code);
					WidgetType widgetType = this.getWidgetType(fragment.getWidgetTypeCode());
					if (null != widgetType) {
						utilizers.add(widgetType);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting utilizers", t);
			throw new ApsSystemException("Error extracting utilizers", t);
		}
		return utilizers;
	}
	
	private void notifyWidgetTypeChanging(String widgetTypeCode, int operationCode) throws ApsSystemException {
		WidgetTypeChangedEvent event = new WidgetTypeChangedEvent();
		event.setWidgetTypeCode(widgetTypeCode);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	protected IWidgetTypeDAO getWidgetTypeDAO() {
		return _widgetTypeDAO;
	}
	public void setWidgetTypeDAO(IWidgetTypeDAO widgetTypeDAO) {
		this._widgetTypeDAO = widgetTypeDAO;
	}
	
	protected IGuiFragmentManager getGuiFragmentManager() {
		return _guiFragmentManager;
	}
	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this._guiFragmentManager = guiFragmentManager;
	}
	
	public void setWidgetTypes(Map<String, WidgetType> widgetTypes) {
		this._widgetTypes = widgetTypes;
	}
	
	private Map<String, WidgetType> _widgetTypes;
	
	private IWidgetTypeDAO _widgetTypeDAO;
	private IGuiFragmentManager _guiFragmentManager;
	
}
