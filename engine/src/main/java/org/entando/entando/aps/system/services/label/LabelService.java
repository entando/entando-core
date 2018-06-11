/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.label;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.label.LabelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class LabelService implements ILabelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LABEL_KEY_FILTER_KEY = "key";
    private static final String LABEL_KEY_FILTER_VALUE = "value";

    private II18nManager i18nManager;
    private ILangManager langManager;
    private LabelDtoBuilder dtoBuilder = new LabelDtoBuilder();

    protected II18nManager getI18nManager() {
        return i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this.i18nManager = i18nManager;
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected LabelDtoBuilder getDtoBuilder() {
        return dtoBuilder;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public PagedMetadata<LabelDto> getLabelGroups(RestListRequest restRequest) {
        Map<String, ApsProperties> result = this.i18nManager.getLabelGroups();
        List<LabelDto> dtoList = this.getDtoBuilder().convert(result);
        if (restRequest.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
            dtoList = dtoList.stream().sorted(Comparator.comparing(LabelDto::getKey).reversed()).collect(Collectors.toList());
        } else {
            dtoList = dtoList.stream().sorted(Comparator.comparing(LabelDto::getKey)).collect(Collectors.toList());
        }

        if (null != restRequest.getFilters()) {
            for (Filter f : restRequest.getFilters()) {
                if (f.getAttributeName().equals(LABEL_KEY_FILTER_KEY)) {
                    dtoList = dtoList
                                     .stream()
                                     .filter(i -> i.getKey().toLowerCase().contains(f.getValue().toLowerCase()))
                                     .collect(Collectors.toList());
                }
                if (f.getAttributeName().equals(LABEL_KEY_FILTER_VALUE)) {
                    dtoList = dtoList
                                     .stream()
                                     .filter(i -> i.getTitles().values().stream().filter(k -> k.contains(f.getValue())).collect(Collectors.toList()).size() > 0)
                                     .collect(Collectors.toList());
                }
            }
        }
        List<?> subList = restRequest.getSublist(dtoList);
        SearcherDaoPaginatedResult<LabelDto> resultx = new SearcherDaoPaginatedResult(dtoList.size(), subList);
        PagedMetadata<LabelDto> pagedMetadata = new PagedMetadata<>(restRequest, resultx);
        pagedMetadata.setBody((List<LabelDto>) subList);
        return pagedMetadata;
    }

    @Override
    public LabelDto getLabelGroup(String code) {
        try {
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);
            if (null == labelGroup) {
                logger.warn("no label found with key {}", code);
                throw new RestRourceNotFoundException(LabelValidator.ERRCODE_LABELGROUP_NOT_FOUND, "label", code);
            }
            return this.getDtoBuilder().convert(code, labelGroup);
        } catch (ApsSystemException t) {
            logger.error("error in get label group with code {}", code, t);
            throw new RestServerError("error in get label group", t);
        }
    }

    @Override
    public LabelDto updateLabelGroup(LabelDto labelRequest) {
        try {
            String code = labelRequest.getKey();
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);
            if (null == labelGroup) {
                logger.warn("no label found with key {}", code);
                throw new RestRourceNotFoundException(LabelValidator.ERRCODE_LABELGROUP_NOT_FOUND, "label", code);
            }
            BeanPropertyBindingResult validationResult = this.validateUpdateLabelGroup(labelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            ApsProperties languages = new ApsProperties();
            languages.putAll(labelRequest.getTitles());
            this.getI18nManager().updateLabelGroup(code, languages);
            return labelRequest;
        } catch (ApsSystemException t) {
            logger.error("error in update label group with code {}", labelRequest.getKey(), t);
            throw new RestServerError("error in update label group", t);
        }
    }

    @Override
    public LabelDto addLabelGroup(LabelDto labelRequest) {
        try {
            BeanPropertyBindingResult validationResult = this.validateAddLabelGroup(labelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            String code = labelRequest.getKey();
            ApsProperties languages = new ApsProperties();
            languages.putAll(labelRequest.getTitles());
            this.getI18nManager().addLabelGroup(code, languages);
            return labelRequest;
        } catch (ApsSystemException t) {
            logger.error("error in add label group with code {}", labelRequest.getKey(), t);
            throw new RestServerError("error in add label group", t);
        }
    }

    @Override
    public void removeLabelGroup(String code) {
        try {
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);
            if (null == labelGroup) {
                logger.warn("no label found with key {}", code);
                return;
            }
            this.getI18nManager().deleteLabelGroup(code);
        } catch (ApsSystemException t) {
            logger.error("error in delete label group with code {}", code, t);
            throw new RestServerError("error in delete label group", t);
        }
    }

    protected BeanPropertyBindingResult validateUpdateLabelGroup(LabelDto labelDto) {
        try {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(labelDto, "labelGroup");
            String defaultLang = this.getLangManager().getDefaultLang().getCode();
            boolean isDefaultLangValid = validateDefaultLang(labelDto, bindingResult, defaultLang);
            if (!isDefaultLangValid) {
                return bindingResult;
            }
            validateLabelEntry(labelDto, defaultLang, bindingResult);
            return bindingResult;
        } catch (ApsSystemException t) {
            logger.error("error in validate add label group with code {}", labelDto.getKey(), t);
            throw new RestServerError("error in validate add label group", t);
        }
    }

    protected BeanPropertyBindingResult validateAddLabelGroup(LabelDto labelDto) {
        try {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(labelDto, "labelGroup");
            String code = labelDto.getKey();
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);
            if (null != labelGroup) {
                bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_EXISTS, new String[]{code}, "labelGroup.code.already.present");
                return bindingResult;
            }
            String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
            boolean isDefaultLangValid = this.validateDefaultLang(labelDto, bindingResult, defaultLangCode);
            if (!isDefaultLangValid) {
                return bindingResult;
            }
            this.validateLabelEntry(labelDto, defaultLangCode, bindingResult);
            return bindingResult;
        } catch (ApsSystemException t) {
            logger.error("error in validate add label group with code {}", labelDto.getKey(), t);
            throw new RestServerError("error in validate add label group", t);
        }
    }

    protected void validateLabelEntry(LabelDto labelDto, String defaultLang, BeanPropertyBindingResult bindingResult) throws ApsSystemException {
        List<String> configuredLangs = this.getLangManager().getLangs().stream().map(i -> i.getCode()).collect(Collectors.toList());
        List<String> systemLangs = this.getLangManager().getAssignableLangs().stream()
                                       .map(i -> i.getCode()).collect(Collectors.toList());
        labelDto.getTitles().entrySet().forEach(i -> validateLangEntry(i, systemLangs, configuredLangs, defaultLang, bindingResult));
    }

    protected boolean validateDefaultLang(LabelDto labelDto, BeanPropertyBindingResult bindingResult, String defaultLang) {
        String label = (null != labelDto && null != labelDto.getTitles()) ? labelDto.getTitles().get(defaultLang) : null;
        if (StringUtils.isEmpty(label)) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_DEFAULT_LANG_REQUIRED, new String[]{defaultLang}, "labelGroup.langs.defaultLang.required");
            return false;
        }
        return true;
    }

    private void validateLangEntry(Entry<String, String> entry, List<String> systemLangs, List<String> configuredLangs, String defaultLangCode, BeanPropertyBindingResult bindingResult) {
        String currentLangCode = entry.getKey();
        if (!systemLangs.contains(currentLangCode)) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_INVALID_LANG, new String[]{currentLangCode}, "labelGroup.langs.lang.invalid");
            return;
        }
        if (!configuredLangs.contains(currentLangCode)) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_NOT_ACTIVE_LANG, new String[]{currentLangCode}, "labelGroup.langs.lang.notActive");
        }
        if (currentLangCode.equals(defaultLangCode) && StringUtils.isBlank(entry.getValue())) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_TEXT_REQUIRED, new String[]{currentLangCode}, "labelGroup.langs.defaultLang.textRequired");
        }
    }

}
