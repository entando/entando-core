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
package com.agiletec.aps.system.services.lang.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import java.util.List;

/**
 * @author E.Santoboni
 */
public interface ILangManagerCacheWrapper {

	public static final String LANG_MANAGER_CACHE_NAME = "Entando_LangManager";

	public static final String LANG_CACHE_NAME_PREFIX = "LangManager_lang_";
	public static final String LANG_CODES_CACHE_NAME = "LangManager_codes";
	public static final String LANG_DEFAULT_CACHE_NAME = "LangManager_default";

	public void initCache(String xmlConfig) throws ApsSystemException;

	public List<Lang> getLangs();

	public Lang getDefaultLang();

	public Lang getLang(String code);

	public void addLang(Lang lang);

	public void updateLang(Lang lang);

	public void removeLang(Lang lang);

}
