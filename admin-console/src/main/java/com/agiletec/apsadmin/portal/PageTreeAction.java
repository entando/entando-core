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
package com.agiletec.apsadmin.portal;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;

/**
 * Action principale per la gestione dell'albero delle pagine.
 *
 * @author E.Santoboni
 */
public class PageTreeAction extends AbstractTreeAction {

    private static final Logger _logger = LoggerFactory.getLogger(PageTreeAction.class);

    @Override
    public String execute() throws Exception {
        if (null != this.getSelectedNode()) {
            this.getTreeNodesToOpen().add(this.getSelectedNode());
        }
        return super.execute();
    }

    public String moveUp() {
        return this.movePage(true);
    }

    public String moveDown() {
        return this.movePage(false);
    }

    protected String movePage(boolean moveUp) {
        String selectedNode = this.getSelectedNode();
        try {
            String check = this.checkSelectedNode(selectedNode);
            if (null != check) {
                return check;
            }
            IPage currentPage = this.getPageManager().getDraftPage(selectedNode);
            if (!isUserAllowed(currentPage.getParent())) {
                this.addActionError(this.getText("error.page.userNotAllowed"));
                return SUCCESS;
            }
            if (this.getPageManager().getRoot().getCode().equals(currentPage.getCode())) {
                this.addActionError(this.getText("error.page.movementHome.notAllowed"));
                return SUCCESS;
            }
            boolean result = this.getPageManager().movePage(selectedNode, moveUp);
            if (!result) {
                if (moveUp) {
                    this.addActionError(this.getText("error.page.movementUp.notAllowed"));
                } else {
                    this.addActionError(this.getText("error.page.movementDown.notAllowed"));
                }
            }
        } catch (Throwable t) {
            _logger.error("error in movePage", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String moveTree() {
        String selectedNode = this.getSelectedNode();
        String parentPageCode = this.getRequest().getParameter("parentPageCode");
        if (null == parentPageCode) {
            parentPageCode = this.getPageManager().getRoot().getCode();
        }
        try {
            String check = this.checkMovePage(selectedNode, parentPageCode);
            if (null != check) {
                return check;
            }
            IPage currentPage = this.getPage(selectedNode);
            IPage parent = this.getPage(parentPageCode);
            this.getPageManager().movePage(currentPage, parent);
        } catch (Throwable t) {
            _logger.error("error in move page", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected String checkMovePage(String selectedNode, String parentPageCode) {
        IPage currentPage = this.getPage(selectedNode);
        if (null == currentPage) {
            _logger.info("Required a selected node");
            this.addActionError(this.getText("error.page.selectPage"));
            return "pageTree";
        }
        if (currentPage.getCode().equals(this.getPageManager().getRoot().getCode())) {
            _logger.info("Root page cannot be moved");
            this.addActionError(this.getText("error.page.move.rootNotAllowed"));
            return "pageTree";
        }
        if ("".equals(parentPageCode) || null == this.getPageManager().getPage(parentPageCode)) {
            this.addActionError(this.getText("error.page.move.selectPageParent"));
            return "pageTree";
        }
        IPage parent = this.getPage(parentPageCode);
        if (null == parent) {
            _logger.info("Required a selected node");
            this.addActionError(this.getText("error.page.selectPageParent"));
            return "pageTree";
        }
        if (parent.getCode().equals(currentPage.getParentCode())) {
            _logger.debug("trying to move a node under it's own parent..");
            this.addActionError(this.getText("error.page.move.parentUnderChild.notAllowed"));
            return "pageTree";
        }
        if (parent.isChildOf(selectedNode)) {
            List<String> args = new ArrayList<String>();
            args.add(parent.getCode());
            args.add(selectedNode);
            this.addActionError(this.getText("error.page.move.parentUnderChild.notAllowed"));
            return "pageTree";
        }
        //group check
        if (!currentPage.getGroup().equals(Group.FREE_GROUP_NAME)
                && !currentPage.getGroup().equals(parent.getGroup())) {
            this.addActionError(this.getText("error.page.move.differentRestriction.notAllowed"));
            return "pageTree";
        }
        return null;
    }

    public String moveWidgetUp() {
        return this.moveWidget(this.getFrame() - 1);
    }

    public String moveWidgetDown() {
        return this.moveWidget(this.getFrame() + 1);
    }

    protected String moveWidget(int destFrame) {
        try {
            int frame = this.getFrame();
            this.getPageManager().moveWidget(this.getSelectedNode(), frame, destFrame);
        } catch (Throwable t) {
            _logger.error("error in moveWidget", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String copy() {
        String selectedNode = this.getSelectedNode();
        String check = this.checkSelectedNode(selectedNode);
        if (null != check) {
            return check;
        }
        //FIXME RICORDARSI DELL'AREA DI NOTIFICA QUANDO PASSEREMO ALL'ULTIMO LAYER... se Ã¨ possibile mettere un'indicazione della pagina copiata
        this.setCopyingPageCode(selectedNode);
        return SUCCESS;
    }

    protected String checkSelectedNode(String selectedNode) {
        if (null == selectedNode || selectedNode.trim().length() == 0) {
            this.addActionError(this.getText("error.page.noSelection"));
            return "pageTree";
        }
        if (AbstractPortalAction.VIRTUAL_ROOT_CODE.equals(selectedNode)) {
            this.addActionError(this.getText("error.page.virtualRootSelected"));
            return "pageTree";
        }
        IPage selectedPage = this.getPageManager().getDraftPage(selectedNode);
        if (null == selectedPage) {
            this.addActionError(this.getText("error.page.selectedPage.null"));
            return "pageTree";
        }
        if (!this.isUserAllowed(selectedPage)) {
            this.addActionError(this.getText("error.page.userNotAllowed"));
            return "pageTree";
        }
        return null;
    }

    /**
     * Check if the current user can access the specified page.
     *
     * @param page The page to check against the current user.
     * @return True if the user has can access the given page, false otherwise.
     */
    protected boolean isUserAllowed(IPage page) {
        if (page == null) {
            return false;
        }
        String pageGroup = page.getGroup();
        Collection<String> codes = this.getNodeGroupCodes();
        return codes.contains(pageGroup);
    }

    /**
     * Return the page given its code.
     *
     * @param pageCode The code of the requested page.
     * @return The page associated to the given code, null if the code is
     * unknown.
     */
    public IPage getPage(String pageCode) {
        return this.getPageManager().getDraftPage(pageCode);
    }

    @Deprecated
    public IPage getRoot() {
        return this.getPageManager().getRoot();
    }

    @Deprecated
    public ITreeNode getTreeRootNode() {
        return this.getAllowedTreeRootNode();
    }

    public String getSelectedNode() {
        return _selectedNode;
    }

    public void setSelectedNode(String selectedNode) {
        this._selectedNode = selectedNode;
    }

    public String getCopyingPageCode() {
        return _copyingPageCode;
    }

    public void setCopyingPageCode(String copyingPageCode) {
        this._copyingPageCode = copyingPageCode;
    }

    public Integer getFrame() {
        return _frame;
    }

    public void setFrame(Integer frame) {
        this._frame = frame;
    }

    @Override
    protected IPageActionHelper getTreeHelper() {
        return (IPageActionHelper) super.getTreeHelper();
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    private String _selectedNode;

    private String _copyingPageCode;

    private Integer _frame;

    private IPageManager _pageManager;

}
