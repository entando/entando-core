package org.entando.entando.aps.system.services.language;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LanguageService implements ILanguageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILangManager langManager;

    private IDtoBuilder<Lang, LanguageDto> languageDtoBuilder;

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IDtoBuilder<Lang, LanguageDto> getLanguageDtoBuilder() {
        if (null == languageDtoBuilder) {
            this.languageDtoBuilder = new DtoBuilder<Lang, LanguageDto>() {
                @Override
                protected LanguageDto toDto(Lang src) {
                    return new LanguageDto(src);
                }
            };
        }
        return languageDtoBuilder;
    }


    @Override
    public PagedMetadata<LanguageDto> getLanguages(RestListRequest requestList) {
        try {
            List<Lang> langs = this.getLangManager().getLangs();
            SearcherDaoPaginatedResult<Lang> langsRes = new SearcherDaoPaginatedResult<>(langs.size(), langs);
            List<LanguageDto> dtoList = this.getLanguageDtoBuilder().convert(langsRes.getList());
            langsRes.setCount(langs.size());

            PagedMetadata<LanguageDto> pagedMetadata = new PagedMetadata<>(requestList, langsRes);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search langs", t);
            throw new RestServerError("error in search langs", t);
        }
    }


}
