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
package com.agiletec.apsadmin.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryManager;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.category.ReloadingCategoryReferencesThread;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.category.helper.ICategoryActionHelper;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.apsadmin.system.TreeNodeWrapper;

/**
 * Action class which handles categories.
 *
 * @author E.Santoboni - G.Cocco
 */
public class CategoryAction extends AbstractTreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(CategoryAction.class);

	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD
				|| this.getStrutsAction() == ApsAdminSystemConstants.PASTE) {
			this.checkParentNode(this.getParentCategoryCode());
		}
		this.checkCode();
		this.checkTitles(true);
	}

	public Integer getServiceStatus() {
		return this.getCategoryManager().getMoveTreeStatus();
	}

	public Map<String, Integer> getServiceStatusMap() {
		return this.getCategoryManager().getReloadStatus();
	}

	public List<TreeNodeWrapper> getAvailableNodesForMoveTreeAjax() {
		List<TreeNodeWrapper> result = new ArrayList<TreeNodeWrapper>();
		try {
			String startCategoryCode = this.getSelectedNode();
			if (StringUtils.isBlank(startCategoryCode)) {
				_logger.warn("required parameter 'selectedNode' missing");
				return result;
			}
			Category nodeToMove = this.getCategory(startCategoryCode);
			if (null == nodeToMove) {
				_logger.warn("category {} is null", startCategoryCode);
				return result;
			}

			//XXX FIX JS
			this.setCategoryCodeToken(super.getParameter("categoryCodeToken"));

			List<Category> searchResult = this.getCategoryManager().searchCategories(this.getCategoryCodeToken());
			if (null == searchResult || searchResult.isEmpty()) {
				return result;
			}

			BeanComparator comparator = new BeanComparator("code");
			Collections.sort(result, comparator);

			int maxIndex = 30;
			String maxParam = super.getParameter("max");
			if (StringUtils.isNotBlank(maxParam) && StringUtils.isNumeric(maxParam)) {
				maxIndex = new Integer(maxParam).intValue();
			}

			Iterator<Category> it = searchResult.iterator();
			while (result.size() < maxIndex && it.hasNext()) {
				ITreeNode candidate = it.next();
				if (!candidate.isChildOf(nodeToMove.getCode()) && !candidate.getCode().equals(nodeToMove.getParentCode())) {
					result.add(new TreeNodeWrapper(candidate, this.getCurrentLang().getCode()));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on searching categories ajax", t);
			throw new RuntimeException("Error on searching categories ajax", t);
		}
		return result;
	}

	private void checkTitles(boolean checkErrors) {
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = (Lang) langsIter.next();
			String titleKey = "lang" + lang.getCode();
			String title = this.getRequest().getParameter(titleKey);
			if (null != title) {
				this.getTitles().put(lang.getCode(), title.trim());
			}
			if (checkErrors) {
				if (null == title || title.trim().length() == 0) {
					String[] args = {lang.getDescr()};
					this.addFieldError(titleKey, this.getText("error.category.insertTitle", args));
				}
			}
		}
	}

	private void checkCode() {
		String code = this.getCategoryCode();
		if ((this.getStrutsAction() == ApsAdminSystemConstants.ADD
				|| this.getStrutsAction() == ApsAdminSystemConstants.PASTE)
				&& null != code && code.trim().length() > 0) {
			String currectCode = BaseActionHelper.purgeString(code.trim());
			if (currectCode.length() > 0 && null != this.getCategoryManager().getCategory(currectCode)) {
				String[] args = {currectCode};
				this.addFieldError("categoryCode", this.getText("error.category.duplicateCode", args));
			}

			this.setCategoryCode(currectCode);
		}
	}

	protected String checkParentNode(String selectedNode) {
		if (null == selectedNode || selectedNode.trim().length() == 0) {
			this.addFieldError("parentCategoryCode", this.getText("error.category.noParentSelected"));
			return "categoryTree";
		}
		return null;
	}

	public String add() {
		String selectedNode = this.getSelectedNode();
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			this.setParentCategoryCode(selectedNode);
		} catch (Throwable t) {
			_logger.error("error in add", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		return this.extractCategoryFormValues();
	}

	public String showDetail() {
		String result = this.extractCategoryFormValues();
		if (!result.equals(SUCCESS)) {
			return result;
		}
		this.extractReferencingObjects(this.getSelectedNode());
		return result;
	}

	protected String extractCategoryFormValues() {
		String selectedNode = this.getSelectedNode();
		try {
			Category category = this.getCategory(selectedNode);
			if (null == category) {
				this.addActionError(this.getText("error.category.selectCategory"));
				return "categoryTree";
			}
			this.setParentCategoryCode(category.getParentCode());
			this.setCategoryCode(category.getCode());
			this.setTitles(category.getTitles());
		} catch (Throwable t) {
			_logger.error("error in extractCategoryFormValues", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		try {
			String check = this.checkDelete();
			if (null != check) {
				return check;
			}
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String delete() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkDelete();
			if (null != check) {
				return check;
			}
			Category currentCategory = this.getCategory(selectedNode);
			this.getCategoryManager().deleteCategory(selectedNode);
			this.setSelectedNode(currentCategory.getParent().getCode());
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Perform all the needed checks before deleting a category. When errors are
	 * detected a new actionMessaged, containing the appropriate error code and
	 * messaged, is created.
	 *
	 * @return null if the deletion operation is successful, otherwise the error
	 * code
	 */
	protected String checkDelete() {
		Category currentCategory = this.getCategory(this.getSelectedNode());
		if (null == currentCategory) {
			_logger.info("Required a selected node");
			this.addActionError(this.getText("error.category.selectCategory"));
			return "categoryTree";
		}
		if (currentCategory.getCode().equals(currentCategory.getParentCode())) {
			_logger.info("Home category not deletable");
			this.addActionError(this.getText("error.category.homeDelete.notAllowed"));
			return "categoryTree";
		}
		if (currentCategory.getChildren().length != 0) {
			_logger.info("Category with children not deletable");
			this.addActionError(this.getText("error.category.deleteWithChildren.notAllowed"));
			return "categoryTree";
		}
		this.extractReferencingObjects(this.getSelectedNode());
		if (null != this.getReferences() && this.getReferences().size() > 0) {
			return "references";
		}
		return null;
	}

	protected void extractReferencingObjects(String categoryCode) {
		try {
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				Map references = this.getHelper().getReferencingObjects(category, this.getRequest());
				if (references.size() > 0) {
					this.setReferences(references);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting referenced objects by category {}", categoryCode, t);
		}
	}

	public String save() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				Category category = this.getCategory(this.getCategoryCode());
				category.setTitles(this.getTitles());
				this.getCategoryManager().updateCategory(category);
				_logger.debug("Updated category {}", category.getCode());
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				String parentCategoryCode = this.getParentCategoryCode();
				Category category = this.getHelper().buildNewCategory(this.getCategoryCode(), parentCategoryCode, this.getTitles());
				this.getCategoryManager().addCategory(category);
				_logger.debug("Added new category {}", this.getCategoryCode());
			} else {
				_logger.error("Select a position");
				this.addFieldError("categoryCode", this.getText("error.category.noParentSelected"));
				return FAILURE;
			}
		} catch (Exception e) {
			_logger.error("error in save", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String moveTree() {
		String selectedNode = this.getSelectedNode();
		String parentCategoryCode = this.getRequest().getParameter("parentCategoryCode");
		try {
			String check = this.checkMoveCategory(selectedNode, parentCategoryCode);
			if (null != check) {
				return check;
			}
			this.extractReferencingObjectsForMove(this.getSelectedNode());
			if (null != this.getReferences() && this.getReferences().size() > 0) {
				return "moveReferences";
			}
			Category currentCategory = this.getCategory(this.getSelectedNode());
			Category parent = this.getCategory(parentCategoryCode);
			this.getCategoryManager().moveCategory(currentCategory, parent);
		} catch (Throwable t) {
			_logger.error("error in move category", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String executeMoveTree() {
		String selectedNode = this.getSelectedNode();
		String parentCategoryCode = this.getRequest().getParameter("parentCategoryCode");
		try {
			String check = this.checkMoveCategory(selectedNode, parentCategoryCode);
			if (null != check) {
				return check;
			}
			Category currentCategory = this.getCategory(this.getSelectedNode());
			Category parent = this.getCategory(parentCategoryCode);
			this.getCategoryManager().moveCategory(currentCategory, parent);
		} catch (Throwable t) {
			_logger.error("error in move executeMoveTree", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected String checkMoveCategory(String selectedNode, String parentCategoryCode) {
		if (this.getCategoryManager().getMoveTreeStatus() != CategoryManager.STATUS_READY) {
			this.addActionError(this.getText("error.category.move.updateReferencesRunning"));
			return "categoryTree";
		}
		Category currentCategory = this.getCategory(this.getSelectedNode());
		if (null == currentCategory) {
			_logger.info("Required a selected node");
			this.addActionError(this.getText("error.category.selectCategory"));
			return "categoryTree";
		}
		if (currentCategory.getCode().equals(this.getCategoryManager().getRoot().getCode())) {
			_logger.info("Root category cannot be moved");
			this.addActionError(this.getText("error.category.move.rootNotAllowed"));
			return "categoryTree";
		}
		if ("".equals(parentCategoryCode) || null == this.getCategoryManager().getCategory(parentCategoryCode)) {
			this.addActionError(this.getText("error.category.move.selectCategoryParent"));
			return "categoryTree";
		}
		Category parent = this.getCategory(parentCategoryCode);
		if (null == parent) {
			_logger.info("Required a selected node");
			this.addActionError(this.getText("error.category.selectCategoryParent"));
			return "categoryTree";
		}
		if (parent.getCode().equals(currentCategory.getParentCode())) {
			_logger.debug("trying to move a node under it's own parent..");
			return "categoryTree";
		}
		if (parent.isChildOf(selectedNode)) {
			List<String> args = new ArrayList<String>();
			args.add(parent.getCode());
			args.add(selectedNode);
			this.addActionError(this.getText("error.category.move.parentUnderChild.notAllowed"));
			return "categoryTree";
		}
		return null;
	}

	protected void extractReferencingObjectsForMove(String categoryCode) {
		try {
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				Map references = this.getHelper().getReferencingObjectsForMove(category, this.getRequest());
				if (references.size() > 0) {
					this.setReferences(references);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting referenced objects for move by category {}", categoryCode, t);
		}
	}

	/**
	 * provide the result for the progress bar
	 *
	 * @return
	 */
	public Map<String, Integer> getUpdateReferencesStatus() {
		int total = 0;
		int done = 0;
		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		int numThreads = currentGroup.activeCount();
		Thread[] listOfThreads = new Thread[numThreads];
		currentGroup.enumerate(listOfThreads);
		for (int i = 0; i < numThreads; i++) {
			if (listOfThreads[i].getName().startsWith(CategoryManager.RELOAD_CATEGORY_REFERENCES_THREAD_NAME_PREFIX)) {
				ReloadingCategoryReferencesThread thread = (ReloadingCategoryReferencesThread) listOfThreads[i];
				total = total + thread.getListSize();
				done = done + thread.getListIndex();
			}
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("total", total);
		result.put("done", done);
		return result;
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	@Deprecated
	public Category getRoot() {
		return this.getCategoryManager().getRoot();
	}

	public ITreeNode getTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getHelper().getAllowedTreeRoot(new ArrayList<String>());
		} catch (Throwable t) {
			_logger.error("error in getTreeRootNode", t);
		}
		return node;
	}

	@Override
	public String buildTree() {
		this.checkTitles(false);
		return super.buildTree();
	}

	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	public List<Category> getBreadCrumbsTargets(String categoryCode) {
		Category category = this.getCategoryManager().getCategory(categoryCode);
		if (null == category) {
			return null;
		}
		List<Category> categories = new ArrayList<Category>();
		this.getSubBreadCrumbsTargets(categories, category);
		return categories;
	}

	private void getSubBreadCrumbsTargets(List<Category> categories, Category current) {
		categories.add(0, current);
		Category parent = current.getParent();
		if (parent != null && !parent.getCode().equals(current.getCode())) {
			this.getSubBreadCrumbsTargets(categories, parent);
		}
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCategoryCode() {
		return _categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	public String getParentCategoryCode() {
		return _parentCategoryCode;
	}

	public void setParentCategoryCode(String parentCategoryCode) {
		this._parentCategoryCode = parentCategoryCode;
	}

	public ApsProperties getTitles() {
		return _titles;
	}

	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected ICategoryActionHelper getHelper() {
		return (ICategoryActionHelper) super.getTreeHelper();
	}

	public Map getReferences() {
		return _references;
	}

	protected void setReferences(Map references) {
		this._references = references;
	}

	public String getSelectedNode() {
		return _selectedNode;
	}

	public void setSelectedNode(String selectedNode) {
		super.getTreeNodesToOpen().add(selectedNode);
		this._selectedNode = selectedNode;
	}

	public String getCategoryCodeToken() {
		return _categoryCodeToken;
	}

	public void setCategoryCodeToken(String categoryCodeToken) {
		this._categoryCodeToken = categoryCodeToken;
	}

	private int _strutsAction;
	private String _categoryCode;
	private String _parentCategoryCode;
	private String _selectedNode;
	private String _categoryCodeToken;
	private ApsProperties _titles = new ApsProperties();

	private ICategoryManager _categoryManager;

	private Map _references;

}
