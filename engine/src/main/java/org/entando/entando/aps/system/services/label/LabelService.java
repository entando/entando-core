package org.entando.entando.aps.system.services.label;

import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.label.LabelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class LabelService implements ILabelService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    @Override
    public PagedMetadata<LabelDto> getLabelGroups(RestListRequest restRequest) {
        //        restRequest.getSort();
        //        restRequest.getDirection();
        //        Comparator<Entry<String, ApsProperties>> comparator = null;
        //        if ("aaa".equals(restRequest.getSort())) {
        //            comparator = (o1, o2) -> {
        //                return o1.getKey().compareTo(o2.getKey());
        //            };
        //        }
        //
        //        this.getI18nManager().getLabelGroups().entrySet().stream().sorted((o1, o2) -> {
        //            return o1.getKey().compareTo(o2.getKey());
        //        });

        //this.getI18nManager().s
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public LabelDto getLabelGroup(String code) {
        try {
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);
            if (null == labelGroup) {
                logger.warn("no label found with key {}", code);
                throw new RestRourceNotFoundException(null, "label", code);
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
                throw new RestRourceNotFoundException(null, "label", code);
            }
            Properties languages = new Properties();
            languages.putAll(labelRequest.getLanguages());
            this.getI18nManager().updateLabelGroup(code, new ApsProperties(languages));

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
            Properties languages = new Properties();
            languages.putAll(labelRequest.getLanguages());
            this.getI18nManager().addLabelGroup(code, new ApsProperties(languages));

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

    protected BeanPropertyBindingResult validateAddLabelGroup(LabelDto labelDto) {
        try {

            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(labelDto, "labelGroup");

            String code = labelDto.getKey();
            ApsProperties labelGroup = this.getI18nManager().getLabelGroup(code);

            // 1) NON deve esistere
            if (null != labelGroup) {
                bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_EXISTS, new String[]{code}, "labelGroup.code.already.present");
                return bindingResult;
            }

            // 2) deve essre presente la lingua di default
            String defaultLang = this.getLangManager().getDefaultLang().getCode();
            if (!labelDto.getLanguages().keySet().contains(defaultLang)) {
                bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_DEFAULT_LANG_REQUIRED, new String[]{defaultLang}, "labelGroup.langs.defaultLang.required");
                return bindingResult;
            }

            List<String> configuredLangs = this.getLangManager().getLangs().stream().map(i -> i.getCode()).collect(Collectors.toList());

            labelDto.getLanguages().entrySet().forEach(i -> validateLangEntry(i, configuredLangs, defaultLang, bindingResult));

            return bindingResult;

        } catch (ApsSystemException t) {
            logger.error("error in validate add label group with code {}", labelDto.getKey(), t);
            throw new RestServerError("error in validate add label group", t);
        }
    }

    private void validateLangEntry(Entry<String, String> entry, List<String> configuredLangs, String defaultLangCode, BeanPropertyBindingResult bindingResult) {
        String currentLangCode = entry.getKey();
        // 3) nella mappa key deve essere una lingua attiva
        if (!configuredLangs.contains(currentLangCode)) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_INVALID_LANG, new String[]{currentLangCode}, "labelGroup.langs.lang.invalid");
        }

        // 4) nella mappa value non deve essere nullo se defaultlang
        if (currentLangCode.equals(defaultLangCode) && StringUtils.isBlank(entry.getValue())) {
            bindingResult.reject(LabelValidator.ERRCODE_LABELGROUP_LANGS_TEXT_REQURED, new String[]{currentLangCode}, "labelGroup.langs.defaultLang.textRequired");
        }

    }


}
