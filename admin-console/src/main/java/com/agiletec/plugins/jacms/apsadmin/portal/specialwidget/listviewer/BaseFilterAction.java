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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Classe action per la configurazione base del filtro.
 * La classe action fornisce le funzionalità per la gestione di filtri su metadato 
 * (su data creazione e modifica del contenuto) e la funzione base per le classi action 
 * di gestione dei filtri su attributo (di tipo "Text", "Boolean", "Date" o "Number").
 * @author E.Santoboni
 */
public class BaseFilterAction extends SimpleWidgetConfigAction implements IContentListFilterAction {

	private static final Logger _logger = LoggerFactory.getLogger(BaseFilterAction.class);
	
	@Override
	public String newFilter() {
		return SUCCESS;
	}
	
	@Override
	public String setFilterType() {
		try {
			Content prototype = this.getContentManager().createContentType(this.getContentType());
			String key = this.getFilterKey();
			int attrFilterType = -1;
			AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(key);
			if (null != attribute) {
				if (attribute instanceof ITextAttribute) {
					attrFilterType = TEXT_ATTRIBUTE_FILTER_TYPE;
				} else if (attribute instanceof NumberAttribute) {
					attrFilterType = NUMBER_ATTRIBUTE_FILTER_TYPE;
				} else if (attribute instanceof BooleanAttribute) {
					attrFilterType = BOOLEAN_ATTRIBUTE_FILTER_TYPE;
				} else if (attribute instanceof DateAttribute) {
					attrFilterType = DATE_ATTRIBUTE_FILTER_TYPE;
				}
			} else if ((METADATA_KEY_PREFIX+IContentManager.CONTENT_CREATION_DATE_FILTER_KEY).equals(key) 
					|| (METADATA_KEY_PREFIX+IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY).equals(key)) {
				key = key.substring(METADATA_KEY_PREFIX.length());
				this.setFilterKey(key);
				attrFilterType = METADATA_FILTER_TYPE;
			}
			this.setFilterTypeId(attrFilterType);
			if (this.getFilterTypeId() < 0) {
				this.setFilterKey(null);
			}
		} catch (Throwable t) {
			_logger.error("error in setFilterType", t);
			//ApsSystemUtils.logThrowable(t, this, "setFilterType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String setFilterOption() {
		if (this.getFilterOptionId() == ABSENCE_FILTER_OPTION) {
			this.saveFilter();
			return "addFilter";
		}
		return SUCCESS;
	}
	
	@Override
	public String saveFilter() {
		Properties properties = this.createFilterProperties();
		this.setNewFilter(properties);
		return SUCCESS;
	}
	
	/**
	 * Restrituisce la property con i dati di filtro impostati.
	 * Il metodo viene invocato in fase di salvataggio del filtro.
	 * @return La property con i dati di filtro impostati.
	 */
	protected Properties createFilterProperties() {
		Properties properties = new Properties();
		properties.put(EntitySearchFilter.KEY_PARAM, this.getFilterKey());
		properties.put(EntitySearchFilter.FILTER_TYPE_PARAM, String.valueOf(this.isAttributeFilter()));
		if (null != this.getOrder() && this.getOrder().trim().length()>0) {
			properties.put(EntitySearchFilter.ORDER_PARAM, this.getOrder());
		}
		if (ABSENCE_FILTER_OPTION == this.getFilterOptionId()){
			properties.put(EntitySearchFilter.NULL_VALUE_PARAM, "true");
		}
		return properties;
	}
	
	/**
	 * Restituisce la lista di filtri possibili per il tipo di contenuto specificato.
	 * La lista comprende sempre la possibilità di filtrare per i metadati "Data Creazione" e "Data Ultima Modifica" e 
	 * insieme a tutti gli attributi di contenuto (del tipo specificato) dichiarati ricercabili.
	 * La lista è e servizio dell'interfaccia di gestione del filtro.
	 * @return La lista di filtri possibili per il tipo di contenuto specificato.
	 */
	public List<SelectItem> getFilterTypes() {
		List<SelectItem> types = new ArrayList<SelectItem>();
		types.add(new SelectItem(METADATA_KEY_PREFIX+IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, this.getText("label.creationDate")));
		types.add(new SelectItem(METADATA_KEY_PREFIX+IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, this.getText("label.lastModifyDate")));
		Content prototype = this.getContentManager().createContentType(this.getContentType());
		List<AttributeInterface> contentAttributes = prototype.getAttributeList();
		for (int i=0; i<contentAttributes.size(); i++) {
			AttributeInterface attribute = contentAttributes.get(i);
			if (attribute.isSearchable()) {
				types.add(new SelectItem(attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
			}
		}
		return types;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getModelId() {
		return _modelId;
	}
	public void setModelId(String modelId) {
		this._modelId = modelId;
	}
	
	@Deprecated
	public String getCategory() {
		return _category;
	}
	@Deprecated
	public void setCategory(String category) {
		this._category = category;
	}
	
	public String getMaxElemForItem() {
		return _maxElemForItem;
	}
	public void setMaxElemForItem(String maxElemForItem) {
		this._maxElemForItem = maxElemForItem;
	}
	
	public String getFilters() {
		return _filters;
	}
	public void setFilters(String filters) {
		this._filters = filters;
	}
	
	public String getFilterKey() {
		return _filterKey;
	}
	public void setFilterKey(String filterKey) {
		this._filterKey = filterKey;
	}
	
	public int getFilterTypeId() {
		return _filterTypeId;
	}
	public void setFilterTypeId(int filterTypeId) {
		this._filterTypeId = filterTypeId;
	}
	
	public int getFilterOptionId() {
		return _filterOptionId;
	}
	public void setFilterOptionId(int filterOptionId) {
		this._filterOptionId = filterOptionId;
	}
	
	public Properties getNewFilter() {
		return _newFilter;
	}
	public void setNewFilter(Properties newFilter) {
		this._newFilter = newFilter;
	}
	
	public String getOrder() {
		return _order;
	}
	public void setOrder(String order) {
		this._order = order;
	}
	
	public boolean isAttributeFilter() {
		return _attributeFilter;
	}
	public void setAttributeFilter(boolean attributeFilter) {
		this._attributeFilter = attributeFilter;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private String _contentType;
	private String _modelId;
	private String _category;
	private String _maxElemForItem;
	private String _filters;
	
	private Properties _newFilter;
	
	private String _filterKey;
	private int _filterTypeId = -1;
	private int _filterOptionId = -1;
	private String _order;
	private boolean _attributeFilter;
	
	private IContentManager _contentManager;
	
}