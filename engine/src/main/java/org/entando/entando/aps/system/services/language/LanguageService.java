package org.entando.entando.aps.system.services.language;

import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.language.utils.LanguageRequestListProcessor;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.language.validator.LanguageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

public class LanguageService implements ILanguageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILangManager langManager;

    private LanguageDtoBuilder languageDtoBuilder;

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IDtoBuilder<Lang, LanguageDto> getLanguageDtoBuilder() {
        return languageDtoBuilder;
    }

    @PostConstruct
    public void setUpDto() {
        this.languageDtoBuilder = new LanguageDtoBuilder();
        this.languageDtoBuilder.setLangManager(getLangManager());
    }

    @Override
    public PagedMetadata<LanguageDto> getLanguages(RestListRequest requestList) {
        try {
            List<Lang> sysLangs = this.getLangManager().getAssignableLangs();
            List<LanguageDto> langs = this.getLanguageDtoBuilder().convert(sysLangs);
            
            langs = new LanguageRequestListProcessor(requestList, langs)
                    .filterAndSort().toList();

            //page
            SearcherDaoPaginatedResult<LanguageDto> langsResult = new SearcherDaoPaginatedResult<>(langs.size(), langs);
            langsResult.setCount(langs.size());
            PagedMetadata<LanguageDto> pagedMetadata = new PagedMetadata<>(requestList, langsResult);
            pagedMetadata.setBody(langs);
            pagedMetadata.imposeLimits();
            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search langs", t);
            throw new RestServerError("error in search langs", t);
        }
    }

    @Override
    public LanguageDto getLanguage(String code) {
        try {
            Lang lang = this.getLangManager().getAssignableLangs().stream().filter(i -> i.getCode().equals(code)).findFirst().orElse(null);
            if (null == lang) {
                logger.warn("no lang found with code {}", code);
                throw new ResourceNotFoundException(LanguageValidator.ERRCODE_LANGUAGE_DOES_NOT_EXISTS, "language", code);
            }
            return this.getLanguageDtoBuilder().convert(lang);
        } catch (ApsSystemException ex) {
            throw new RestServerError("error in getting language " + code, ex);
        }
    }

    @Override
    public LanguageDto updateLanguage(String code, boolean status) {
        if (status) {
            return this.enableLang(code);
        } else {
            return this.disableLang(code);
        }
    }

    protected LanguageDto disableLang(String code) {
        try {
            Lang sysLang = this.getLangManager().getAssignableLangs().stream().filter(i -> i.getCode().equals(code)).findFirst().orElse(null);
            if (null == sysLang) {
                logger.warn("no lang found with code {}", code);
                throw new ResourceNotFoundException(LanguageValidator.ERRCODE_LANGUAGE_DOES_NOT_EXISTS, "language", code);
            }
            //idempotent
            Lang lang = this.getLangManager().getLang(code);
            if (null != this.getLangManager().getLang(code)) {
                BeanPropertyBindingResult validations = this.validateDisable(lang);
                if (validations.hasErrors()) {
                    throw new ValidationConflictException(validations);
                }
            }
            this.getLangManager().removeLang(code);
            return this.getLanguageDtoBuilder().convert(sysLang);
        } catch (ApsSystemException ex) {
            throw new RestServerError("error disabling language " + code, ex);
        }
    }

    protected LanguageDto enableLang(String code) {
        try {
            Lang lang = this.getLangManager().getAssignableLangs().stream().filter(i -> i.getCode().equals(code)).findFirst().orElse(null);
            if (null == lang) {
                logger.warn("no lang found with code {}", code);
                throw new ResourceNotFoundException(LanguageValidator.ERRCODE_LANGUAGE_DOES_NOT_EXISTS, "language", code);
            }
            //idempotent
            if (null == this.getLangManager().getLang(code)) {
                logger.warn("the lang {} is already active", code);
                this.getLangManager().addLang(lang.getCode());
            }
            return this.getLanguageDtoBuilder().convert(lang);
        } catch (ApsSystemException ex) {
            throw new RestServerError("error enabling lang " + code, ex);
        }
    }

    protected BeanPropertyBindingResult validateDisable(Lang lang) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(lang, "lang");
        if (lang.isDefault()) {
            bindingResult.reject(LanguageValidator.ERRCODE_LANGUAGE_CANNOT_DISABLE_DEFAULT, new Object[]{lang.getCode(), lang.getDescr()}, "language.cannot.disable.default");
        }
        return bindingResult;

    }

}
