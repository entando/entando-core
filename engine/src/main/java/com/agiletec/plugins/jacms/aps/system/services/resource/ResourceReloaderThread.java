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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread Class delegate to execute resource refresh operations.
 * The recognized operation is the reloading of the master file name (to improve the porting from jAPS 2.0.x to version 2.2.x) 
 * and the refreshing of resource instances.
 * @author E.Santoboni
 */
public class ResourceReloaderThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceReloaderThread.class);
	
	public ResourceReloaderThread(ResourceManager resourceManager, int operationCode, List<String> resources) {
		this._resourceManager = resourceManager;
		this.setOperationCode(operationCode);
		this.setResources(resources);
	}
	
	@Override
	public void run() {
		if (null == this.getResources()) return;
		if (this.getOperationCode() == RELOAD_MASTER_FILE_NAME) {
			this._resourceManager.setStatus(IResourceManager.STATUS_RELOADING_RESOURCE_MAIN_FILENAME_IN_PROGRESS);
		} else if (this.getOperationCode() == REFRESH_INSTANCE) {
			this._resourceManager.setStatus(IResourceManager.STATUS_RELOADING_RESOURCE_INSTANCES_IN_PROGRESS);
		}
		try {
			for (int i = 0; i < this.getResources().size(); i++) {
				String resourceId = this.getResources().get(i);
				if (this.getOperationCode() == RELOAD_MASTER_FILE_NAME) {
					this._resourceManager.refreshMasterFileNames(resourceId);
				} else if (this.getOperationCode() == REFRESH_INSTANCE) {
					this._resourceManager.refreshResourceInstances(resourceId);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in run", t);
			//ApsSystemUtils.logThrowable(t, this, "run");
		} finally {
			this._resourceManager.setStatus(IResourceManager.STATUS_READY);
		}
	}
	
	protected List<String> getResources() {
		return resources;
	}
	protected void setResources(List<String> resources) {
		this.resources = resources;
	}
	
	protected int getOperationCode() {
		return _operationCode;
	}
	protected void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	private ResourceManager _resourceManager;
	private List<String> resources;
	
	private int _operationCode;
	
	public static final int REFRESH_INSTANCE = 1;
	public static final int RELOAD_MASTER_FILE_NAME = 2;
	
}