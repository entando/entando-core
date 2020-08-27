package org.entando.entando.aps.servlet.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.agiletec.aps.system.SystemConstants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.entando.entando.aps.system.exception.CSRFProtectionException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CsrfFilterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfFilterTest.class);

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private MockEnvironment mockEnvironment;


    @Before
    public void initTest() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        mockEnvironment = new MockEnvironment();
        setEnvironments(mockEnvironment);
    }

    //ORIGIN
    //Domain different from those set in the environment variable ENTANDO_CSRF_ALLOWED_DOMAINS
    @Test
    public void originForbidden() {
        testFilter("http://xxxx.it", null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    //ORIGIN
    //Domain contained in the environment variable
    @Test
    public void originWithWhiteList() {
        testFilter("http://organization.it", null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    //ORIGIN
    //Domain contained in the environment variable with wildcard
    @Test
    public void originWithWildCard() {
        testFilter("http://xxx.test.entando.com", null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    //REFERER
    //Domain different from those set in the environment variable ENTANDO_CSRF_ALLOWED_DOMAINS
    @Test
    public void refererForbidden() {
        testFilter(null, "http://xxxx.it/index.html", HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    //REFERER
    //Domain contained in the environment variable ENTANDO_CSRF_ALLOWED_DOMAINS
    @Test
    public void refererWithWhiteList() {
        testFilter(null, "http://organization.it/test/index.html", HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    //REFERER
    //Domain contained in the environment variable with wildcard
    @Test
    public void refererWithWildCard() {
        testFilter(null, "http://xxx.yyy.zzz.entando.com/test/index.html", HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    // ENTANDO_CSRF_PROTECTION environment variable other than basic
    @Test
    public void csrfProtectionNotEnabled() {
        mockEnvironment.setProperty(SystemConstants.ENTANDO_CSRF_PROTECTION, "basic");
        testFilter("http://test.it", null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    // referer not valid
    @Test(expected = CSRFProtectionException.class)
    public void refererNotValid() {
        testFilter(null, "xxxxxx", HttpMethod.POST.name());
    }

    // origin not valid
    @Test(expected = CSRFProtectionException.class)
    public void originNotValid() {
        testFilter("xxxxx", null, HttpMethod.POST.name());
    }


    //REFERER and Origin null
    @Test
    public void refererAndOriginNull() {
        testFilter(null, null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    //REFERER and Origin null
    @Test
    public void refererAndOriginNullAsString() {
        testFilter("null", null, HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());

        testFilter(null, "null", HttpMethod.POST.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    //REFERER and Origin null
    @Test
    public void refererAndOriginNullAsStringAndMethodGET() {
        testFilter("null", null, HttpMethod.GET.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());

        testFilter(null, "null", HttpMethod.GET.name());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }


    @Test
    public void instanceFilter() {
        CsrfFilter csrfFilter = new CsrfFilter();
        assertNotNull(csrfFilter);
    }

    @Test
    public void shouldRequestBeCsrfChecked() {
        //Origin and Referer not initialized
        //Not authenticated
        boolean result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "POST");
        assertFalse(result);

        //Origin and Referer not initialized
        //Not authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "PUT");
        assertFalse(result);

        //Origin and Referer not initialized
        //Not authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "DELETE");
        assertFalse(result);

        //Origin and Referer not initialized
        //Not authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "GET");
        assertFalse(result);

        //Origin and Referer not initialized
        //Not authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "HEAD");
        assertFalse(result);

        //Origin and Referer not initialized
        //Not authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "", "OPTIONS");
        assertFalse(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "GET");
        assertFalse(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "HEAD");
        assertFalse(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "OPTIONS");
        assertFalse(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "POST");
        assertTrue(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "PUT");
        assertTrue(result);

        //Origin and Referer not initialized
        //Authenticated
        result = CsrfFilter.shouldRequestBeCsrfChecked(true, "JSESSIONID=xxxxxxxxxxxx", "DELETE");
        assertTrue(result);
    }

    @Test
    public void getUrlFromOriginReferer() {
        String result = CsrfFilter.getUrl("http://origin.it", null);
        assertNotNull(result);

        result = CsrfFilter.getUrl(null, "http://referer");
        assertNotNull(result);

        result = CsrfFilter.getUrl(null, null);
        assertNull(result);
    }


    private void testFilter(String origin, String referer, String method) {

        CsrfFilter csrfFilter = new CsrfFilter(mockEnvironment);
        MockFilterConfig filterConfig = new MockFilterConfig();
        request.addHeader("Cookie", "JSESSIONID=xxxxxxxxxxxx");
        if (origin != null) {
            request.addHeader("origin", origin);
        }
        if (referer != null) {
            request.addHeader("referer", referer);
        }

        request.setMethod(method);

        try {
            csrfFilter.init(filterConfig);
            csrfFilter.doFilterInternal(request, response, filterChain);
        } catch (ServletException | IOException e) {
            LOGGER.error("Error test --> ", e);
        } finally {
            csrfFilter.destroy();
        }
    }

    //Set enviroments for test
    private void setEnvironments(MockEnvironment mockEnvironment) {
        mockEnvironment.setProperty(SystemConstants.ENTANDO_CSRF_PROTECTION, "basic");
        mockEnvironment.setProperty(SystemConstants.ENTANDO_CSRF_ALLOWED_DOMAINS,
                "http://organization.it,https://organization.it,*.entando.com");
    }

}
