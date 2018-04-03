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
package org.entando.entando.aps.system.services.widgettype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.events.LangsChangedEvent;
import com.agiletec.aps.system.services.lang.events.LangsChangedObserver;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.cache.IWidgetTypeManagerCacheWrapper;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio di gestione dei tipi di widget (WidgetType) definiti nel sistema.
 * (Questo servizio non riguarda la configurazione delle istanze di widget nelle
 * pagine)
 *
 * @author M.Diana - E.Santoboni
 */
public class WidgetTypeManager extends AbstractService
        implements IWidgetTypeManager, LangsChangedObserver, GroupUtilizer<WidgetType>, GuiFragmentUtilizer {

    private static final Logger logger = LoggerFactory.getLogger(WidgetTypeManager.class);

    private IWidgetTypeDAO _widgetTypeDAO;
    private IGuiFragmentManager _guiFragmentManager;
    private IWidgetTypeManagerCacheWrapper _cacheWrapper;

    @Override
    public void init() throws Exception {
        this.getCacheWrapper().initCache(this.getWidgetTypeDAO());
        logger.debug("{} ready. Initialized", this.getClass().getName());
    }

    @Override
    public void updateFromLangsChanged(LangsChangedEvent event) {
        try {
            this.init();
        } catch (Throwable t) {
            logger.error("Error on init method", t);
        }
    }

    @Override
    public WidgetType getWidgetType(String code) {
        return this.getCacheWrapper().getWidgetType(code);
    }

    @Override
    public List<WidgetType> getWidgetTypes() {
        List<WidgetType> types = new ArrayList<WidgetType>();
        types.addAll(this.getCacheWrapper().getWidgetTypes());
        BeanComparator comparator = new BeanComparator("code");
        Collections.sort(types, comparator);
        return types;
    }

    @Override
    public void addWidgetType(WidgetType widgetType) throws ApsSystemException {
        try {
            if (null == widgetType) {
                logger.error("Null Widget Type");
                return;
            }
            WidgetType type = this.getWidgetType(widgetType.getCode());
            if (null != type) {
                logger.error("Type already exists : type code {}", widgetType.getCode());
                return;
            }
            String parentTypeCode = widgetType.getParentTypeCode();
            if (null != parentTypeCode && null == this.getWidgetType(parentTypeCode)) {
                throw new ApsSystemException("ERROR : Parent type '" + parentTypeCode + "' doesn't exists");
            }
            if (null == parentTypeCode && null != widgetType.getConfig()) {
                throw new ApsSystemException("ERROR : Parent type null and default config not null");
            }
            if (null != widgetType.getTypeParameters() && null != widgetType.getConfig()) {
                throw new ApsSystemException("ERROR : Params not null and config not null");
            }
            this.getWidgetTypeDAO().addWidgetType(widgetType);
            this.getCacheWrapper().addWidgetType(widgetType);
            this.notifyWidgetTypeChanging(widgetType.getCode(), WidgetTypeChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error adding a Widget Type", t);
            throw new ApsSystemException("Error adding a Widget Type", t);
        }
    }

    @Override
    public void deleteWidgetType(String widgetTypeCode) throws ApsSystemException {
        List<GuiFragment> deletedFragments = new ArrayList<GuiFragment>();
        try {
            WidgetType type = this.getWidgetType(widgetTypeCode);
            if (null == type) {
                logger.error("Type not exists : type code {}", widgetTypeCode);
                return;
            }
            if (type.isLocked()) {
                logger.error("A locked widget can't be deleted - type {}", widgetTypeCode);
                return;
            }
            List<String> fragmentCodes = this.getGuiFragmentManager().getGuiFragmentCodesByWidgetType(widgetTypeCode);
            if (null != fragmentCodes) {
                for (int i = 0; i < fragmentCodes.size(); i++) {
                    String fragmentCode = fragmentCodes.get(i);
                    GuiFragment fragmentToDelete = this.getGuiFragmentManager().getGuiFragment(fragmentCode);
                    deletedFragments.add(fragmentToDelete);
                    this.getGuiFragmentManager().deleteGuiFragment(fragmentCode);
                }
            }
            this.getWidgetTypeDAO().deleteWidgetType(widgetTypeCode);
            this.getCacheWrapper().deleteWidgetType(widgetTypeCode);
            this.notifyWidgetTypeChanging(widgetTypeCode, WidgetTypeChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            for (int i = 0; i < deletedFragments.size(); i++) {
                GuiFragment guiFragment = deletedFragments.get(i);
                if (null == this.getGuiFragmentManager().getGuiFragment(guiFragment.getCode())) {
                    this.getGuiFragmentManager().addGuiFragment(guiFragment);
                }
            }
            logger.error("Error deleting widget type", t);
            throw new ApsSystemException("Error deleting widget type", t);
        }
    }

    @Override
    public void updateWidgetType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) throws ApsSystemException {
        try {
            WidgetType type = this.getWidgetType(widgetTypeCode);
            if (null == type) {
                logger.error("Type not exists : type code {}", widgetTypeCode);
                return;
            }
            if (type.isLocked() || !type.isLogic() || !type.isUserType()) {
                defaultConfig = type.getConfig();
            }
            this.getWidgetTypeDAO().updateWidgetType(widgetTypeCode, titles, defaultConfig, mainGroup);
            type.setTitles(titles);
            type.setConfig(defaultConfig);
            type.setMainGroup(mainGroup);
            this.getCacheWrapper().updateWidgetType(type);
            this.notifyWidgetTypeChanging(widgetTypeCode, WidgetTypeChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error updating Widget type titles : type code {}", widgetTypeCode, t);
            throw new ApsSystemException("Error updating Widget type titles : type code" + widgetTypeCode, t);
        }
    }

    @Override
    public List<WidgetType> getGroupUtilizers(String groupName) throws ApsSystemException {
        List<WidgetType> utilizers = null;
        try {
            boolean freeTypes = (null == groupName || groupName.equals(Group.FREE_GROUP_NAME));
            List<WidgetType> allTypes = this.getWidgetTypes();
            for (int i = 0; i < allTypes.size(); i++) {
                WidgetType type = allTypes.get(i);
                String typeGroup = type.getMainGroup();
                if ((freeTypes && null == typeGroup) || groupName.equals(typeGroup)) {
                    if (null == utilizers) {
                        utilizers = new ArrayList<WidgetType>();
                    }
                    utilizers.add(type);
                }
            }
        } catch (Throwable t) {
            logger.error("Error extracting utilizers", t);
            throw new ApsSystemException("Error extracting utilizers", t);
        }
        return utilizers;
    }

    @Override
    public List getGuiFragmentUtilizers(String guiFragmentCode) throws ApsSystemException {
        List<WidgetType> utilizers = new ArrayList<WidgetType>();
        try {
            FieldSearchFilter codeFilter = new FieldSearchFilter("code", guiFragmentCode, false);
            FieldSearchFilter widgetTypeFilter = new FieldSearchFilter("widgettypecode");
            FieldSearchFilter[] filters = {codeFilter, widgetTypeFilter};
            List<String> widgetUtilizers = this.getGuiFragmentManager().searchGuiFragments(filters);
            if (null != widgetUtilizers && !widgetUtilizers.isEmpty()) {
                for (int i = 0; i < widgetUtilizers.size(); i++) {
                    String code = widgetUtilizers.get(i);
                    GuiFragment fragment = this.getGuiFragmentManager().getGuiFragment(code);
                    WidgetType widgetType = this.getWidgetType(fragment.getWidgetTypeCode());
                    if (null != widgetType) {
                        utilizers.add(widgetType);
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Error extracting utilizers", t);
            throw new ApsSystemException("Error extracting utilizers", t);
        }
        return utilizers;
    }

    private void notifyWidgetTypeChanging(String widgetTypeCode, int operationCode) throws ApsSystemException {
        WidgetTypeChangedEvent event = new WidgetTypeChangedEvent();
        event.setWidgetTypeCode(widgetTypeCode);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    protected IWidgetTypeDAO getWidgetTypeDAO() {
        return _widgetTypeDAO;
    }

    public void setWidgetTypeDAO(IWidgetTypeDAO widgetTypeDAO) {
        this._widgetTypeDAO = widgetTypeDAO;
    }

    protected IGuiFragmentManager getGuiFragmentManager() {
        return _guiFragmentManager;
    }

    public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
        this._guiFragmentManager = guiFragmentManager;
    }

    protected IWidgetTypeManagerCacheWrapper getCacheWrapper() {
        return _cacheWrapper;
    }

    public void setCacheWrapper(IWidgetTypeManagerCacheWrapper cacheWrapper) {
        this._cacheWrapper = cacheWrapper;
    }

}
