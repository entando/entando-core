package org.entando.entando.aps.system.services.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.api.cache.ApiCatalogManagerCacheWrapper;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.agiletec.aps.system.exception.ApsSystemException;

public class ApiCatalogManagerTest {

	@Mock
	private ApiCatalogManagerCacheWrapper cacheWrapper;

	@Mock
	private ApiCatalogDAO apiCatalogDAO;

	@InjectMocks
	private ApiCatalogManager apiCatalogManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testResources() throws ApsSystemException {
		when(cacheWrapper.getMasterResources()).thenReturn(createRersources());
		Map<String, ApiResource> resources = this.apiCatalogManager.getResources();
		assertThat(resources.size(), is(5));
	}

	@Test
	public void testGetMethod() throws Throwable {
		when(cacheWrapper.getMasterResources()).thenReturn(createRersources());
		ApiMethod method = this.apiCatalogManager.getMethod(ApiMethod.HttpMethod.GET, "getService");
		assertNotNull(method);
		assertTrue(method.isActive());
	}

	@Test
	public void testGetMethods() throws Throwable {
		when(cacheWrapper.getMasterResources()).thenReturn(createRersources());
		List<ApiMethod> methods = this.apiCatalogManager.getMethods(ApiMethod.HttpMethod.GET);
		assertNotNull(methods);
		assertTrue(methods.size() > 0);
	}

	@Test
	public void testUpdateMethodStatus() throws Throwable {
		when(cacheWrapper.getMasterResources()).thenReturn(createRersources());
		ApiMethod method = this.apiCatalogManager.getMethod(ApiMethod.HttpMethod.GET, "getService");
		method.setStatus(false);
		this.apiCatalogManager.updateMethodConfig(method);
		method = this.apiCatalogManager.getMethod(ApiMethod.HttpMethod.GET, "getService");
		assertFalse(method.isActive());
	}

	@Test
	public void testGetServices() throws Throwable {
		when(cacheWrapper.getMasterResources()).thenReturn(createRersources());
		Map<String, ApiService> services = this.apiCatalogManager.getServices();
		assertNotNull(services);
		assertTrue(services.size() == 0);
	}

	private Map<String, ApiResource> createRersources() throws ApsSystemException {
		ApiResourceLoader loader = new ApiResourceLoader(ApiCatalogManager.DEFAULT_LOCATION_PATTERN);
		Map<String, ApiResource> res = loader.getResources();
		Map<String, ApiResource> resources = new HashMap<>();
		for (Map.Entry<String, ApiResource> entry : res.entrySet()) {
			if (StringUtils.startsWithAny(entry.getKey(), new String[] { "getS", "core:user" })) {
				resources.put(entry.getKey(), entry.getValue());
			}
		}
		return resources;
	}

}
