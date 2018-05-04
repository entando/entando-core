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
package com.agiletec.aps.system.services.category;

import com.agiletec.aps.system.services.category.thread.NotifyingThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReloadingCategoryReferencesThread extends NotifyingThread {

	private static final Logger _logger = LoggerFactory.getLogger(ReloadingCategoryReferencesThread.class);

	/**
	 * Setup the thread for the references reloading
	 * @param categoryManager 
	 * @param beanName 
	 */
	public ReloadingCategoryReferencesThread(ICategoryManager categoryManager, String beanName, String categoryCode) {
		this._categoryManager = categoryManager;
		this._beanName = beanName;
		this._categoryCode = categoryCode;
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public void doRun() {
		try {
			//System.out.println(this._label + " STARTING....");
			((CategoryManager)this._categoryManager).reloadCategoryReferencesByBeanName(_beanName, _categoryCode);
			//System.out.println(this._label + " ENDED");
		} catch (Throwable e) {
			_logger.error("failed to reload in category manager ",e);
		}
	}
	
	
	public int getListIndex() {
		return _listIndex;
	}
	public void setListIndex(int listIndex) {
		this._listIndex = listIndex;
	}


	public int getListSize() {
		return _listSize;
	}
	public void setListSize(int listSize) {
		this._listSize = listSize;
	}


	private ICategoryManager _categoryManager;
	private String _beanName;
	private String _categoryCode;
	private int _listIndex;
	private int _listSize;

}
