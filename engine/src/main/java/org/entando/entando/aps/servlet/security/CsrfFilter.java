package org.entando.entando.aps.servlet.security;

import com.agiletec.aps.system.SystemConstants;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.entando.entando.aps.system.exception.CSRFProtectionException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfFilter extends OncePerRequestFilter {


    private static final String JOLLY_CHARACTER = "*.";
    private Environment env;

    public CsrfFilter() {
    }

    public CsrfFilter(Environment env) {
        this.env = env;
    }

    public String getEnv(String key) {
        String activation = System.getenv(key);
        if (null != activation) {
            return activation;
        }
        return (this.env != null) ? env.getProperty(key) : null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isCsfrProtectionActive = SystemConstants.CSRF_BASIC_PROTECTION
                .equalsIgnoreCase(getEnv(SystemConstants.ENTANDO_CSRF_PROTECTION));

        String origin = req.getHeader(SystemConstants.ORIGIN);
        String referer = req.getHeader(SystemConstants.REFERER);

        String url = getUrl(origin, referer);

        String headerCookie = req.getHeader(SystemConstants.COOKIE);
        String method = req.getMethod();

        if("null".equals(origin) || "null".equals(referer)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (shouldRequestBeCsrfChecked(isCsfrProtectionActive, headerCookie, method)) {

            if (url != null && checkUrlInWhiteList(url)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(req, response);
    }

    public static String getUrl(String origin, String referer) {

        String url;

        if (origin != null && !origin.equals("")) {
            url = origin.trim();
        } else if (referer != null && !referer.equals("")) {
            url = referer.trim();
        } else {
            url = null;
        }

        return url;
    }

    public static boolean shouldRequestBeCsrfChecked(boolean isCsfrProtectionActive, String cookieHeader,String method) {

        if (!isCsfrProtectionActive) {
            return false;
        }

        boolean isAuthenticated = cookieHeader != null && cookieHeader.contains(SystemConstants.JSESSIONID);

        if (isAuthenticated) {
            return !isSafeVerbs(method);
        } else {
            return false;
        }
    }

    private boolean checkUrlInWhiteList(String url) {
        try {
            URI uri = new URI(url);
            url = uri.getScheme().concat("://").concat(uri.getHost());
        } catch (Exception e) {
            throw new CSRFProtectionException("URISyntaxException --> ", e);
        }
        String finalUrl = url;
        return this.getWhiteList().stream().anyMatch(domain -> domain.equals(finalUrl))
                ||
                this.getSubDomainFromWildCard().stream().anyMatch(url::endsWith);
    }

    private List<String> getWhiteList() {
        return getDomais(this.getEnv(SystemConstants.ENTANDO_CSRF_ALLOWED_DOMAINS))
                .stream()
                .filter(rs -> !rs.startsWith(JOLLY_CHARACTER))
                .collect(Collectors.toList());
    }

    private List<String> getSubDomainFromWildCard() {
        return getDomais(this.getEnv(SystemConstants.ENTANDO_CSRF_ALLOWED_DOMAINS))
                .stream()
                .filter(rs -> rs.startsWith(JOLLY_CHARACTER))
                .map(rs -> rs.replace("*.", "").trim())
                .collect(Collectors.toList());
    }

    private List<String> getDomais(String allowedDomainsString) {
        return Arrays.asList(allowedDomainsString.split(SystemConstants.SEPARATOR_DOMAINS));
    }

    private static boolean isSafeVerbs(String method) {
        return HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method) || HttpMethod.OPTIONS.matches(method);
    }

}
