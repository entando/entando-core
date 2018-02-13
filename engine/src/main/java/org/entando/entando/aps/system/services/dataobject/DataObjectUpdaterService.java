/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando Enterprise Edition software.
 * You can redistribute it and/or modify it
 * under the terms of the Entando's EULA
 *
 * See the file License for the specific language governing permissions
 * and limitations under the License
 *
 *
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.aps.system.services.dataobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.cache.ICacheInfoManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.category.ReloadingCategoryReferencesThread;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Manages the operations on the contents in case of events on categories. In
 * the case of moving categories from one node to another must be recharged
 * references EVOLUZIONE DEL CORE - AGGIUNTA FIRST EDITOR e funzioni
 * aggiornamento referenze
 *
 */
public class DataObjectUpdaterService extends AbstractService implements IDataObjectUpdaterService {

	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " ready");
	}

	@Override
	public void reloadCategoryReferences(String categoryCode) {
		try {
			Set<String> contents = this.getDataObjectsId(categoryCode);
			ApsSystemUtils.getLogger().debug("start reload category references for " + contents.size() + " contents");
			ReloadingCategoryReferencesThread th = null;
			Thread currentThread = Thread.currentThread();
			if (currentThread instanceof ReloadingCategoryReferencesThread) {
				th = (ReloadingCategoryReferencesThread) Thread.currentThread();
				th.setListSize(contents.size());
			}
			Iterator<String> it = contents.iterator();
			while (it.hasNext()) {
				String dataid = it.next();
				this.reloadEntityReferences(dataid);
				if (null != th) {
					th.setListIndex(th.getListIndex() + 1);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reloadCategoryReferences");
		}
	}

	@Override
	public Set<String> getDataObjectsId(String categoryCode) throws ApsSystemException {
		Set<String> allContents = new HashSet<String>();
		try {
			//Ricerca contenuti per
			EntitySearchFilter[] filters = null;
			boolean orCategoryFilter = false;
			//tutti i gruppi
			List<String> userGroupCodes = new ArrayList<String>();
			userGroupCodes.add(Group.ADMINS_GROUP_NAME);

			//associati alla categoria che è stata spostata...
			String[] categories = new String[]{categoryCode};

			List<String> publicContents = this.getContentManager().loadDataObjectsId(categories, orCategoryFilter, filters, userGroupCodes);
			ApsSystemUtils.getLogger().debug("public contents: " + publicContents.size());

			allContents.addAll(publicContents);

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentsId");
			throw new ApsSystemException("Error loading contents to update", t);
		}
		return allContents;
	}

	public void reloadEntityReferences(String entityId) {
		try {
			String cacheKey = JacmsSystemConstants.CONTENT_CACHE_PREFIX + entityId;
			this.getCacheInfoManager().flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, cacheKey);
			ApsSystemUtils.getLogger().debug("removing_from_cache " + cacheKey);
			DataObject content = this.getContentManager().loadDataObject(entityId, true);
			if (content != null) {
				this.getContentUpdaterDAO().reloadDataObjectCategoryReferences(content);
			}
			ApsSystemUtils.getLogger().debug("Reload content references for content " + entityId + "- DONE");
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reloadEntityReferences");
		}
	}

	protected IDataObjectManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IDataObjectManager contentManager) {
		this._contentManager = contentManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected IDataObjectUpdaterDAO getContentUpdaterDAO() {
		return _contentUpdaterDAO;
	}

	public void setContentUpdaterDAO(IDataObjectUpdaterDAO contentUpdaterDAO) {
		this._contentUpdaterDAO = contentUpdaterDAO;
	}

	protected ICacheInfoManager getCacheInfoManager() {
		return _cacheInfoManager;
	}

	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this._cacheInfoManager = cacheInfoManager;
	}

	private IDataObjectManager _contentManager;
	private ICategoryManager _categoryManager;
	private IDataObjectUpdaterDAO _contentUpdaterDAO;
	private ICacheInfoManager _cacheInfoManager;

}
