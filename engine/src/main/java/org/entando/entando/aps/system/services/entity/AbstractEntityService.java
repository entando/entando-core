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
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 * @param <I>
 * @param <T>
 */
public abstract class AbstractEntityService<I extends IApsEntity, T extends EntityDto> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<IEntityManager> entityManagers;

    @Autowired
    private ICategoryManager categoryManager;

    protected abstract T buildEntityDto(I entity);

    protected T getEntity(String entityManagerCode, String id) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            I entity = (I) entityManager.getEntity(id);
            if (null == entity) {
                throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Entity", id);
            }
            return this.buildEntityDto(entity);
        } catch (ResourceNotFoundException rnf) {
            throw rnf;
        } catch (Exception e) {
            logger.error("Error extracting entity", e);
            throw new RestServerError("error extracting entity", e);
        }
    }

    protected T addEntity(String entityManagerCode, EntityDto request, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            String id = request.getId();
            I oldEntity = (I) entityManager.getEntity(id);
            if (null != oldEntity) {
                bindingResult.reject(EntityValidator.ERRCODE_ENTITY_ALREADY_EXISTS,
                        new String[]{id}, "entity.exists");
                throw new ValidationConflictException(bindingResult);
            }
            I entity = this.getEntityPrototype(entityManager, request.getTypeCode());
            request.fillEntity(entity, this.getCategoryManager(), bindingResult);
            this.scanEntity(entity, bindingResult);
            if (!bindingResult.hasErrors()) {
                I newEntity = (I) this.addEntity(entityManager, entity);
                return this.buildEntityDto(newEntity);
            }
        } catch (ValidationConflictException vce) {
            throw vce;
        } catch (Exception e) {
            logger.error("Error adding entity", e);
            throw new RestServerError("error add entity", e);
        }
        return null;
    }

    protected abstract I addEntity(IEntityManager entityManager, I entityToAdd);

    protected synchronized T updateEntity(String entityManagerCode, EntityDto request, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            String id = request.getId();

            I entity = (I) entityManager.getEntity(id);

            if (null == entity) {
                bindingResult.reject(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST,
                        new String[]{id}, "entity.notExists");
                throw new ResourceNotFoundException(bindingResult);
            }
            String typeCode = request.getTypeCode();
            if (!entity.getTypeCode().equals(typeCode)) {
                bindingResult.reject(EntityValidator.ERRCODE_TYPE_MISMATCH,
                        new String[]{entity.getTypeCode(), typeCode}, "entity.type.invalid");
                throw new ValidationConflictException(bindingResult);
            }

            request.fillEntity(entity, this.getCategoryManager(), bindingResult);
            this.scanEntity(entity, bindingResult);
            if (!bindingResult.hasErrors()) {
                I updatedEntity = (I) this.updateEntity(entityManager, entity);
                return this.buildEntityDto(updatedEntity);
            }
        } catch (Exception e) {
            logger.error("Error updating entity", e);
            throw new RestServerError("error updating entity", e);
        }
        return null;
    }

    protected abstract I updateEntity(IEntityManager entityManager, I entityToUpdate);

    protected void scanEntity(I currentEntity, BindingResult bindingResult) {
        List<AttributeInterface> attributes = currentEntity.getAttributeList();
        for (AttributeInterface entityAttribute : attributes) {
            if (entityAttribute.isActive()) {
                List<AttributeFieldError> errors = entityAttribute.validate(new AttributeTracer());
                if (null != errors && errors.size() > 0) {
                    for (AttributeFieldError attributeFieldError : errors) {
                        AttributeTracer tracer = attributeFieldError.getTracer();
                        AttributeInterface attribute = attributeFieldError.getAttribute();
                        String messagePrefix = this.createErrorMessageAttributePositionPrefix(attribute, tracer);
                        bindingResult.reject(EntityValidator.ERRCODE_ATTRIBUTE_INVALID,
                                messagePrefix + " " + this.getErrorMessage(
                                        attributeFieldError.getErrorCode()) + ": " + attributeFieldError.getMessage());
                    }
                }
            }
        }
    }

    protected String getErrorMessage(String errorCode) {
        if (errorCode.equals(FieldError.MANDATORY)) {
            return "Mandatory";
        } else if (errorCode.equals(FieldError.INVALID)) {
            return "Invalid";
        } else if (errorCode.equals(FieldError.INVALID_FORMAT)) {
            return "Invalid format";
        } else if (errorCode.equals(FieldError.INVALID_MIN_LENGTH)) {
            return "Invalid min length";
        } else if (errorCode.equals(FieldError.INVALID_MAX_LENGTH)) {
            return "Invalid max length";
        } else if (errorCode.equals(FieldError.LESS_THAN_ALLOWED)) {
            return "Less than allowed";
        } else if (errorCode.equals(FieldError.GREATER_THAN_ALLOWED)) {
            return "Greater than allowed";
        } else if (errorCode.equals(AttributeFieldError.OGNL_VALIDATION)) {
            return "Invalid (ognl)";
        } else {
            return "Invalid";
        }
    }

    private String createErrorMessageAttributePositionPrefix(AttributeInterface attribute, AttributeTracer tracer) {
        if (tracer.isMonoListElement()) {
            if (tracer.isCompositeElement()) {
                String[] args = {tracer.getParentAttribute().getName(), String.valueOf(tracer.getListIndex() + 1), attribute.getName()};
                return "List attribute '" + args[0] + "' - Index " + args[1] + "' - Name '" + args[2] + "'";
            } else {
                String[] args = {attribute.getName(), String.valueOf(tracer.getListIndex() + 1)};
                return "List attribute '" + args[0] + "' - Index " + args[1] + "'";
            }
        } else if (tracer.isCompositeElement()) {
            String[] args = {tracer.getParentAttribute().getName(), attribute.getName()};
            return "Composite attribute '" + args[0] + "' - Name '" + args[1] + "'";
        } else if (tracer.isListElement()) {
            String[] args = {attribute.getName(), tracer.getListLang().getDescr(), String.valueOf(tracer.getListIndex() + 1)};
            return "List attribute '" + args[0] + "' - Lang '" + args[1] + "' - Index '" + args[2] + "'";
        } else {
            String[] args = {attribute.getName()};
            return "Attribute '" + args[0] + "'";
        }
    }

    protected I getEntityPrototype(IEntityManager entityManager, String entityTypeCode) {
        I entityType = (I) entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn("no type found with code {}", entityTypeCode);
            throw new ResourceNotFoundException(AbstractEntityTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Type Code", entityTypeCode);
        }
        return (I) entityType.getEntityPrototype();
    }

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
            throw new ResourceNotFoundException("entityManagerCode", entityManagerCode);
        }
        return entityManager;
    }

    public List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
