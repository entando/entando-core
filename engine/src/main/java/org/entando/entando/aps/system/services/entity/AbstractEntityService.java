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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.AttributeRoleDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeValidationDto;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.entando.entando.web.entity.validator.EntityTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 * @param <I>
 * @param <O>
 */
public abstract class AbstractEntityService<I extends IApsEntity, O extends EntityTypeFullDto> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<IEntityManager> entityManagers;

    protected IDtoBuilder<IEntityManager, EntityManagerDto> getEntityManagerDtoBuilder() {
        return new DtoBuilder<IEntityManager, EntityManagerDto>() {
            @Override
            protected EntityManagerDto toDto(IEntityManager src) {
                return new EntityManagerDto(src);
            }
        };
    }

    protected IDtoBuilder<IApsEntity, EntityTypeShortDto> getEntityTypeShortDtoBuilder() {
        return new DtoBuilder<IApsEntity, EntityTypeShortDto>() {
            @Override
            protected EntityTypeShortDto toDto(IApsEntity src) {
                return new EntityTypeShortDto(src);
            }
        };
    }

    protected PagedMetadata<EntityTypeShortDto> getShortEntityTypes(String entityManagerCode, RestListRequest requestList) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        List<IApsEntity> entityTypes = new ArrayList<>(entityManager.getEntityPrototypes().values());
        Map<String, String> fieldMapping = this.getEntityTypeFieldNameMapping();
        entityTypes.stream().filter(i -> this.filterObjects(i, requestList.getFilter(), fieldMapping));
        Collections.sort(entityTypes, new BeanComparator(this.getFieldName(requestList.getSort(), fieldMapping)));
        if (!RestListRequest.DIRECTION_VALUE_DEFAULT.equals(requestList.getDirection())) {
            Collections.reverse(entityTypes);
        }
        List<IApsEntity> sublist = requestList.getSublist(entityTypes);
        SearcherDaoPaginatedResult<IApsEntity> result = new SearcherDaoPaginatedResult(entityTypes.size(), sublist);
        PagedMetadata<EntityTypeShortDto> pagedMetadata = new PagedMetadata<>(requestList, result);
        List<EntityTypeShortDto> body = this.getEntityTypeShortDtoBuilder().convert(sublist);
        for (EntityTypeShortDto entityTypeShortDto : body) {
            String code = entityTypeShortDto.getCode();
            entityTypeShortDto.setStatus(String.valueOf(entityManager.getStatus(code)));
        }
        pagedMetadata.setBody(body);
        return pagedMetadata;
    }

    protected Map<String, String> getEntityTypeFieldNameMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put(RestListRequest.SORT_VALUE_DEFAULT, "typeCode");
        mapping.put("name", "typeDescription");
        return mapping;
    }

    protected String getFieldName(String dtoFieldName, Map<String, String> mapping) {
        String name = mapping.get(dtoFieldName);
        return ((null != name) ? name : mapping.get(RestListRequest.SORT_VALUE_DEFAULT));
    }

    protected O getFullEntityType(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        I entityType = (I) entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityManager) {
            logger.warn("no entity type found with code {}", entityTypeCode);
            throw new RestRourceNotFoundException("entityTypeCode", entityTypeCode);
        }
        O type = this.convertEntityType(entityManager, entityType);
        type.setStatus(String.valueOf(entityManager.getStatus(entityTypeCode)));
        return type;
    }

    protected O convertEntityType(IEntityManager entityManager, I entityType) {
        return this.getEntityTypeFullDtoBuilder(entityManager).convert(entityType);
    }

    protected abstract IDtoBuilder<I, O> getEntityTypeFullDtoBuilder(IEntityManager masterManager);

    protected synchronized O addEntityType(String entityManagerCode, EntityTypeDtoRequest bodyRequest, BindingResult bindingResult) {
        O response = null;
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            IDtoBuilder<I, O> builder = this.getEntityTypeFullDtoBuilder(entityManager);
            if (null != entityManager.getEntityPrototype(bodyRequest.getCode())) {
                this.addError(EntityTypeValidator.ERRCODE_ENTITY_TYPE_ALREADY_EXISTS,
                        bindingResult, new String[]{bodyRequest.getCode()}, "entityType.exists");
            }
            I entityPrototype = this.createEntityType(entityManager, bodyRequest, bindingResult);
            if (bindingResult.hasErrors()) {
                return response;
            } else {
                ((IEntityTypesConfigurer) entityManager).addEntityPrototype(entityPrototype);
                response = builder.convert(entityPrototype);
            }
        } catch (Throwable e) {
            logger.error("Error adding entity type", e);
            throw new RestServerError("error add entity type", e);
        }
        return response;
    }

    protected synchronized O updateEntityType(String entityManagerCode, EntityTypeDtoRequest request, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            if (null == entityManager.getEntityPrototype(request.getCode())) {
                this.addError(EntityTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST,
                        bindingResult, new String[]{request.getCode()}, "entityType.notExists");
                return null;
            }
            IDtoBuilder<I, O> builder = this.getEntityTypeFullDtoBuilder(entityManager);
            I entityPrototype = this.createEntityType(entityManager, request, bindingResult);
            if (bindingResult.hasErrors()) {
                return null;
            } else {
                ((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entityPrototype);
                I newPrototype = (I) entityManager.getEntityPrototype(request.getCode());
                O newType = builder.convert(newPrototype);
                newType.setStatus(String.valueOf(entityManager.getStatus(request.getCode())));
                return newType;
            }
        } catch (Throwable e) {
            logger.error("Error updating entity type", e);
            throw new RestServerError("error updating entity type", e);
        }
    }

    protected void addError(String errorCode, BindingResult bindingResult, String[] args, String message) {
        bindingResult.reject(errorCode, args, message);
    }

    protected I createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        Class entityClass = entityManager.getEntityClass();
        ApsEntity entityType = (ApsEntity) entityClass.newInstance();
        if (StringUtils.isEmpty(dto.getCode()) || dto.getCode().length() != 3) {
            this.addError(EntityTypeValidator.ERRCODE_INVALID_TYPE_CODE, bindingResult, new String[]{dto.getCode()}, "entityType.typeCode.invalid");
        }
        entityType.setTypeCode(dto.getCode());
        if (StringUtils.isEmpty(dto.getName())) {
            this.addError(EntityTypeValidator.ERRCODE_INVALID_TYPE_DESCR, bindingResult, new String[]{}, "entityType.typeDescription.invalid");
        }
        entityType.setTypeDescription(dto.getName());
        if (bindingResult.hasErrors()) {
            return (I) entityType;
        }
        Map<String, AttributeInterface> attributeMap = entityManager.getEntityAttributePrototypes();
        List<EntityAttributeFullDto> attributeDtos = dto.getAttributes();
        if (null != attributeDtos) {
            for (EntityAttributeFullDto attributeDto : attributeDtos) {
                AttributeInterface attribute = this.buildAttribute(dto.getCode(), attributeDto, attributeMap, bindingResult);
                if (null != attribute) {
                    entityType.addAttribute(attribute);
                } else {
                    logger.warn("Create Entity Type - Attribute type {} undefined in manager {}", attributeDto.getType(), entityManager.getName());
                }
            }
        }
        return (I) entityType;
    }

    protected AttributeInterface buildAttribute(String typeCode,
            EntityAttributeFullDto attributeDto, Map<String, AttributeInterface> attributeMap, BindingResult bindingResult) {
        String type = attributeDto.getType();
        AttributeInterface prototype = attributeMap.get(type);
        if (null == prototype) {
            logger.warn("Undefined attribute of type {}", type);
            this.addError(EntityTypeValidator.ERRCODE_INVALID_ATTRIBUTE_TYPE, bindingResult, new String[]{typeCode, type}, "entityType.attribute.type.invalid");
            return null;
        }
        AttributeInterface attribute = (AttributeInterface) prototype.getAttributePrototype();
        attribute.setName(attributeDto.getCode());
        attribute.setDescription(attributeDto.getName());
        attribute.setIndexingType(attributeDto.isIndexable() ? IndexableAttributeInterface.INDEXING_TYPE_TEXT : null);
        List<AttributeRoleDto> dtoRoles = attributeDto.getRoles();
        if (null != dtoRoles && !dtoRoles.isEmpty()) {
            List<String> codes = dtoRoles.stream().map(AttributeRoleDto::getCode).collect(Collectors.toList());
            attribute.setRoles(codes.toArray(new String[codes.size()]));
        }
        attribute.setRequired(attributeDto.isMandatory());
        attribute.setSearchable(attributeDto.isListFilter());
        if (attribute instanceof EnumeratorAttribute) {
            // to check into validator
            String staticItems = attributeDto.getEnumeratorStaticItems();
            String extractor = attributeDto.getEnumeratorExtractorBean();
            if (StringUtils.isEmpty(staticItems) && StringUtils.isEmpty(extractor)) {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_ENUMERATOR, bindingResult, new String[]{typeCode, attributeDto.getCode()}, "entityType.attribute.enumerator.invalid");
            }
            ((EnumeratorAttribute) attribute).setStaticItems(staticItems);
            ((EnumeratorAttribute) attribute).setExtractorBeanName(extractor);
            ((EnumeratorAttribute) attribute).setCustomSeparator(attributeDto.getEnumeratorStaticItemsSeparator());
        }
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        validationRules.setRequired(attributeDto.isMandatory());
        EntityAttributeValidationDto validationDto = attributeDto.getValidationRules();
        if (null != validationDto) {
            validationDto.buildAttributeValidation(typeCode, attribute, bindingResult);
        }
        if (attribute instanceof AbstractListAttribute) {
            if (null != attributeDto.getNestedAttribute()) {
                EntityAttributeFullDto nestedAttributeDto = attributeDto.getNestedAttribute();
                ((AbstractListAttribute) attribute).setNestedAttributeType(this.buildAttribute(typeCode, nestedAttributeDto, attributeMap, bindingResult));
            } else {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_LIST, bindingResult, new String[]{typeCode, type}, "entityType.attribute.list.missingNestedAttribute");
            }
        } else if (attribute instanceof CompositeAttribute) {
            List<EntityAttributeFullDto> compositeElementsDto = attributeDto.getCompositeAttributes();
            if (null != compositeElementsDto && !compositeElementsDto.isEmpty()) {
                for (EntityAttributeFullDto attributeElementDto : compositeElementsDto) {
                    AttributeInterface attributeElement = this.buildAttribute(typeCode, attributeElementDto, attributeMap, bindingResult);
                    ((CompositeAttribute) attribute).getAttributeMap().put(attributeElement.getName(), attributeElement);
                    ((CompositeAttribute) attribute).getAttributes().add(attributeElement);
                }
            } else {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_COMPOSITE, bindingResult, new String[]{typeCode, type}, "entityType.attribute.composite.missingElements");
            }
        }
        return attribute;
    }

    protected void deleteEntityType(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
            if (null == entityType) {
                return;
            }
            List<String> ids = entityManager.searchId(entityTypeCode, null);
            if (null != ids && ids.size() > 0) {
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(entityType, "entityType");
                bindingResult.reject(EntityTypeValidator.ERRCODE_ENTITY_TYPE_REFERENCES, new Object[]{entityTypeCode}, "entityType.cannot.delete.references");
                throw new ValidationConflictException(bindingResult);
            }
            ((IEntityTypesConfigurer) entityManager).removeEntityPrototype(entityTypeCode);
        } catch (ApsSystemException e) {
            logger.error("Error in delete entityType {}", entityTypeCode, e);
            throw new RestServerError("error in delete entityType", e);
        }
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
            throw new RestRourceNotFoundException("entityManagerCode", entityManagerCode);
        }
        return entityManager;
    }

    protected boolean filterObjects(Object bean, Filter[] filters, Map<String, String> mapping) {
        try {
            if (null == filters) {
                return true;
            }
            Map<String, Object> properties = BeanUtils.describe(bean);
            for (Filter filter : filters) {
                String fieldName = this.getFieldName(filter.getAttribute(), mapping);
                String value = (null != properties.get(fieldName))
                        ? properties.get(fieldName).toString() : null;
                if (null == value) {
                    continue;
                }
                if (!value.toLowerCase().contains(filter.getValue().toLowerCase())) {
                    return false;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("error filtering bean " + bean.getClass().getName(), e);
            throw new RestServerError("error filtering bean " + bean.getClass().getName(), e);
        }
        return true;
    }

    protected List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

}
