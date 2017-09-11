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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import org.entando.entando.aps.system.services.dataobject.DataObjectsStatus;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.apsadmin.dataobject.helper.IDataObjectActionHelper;
import org.entando.entando.apsadmin.dataobject.rs.model.DataObjectJO;
import org.entando.entando.apsadmin.dataobject.rs.model.DataObjectsStatusResponse;

/**
 * Action per la ricerca contenuti.
 *
 * @author E.Santoboni
 */
public class DataObjectFinderAction extends AbstractApsEntityFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectFinderAction.class);

	private static final int DEFAULT_LASTUPDATE_RESPONSE_SIZE = 5;

	public DataObjectsStatusResponse getContentsStatusResponse() {
		DataObjectsStatusResponse response = null;
		try {
			DataObjectsStatus pagesStatus = this.getDataObjectManager().getDataObjectsStatus();
			response = new DataObjectsStatusResponse(pagesStatus);
		} catch (Throwable t) {
			_logger.error("Error loading tDataObject status", t);
			throw new RuntimeException("Error loading DataObject status", t);
		}
		return response;
	}

	public String getLastUpdated() {
		if (this.getLastUpdateResponseSize() < 1) {
			this.setLastUpdateResponseSize(DEFAULT_LASTUPDATE_RESPONSE_SIZE);
		}
		return SUCCESS;
	}

	@SuppressWarnings("rawtypes")
	public List<DataObjectJO> getLastUpdateContentResponse() {
		List<DataObjectJO> response = null;
		try {
			EntitySearchFilter modifyOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_MODIFY_DATE_FILTER_KEY,
					false);
			modifyOrder.setOrder(EntitySearchFilter.DESC_ORDER);

			List<String> ids = this.getDataObjectManager().searchId(new EntitySearchFilter[]{modifyOrder});
			if (null != ids && !ids.isEmpty()) {
				if (this.getLastUpdateResponseSize() > ids.size() - 1) {
					this.setLastUpdateResponseSize(ids.size() - 1);
				}
				List<String> subList = ids.subList(0, this.getLastUpdateResponseSize());
				response = new ArrayList<DataObjectJO>();
				Iterator<String> sublist = subList.iterator();
				while (sublist.hasNext()) {
					String contentId = sublist.next();
					DataObject content = this.getDataObjectManager().loadDataObject(contentId, false);
					DataObjectRecordVO vo = this.getDataObjectManager().loadDataObjectVO(contentId);
					DataObjectJO contentJO = new DataObjectJO(content, vo);
					response.add(contentJO);
				}
			}

		} catch (Throwable t) {
			_logger.error("Error loading last updated DataObject response", t);
			throw new RuntimeException("Error loading last updated DataObject response", t);
		}
		return response;
	}

	@Override
	public String execute() {
		try {
			DataObjectFinderSearchInfo searchInfo = this.getContentSearchInfo();
			if (null == searchInfo) {
				searchInfo = new DataObjectFinderSearchInfo();
				this.getRequest().getSession().setAttribute(DataObjectFinderSearchInfo.SESSION_NAME, searchInfo);
			}
			this.createFilters();
		} catch (Throwable t) {
			_logger.error("error in execute", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String list() {
		try {
			this.getRequest().getSession().setAttribute(DataObjectFinderSearchInfo.SESSION_NAME,
					new DataObjectFinderSearchInfo());
			this.createFilters();
		} catch (Throwable t) {
			_logger.error("error in execute", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String results() {
		try {
			DataObjectFinderSearchInfo searchInfo = this.getContentSearchInfo();
			if (null == searchInfo) {
				searchInfo = new DataObjectFinderSearchInfo();
				this.getRequest().getSession().setAttribute(DataObjectFinderSearchInfo.SESSION_NAME, searchInfo);
			}
			this.restoreCommonSearchState(searchInfo);
			this.restoreCategorySearchState(searchInfo);
			this.restoreEntitySearchState(searchInfo);
			this.restorePagerSearchState(searchInfo);
			this.addFilter(searchInfo.getFilter(DataObjectFinderSearchInfo.ORDER_FILTER));
		} catch (Throwable t) {
			_logger.error("error in results", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Restituisce la lista identificativi di DataObject che deve essere erogata
	 * dall'interfaccia di visualizzazione dei DataObject. La lista deve essere
	 * filtrata secondo i parametri di ricerca impostati.
	 *
	 * @return La lista di DataObject che deve essere erogata dall'interfaccia
	 * di visualizzazione dei DataObject.
	 */
	public List<String> getDataObjects() {
		List<String> result = null;
		try {
			DataObjectFinderSearchInfo searchInfo = getContentSearchInfo();
			List<String> allowedGroups = this.getContentGroupCodes();
			String[] categories = null;
			Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
			if (null != category && !category.isRoot()) {
				String catCode = this.getCategoryCode().trim();
				categories = new String[]{catCode};
				searchInfo.setCategoryCode(catCode);
			} else {
				searchInfo.setCategoryCode(null);
			}
			result = this.getDataObjectManager().loadWorkDataObjectsId(categories, this.getFilters(), allowedGroups);
		} catch (Throwable t) {
			_logger.error("error in getDataObjects", t);
			throw new RuntimeException("error in getDataObjects", t);
		}
		return result;
	}

	/**
	 * Restituisce la lista di gruppi (codici) dei contenuti che devono essere
	 * visualizzati in lista. La lista viene ricavata in base alle
	 * autorizzazioni dall'utente corrente.
	 *
	 * @return La lista di gruppi cercata.
	 */
	protected List<String> getContentGroupCodes() {
		return super.getActualAllowedGroupCodes();
	}

	/**
	 * Restitusce i filtri per la selezione e l'ordinamento dei contenuti
	 * erogati nell'interfaccia.
	 *
	 * @return Il filtri di selezione ed ordinamento dei contenuti.
	 */
	@SuppressWarnings("rawtypes")
	protected EntitySearchFilter[] createFilters() {
		DataObjectFinderSearchInfo searchInfo = getContentSearchInfo();
		this.createBaseFilters();

		if (null != this.getState() && this.getState().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY, false,
					this.getState(), false);
			this.addFilter(filterToAdd);
			searchInfo.addFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY, filterToAdd);
		} else {
			searchInfo.removeFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY);
		}

		if (null != this.getText() && this.getText().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false,
					this.getText(), true);
			this.addFilter(filterToAdd);
			searchInfo.addFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, filterToAdd);
		} else {
			searchInfo.removeFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY);
		}

		if (null != this.getOwnerGroupName() && this.getOwnerGroupName().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY,
					false, this.getOwnerGroupName(), false);
			this.addFilter(filterToAdd);
			searchInfo.addFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY, filterToAdd);
		} else {
			searchInfo.removeFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY);
		}

		if (null != this.getOnLineState() && this.getOnLineState().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY, false);
			filterToAdd.setNullOption(this.getOnLineState().trim().equals("no"));
			this.addFilter(filterToAdd);
			searchInfo.addFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY, filterToAdd);
		} else {
			searchInfo.removeFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY);
		}

		if (null != this.getContentIdToken() && this.getContentIdToken().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false,
					this.getContentIdToken(), true);
			this.addFilter(filterToAdd);
			searchInfo.addFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, filterToAdd);
		} else {
			searchInfo.removeFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY);
		}

		this.savePagerSearchState(searchInfo);
		EntitySearchFilter orderFilter = this.getDataObjectActionHelper().getOrderFilter(this.getLastGroupBy(),
				this.getLastOrder());
		super.addFilter(orderFilter);
		searchInfo.addFilter(DataObjectFinderSearchInfo.ORDER_FILTER, orderFilter);

		return this.getFilters();
	}

	@SuppressWarnings("rawtypes")
	protected void createBaseFilters() {
		try {
			int initSize = this.getFilters().length;
			DataObjectFinderSearchInfo searchInfo = getContentSearchInfo();

			EntitySearchFilter[] roleFilters = this.getEntityActionHelper().getRoleFilters(this);
			this.addFilters(roleFilters);
			IApsEntity prototype = this.getEntityPrototype();
			searchInfo.removeFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY);
			searchInfo.removeFilterByPrefix(DataObjectFinderSearchInfo.ATTRIBUTE_FILTER);
			if (null != prototype) {
				EntitySearchFilter filterToAdd = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY,
						false, prototype.getTypeCode(), false);
				this.addFilter(filterToAdd);
				searchInfo.addFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, filterToAdd);

				EntitySearchFilter[] filters = this.getEntityActionHelper().getAttributeFilters(this, prototype);
				this.addFilters(filters);
				searchInfo.addAttributeFilters(filters);
			}
			this.setAddedAttributeFilter(this.getFilters().length > initSize);
		} catch (Throwable t) {
			_logger.error("Error while creating entity filters", t);
			throw new RuntimeException("Error while creating entity filters", t);
		}
	}

	private DataObjectFinderSearchInfo getContentSearchInfo() {
		DataObjectFinderSearchInfo searchInfo = (DataObjectFinderSearchInfo) this.getRequest().getSession()
				.getAttribute(DataObjectFinderSearchInfo.SESSION_NAME);
		return searchInfo;
	}

	public String changeOrder() {
		try {
			if (null == this.getGroupBy()) {
				return SUCCESS;
			}
			if (this.getGroupBy().equals(this.getLastGroupBy())) {
				boolean condition = (null == this.getLastOrder()
						|| this.getLastOrder().equals(EntitySearchFilter.ASC_ORDER));
				String order = (condition ? EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);
				this.setLastOrder(order);
			} else {
				this.setLastOrder(EntitySearchFilter.DESC_ORDER);
			}
			this.setLastGroupBy(this.getGroupBy());
		} catch (Throwable t) {
			_logger.error("error in changeOrder", t);
			throw new RuntimeException("error in changeOrder", t);
		}
		return this.execute();
	}

	public String insertOnLine() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> iter = this.getContentIds().iterator();
			List<DataObject> publishedContents = new ArrayList<DataObject>();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				DataObject contentToPublish = this.getDataObjectManager().loadDataObject(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToPublish) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToPublishNull", msgArg));
					continue;
				}
				msgArg[0] = contentToPublish.getDescription();
				if (!this.isUserAllowed(contentToPublish)) {
					this.addActionError(this.getText("error.content.userNotAllowedToPublishContent", msgArg));
					continue;
				}
				this.getDataObjectActionHelper().scanEntity(contentToPublish, this);
				if (this.getFieldErrors().size() > 0) {
					this.addActionError(this.getText("error.content.publishingContentWithErrors", msgArg));
					continue;
				}
				this.getDataObjectManager().insertOnLineDataObject(contentToPublish);
				_logger.info("Published dataObject {} by user {}", contentToPublish.getId(),
						this.getCurrentUser().getUsername());
				publishedContents.add(contentToPublish);
				this.addActivityStreamInfo(contentToPublish, (ApsAdminSystemConstants.ADD + 10), true);
			}
			// RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.publishedContents", publishedContents);
		} catch (Throwable t) {
			_logger.error("error in insertOnLine", t);
			throw new RuntimeException("Error publishing contents", t);
		}
		return SUCCESS;
	}

	/**
	 * Esegue la rimozione dall'area pubblica di un singolo contenuto
	 * direttamente dall'interfaccia di visualizzazione dei contenuti in lista.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeOnLine() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> contentsIdsItr = this.getContentIds().iterator();
			List<DataObject> removedContents = new ArrayList<DataObject>();
			while (contentsIdsItr.hasNext()) {
				String contentId = (String) contentsIdsItr.next();
				DataObject contentToSuspend = this.getDataObjectManager().loadDataObject(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToSuspend) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToSuspendNull", msgArg));
					continue;
				}
				msgArg[0] = contentToSuspend.getDescription();
				if (!this.isUserAllowed(contentToSuspend)) {
					this.addActionError(this.getText("error.content.userNotAllowedToSuspendContent", msgArg));
					continue;
				}
				Map references = this.getDataObjectActionHelper().getReferencingObjects(contentToSuspend, this.getRequest());
				if (references.size() > 0) {
					this.addActionError(this.getText("error.content.suspendingContentWithReferences", msgArg));
					continue;
				}
				this.getDataObjectManager().removeOnLineDataObject(contentToSuspend);
				_logger.info("Suspended DataObject '{}' by user {}", contentToSuspend.getId(),
						this.getCurrentUser().getUsername());
				removedContents.add(contentToSuspend);
				this.addActivityStreamInfo(contentToSuspend, (ApsAdminSystemConstants.DELETE + 10), true);
			}
			// RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.suspendedContents", removedContents);
		} catch (Throwable t) {
			_logger.error("Error on suspending contents", t);
			throw new RuntimeException("Error on suspending contents", t);
		}
		return SUCCESS;
	}

	/**
	 * We've moved to deletion check here in the 'trash' action so to have
	 * errors notified immediately. Be design we share all the messages with the
	 * 'delete' action.
	 *
	 * @return the result code of the action: "success" if all the contents can
	 * be deleted, "cannotProceed" if blocking errors are detected
	 */
	public String trash() {
		if (null == this.getContentIds()) {
			this.addActionError(this.getText("error.contents.nothingSelected"));
			return INPUT;
		}
		try {
			Iterator<String> itr = this.getContentIds().iterator();
			while (itr.hasNext()) {
				String currentContentId = itr.next();
				String msgArg[] = new String[1];
				DataObject contentToTrash = this.getDataObjectManager().loadDataObject(currentContentId, false);
				if (null == contentToTrash) {
					msgArg[0] = currentContentId;
					this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
					continue;
				}
				msgArg[0] = contentToTrash.getDescription();
				if (!this.isUserAllowed(contentToTrash)) {
					this.addActionError(this.getText("error.content.userNotAllowedToContentToDelete", msgArg));
					continue;
				}
				if (contentToTrash.isOnLine()) {
					this.addActionError(this.getText("error.content.notAllowedToDeleteOnlineContent", msgArg));
					continue;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on deleting contents - trash", t);
			throw new RuntimeException("Error on deleting contents", t);
		}
		if (this.getActionErrors().isEmpty()) {
			return SUCCESS;
		}
		return "cannotProceed";
	}

	/**
	 * Esegue l'operazione di cancellazione contenuto o gruppo contenuti.
	 *
	 * @return Il codice del risultato.
	 */
	public String delete() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> iter = this.getContentIds().iterator();
			List<DataObject> deletedContents = new ArrayList<DataObject>();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				DataObject contentToDelete = this.getDataObjectManager().loadDataObject(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToDelete) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
					continue;
				}
				msgArg[0] = contentToDelete.getDescription();
				if (!this.isUserAllowed(contentToDelete)) {
					this.addActionError(this.getText("error.content.userNotAllowedToContentToDelete", msgArg));
					continue;
				}
				if (contentToDelete.isOnLine()) {
					this.addActionError(this.getText("error.content.notAllowedToDeleteOnlineContent", msgArg));
					continue;
				}
				this.getDataObjectManager().deleteDataObject(contentToDelete);
				_logger.info("Deleted DataObject '{}' by user {}", contentToDelete.getId(),
						this.getCurrentUser().getUsername());
				deletedContents.add(contentToDelete);
				this.addActivityStreamInfo(contentToDelete, ApsAdminSystemConstants.DELETE, false);
			}
			// RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.deletedContents", deletedContents);
		} catch (Throwable t) {
			_logger.error("Error deleting contentd - delete", t);
			throw new RuntimeException("Errore in cancellazione DataObjects", t);
		}
		return SUCCESS;
	}

	protected boolean isUserAllowed(DataObject dataObject) {
		return this.getDataObjectActionHelper().isUserAllowed(dataObject, this.getCurrentUser());
	}

	protected void addConfirmMessage(String key, List<DataObject> contents) {
		if (contents.size() > 0) {
			// RIVISITARE LOGICA DI COSTRUZIONE MESSAGGIO
			String confirm = this.getText(key);
			for (int i = 0; i < contents.size(); i++) {
				DataObject dataObject = contents.get(i);
				if (i > 0) {
					confirm += " - ";
				}
				confirm += " '" + dataObject.getDescription() + "'";
			}
			this.addActionMessage(confirm);
		}
	}

	/**
	 * Restituisce il DataObject vo in base all'identificativo.
	 *
	 * @param dataObjectId L'identificativo del DataObject.
	 * @return Il DataObject vo cercato.
	 */
	public DataObjectRecordVO getDataObjectVo(String dataObjectId) {
		DataObjectRecordVO dataObjectVo = null;
		try {
			dataObjectVo = this.getDataObjectManager().loadDataObjectVO(dataObjectId);
		} catch (Throwable t) {
			_logger.error("error in getDataObjectVo for DataObject {}", dataObjectId, t);
			throw new RuntimeException("Errore in caricamento DataObject vo", t);
		}
		return dataObjectVo;
	}

	public List<SmallEntityType> getContentTypes() {
		return this.getDataObjectManager().getSmallEntityTypes();
	}

	public SmallEntityType getSmallDataObjectType(String code) {
		return this.getDataObjectManager().getSmallDataTypesMap().get(code);
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

	protected void addActivityStreamInfo(DataObject dataObject, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = this.getDataObjectActionHelper().createActivityStreamInfo(dataObject, strutsAction, addLink);
		super.addActivityStreamInfo(asi);
	}

	protected void savePagerSearchState(DataObjectFinderSearchInfo searchInfo) {
		boolean found = searchInfo.setPagerFromParameters(this.getRequest().getParameterNames());
		if (!found) {
			String pagerName = this.getPagerName();
			String pos = this.getRequest().getParameter(pagerName);
			if (StringUtils.isNotBlank(pos) && StringUtils.isNumeric(pos)) {
				searchInfo.setPageName(pagerName);
				searchInfo.setPagePos(new Integer(pos));
			}
		}
	}

	protected void restorePagerSearchState(DataObjectFinderSearchInfo searchInfo) {
		String pageName = searchInfo.getPageName();
		if (StringUtils.isNotBlank(pageName)) {
			this.getRequest().setAttribute(pageName, searchInfo.getPagePos());
		}
	}

	@SuppressWarnings("rawtypes")
	protected void restoreEntitySearchState(DataObjectFinderSearchInfo searchInfo) {
		if (null != searchInfo.getFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
			EntitySearchFilter filter = searchInfo.getFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY);
			this.addFilter(filter);
			this.setEntityTypeCode((String) filter.getValue());

			EntitySearchFilter[] filters = searchInfo.getFiltersByKeyPrefix(DataObjectFinderSearchInfo.ATTRIBUTE_FILTER);
			if (null != filters && filters.length > 0) {
				for (EntitySearchFilter entitySearchFilter : filters) {
					this.addFilter(entitySearchFilter);

					String attrName = entitySearchFilter.getKey();
					ApsEntity proto = (ApsEntity) this.getEntityPrototype();

					String[] attributeFilterFieldNames = this.getEntityActionHelper().getAttributeFilterFieldName(proto,
							attrName);
					if (null != attributeFilterFieldNames && attributeFilterFieldNames.length == 1) {
						this.getRequest().setAttribute(attributeFilterFieldNames[0], entitySearchFilter.getValue());
					} else if (null != attributeFilterFieldNames && attributeFilterFieldNames.length == 2) {
						Date start = (Date) entitySearchFilter.getStart();
						if (null != start) {
							this.getRequest().setAttribute(attributeFilterFieldNames[0],
									DateConverter.getFormattedDate(start, EntitySearchFilter.DATE_PATTERN));
						}
						Date end = (Date) entitySearchFilter.getEnd();
						if (null != end) {
							this.getRequest().setAttribute(attributeFilterFieldNames[1],
									DateConverter.getFormattedDate(end, EntitySearchFilter.DATE_PATTERN));
						}
					}

				}
			}
		}
	}

	private void restoreCategorySearchState(DataObjectFinderSearchInfo searchInfo) {
		String catCode = searchInfo.getCategoryCode();
		if (StringUtils.isNotBlank(catCode)) {
			Category category = this.getCategoryManager().getCategory(catCode);
			if (null != category && !category.isRoot()) {
				this.setCategoryCode(catCode);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected void restoreCommonSearchState(DataObjectFinderSearchInfo searchInfo) {
		if (null != searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY)) {
			EntitySearchFilter filterToAdd = searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY);
			this.addFilter(filterToAdd);
			this.setState((String) filterToAdd.getValue());
		}

		if (null != searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY)) {
			EntitySearchFilter filterToAdd = searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY);
			this.addFilter(filterToAdd);
			this.setText((String) filterToAdd.getValue());
		}

		if (null != searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY)) {
			EntitySearchFilter filterToAdd = searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY);
			this.addFilter(filterToAdd);
			this.setOwnerGroupName((String) filterToAdd.getValue());
		}
		if (null != searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY)) {
			EntitySearchFilter filterToAdd = searchInfo.getFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY);
			this.addFilter(filterToAdd);
			this.setOnLineState(filterToAdd.isNullOption() ? "no" : "yes");
		}
		if (null != searchInfo.getFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY)) {
			EntitySearchFilter filterToAdd = searchInfo.getFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY);
			this.addFilter(filterToAdd);
			this.setContentIdToken((String) filterToAdd.getValue());
		}
	}

	public String entryBulkActions() {
		try {
			Set<String> contentIds = null;
			if (this.isAllContentsSelected()) {
				this.getRequest().getSession().setAttribute(DataObjectFinderSearchInfo.SESSION_NAME,
						new DataObjectFinderSearchInfo());
				this.createFilters();
				List<String> allContentIds = this.getDataObjects();
				if (allContentIds != null) {
					contentIds = new TreeSet<String>(allContentIds);
				}
			} else {
				contentIds = this.getContentIds();
			}
			this.setSelectedIds(contentIds);
			if (contentIds == null || contentIds.isEmpty()) {
				this.addActionError(this.getText("error.content.bulkAction.empty"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in execute", t);
			return FAILURE;
		}
		return SUCCESS;
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

	public List<Group> getAllowedGroups() {
		return super.getActualAllowedGroups();
	}

	/**
	 * Restituisce la lista ordinata dei gruppi presenti nel sistema.
	 *
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}

	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}

	protected IDataObjectActionHelper getDataObjectActionHelper() {
		return (IDataObjectActionHelper) super.getEntityActionHelper();
	}

	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		// method not supported
	}

	@Override
	protected IEntityManager getEntityManager() {
		return this.getDataObjectManager();
	}

	public String getContentType() {
		return super.getEntityTypeCode();
	}

	public void setContentType(String contentType) {
		super.setEntityTypeCode(contentType);
	}

	public String getState() {
		return _state;
	}

	public void setState(String state) {
		this._state = state;
	}

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		this._text = text;
	}

	public String getOnLineState() {
		return _onLineState;
	}

	public void setOnLineState(String onLineState) {
		this._onLineState = onLineState;
	}

	public void setContentIdToken(String contentIdToken) {
		this._contentIdToken = contentIdToken;
	}

	public String getContentIdToken() {
		return _contentIdToken;
	}

	public String getOwnerGroupName() {
		return _ownerGroupName;
	}

	public void setOwnerGroupName(String ownerGroupName) {
		this._ownerGroupName = ownerGroupName;
	}

	public String getCategoryCode() {
		return _categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	public String getLastOrder() {
		return _lastOrder;
	}

	public void setLastOrder(String order) {
		this._lastOrder = order;
	}

	public String getLastGroupBy() {
		return _lastGroupBy;
	}

	public void setLastGroupBy(String lastGroupBy) {
		this._lastGroupBy = lastGroupBy;
	}

	public String getGroupBy() {
		return _groupBy;
	}

	public void setGroupBy(String groupBy) {
		this._groupBy = groupBy;
	}

	public boolean isViewCode() {
		return _viewCode;
	}

	public void setViewCode(boolean viewCode) {
		this._viewCode = viewCode;
	}

	public boolean isViewStatus() {
		return _viewStatus;
	}

	public void setViewStatus(boolean viewStatus) {
		this._viewStatus = viewStatus;
	}

	public boolean isViewCreationDate() {
		return _viewCreationDate;
	}

	public void setViewCreationDate(boolean viewCreationDate) {
		this._viewCreationDate = viewCreationDate;
	}

	public boolean getViewGroup() {
		return _viewGroup;
	}

	public void setViewGroup(boolean viewGroup) {
		this._viewGroup = viewGroup;
	}

	public boolean getViewTypeDescr() {
		return _viewTypeDescr;
	}

	public void setViewTypeDescr(boolean viewTypeDescr) {
		this._viewTypeDescr = viewTypeDescr;
	}

	public String getPagerName() {
		return pagerName;
	}

	public void setPagerName(String pagerName) {
		this.pagerName = pagerName;
	}

	public Set<String> getContentIds() {
		return _contentIds;
	}

	public void setContentIds(Set<String> contentIds) {
		this._contentIds = contentIds;
	}

	public Set<String> getSelectedIds() {
		return _selectedIds;
	}

	public void setSelectedIds(Set<String> selectedIds) {
		this._selectedIds = selectedIds;
	}

	public String getActionCode() {
		return _actionCode;
	}

	public void setActionCode(String actionCode) {
		this._actionCode = actionCode;
	}

	public boolean isAllContentsSelected() {
		return _allContentsSelected;
	}

	public void setAllContentsSelected(boolean allContentsSelected) {
		this._allContentsSelected = allContentsSelected;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public int getLastUpdateResponseSize() {
		return _lastUpdateResponseSize;
	}

	public void setLastUpdateResponseSize(int lastUpdateResponseSize) {
		this._lastUpdateResponseSize = lastUpdateResponseSize;
	}

	private String _state = "";
	private String _text = "";
	private String _onLineState = "";
	private String _contentIdToken = "";
	private String _ownerGroupName;
	private String _categoryCode;

	private String _lastOrder;
	private String _lastGroupBy;
	private String _groupBy;

	private boolean _viewCode;
	private boolean _viewGroup;
	private boolean _viewStatus;
	private boolean _viewTypeDescr;
	private boolean _viewCreationDate;

	private boolean _allContentsSelected;
	private int _strutsAction;

	private Set<String> _contentIds;
	private Set<String> _selectedIds;// Used in bulk actions
	private int _lastUpdateResponseSize;

	private String _actionCode = null;

	private String pagerName = AdminPagerTagHelper.DEFAULT_PAGER_NAME;

	private IDataObjectManager _dataObjectManager;
	private IGroupManager _groupManager;
	private ICategoryManager _categoryManager;

}
