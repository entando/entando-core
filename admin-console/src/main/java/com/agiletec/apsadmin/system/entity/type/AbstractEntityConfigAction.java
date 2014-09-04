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
package com.agiletec.apsadmin.system.entity.type;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Base action for Configure Entity Objects.
 * @author E.Santoboni
 */
public abstract class AbstractEntityConfigAction extends BaseAction implements BeanFactoryAware {
	
	/**
	 * Return an attribute role.
	 * @param roleName The name of the tole to return.
	 * @return The required role.
	 */
	public AttributeRole getAttributeRole(String roleName) {
		return this.getEntityManager().getAttributeRole(roleName);
	}
	
	/**
	 * Return the entity manager that manages the current entity on edit.
	 * @return The entity manager.
	 */
	protected IEntityManager getEntityManager() {
		String entityManagerName = this.getEntityManagerName();
		return (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
	}
	
	/**
	 * Return the namespace prefix specific for the current entity manager.
	 * The prefix will extract by the object {@link EntityTypeNamespaceInfoBean} associated to the current entity manager.
	 * @return The namespace prefix specific for the current entity manager.
	 */
	public String getEntityTypeManagementNamespacePrefix() {
		try {
			EntityTypeNamespaceInfoBean infoBean = (EntityTypeNamespaceInfoBean) this.getBeanFactory().getBean(this.getEntityManagerName()+"NamespaceInfoBean");
			return infoBean.getNamespacePrefix();
		} catch (Throwable t) {
			//nothing to do
		}
		return "";
	}
	
	public String getEntityManagerName() {
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}
	
	public String getEntityTypeCode() {
		return _entityTypeCode;
	}
	public void setEntityTypeCode(String entityTypeCode) {
		this._entityTypeCode = entityTypeCode;
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private String _entityManagerName;
	
	private String _entityTypeCode;
	
	private BeanFactory _beanFactory;
	
}
