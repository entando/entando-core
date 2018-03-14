package org.entando.entando.aps.system.services.language;

import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface ILanguageService {

    String BEAN_NAME = "LanguageService";

    public PagedMetadata<LanguageDto> getLanguages(RestListRequest requestList);

    public LanguageDto getLanguage(String code);

    public LanguageDto updateLanguage(String code, boolean active);

}

