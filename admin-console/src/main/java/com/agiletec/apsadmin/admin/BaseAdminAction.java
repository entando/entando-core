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
package com.agiletec.apsadmin.admin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesEvent;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This base action implements the default actions available for the system
 * administration.
 *
 * @author E.Santoboni
 */
public class BaseAdminAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(BaseAdminAction.class);

    /**
     * Reload the system configuration.
     *
     * @return the result code.
     */
    public String reloadConfig() {
        try {
            ApsWebApplicationUtils.executeSystemRefresh(this.getRequest());
            logger.info("Reload config started");
            this.setReloadingResult(SUCCESS_RELOADING_RESULT_CODE);
        } catch (Throwable t) {
            logger.error("error in reloadConfig", t);
            this.setReloadingResult(FAILURE_RELOADING_RESULT_CODE);
        }
        return SUCCESS;
    }

    /**
     * Reload the references of all the existing entities.
     *
     * @return the result code.
     */
    public String reloadEntitiesReferences() {
        try {
            ReloadingEntitiesReferencesEvent event = new ReloadingEntitiesReferencesEvent();
            WebApplicationContext wac = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
            wac.publishEvent(event);
            logger.info("Reloading entity references started");
        } catch (Throwable t) {
            logger.error("error in reloadEntitiesReferences", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public int getReloadingResult() {
        return _reloadingResult;
    }

    public void setReloadingResult(int reloadingResult) {
        this._reloadingResult = reloadingResult;
    }

    /**
     * Get the system parameters in order to edit them.
     *
     * @return the result code.
     */
    public String configSystemParams() {
        try {
            this.initLocalMap();
        } catch (Throwable t) {
            logger.error("error in configSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    /**
     * Update the system params.
     *
     * @return the result code.
     */
    public String updateSystemParams() {
        return this.updateSystemParams(false);
    }

    public String updateSystemParamsForAjax() {
        return this.updateSystemParams(true);
    }

    protected String updateSystemParams(boolean keepOldParam) {
        try {
            this.initLocalMap();
            this.updateLocalParams(keepOldParam);
            this.extractExtraParameters();
            String xmlParams = this.getConfigParameter();
            String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, this.getSystemParams());
            this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
            this.addActionMessage(this.getText("message.configSystemParams.ok"));
        } catch (Throwable t) {
            logger.error("error in updateSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected void initLocalMap() throws Throwable {
        String xmlParams = this.getConfigParameter();
        Map<String, String> systemParams = SystemParamsUtils.getParams(xmlParams);
        this.setSystemParams(systemParams);
    }

    protected String getConfigParameter() {
        return this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
    }

    /**
     * Refresh the map of parameters with values fetched from the request
     *
     * @param keepOldParam when true, when a system parameter is not found in
     * request, the previous system parameter will be stored
     */
    protected void updateLocalParams(boolean keepOldParam) {
        Iterator<String> paramNames = this.getSystemParams().keySet().iterator();
        while (paramNames.hasNext()) {
            String paramName = (String) paramNames.next();
            String newValue = this.getRequest().getParameter(paramName);
            if (null != newValue) {
                this.getSystemParams().put(paramName, newValue);
            } else if (!keepOldParam) {
                this.getSystemParams().put(paramName, "false");
            }
        }
    }

    public void extractExtraParameters() {
        try {
            Enumeration<String> parameterNames = this.getRequest().getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (paramName.endsWith(this.getExternalParamMarker())) {
                    String newParamName = paramName.substring(0, paramName.indexOf(this.getExternalParamMarker()));
                    if (null == this.getSystemParams().get(newParamName)) {
                        String newParamValue = this.getRequest().getParameter(newParamName);
                        if (null != newParamValue) {
                            this.getSystemParams().put(newParamName, newParamValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting extra parameters", e);
        }
    }

    /**
     * Return a plain list of the free pages in the portal.
     *
     * @return the list of the free pages of the portal.
     */
    public List<IPage> getFreePages() {
        IPage root = this.getPageManager().getOnlineRoot();
        List<IPage> pages = new ArrayList<>();
        this.addFreePublicPages(root, pages);
        return pages;
    }

    private void addFreePublicPages(IPage page, List<IPage> pages) {
        if (null == page) {
            return;
        }
        if (page.getGroup().equals(Group.FREE_GROUP_NAME)) {
            pages.add(page);
        }
        String[] children = page.getChildrenCodes();
        for (int i = 0; i < children.length; i++) {
            IPage child = this.getPageManager().getOnlinePage(children[i]);
            this.addFreePublicPages(child, pages);
        }
    }

    protected ConfigInterface getConfigManager() {
        return _configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    public Map<String, String> getSystemParams() {
        return _systemParams;
    }

    public void setSystemParams(Map<String, String> systemParams) {
        this._systemParams = systemParams;
    }

    public String getExternalParamMarker() {
        return "_newParamMarker";
    }

    private ConfigInterface _configManager;
    private IPageManager _pageManager;

    private Map<String, String> _systemParams;

    private int _reloadingResult = -1;

    public static final int FAILURE_RELOADING_RESULT_CODE = 0;
    public static final int SUCCESS_RELOADING_RESULT_CODE = 1;

}
