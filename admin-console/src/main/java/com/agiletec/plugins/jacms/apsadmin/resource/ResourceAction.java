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
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Class used to handle resource objects.
 * 
 * @author E.Santoboni
 */
public class ResourceAction extends AbstractResourceAction implements ResourceDataBean {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceAction.class);

	@Override
	public void validate() {
		super.validate();
		if (null != this.getFile()) {
			ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
			this.checkRightFileType(resourcePrototype);
		}
	}

	protected void checkRightFileType(ResourceInterface resourcePrototype) {
		boolean isRight = false;
		if (this.getFileName().length() > 0) {
			String fileName = this.getFileName();
			String docType = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
			String[] types = resourcePrototype.getAllowedFileTypes();
			isRight = this.isValidType(docType, types);
		} else {
			isRight = true;
		}
		if (!isRight) {
			this.addFieldError("upload", this.getText("error.resource.file.wrongFormat"));
		}
	}

	private boolean isValidType(String docType, String[] rightTypes) {
		boolean isValid = false;
		if (rightTypes.length > 0) {
			for (int i = 0; i < rightTypes.length; i++) {
				if (docType.toLowerCase().equals(rightTypes[i])) {
					isValid = true;
					break;
				}
			}
		} else {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Executes the specific action to create a new content
	 * 
	 * @return The result code.
	 */
	public String newResource() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		try {
			if (this.getAuthorizationManager().isAuthOnGroup(this.getCurrentUser(), Group.FREE_GROUP_NAME)) {
				this.setMainGroup(Group.FREE_GROUP_NAME);
			}
		} catch (Throwable t) {
			_logger.error("error in newResource", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Executes the specific action to modify an existing resource.
	 * 
	 * @return The result code.
	 */
	public String edit() {
		try {
			ResourceInterface resource = this.loadResource(this.getResourceId());
			this.setResourceTypeCode(resource.getType());
			this.setDescr(resource.getDescription());
			List<Category> resCategories = resource.getCategories();
			for (int i = 0; i < resCategories.size(); i++) {
				Category resCat = resCategories.get(i);
				this.getCategoryCodes().add(resCat.getCode());
			}
			this.setMainGroup(resource.getMainGroup());
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String detail() {
		try {
			ResourceInterface resource = this.loadResource(this.getResourceId());
			if (null == resource) {
				return INPUT;
			}
			this.setResourceTypeCode(resource.getType());
			this.setDescr(resource.getDescription());
			List<Category> resCategories = resource.getCategories();
			for (int i = 0; i < resCategories.size(); i++) {
				Category resCat = resCategories.get(i);
				this.getCategoryCodes().add(resCat.getCode());
			}
			this.setMainGroup(resource.getMainGroup());
		} catch (Throwable t) {
			_logger.error("error in detail", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Executes the specific action to modify an existing resource.
	 * 
	 * @return The result code.
	 */
	public String save() {
		try {
			if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				this.getResourceManager().addResource(this);
			} else if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
				this.getResourceManager().updateResource(this);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Executes the specific action to delete a resource. This does NOT perform
	 * any deletion, it just ensures that there are no hindrances to a deletion
	 * process.
	 * 
	 * @return The result code.
	 */
	public String trash() {
		try {
			String result = this.checkDeleteResource();
			if (null != result)
				return result;
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * This forces the deletion of a resource. NOTE! This method is invoked, in
	 * the administration interface, when deleting a referenced resource.
	 * 
	 * @return The result code.
	 */
	public String delete() {
		try {
			String result = this.checkDeleteResource();
			if (null != result)
				return result;
			ResourceInterface resource = this.loadResource(this.getResourceId());
			this.getResourceManager().deleteResource(resource);
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected String checkDeleteResource() throws Throwable {
		String resourceId = this.getResourceId();
		ResourceInterface resource = this.loadResource(resourceId);
		if (resource == null) {
			this.addActionError(this.getText("error.resource.delete.invalid"));
			return INPUT;
		}
		Map<String, List> references = this.getResourceActionHelper().getReferencingObjects(resource, this.getRequest());
		this.setReferences(references);
		if (references.size() > 0) {
			return "references";
		}
		return null;
	}

	/**
	 * Executes the specific action in order to associate a category to the
	 * resource on edit.
	 * 
	 * @return The result code.
	 */
	public String joinCategory() {
		return this.joinRemoveCategory(true, this.getCategoryCode());
	}

	/**
	 * Executes the specific action in order to remove the association between a
	 * category and the resource on edit.
	 * 
	 * @return The result code.
	 */
	public String removeCategory() {
		return this.joinRemoveCategory(false, this.getCategoryCode());
	}

	/**
	 * This method perfoms either the linking of a resource to a category or the
	 * removal of such association. NOTE: in the current implementation
	 * operations carried on invalid or unknown categories do not return error
	 * code on purpose, since the join or unlink process does not take place.
	 * 
	 * @param isJoin
	 * if 'true' associates a resource to a category, otherwise remove it
	 * @param categoryCode
	 * the string code of the category to work with.
	 * @return FAILURE if error detected, SUCCESS otherwise.
	 */
	private String joinRemoveCategory(boolean isJoin, String categoryCode) {
		try {
			Category category = this.getCategory(categoryCode);
			if (category == null)
				return SUCCESS;
			List<String> categories = this.getCategoryCodes();
			if (isJoin) {
				if (!categories.contains(categoryCode)) {
					categories.add(categoryCode);
				}
			} else {
				categories.remove(categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("error in joinRemoveCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	@Override
	public String getResourceId() {
		return _resourceId;
	}

	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public Map<String, List> getReferences() {
		try {
			if (null == this._references) {
				this._references = this.getResourceActionHelper().getReferencingObjects(this.getResourceId(), this.getRequest());
			}
		} catch (Throwable t) {
			_logger.error("Error extracting references", t);
		}
		return _references;
	}

	public void setReferences(Map<String, List> references) {
		this._references = references;
	}

	public void setUpload(File file) {
		this._file = file;
	}

	public File getUpload() {
		return this._file;
	}

	public void setUploadContentType(String contentType) {
		this._contentType = contentType;
	}

	public void setUploadFileName(String filename) {
		this._filename = filename;
	}

	@Override
	public String getDescr() {
		return _descr;
	}

	public void setDescr(String descr) {
		this._descr = descr;
	}

	@Override
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>(this.getCategoryCodes().size());
		Iterator<String> iter = this.getCategoryCodes().iterator();
		while (iter.hasNext()) {
			String categoryCode = iter.next();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category)
				categories.add(category);
		}
		return categories;
	}

	@Override
	public String getFileName() {
		return this._filename;
	}

	@Override
	public int getFileSize() {
		return (int) this._file.length() / 1000;
	}

	@Override
	public File getFile() {
		return _file;
	}

	@Override
	public InputStream getInputStream() throws Throwable {
		if (null == this.getFile())
			return null;
		return new FileInputStream(this.getFile());
	}

	@Override
	public String getMainGroup() {
		return this._mainGroup;
	}

	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}

	public List<String> getCategoryCodes() {
		return _categoryCodes;
	}

	public void setCategoryCodes(List<String> categoryCodes) {
		this._categoryCodes = categoryCodes;
	}

	@Override
	public String getMimeType() {
		return this._contentType;
	}

	@Override
	public String getResourceType() {
		return this.getResourceTypeCode();
	}

	public String getCategoryCode() {
		return _categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	private String _resourceId;
	private String _descr;
	private String _mainGroup;
	private List<String> _categoryCodes = new ArrayList<String>();

	private File _file;
	private String _contentType;
	private String _filename;

	private int _strutsAction;

	private Map<String, List> _references;

	private String _categoryCode;

	private IGroupManager _groupManager;

}
