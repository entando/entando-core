package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.entando.entando.aps.system.services.oauth2.IApiOAuthorizationCodeManager;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2ClientDetailManager;
import org.entando.entando.aps.system.services.oauth2.model.AuthorizationCode;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthEndpointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(AuthEndpointServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OAuthAuthzRequest oauthRequest = null;
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        IApiOAuthorizationCodeManager codeManager = (IApiOAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);

        try {

            oauthRequest = new OAuthAuthzRequest(request);

            if (validateClient(oauthRequest, request, response)) {

                //build response according to response_type
                String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) == null ? OAuth.OAUTH_RESPONSE_TYPE : oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
                OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                        .authorizationResponse(request, HttpServletResponse.SC_FOUND);

                final String authorizationCode = oauthIssuerImpl.authorizationCode();
                final Long expires = 3600l;

                AuthorizationCode authCode = new AuthorizationCode();

                authCode.setAuthorizationCode(authorizationCode);
                authCode.setExpires(System.currentTimeMillis() + expires);
                authCode.setClientId(oauthRequest.getClientId());
                authCode.setSource(request.getRemoteAddr());

                codeManager.addAuthorizationCode(authCode);

                if (responseType.equals(ResponseType.CODE.toString())) {
                    builder.setCode(authorizationCode);

                }
                if (responseType.equals(ResponseType.TOKEN.toString())) {
                    builder.setAccessToken(authorizationCode);
                    builder.setExpiresIn(expires);
                }

                String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
                final OAuthResponse resp = builder.location(redirectURI).buildQueryMessage();
                final int status = resp.getResponseStatus();
                response.setStatus(status);
                response.sendRedirect(resp.getLocationUri());
            } else {
                _logger.warn("OAuth2 authentication failed");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

            }

        } catch (OAuthSystemException ex) {
            _logger.error("System exception {} ", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (OAuthProblemException ex) {
            _logger.error("OAuth2 error {} ", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    private boolean validateClient(final OAuthAuthzRequest oauthRequest, HttpServletRequest request, HttpServletResponse response) throws OAuthProblemException, IOException {
        final IApiOAuth2ClientDetailManager clientDetailManager =
                (IApiOAuth2ClientDetailManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_CLIENT_DETAIL_MANAGER, request);
        final String clientId = oauthRequest.getClientId();

        try {
            final OAuth2ClientDetail clientDetail = clientDetailManager.getApiOAuth2ClientDetail(clientId);
            if (clientDetail != null) {

                if (!clientDetail.getClientId().equals(oauthRequest.getClientId())) {
                    throw OAuthUtils.handleOAuthProblemException("Invalid clientId");
                }
                else if (clientDetail.getExpiresIn().getTime() < System.currentTimeMillis() ){
                    throw OAuthUtils.handleOAuthProblemException("ClientId is expired");
                }
                else if (!clientDetail.getRedirectUri().equals(oauthRequest.getRedirectURI()) ){
                    throw OAuthUtils.handleOAuthProblemException("Invalid redirectUri");
                }

                return true;
            }

        } catch (ApsSystemException e) {
            _logger.error("ApsSystemException {}", e.getMessage());
            response.sendError(500);
            return false;

        }
        return false;
    }
}
