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
package org.entando.entando.apsadmin.api;

import java.util.List;

import org.entando.entando.aps.system.services.oauth.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class ConsumerFinderAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ConsumerAction.class);

	public List<String> getSearchResult() throws Throwable {
		List<String> consumerKeys = null;
		try {
			consumerKeys = this.getOauthConsumerManager().getConsumerKeys(this.getSearchFilters());
		} catch (Throwable t) {
			_logger.error("error in getSearchResult", t);
			//ApsSystemUtils.logThrowable(t, this, "getSearchResult");
			throw t;
		}
		return consumerKeys;
	}

	private FieldSearchFilter[] getSearchFilters() {
		FieldSearchFilter[] filters = new FieldSearchFilter[0];
		FieldSearchFilter keyFilter = null;
		if (null != this.getInsertedKey() && this.getInsertedKey().trim().length() > 0) {
			keyFilter = new FieldSearchFilter(IOAuthConsumerManager.CONSUMER_KEY_FILTER_KEY, this.getInsertedKey(), true);
		} else {
			keyFilter = new FieldSearchFilter(IOAuthConsumerManager.CONSUMER_KEY_FILTER_KEY);
		}
		keyFilter.setOrder(FieldSearchFilter.ASC_ORDER);
		filters = this.addFilter(keyFilter, filters);
		if (null != this.getInsertedDescription() && this.getInsertedDescription().trim().length() > 0) {
			FieldSearchFilter descrFilter
					= new FieldSearchFilter(IOAuthConsumerManager.CONSUMER_DESCRIPTION_FILTER_KEY, this.getInsertedDescription(), true);
			filters = this.addFilter(descrFilter, filters);
		}
		return filters;
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter filterToAdd, FieldSearchFilter[] filters) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for (int i = 0; i < len; i++) {
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	/*
    public Map<String, Integer> getTokenOccurrencesByConsumer() throws Throwable {
        try {
            return this.getOauthConsumerManager().getTokenOccurrencesByConsumer();
        } catch (Throwable t) {
        	_logger.error("error in getTokenOccurrencesByConsumer", t);
           //ApsSystemUtils.logThrowable(t, this, "getTokenOccurrencesByConsumer");
            throw t;
        }
    }
	 */
	public ConsumerRecordVO getConsumer(String key) throws Throwable {
		try {
			return this.getOauthConsumerManager().getConsumerRecord(key);
		} catch (Throwable t) {
			_logger.error("error in getConsumer", t);
			//ApsSystemUtils.logThrowable(t, this, "getConsumer");
			throw t;
		}
	}

	public String getInsertedDescription() {
		return _insertedDescription;
	}

	public void setInsertedDescription(String insertedDescription) {
		this._insertedDescription = insertedDescription;
	}

	public String getInsertedKey() {
		return _insertedKey;
	}

	public void setInsertedKey(String insertedKey) {
		this._insertedKey = insertedKey;
	}

	protected IOAuthConsumerManager getOauthConsumerManager() {
		return _oauthConsumerManager;
	}

	public void setOauthConsumerManager(IOAuthConsumerManager oauthConsumerManager) {
		this._oauthConsumerManager = oauthConsumerManager;
	}

	private String _insertedKey;
	private String _insertedDescription;

	private IOAuthConsumerManager _oauthConsumerManager;

}
