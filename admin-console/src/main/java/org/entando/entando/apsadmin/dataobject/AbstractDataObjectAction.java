/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.apsadmin.dataobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;
import org.entando.entando.apsadmin.dataobject.helper.IDataObjectActionHelper;

/**
 * Action Astratta Base per la gestione DataObject.
 *
 * @author E.Santoboni
 */
public abstract class AbstractDataObjectAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractDataObjectAction.class);

	protected void addActivityStreamInfo(DataObject dataObject, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = this.getDataObjectActionHelper().createActivityStreamInfo(dataObject, strutsAction, addLink);
		super.addActivityStreamInfo(asi);
	}

	/**
	 * Restituisce il DataObject vo in base all'identificativo.
	 *
	 * @param contentId L'identificativo del DataObject.
	 * @return Il DataObject vo cercato.
	 */
	public DataObjectRecordVO getContentVo(String contentId) {
		DataObjectRecordVO dataObjectVo = null;
		try {
			dataObjectVo = this.getDataObjectManager().loadDataObjectVO(contentId);
		} catch (Throwable t) {
			_logger.error("error loading DataObjectVo {}", contentId, t);
			throw new RuntimeException("error loading DataObjectVo", t);
		}
		return dataObjectVo;
	}

	/**
	 * Verifica se l'utente corrente è abilitato all'accesso del DataObject
	 * specificato.
	 *
	 * @param dataObject Il DataObject su cui verificare il permesso di accesso.
	 * @return True se l'utente corrente è abilitato all'eccesso al DataObject,
	 * false in caso contrario.
	 */
	protected boolean isUserAllowed(DataObject dataObject) {
		return this.getDataObjectActionHelper().isUserAllowed(dataObject, this.getCurrentUser());
	}

	/**
	 * Restituisce il DataObject in sesione.
	 *
	 * @return Il DataObject in sesione.
	 */
	public DataObject getContent() {
		return (DataObject) this.getRequest().getSession().getAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX + this.getContentOnSessionMarker());
	}

	protected DataObject updateDataObjectOnSession() {
		return this.updateDataObjectOnSession(false);
	}

	protected DataObject updateDataObjectOnSession(boolean updateMainGroup) {
		DataObject content = this.getContent();
		this.getDataObjectActionHelper().updateDataObject(content, updateMainGroup, this.getRequest());
		return content;
	}

	/**
	 * Restituisce la lista di DataObject (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire
	 * una corretta visualizzazione della pagina.
	 *
	 * @return La lista di tipi di DataObject (in forma small) definiti nel
	 * sistema.
	 */
	public List<SmallDataType> getContentTypes() {
		return this.getDataObjectManager().getSmallDataTypes();
	}

	/**
	 * Restituisce la lista di stati di DataObject definiti nel sistema. Il
	 * metodo è a servizio delle jsp che richiedono questo dato per fornire una
	 * corretta visualizzazione della pagina.
	 *
	 * @return La lista di stati di DataObject definiti nel sistema.
	 * @deprecated use getAvalaibleStatus()
	 */
	public String[] getStatesList() {
		return DataObject.AVAILABLE_STATUS;
	}

	/**
	 * Restituisce la lista di stati di DataObject definiti nel sistema, come
	 * insieme di chiave e valore Il metodo è a servizio delle jsp che
	 * richiedono questo dato per fornire una corretta visualizzazione della
	 * pagina.
	 *
	 * @return La lista di stati di DataObject definiti nel sistema.
	 */
	public List<SelectItem> getAvalaibleStatus() {
		String[] status = DataObject.AVAILABLE_STATUS;
		List<SelectItem> items = new ArrayList<SelectItem>(status.length);
		for (int i = 0; i < status.length; i++) {
			SelectItem item = new SelectItem(status[i], "name.contentStatus." + status[i]);
			items.add(item);
		}
		return items;
	}

	/**
	 * Restituisce la lista di lingue definite nel sistema. Il metodo è a
	 * servizio delle jsp che richiedono questo dato per fornire una corretta
	 * visualizzazione della pagina.
	 *
	 * @return La lista di lingue definite nel sistema.
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	public static String buildDataObjectOnSessionMarker(DataObject dataObject, int operation) {
		String marker = null;
		switch (operation) {
			case ApsAdminSystemConstants.ADD:
				marker = dataObject.getTypeCode() + "_newContent";
				break;
			case ApsAdminSystemConstants.EDIT:
				marker = dataObject.getTypeCode() + "_editContent_" + dataObject.getId();
				break;
			case ApsAdminSystemConstants.PASTE:
				marker = dataObject.getTypeCode() + "_pasteContent_" + dataObject.getId();
				break;
			default:
				throw new RuntimeException("Unrecognized operation : " + operation);
		}
		return marker;
	}

	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}

	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}

	public IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	public IDataObjectActionHelper getDataObjectActionHelper() {
		return _dataObjectActionHelper;
	}

	public void setDataObjectActionHelper(IDataObjectActionHelper dataObjectActionHelper) {
		this._dataObjectActionHelper = dataObjectActionHelper;
	}

	/**
	 * Restituisce la mappa dei gruppi presenti nel sistema. La mappa è
	 * indicizzata in base al nome del gruppo.
	 *
	 * @return La mappa dei gruppi presenti nel sistema.
	 * @deprecated
	 */
	public Map<String, Group> getGroupsMap() {
		return this.getGroupManager().getGroupsMap();
	}

	/**
	 * Restituisce un gruppo in base al nome.
	 *
	 * @param groupName Il nome del gruppo da restituire.
	 * @return Il gruppo cercato.
	 */
	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}

	/**
	 * Restituisce un tipo di DataType in forma small.
	 *
	 * @param typeCode Il codice del tipo di DataType.
	 * @return Il tipo di DataType (in forma small) cercato.
	 */
	public SmallDataType getSmallContentType(String typeCode) {
		Map<String, SmallDataType> smallContentTypes = this.getDataObjectManager().getSmallDataTypesMap();
		return (SmallDataType) smallContentTypes.get(typeCode);
	}

	public List<Group> getAllowedGroups() {
		return super.getActualAllowedGroups();
	}

	/**
	 * Restituisce la lista ordinata dei gruppi presenti nel sistema.
	 *
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	public List<Group> getGroups() {
		List<Group> groups = this.getGroupManager().getGroups();
		BeanComparator c = new BeanComparator("description");
		Collections.sort(groups, c);
		return groups;
	}

	/**
	 * Restituisce il manager dei gruppi.
	 *
	 * @return Il manager dei gruppi.
	 */
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	/**
	 * Setta il manager dei gruppi.
	 *
	 * @param groupManager Il manager dei gruppi.
	 */
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	private String _contentOnSessionMarker;

	private IDataObjectManager _dataObjectManager;
	private IDataObjectActionHelper _dataObjectActionHelper;

	private IGroupManager _groupManager;

}
