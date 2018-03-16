package org.entando.entando.aps.system.services.language;

import java.util.stream.Collectors;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import org.entando.entando.aps.system.services.DtoBuilder;

public class LanguageDtoBuilder extends DtoBuilder<Lang, LanguageDto> {

    private ILangManager langManager;

    public ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }


    @Override
    protected LanguageDto toDto(Lang src) {
        return new LanguageDto(src, this.getLangManager().getLangs().stream().map(i -> i.getCode()).collect(Collectors.toList()), langManager.getDefaultLang().getCode());
    }

}
