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

import com.agiletec.aps.system.common.entity.*;
import com.agiletec.aps.system.common.entity.model.*;
import com.agiletec.aps.system.common.entity.model.attribute.*;
import com.agiletec.aps.system.common.entity.model.attribute.util.*;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.beanutils.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.common.entity.model.attribute.util.EnumeratorMapAttributeItemsExtractor;
import org.entando.entando.aps.system.exception.*;
import org.entando.entando.aps.system.services.*;
import org.entando.entando.aps.system.services.entity.model.*;
import org.entando.entando.web.common.exceptions.*;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.slf4j.*;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.entando.entando.web.entity.validator.AbstractEntityTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST;

public abstract class AbstractEntityTypeService<I extends IApsEntity, O extends EntityTypeFullDto> implements BeanFactoryAware {

    private static final String NO_TYPE_FOUND_WITH_CODE = "no type found with code {}";
    private static final String ERROR_UPDATING_ENTITY_TYPE = "Error updating entity type";
    private static final String TYPE_CODE = "Type Code";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BeanFactory beanFactory;

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

    protected PagedMetadata<EntityTypeShortDto> getShortEntityTypes(String entityManagerCode,
                                                                    RestListRequest requestList) {

        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);

        Map<String, String> fieldMapping = this.getEntityTypeFieldNameMapping();

        TransformingComparator caseInsensitiveComparator = createCaseInsensitiveComparator();
        if (!RestListRequest.DIRECTION_VALUE_DEFAULT.equals(requestList.getDirection())) {
            caseInsensitiveComparator.reversed();
        }

        List<IApsEntity> entityTypes = entityManager.getEntityPrototypes()
                .values()
                .stream()
                .filter(i -> this.filterObjects(i, requestList.getFilters(), fieldMapping))
                .sorted(new BeanComparator<>(getFieldName(requestList.getSort(), fieldMapping), caseInsensitiveComparator))
                .collect(Collectors.toList());

        List<IApsEntity> sublist = requestList.getSublist(entityTypes);
        PagedMetadata<EntityTypeShortDto> pagedMetadata = new PagedMetadata<>(requestList, entityTypes.size());
        List<EntityTypeShortDto> body = this.getEntityTypeShortDtoBuilder().convert(sublist);
        for (EntityTypeShortDto entityTypeShortDto : body) {
            String code = entityTypeShortDto.getCode();
            entityTypeShortDto.setStatus(String.valueOf(entityManager.getStatus(code)));
        }
        pagedMetadata.setBody(body);
        return pagedMetadata;
    }

    private TransformingComparator createCaseInsensitiveComparator() {
        Transformer caseInsensitiveTransformer = input -> {
            if (input instanceof String) {
                return ((String)input).toLowerCase();
            } else {
                return input;
            }
        };

        return new TransformingComparator(caseInsensitiveTransformer);
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

    protected PagedMetadata<String> getAttributeTypes(String entityManagerCode, RestListRequest requestList) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        List<AttributeInterface> mainList = new ArrayList<>(entityManager.getEntityAttributePrototypes().values());
        List<String> attributeCodes = new AttributeTypeRequestListProcessor(requestList, mainList)
                .filterAndSort().getStream()
                .map(AttributeInterface::getType)
                .collect(Collectors.toList());
        List<String> sublist = requestList.getSublist(attributeCodes);
        PagedMetadata<String> pagedMetadata = new PagedMetadata<>(requestList, attributeCodes.size());
        pagedMetadata.setBody(sublist);
        return pagedMetadata;
    }

    protected AttributeTypeDto getAttributeType(String entityManagerCode, String attributeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        AttributeInterface attribute = entityManager.getEntityAttributePrototypes().get(attributeCode);
        if (null == attribute) {
            logger.warn("no attribute type found with code {}", attributeCode);
            throw new RestRourceNotFoundException(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_NOT_EXISTS, "attribute", attributeCode);
        }
        AttributeTypeDto dto = new AttributeTypeDto(attribute, entityManager);
        if (dto.isEnumeratorMapOptionsSupported()) {
            dto.setEnumeratorMapExtractorBeans(this.getEnumeratorMapExtractorBeans());
        } else if (dto.isEnumeratorOptionsSupported()) {
            dto.setEnumeratorExtractorBeans(this.getEnumeratorExtractorBeans());
        }
        return dto;
    }

    protected O getFullEntityType(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        I entityType = (I) entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
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
                this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_TYPE_ALREADY_EXISTS,
                        bindingResult, new String[]{bodyRequest.getCode()}, "entityType.exists");
                throw new ValidationConflictException(bindingResult);
            }
            I entityPrototype = this.createEntityType(entityManager, bodyRequest, bindingResult);
            if (bindingResult.hasErrors()) {
                return response;
            } else {
                ((IEntityTypesConfigurer) entityManager).addEntityPrototype(entityPrototype);
                response = builder.convert(entityPrototype);
            }
        } catch (ValidationConflictException vce) {
            throw vce;
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
                this.addError(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST,
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
            logger.error(ERROR_UPDATING_ENTITY_TYPE, e);
            throw new RestServerError(ERROR_UPDATING_ENTITY_TYPE, e);
        }
    }

    protected void addError(String errorCode, BindingResult bindingResult, String[] args, String message) {
        bindingResult.reject(errorCode, args, message);
    }

    protected I createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        Class entityClass = entityManager.getEntityClass();
        ApsEntity entityType = (ApsEntity) entityClass.newInstance();
        if (StringUtils.isEmpty(dto.getCode()) || dto.getCode().length() != 3) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_TYPE_CODE, bindingResult, new String[]{dto.getCode()}, "entityType.typeCode.invalid");
        }
        entityType.setTypeCode(dto.getCode());
        if (StringUtils.isEmpty(dto.getName())) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_TYPE_DESCR, bindingResult, new String[]{}, "entityType.typeDescription.invalid");
        }
        entityType.setTypeDescription(dto.getName());
        if (bindingResult.hasErrors()) {
            return (I) entityType;
        }
        Map<String, AttributeInterface> attributeMap = entityManager.getEntityAttributePrototypes();
        List<EntityTypeAttributeFullDto> attributeDtos = dto.getAttributes();
        if (null != attributeDtos) {
            for (EntityTypeAttributeFullDto attributeDto : attributeDtos) {
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
            EntityTypeAttributeFullDto attributeDto, Map<String, AttributeInterface> attributeMap, BindingResult bindingResult) {
        String type = attributeDto.getType();
        AttributeInterface prototype = attributeMap.get(type);
        if (null == prototype) {
            logger.warn("Undefined attribute of type {}", type);
            this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_ATTRIBUTE_TYPE, bindingResult, new String[]{typeCode, type}, "entityType.attribute.type.invalid");
            return null;
        }
        AttributeInterface attribute = (AttributeInterface) prototype.getAttributePrototype();
        attribute.setName(attributeDto.getCode());
        attribute.setDescription(attributeDto.getName());
        attribute.setIndexingType(attributeDto.isIndexable() ? IndexableAttributeInterface.INDEXING_TYPE_TEXT : null);
        List<AttributePropertyDto> dtoRoles = attributeDto.getRoles();
        if (!CollectionUtils.isEmpty(dtoRoles)) {
            attribute.setRoles(dtoRoles.stream().map(AttributePropertyDto::getCode).toArray(String[]::new));
        }
        attribute.setRequired(attributeDto.isMandatory());
        attribute.setSearchable(attributeDto.isListFilter());
        if (attribute instanceof EnumeratorAttribute) {
            // to check into validator
            String staticItems = attributeDto.getEnumeratorStaticItems();
            String extractor = attributeDto.getEnumeratorExtractorBean();
            if (StringUtils.isEmpty(staticItems) && StringUtils.isEmpty(extractor)) {
                this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_ENUMERATOR, bindingResult, new String[]{typeCode, attributeDto.getCode()}, "entityType.attribute.enumerator.invalid");
            }
            ((EnumeratorAttribute) attribute).setStaticItems(staticItems);
            ((EnumeratorAttribute) attribute).setExtractorBeanName(extractor);
            ((EnumeratorAttribute) attribute).setCustomSeparator(attributeDto.getEnumeratorStaticItemsSeparator());
        }
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        validationRules.setRequired(attributeDto.isMandatory());
        EntityTypeAttributeValidationDto validationDto = attributeDto.getValidationRules();
        if (null != validationDto) {
            validationDto.buildAttributeValidation(typeCode, attribute, bindingResult);
        }
        if (attribute instanceof AbstractListAttribute) {
            if (null != attributeDto.getNestedAttribute()) {
                EntityTypeAttributeFullDto nestedAttributeDto = attributeDto.getNestedAttribute();
                ((AbstractListAttribute) attribute).setNestedAttributeType(this.buildAttribute(typeCode, nestedAttributeDto, attributeMap, bindingResult));
            } else {
                this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_LIST, bindingResult, new String[]{typeCode, type}, "entityType.attribute.list.missingNestedAttribute");
            }
        } else if (attribute instanceof CompositeAttribute) {
            List<EntityTypeAttributeFullDto> compositeElementsDto = attributeDto.getCompositeAttributes();
            if (!CollectionUtils.isEmpty(compositeElementsDto)) {
                for (EntityTypeAttributeFullDto attributeElementDto : compositeElementsDto) {
                    AttributeInterface attributeElement = this.buildAttribute(typeCode, attributeElementDto, attributeMap, bindingResult);
                    ((CompositeAttribute) attribute).getAttributeMap().put(attributeElement.getName(), attributeElement);
                    ((CompositeAttribute) attribute).getAttributes().add(attributeElement);
                }
            } else {
                this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_COMPOSITE, bindingResult, new String[]{typeCode, type}, "entityType.attribute.composite.missingElements");
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
            if (!CollectionUtils.isEmpty(ids)) {
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(entityType, "entityType");
                bindingResult.reject(AbstractEntityTypeValidator.ERRCODE_ENTITY_TYPE_REFERENCES, new Object[]{entityTypeCode}, "entityType.cannot.delete.references");
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
            if (manager.getName().equals(entityManagerCode)) {
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
            if (ArrayUtils.isEmpty(filters)) {
                return true;
            }
            Map<String, String> properties = BeanUtils.describe(bean);
            for (Filter filter : filters) {
                String fieldName = this.getFieldName(filter.getAttribute(), mapping);
                String value = properties.get(fieldName);
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

    protected EntityTypeAttributeFullDto getEntityAttribute(String entityManagerCode, String entityTypeCode, String attributeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        AttributeInterface attribute = entityType.getAttribute(attributeCode);
        if (null == attribute) {
            logger.warn("no attribute found with code {}", entityTypeCode);
            throw new RestRourceNotFoundException(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_NOT_EXISTS, "Attribute", attributeCode);
        }
        return new EntityTypeAttributeFullDto(attribute, entityManager.getAttributeRoles());
    }

    protected EntityTypeAttributeFullDto addEntityAttribute(String entityManagerCode,
            String entityTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        AttributeInterface oldAttribute = entityType.getAttribute(bodyRequest.getCode());
        if (null != oldAttribute) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_ALREADY_EXISTS,
                    bindingResult, new String[]{entityTypeCode, bodyRequest.getCode()}, "entityType.attribute.exists");
            throw new ValidationConflictException(bindingResult);
        }
        Map<String, AttributeInterface> attributeMap = entityManager.getEntityAttributePrototypes();
        AttributeInterface attribute = this.buildAttribute(entityTypeCode, bodyRequest, attributeMap, bindingResult);
        if (bindingResult.hasErrors()) {
            return null;
        }

        try {
            entityType.addAttribute(attribute);
            ((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entityType);
            IApsEntity newEntityType = entityManager.getEntityPrototype(entityTypeCode);
            AttributeInterface newAttribute = newEntityType.getAttribute(bodyRequest.getCode());
            return new EntityTypeAttributeFullDto(newAttribute, entityManager.getAttributeRoles());
        } catch (Throwable e) {
            logger.error(ERROR_UPDATING_ENTITY_TYPE, e);
            throw new RestServerError(ERROR_UPDATING_ENTITY_TYPE, e);
        }
    }

    protected EntityTypeAttributeFullDto updateEntityAttribute(String entityManagerCode,
            String entityTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        AttributeInterface oldAttribute = entityType.getAttribute(bodyRequest.getCode());
        if (null == oldAttribute) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_NOT_EXISTS,
                    bindingResult, new String[]{entityTypeCode, bodyRequest.getCode()}, "entityType.attribute.notExists");
            throw new RestRourceNotFoundException(bindingResult);
        } else if (!oldAttribute.getType().equals(bodyRequest.getType())) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_TYPE_MISMATCH,
                    bindingResult, new String[]{entityTypeCode,
                        bodyRequest.getCode(), oldAttribute.getType(), bodyRequest.getType()}, "entityType.attribute.typeMismatch");
        }
        Map<String, AttributeInterface> attributeMap = entityManager.getEntityAttributePrototypes();
        AttributeInterface attribute = this.buildAttribute(entityTypeCode, bodyRequest, attributeMap, bindingResult);
        if (bindingResult.hasErrors()) {
            return null;
        }

        try {
            this.removeAttribute(entityType, bodyRequest.getCode());
            entityType.addAttribute(attribute);
            ((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entityType);
            IApsEntity newEntityType = entityManager.getEntityPrototype(entityTypeCode);
            AttributeInterface newAttribute = newEntityType.getAttribute(bodyRequest.getCode());
            return new EntityTypeAttributeFullDto(newAttribute, entityManager.getAttributeRoles());
        } catch (Throwable e) {
            logger.error(ERROR_UPDATING_ENTITY_TYPE, e);
            throw new RestServerError(ERROR_UPDATING_ENTITY_TYPE, e);
        }
    }

    protected void deleteEntityAttribute(String entityManagerCode, String entityTypeCode, String attributeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        if (!entityType.getAttributeMap().containsKey(attributeCode)) {
            return;
        }
        try {
            this.removeAttribute(entityType, attributeCode);
            ((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entityType);
        } catch (Throwable e) {
            logger.error(ERROR_UPDATING_ENTITY_TYPE, e);
            throw new RestServerError(ERROR_UPDATING_ENTITY_TYPE, e);
        }
    }

    private void removeAttribute(IApsEntity entityType, String attributeToRemove) {
        List<AttributeInterface> attributes = entityType.getAttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface old = attributes.get(i);
            if (old.getName().equals(attributeToRemove)) {
                attributes.remove(i);
                break;
            }
        }
        entityType.getAttributeMap().remove(attributeToRemove);
    }

    protected void moveEntityAttribute(String entityManagerCode, String entityTypeCode, String attributeCode, boolean moveUp) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        int index = -1;
        AttributeInterface attributeToMove = null;
        List<AttributeInterface> attributes = entityType.getAttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface attribute = attributes.get(i);
            if (attribute.getName().equals(attributeCode)) {
                attributeToMove = attribute;
                index = i;
            }
        }
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(entityType, "entityType");
        if (null == attributeToMove) {
            this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_NOT_EXISTS,
                    bindingResult, new String[]{entityTypeCode, attributeCode}, "entityType.attribute.notExists");
            throw new RestRourceNotFoundException(bindingResult);
        }
        if ((index == 0 && moveUp) || (index == attributes.size() - 1 && !moveUp)) {
            String movement = (moveUp) ? "UP" : "DOWN";
            this.addError(AbstractEntityTypeValidator.ERRCODE_ENTITY_ATTRIBUTE_INVALID_MOVEMENT,
                    bindingResult, new String[]{entityTypeCode, attributeCode, movement}, "entityType.attribute.movement.invalid");
            throw new ValidationGenericException(bindingResult);
        }
        attributes.remove(index);
        if (moveUp) {
            attributes.add(index - 1, attributeToMove);
        } else {
            attributes.add(index + 1, attributeToMove);
        }
        try {
            ((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entityType);
        } catch (Throwable e) {
            logger.error(ERROR_UPDATING_ENTITY_TYPE, e);
            throw new RestServerError(ERROR_UPDATING_ENTITY_TYPE, e);
        }
    }

    protected void reloadEntityTypeReferences(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityType) {
            logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
            throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
        }
        entityManager.reloadEntitiesReferences(entityTypeCode);
    }

    protected Map<String, Integer> reloadEntityTypesReferences(String entityManagerCode, List<String> entityTypeCodes) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        Map<String, Integer> status = new HashMap<>();
        if (CollectionUtils.isEmpty(entityTypeCodes)) {
            return status;
        }
        for (String entityTypeCode : entityTypeCodes) {
            IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
            if (null == entityType) {
                logger.warn(NO_TYPE_FOUND_WITH_CODE, entityTypeCode);
                throw new RestRourceNotFoundException(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, TYPE_CODE, entityTypeCode);
            }
        }
        for (String entityTypeCode : entityTypeCodes) {
            entityManager.reloadEntitiesReferences(entityTypeCode);
            status.put(entityTypeCode, entityManager.getStatus(entityTypeCode));
        }
        return status;
    }

    protected EntityTypesStatusDto getEntityTypesRefreshStatus(String entityManagerCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        return new EntityTypesStatusDto(entityManager);
    }

    public List<String> getEnumeratorExtractorBeans() {
        return this.getEnumeratorExtractorBeans(EnumeratorAttributeItemsExtractor.class);
    }

    public List<String> getEnumeratorMapExtractorBeans() {
        return this.getEnumeratorExtractorBeans(EnumeratorMapAttributeItemsExtractor.class);
    }

    protected List<String> getEnumeratorExtractorBeans(Class type) {
        try {
            ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
            String[] defNames = factory.getBeanNamesForType(type);
            return Arrays.asList(defNames);
        } catch (Throwable t) {
            logger.error("Error while extracting enumerator extractor beans", t);
            throw new RuntimeException("Error while extracting enumerator extractor beans", t);
        }
    }

    protected List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
