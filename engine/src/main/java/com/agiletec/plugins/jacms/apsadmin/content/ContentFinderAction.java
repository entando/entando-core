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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;

/**
 * Action per la ricerca contenuti.
 * @author E.Santoboni
 */
public class ContentFinderAction extends AbstractApsEntityFinderAction implements IContentFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentFinderAction.class);
	
	@Override
	public String execute() {
		try {
			this.createFilters();
		} catch (Throwable t) {
			_logger.error("error in execute", t);
			//ApsSystemUtils.logThrowable(t, this, "execute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
			String[] categories = null;
			Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
			if (null != category && !category.isRoot()) {
				categories = new String[]{this.getCategoryCode().trim()};
			}
			result = this.getContentManager().loadWorkContentsId(categories, this.getFilters(), allowedGroups);
		} catch (Throwable t) {
			_logger.error("error in getContents", t);
			//ApsSystemUtils.logThrowable(t, this, "getContents");
			throw new RuntimeException("error in getContents", t);
		}
		return result;
	}
	
	/**
	 * Restituisce la lista di gruppi (codici) dei contenuti che devono essere visualizzati in lista.
	 * La lista viene ricavata in base alle autorizzazioni dall'utente corrente.
	 * @return La lista di gruppi cercata.
	 */
	protected List<String> getContentGroupCodes() {
		List<String> allowedGroups = new ArrayList<String>();
		List<Group> userGroups = this.getContentActionHelper().getAllowedGroups(this.getCurrentUser());
		Iterator<Group> iter = userGroups.iterator();
    	while (iter.hasNext()) {
    		Group group = iter.next();
    		allowedGroups.add(group.getName());
    	}
    	return allowedGroups;
	}
	
	/**
	 * Restitusce i filtri per la selezione e l'ordinamento dei contenuti erogati nell'interfaccia.
	 * @return Il filtri di selezione ed ordinamento dei contenuti.
	 */
	protected EntitySearchFilter[] createFilters() {
		super.createBaseFilters();
		if (null != this.getState() && this.getState().trim().length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false, this.getState(), false);
			this.addFilter(filterToAdd);
		}
		if (null != this.getText() && this.getText().trim().length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, this.getText(), true);
			this.addFilter(filterToAdd);
		}
		if (null != this.getOwnerGroupName() && this.getOwnerGroupName().trim().length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, false, this.getOwnerGroupName(), false);
			this.addFilter(filterToAdd);
		}
		if (null != this.getOnLineState() && this.getOnLineState().trim().length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
			filterToAdd.setNullOption(this.getOnLineState().trim().equals("no"));
			this.addFilter(filterToAdd);
		}
		if (null != this.getContentIdToken() && this.getContentIdToken().trim().length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, this.getContentIdToken(), true);
			this.addFilter(filterToAdd);
		}
		EntitySearchFilter orderFilter = this.getContentActionHelper().getOrderFilter(this.getLastGroupBy(), this.getLastOrder());
		super.addFilter(orderFilter);
		return this.getFilters();
	}
	
	public String changeOrder() {
		try {
			if (null == this.getGroupBy()) return SUCCESS;
			if (this.getGroupBy().equals(this.getLastGroupBy())) {
				boolean condition = (null == this.getLastOrder() || this.getLastOrder().equals(EntitySearchFilter.ASC_ORDER));
				String order = (condition ? EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);
				this.setLastOrder(order);
			} else {
				this.setLastOrder(EntitySearchFilter.DESC_ORDER);
			}
			this.setLastGroupBy(this.getGroupBy());
		} catch (Throwable t) {
			_logger.error("error in changeOrder", t);
			//ApsSystemUtils.logThrowable(t, this, "changeOrder");
			throw new RuntimeException("error in changeOrder", t);
		}
		return this.execute();
	}
	
	@Override
	public String insertOnLine() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> iter = this.getContentIds().iterator();
			List<Content> publishedContents = new ArrayList<Content>();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				Content contentToPublish = this.getContentManager().loadContent(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToPublish) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToPublishNull", msgArg));
					continue;
				}
				msgArg[0] = contentToPublish.getDescr();
				if (!this.isUserAllowed(contentToPublish)) {
					this.addActionError(this.getText("error.content.userNotAllowedToPublishContent", msgArg));
					continue;
				}
				this.getContentActionHelper().scanEntity(contentToPublish, this);
				if (this.getFieldErrors().size()>0) {
					this.addActionError(this.getText("error.content.publishingContentWithErrors", msgArg));
					continue;
				}
				this.getContentManager().insertOnLineContent(contentToPublish);
				_logger.info("Published content {} by user {}", contentToPublish.getId(), this.getCurrentUser().getUsername());
				publishedContents.add(contentToPublish);
				this.addActivityStreamInfo(contentToPublish, (ApsAdminSystemConstants.ADD + 10), true);
			}
			// RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.publishedContents", publishedContents);
		} catch (Throwable t) {
			_logger.error("error in insertOnLine", t);
			//ApsSystemUtils.logThrowable(t, this, "insertOnLine");
			throw new RuntimeException("Error publishing contents", t);
		}
		return SUCCESS;
	}
	
	@Override
	public String removeOnLine() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> contentsIdsItr = this.getContentIds().iterator();
			List<Content> removedContents = new ArrayList<Content>();
			while (contentsIdsItr.hasNext()) {
			String contentId = (String) contentsIdsItr.next();
				Content contentToSuspend = this.getContentManager().loadContent(contentId, false);
				String[] msgArg = new String[1];					
				if (null == contentToSuspend) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToSuspendNull", msgArg));
					continue;
				}
				msgArg[0] = contentToSuspend.getDescr();
				if (!this.isUserAllowed(contentToSuspend)) {
					this.addActionError(this.getText("error.content.userNotAllowedToSuspendContent", msgArg));
					continue;
				}
				Map references = this.getContentActionHelper().getReferencingObjects(contentToSuspend, this.getRequest());
				if (references.size()>0) {
					this.addActionError(this.getText("error.content.suspendingContentWithReferences", msgArg));
					continue;
				}
				this.getContentManager().removeOnLineContent(contentToSuspend);
				_logger.info("Suspended Content '{}' by user {}",contentToSuspend.getId(), this.getCurrentUser().getUsername());
				removedContents.add(contentToSuspend);
				this.addActivityStreamInfo(contentToSuspend, (ApsAdminSystemConstants.DELETE + 10), true);
			}
			// RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.suspendedContents", removedContents);
		} catch (Throwable t) {
			_logger.error("Error on suspending contents", t);
			//ApsSystemUtils.logThrowable(t, this, "removeOnLine");
			throw new RuntimeException("Error on suspending contents", t);
		}
		return SUCCESS;
	}
	
	/**
	 * We've moved to deletion check here in the 'trash' action so to have errors notified immediately. Be design we
	 * share all the messages with the 'delete' action.
	 * @return the result code of the action: "success" if all the contents can be deleted, "cannotProceed" if blocking errors are detected
	 */
	@Override
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
				Content contentToTrash = this.getContentManager().loadContent(currentContentId, false);
				if (null == contentToTrash) {
					msgArg[0] = currentContentId;
					this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
					continue;
				} 
				msgArg[0] = contentToTrash.getDescr();			
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
			//ApsSystemUtils.logThrowable(t, this, "trash");
			throw new RuntimeException("Error on deleting contents", t);
		}
		if (this.getActionErrors().isEmpty()) return SUCCESS;
		return "cannotProceed";
	}
	
	@Override
	public String delete() {
		try {
			if (null == this.getContentIds()) {
				this.addActionError(this.getText("error.contents.nothingSelected"));
				return INPUT;
			}
			Iterator<String> iter = this.getContentIds().iterator();
			List<Content> deletedContents = new ArrayList<Content>();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				Content contentToDelete = this.getContentManager().loadContent(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToDelete) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
					continue;
				} 
				msgArg[0] = contentToDelete.getDescr();
				if (!this.isUserAllowed(contentToDelete)) {
					this.addActionError(this.getText("error.content.userNotAllowedToContentToDelete", msgArg));
					continue;
				}
				if (contentToDelete.isOnLine()) {
					this.addActionError(this.getText("error.content.notAllowedToDeleteOnlineContent", msgArg));
					continue;
				}
				this.getContentManager().deleteContent(contentToDelete);
				_logger.info("Deleted Content '{}' by user {}",contentToDelete.getId(), this.getCurrentUser().getUsername());
				deletedContents.add(contentToDelete);
				this.addActivityStreamInfo(contentToDelete, ApsAdminSystemConstants.DELETE, false);
			}
			//RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.deletedContents", deletedContents);
		} catch (Throwable t) {
			_logger.error("Error deleting contentd - delete", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			throw new RuntimeException("Errore in cancellazione contenuti", t);
		}
		return SUCCESS;
	}
	
	protected boolean isUserAllowed(Content content) {
		return this.getContentActionHelper().isUserAllowed(content, this.getCurrentUser());
	}
	
	private void addConfirmMessage(String key, List<Content> deletedContents) {
		if (deletedContents.size()>0) {
			//RIVISITARE LOGICA DI COSTRUZIONE MESSAGGIO
			String confirm = this.getText(key);
			for (int i=0; i<deletedContents.size(); i++) {
				Content content = deletedContents.get(i);
				if (i>0) confirm += " - ";
				confirm += " '" + content.getDescr() + "'";
			}
			this.addActionMessage(confirm);
		}
	}
	
	/**
	 * Restituisce il contenuto vo in base all'identificativo.
	 * @param contentId L'identificativo del contenuto.
	 * @return Il contenuto vo cercato.
	 */
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			_logger.error("error in getContentVo for content {}", contentId, t);
			//ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
	}
	
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	public SmallContentType getSmallContentType(String code) {
		return this.getContentManager().getSmallContentTypesMap().get(code);
	}
	
	/**
	 * Restituisce la lista di stati di contenuto definiti nel sistema, come insieme di chiave e valore
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire 
	 * una corretta visualizzazione della pagina.
	 * @return La lista di stati di contenuto definiti nel sistema.
	 */
	public List<SelectItem> getAvalaibleStatus() {
		String[] status = Content.AVAILABLE_STATUS;
		List<SelectItem> items = new ArrayList<SelectItem>(status.length);
		for (int i = 0; i < status.length; i++) {
			SelectItem item = new SelectItem(status[i], "name.contentStatus." + status[i]);
			items.add(item);
		}
		return items;
	}
	
	protected void addActivityStreamInfo(Content content, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = this.getContentActionHelper().createActivityStreamInfo(content, strutsAction, addLink);
		super.addActivityStreamInfo(asi);
	}
	
	/**
	 * Restituisce un gruppo in base al nome.
	 * @param groupName Il nome del gruppo da restituire.
	 * @return Il gruppo cercato.
	 */
	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}
	
	public List<Group> getAllowedGroups() {
		return this.getContentActionHelper().getAllowedGroups(this.getCurrentUser());
	}
	
	/**
	 * Restituisce la lista ordinata dei gruppi presenti nel sistema.
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}
	
	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}
	
	protected IContentActionHelper getContentActionHelper() {
		return (IContentActionHelper) super.getEntityActionHelper();
	}
	
	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		// method not supported
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		return this.getContentManager();
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
	
	public Set<String> getContentIds() {
		return _contentIds;
	}
	public void setContentIds(Set<String> contentIds) {
		this._contentIds = contentIds;
	}
	
	public String getActionCode() {
		return _actionCode;
	}
	public void setActionCode(String actionCode) {
		this._actionCode = actionCode;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
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
	
	private Set<String> _contentIds;
	
	private String _actionCode = null;
	
	private IContentManager _contentManager;
	private IGroupManager _groupManager;
	private ICategoryManager _categoryManager;
	
}
