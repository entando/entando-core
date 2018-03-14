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
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.AttributeRoleDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeOgnlValidationDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeValidationDto;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypesBodyRequest;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        return this.convertEntityType(entityManager, entityType);
    }

    protected O convertEntityType(IEntityManager entityManager, I entityType) {
        return this.getEntityTypeFullDtoBuilder(entityManager).convert(entityType);
    }

    protected abstract IDtoBuilder<I, O> getEntityTypeFullDtoBuilder(IEntityManager masterManager);

    protected synchronized List<O> addEntityTypes(String entityManagerCode, DataTypesBodyRequest bodyRequest, BindingResult bindingResult) {
        List<O> response = new ArrayList<>();
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            List<I> entityTypesToAdd = new ArrayList<>();
            IDtoBuilder<I, O> builder = this.getEntityTypeFullDtoBuilder(entityManager);
            for (EntityTypeDtoRequest dto : bodyRequest.getDataTypes()) {
                I entityPrototype = this.createEntityType(entityManager, dto, bindingResult);
                entityTypesToAdd.add(entityPrototype);
            }
            if (bindingResult.hasErrors()) {
                throw new ValidationGenericException(bindingResult);
            } else {
                for (I i : entityTypesToAdd) {
                    ((IEntityTypesConfigurer) entityManager).addEntityPrototype(i);
                    response.add(builder.convert(i));
                }
            }
        } catch (Throwable e) {
            logger.error("Error adding entity types", e);
            throw new RestServerError("error add entity types", e);
        }
        return response;
    }

    protected EntityTypeFullDto updateEntityType(String entityManagerCode, EntityTypeDtoRequest request, BindingResult bindingResult) {
        /*
        Group group = this.getGroupManager().getGroup(groupCode);
        if (null == group) {
            throw new RestRourceNotFoundException("group", groupCode);
        }
        group.setDescription(descr);
        try {
            this.getGroupManager().updateGroup(group);
            return this.getDtoBuilder().convert(group);
        } catch (ApsSystemException e) {
            logger.error("Error updating group {}", groupCode, e);
            throw new RestServerError("error in update group", e);
        }
         */
        return null;
    }

    protected void addError(BindingResult bindingResult, String[] args, String message) {
        bindingResult.reject(EntityTypeValidator.ERRCODE_GENERIC_VALIDATION, args, message);
    }

    protected I createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        Class entityClass = entityManager.getEntityClass();
        ApsEntity entityType = (ApsEntity) entityClass.newInstance();
        if (StringUtils.isEmpty(dto.getCode()) || dto.getCode().length() != 3) {
            this.addError(bindingResult, new String[]{dto.getCode()}, "entityType.typeCode.invalid");
        }
        entityType.setTypeCode(dto.getCode());
        if (StringUtils.isEmpty(dto.getCode()) || dto.getCode().length() != 3) {
            this.addError(bindingResult, new String[]{}, "entityType.typeDescription.invalid");
        }
        entityType.setTypeDescription(dto.getName());
        Map<String, AttributeInterface> attributeMap = entityManager.getEntityAttributePrototypes();
        List<EntityAttributeFullDto> attributeDtos = dto.getAttributes();
        if (null != attributeDtos) {
            for (EntityAttributeFullDto attributeDto : attributeDtos) {
                AttributeInterface attribute = this.buildAttribute(attributeDto, attributeMap);
                if (null != attribute) {
                    entityType.addAttribute(attribute);
                } else {
                    logger.warn("Create Entity Type - Attribute type {} undefined in manager {}", attributeDto.getType(), entityManager.getName());
                }
            }
        }
        return (I) entityType;
    }

    private AttributeInterface buildAttribute(EntityAttributeFullDto attributeDto, Map<String, AttributeInterface> attributeMap) {
        String type = attributeDto.getType();
        AttributeInterface prototype = attributeMap.get(type);
        if (null == prototype) {
            logger.warn("Undefined attribute of type {}", type);
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
            ((EnumeratorAttribute) attribute).setStaticItems(attributeDto.getEnumeratorStaticItems());
            ((EnumeratorAttribute) attribute).setExtractorBeanName(attributeDto.getEnumeratorExtractorBean());
            ((EnumeratorAttribute) attribute).setCustomSeparator(attributeDto.getEnumeratorStaticItemsSeparator());
        }
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        validationRules.setRequired(attributeDto.isMandatory());
        EntityAttributeValidationDto validationDto = attributeDto.getValidationRules();
        if (null != validationDto) {
            EntityAttributeOgnlValidationDto ognlValidationDto = validationDto.getOgnlValidation();
            if (null != ognlValidationDto && !StringUtils.isEmpty(ognlValidationDto.getOgnlExpression())) {
                // to check into validator
                OgnlValidationRule ognlValidationRule = new OgnlValidationRule();
                ognlValidationRule.setErrorMessage(ognlValidationDto.getErrorMessage());
                ognlValidationRule.setErrorMessageKey(ognlValidationDto.getKeyForErrorMessage());
                ognlValidationRule.setEvalExpressionOnValuedAttribute(ognlValidationDto.isApplyOnlyToFilledAttr());
                ognlValidationRule.setExpression(ognlValidationDto.getOgnlExpression());
                ognlValidationRule.setHelpMessage(ognlValidationDto.getHelpMessage());
                ognlValidationRule.setHelpMessageKey(ognlValidationDto.getKeyForHelpMessage());
            }
        }
        if (attribute instanceof AbstractListAttribute && null != attributeDto.getNestedAttribute()) {
            EntityAttributeFullDto nestedAttributeDto = attributeDto.getNestedAttribute();
            ((AbstractListAttribute) attribute).setNestedAttributeType(this.buildAttribute(nestedAttributeDto, attributeMap));
        } else if (attribute instanceof CompositeAttribute
                && null != attributeDto.getCompositeAttributes()
                && !attributeDto.getCompositeAttributes().isEmpty()) {
            List<EntityAttributeFullDto> attributes = attributeDto.getCompositeAttributes();
            for (EntityAttributeFullDto attributeElementDto : attributes) {
                AttributeInterface attributeElement = this.buildAttribute(attributeElementDto, attributeMap);
                ((CompositeAttribute) attribute).getAttributeMap().put(attributeElement.getName(), attributeElement);
                ((CompositeAttribute) attribute).getAttributes().add(attributeElement);
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
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(entityType, "entityType");
            List<String> ids = entityManager.searchId(entityTypeCode, null);
            if (null != ids && ids.size() > 0) {
                bindingResult.reject(EntityTypeValidator.ERRCODE_ENTITY_TYPE_REFERENCES, new Object[]{entityTypeCode}, "entityType.cannot.delete.references");
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
