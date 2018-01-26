/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiletec.aps.system.services.lang;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.cache.ILangManagerCacheWrapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

/**
 *
 * @author eu
 */
public class TestLangManagerUnit {

	@Mock
	private ConfigInterface configManager;

	@Mock
	private ILangManagerCacheWrapper cacheWrapper;

	@InjectMocks
	private LangManager langManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getDefaultLang() {
		Lang defaultLang = new Lang();
		defaultLang.setCode("de");
		defaultLang.setCode("Tedesco");
		Mockito.when(cacheWrapper.getDefaultLang()).thenReturn(defaultLang);
		Assert.assertThat(this.langManager.getDefaultLang(), CoreMatchers.is(CoreMatchers.notNullValue()));
	}

}
