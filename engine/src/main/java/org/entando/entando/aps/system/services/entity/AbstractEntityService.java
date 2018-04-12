/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.entity;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import java.util.List;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public abstract class AbstractEntityService<I extends IApsEntity> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void addEntity(String entityManagerCode, EntityDto request, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        //TODO
    }

    protected void updateEntity(String entityManagerCode, EntityDto request, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        //TODO
    }

    protected I getEntityPrototype(IEntityManager entityManager, String entityTypeCode) {
        I entityType = (I) entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn("no type found with code {}", entityTypeCode);
            throw new RestRourceNotFoundException(AbstractEntityTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Type Code", entityTypeCode);
        }
        return (I) entityType.getEntityPrototype();
    }

    @Autowired
    private List<IEntityManager> entityManagers;

    protected IEntityManager extractEntityManager(String entityManagerCode) {
        IEntityManager entityManager = null;
        List<IEntityManager> managers = this.getEntityManagers();
        for (IEntityManager manager : managers) {
            if (((IManager) manager).getName().equals(entityManagerCode)) {
                entityManager = manager;
                break;
            }
        }
        if (null == entityManager) {
            logger.warn("no entity manager found with code {}", entityManagerCode);
            throw new RestRourceNotFoundException("entityManagerCode", entityManagerCode);
        }
        return entityManager;
    }

    public List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

}
