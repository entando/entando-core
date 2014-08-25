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
package com.agiletec.aps.system.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.common.notify.INotifyManager;

/**
 * Base class for implementation of the System Services.
 * Services are instantiated and initialized to the system boot 
 * or when prompted for the re-loading the entire system.
 * @author M.Diana - E.Santoboni
 */
public abstract class AbstractService 
		implements IManager, BeanNameAware, BeanFactoryAware {
	
	/**
	 * Method to be invoked to refresh the service.
	 * @throws Throwable In case of error.
	 */
	@Override
	public void refresh() throws Throwable {
		this.release();
		this.init();
	}
	
	/**
	 * Method to implement if you want to give fields of the service before the refresh.
	 * By default does nothing.
	 */
	protected void release() {
		//do nothing
	}
	
	/**
	 * Default implementation of destroy method.
	 * This method can be override if the service has to release objects before the destroy of the service.
	 */
	@Override
	public void destroy() {
		//do nothing
	}
	
	protected BeanFactory getBeanFactory() {
		return this._beanFactory;
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	/**
	 * Returns a service by name.
	 * @param name The name of the service to return.
	 * @return The required service.
	 * @deprecated It's better to avoid the use of this method: you should use the Spring Injection instead.
	 */
	protected IManager getService(String name) {
		return (IManager) this._beanFactory.getBean(name);
	}
	
	@Override
	public void setBeanName(String name) {
		this._name = name;
	}
	
	/** 
	 * Return the name of the service.
	 * @return The name of the service.
	 */
	@Override
	public String getName() {
		return _name;
	}
	
	protected INotifyManager getNotifyManager() {
		return _notifyManager;
	}
	public void setNotifyManager(INotifyManager notifyManager) {
		this._notifyManager = notifyManager;
	}
	
	/**
	 * Notification of an internal event to the service of notification.
	 * @param event The event to notify.
	 */
	protected void notifyEvent(ApsEvent event) {
		this.getNotifyManager().publishEvent(event);
	}
	
	/**
	 * Updates the service in response of an event.
	 * @param event The notified event
	 */
	public void update(ApsEvent event) {
		event.notify(this);
	}
	
	private String _name;
	
	private BeanFactory _beanFactory;
	
	private transient INotifyManager _notifyManager;
	
}
