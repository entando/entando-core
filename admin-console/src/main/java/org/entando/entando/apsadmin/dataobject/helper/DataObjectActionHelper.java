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
package org.entando.entando.apsadmin.dataobject.helper;

import com.agiletec.aps.system.SystemConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.EntityActionHelper;
import com.opensymphony.xwork2.ActionSupport;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.helper.IDataAuthorizationHelper;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeHelper;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Classe Helper della DataObjectAction.
 *
 * @author E.Santoboni
 */
public class DataObjectActionHelper extends EntityActionHelper implements IDataObjectActionHelper {

    private static final Logger _logger = LoggerFactory.getLogger(DataObjectActionHelper.class);

    @Override
    public void updateDataObject(IApsEntity entity, boolean updateMainGroup, HttpServletRequest request) {
        this.updateEntity(entity, request);
        if (null != entity && updateMainGroup) {
            DataObject dataObject = (DataObject) entity;
            if (null == dataObject.getId() && StringUtils.isEmpty(dataObject.getMainGroup())) {
                String mainGroup = request.getParameter("mainGroup");
                if (mainGroup != null) {
                    dataObject.setMainGroup(mainGroup);
                }
            }
        }
    }

    @Override
    public void updateEntity(IApsEntity entity, HttpServletRequest request) {
        DataObject dataObject = (DataObject) entity;
        try {
            if (null != dataObject) {
                String descr = request.getParameter("descr");
                if (descr != null) {
                    dataObject.setDescription(descr.trim());
                }
                String status = request.getParameter("status");
                if (status != null) {
                    dataObject.setStatus(status);
                }
                if (null == dataObject.getId()) {
                    String mainGroup = request.getParameter("mainGroup");
                    if (mainGroup != null) {
                        request.getSession().setAttribute("dataObjectGroupOnSession", mainGroup);
                    }
                }
                super.updateEntity(dataObject, request);
                String description = dataObject.getDescription();
                if (null == description || description.trim().length() == 0) {
                    ITextAttribute titleAttribute = (ITextAttribute) dataObject.getAttributeByRole(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE);
                    if (null != titleAttribute && StringUtils.isNotEmpty(titleAttribute.getText())) {
                        dataObject.setDescription(titleAttribute.getText());
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("DataObjectActionHelper - updateDataObject", t);
            throw new RuntimeException("Error updating DataObject", t);
        }
    }

    @Override
    public void scanEntity(IApsEntity entity, ActionSupport action) {
        DataObject dataObject = (DataObject) entity;
        if (null == dataObject) {
            _logger.error("Null DataObject");
            return;
        }
        String descr = dataObject.getDescription();
        if (descr == null || descr.length() == 0) {
            action.addFieldError("descr", action.getText("error.dataobject.descr.required"));
        } else {
            int maxLength = 250;
            if (descr.length() > maxLength) {
                String[] args = {String.valueOf(maxLength)};
                action.addFieldError("descr", action.getText("error.dataobject.descr.wrongMaxLength", args));
            }
            if (!descr.matches("([^\"])+")) {
                action.addFieldError("descr", action.getText("error.dataobject.descr.wrongCharacters"));
            }
        }
        if (null == dataObject.getId() && (dataObject.getMainGroup() == null || dataObject.getMainGroup().length() == 0)) {
            action.addFieldError("mainGroup", action.getText("error.dataobject.mainGroup.required"));
        }
        try {
            super.scanEntity(dataObject, action);
        } catch (Throwable t) {
            _logger.error("DataObjectActionHelper - scanEntity", t);
            throw new RuntimeException("Error checking entity", t);
        }
    }

    @Override
    public EntitySearchFilter getOrderFilter(String groupBy, String order) {
        String key = null;
        if (null == groupBy || groupBy.trim().length() == 0 || groupBy.equals("lastModified")) {
            key = IDataObjectManager.DATA_OBJECT_MODIFY_DATE_FILTER_KEY;
        } else if (groupBy.equals("code")) {
            key = IDataObjectManager.ENTITY_ID_FILTER_KEY;
        } else if (groupBy.equals("descr")) {
            key = IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY;
        } else if (groupBy.equals("created")) {
            key = IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY;
        } else {
            throw new RuntimeException("Invalid Filter '" + groupBy + "'");
        }
        EntitySearchFilter filter = new EntitySearchFilter(key, false);
        if (null == order || order.trim().length() == 0) {
            filter.setOrder(EntitySearchFilter.DESC_ORDER);
        } else {
            filter.setOrder(order);
        }
        return filter;
    }

    /**
     * Verifica che l'utente corrente possegga i diritti di accesso al
     * dataObject selezionato.
     *
     * @param dataObject Il dataObject.
     * @param currentUser Il dataObject corrente.
     * @return True nel caso che l'utente corrente abbia i permessi di
     * lettura/scrittura sul dataObject, false in caso contrario.
     */
    @Override
    public boolean isUserAllowed(DataObject dataObject, UserDetails currentUser) {
        try {
            return this.getDataObjectAuthorizationHelper().isAuthToEdit(currentUser, dataObject);
        } catch (Throwable t) {
            _logger.error("Error checking user authority", t);
            throw new RuntimeException("Error checking user authority", t);
        }
    }

    @Override
    public Map getReferencingObjects(DataObject dataobject, HttpServletRequest request) throws ApsSystemException {
        return this.getDataObjectHelper().getReferencingObjects(dataobject);
    }

    @Override
    public ActivityStreamInfo createActivityStreamInfo(DataObject dataobject, int strutsAction, boolean addLink) {
        ActivityStreamInfo asi = new ActivityStreamInfo();
        asi.setActionType(strutsAction);
        Lang defaultLang = this.getLangManager().getDefaultLang();
        Properties titles = new Properties();
        titles.setProperty(defaultLang.getCode(), (null != dataobject.getDescription()) ? dataobject.getDescription() : "-");
        asi.setObjectTitles(titles);
        if (addLink) {
            asi.setLinkNamespace("/do/dataobject");
            asi.setLinkActionName("edit");
            asi.addLinkParameter("dataId", dataobject.getId());
            asi.setLinkAuthGroup(dataobject.getMainGroup());
            asi.setLinkAuthPermission(Permission.CONTENT_EDITOR);
        }
        List<String> groupCodes = new ArrayList<String>();
        if (null != dataobject.getMainGroup()) {
            groupCodes.addAll(dataobject.getGroups());
        }
        groupCodes.add(dataobject.getMainGroup());
        asi.setGroups(groupCodes);
        return asi;
    }

    protected IDataObjectManager getDataObjectManager() {
        return _dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this._dataObjectManager = dataObjectManager;
    }

    protected IDataTypeHelper getDataObjectHelper() {
        return _dataObjectHelper;
    }

    public void setDataObjectHelper(IDataTypeHelper dataObjectHelper) {
        this._dataObjectHelper = dataObjectHelper;
    }

    protected IDataAuthorizationHelper getDataObjectAuthorizationHelper() {
        return _dataObjectAuthorizationHelper;
    }

    public void setDataObjectAuthorizationHelper(IDataAuthorizationHelper dataObjectAuthorizationHelper) {
        this._dataObjectAuthorizationHelper = dataObjectAuthorizationHelper;
    }

    private IDataObjectManager _dataObjectManager;
    private IDataTypeHelper _dataObjectHelper;

    private IDataAuthorizationHelper _dataObjectAuthorizationHelper;

}
