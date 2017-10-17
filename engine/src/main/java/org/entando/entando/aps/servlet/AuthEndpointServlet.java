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
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2ClientDetailManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class AuthEndpointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(AuthEndpointServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OAuthAuthzRequest oauthRequest = null;
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            try {
                oauthRequest = new OAuthAuthzRequest(request);
            } catch (OAuthSystemException e) {
                e.printStackTrace();
            }
            validateClient(oauthRequest, request);

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND);

            if (responseType.equals(ResponseType.CODE.toString())) {
                builder.setCode(oauthIssuerImpl.authorizationCode());
            }
            if (responseType.equals(ResponseType.TOKEN.toString())) {
                builder.setAccessToken(oauthIssuerImpl.accessToken());
                builder.setExpiresIn(3600l);
            }

            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            final OAuthResponse resp = builder.location(redirectURI).buildQueryMessage();
            response.sendRedirect(resp.getLocationUri());


        } catch (OAuthProblemException e) {

            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);

            String redirectUri = e.getRedirectUri();

            if (OAuthUtils.isEmpty(redirectUri)) {

                try {
                    throw OAuthUtils.handleBadContentTypeException("OAuth callback url needs to be provided by client!!!");
                } catch (OAuthProblemException e1) {
                    e1.printStackTrace();
                }
            }
            final OAuthResponse resp;
            try {
                resp = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_FOUND)
                        .error(e)
                        .location(redirectUri).buildQueryMessage();
                response.sendRedirect(resp.getLocationUri());

            } catch (OAuthSystemException e1) {
                e1.printStackTrace();
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void validateClient(final OAuthAuthzRequest oauthRequest, HttpServletRequest request) throws OAuthProblemException, ServletException {
        final IApiOAuth2ClientDetailManager clientDetailManager =
                (IApiOAuth2ClientDetailManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_CONSUMER_MANAGER, request);
        final String clientId = oauthRequest.getClientId();

        try {
            final OAuth2ClientDetail clientDetail = clientDetailManager.getApiOAuth2ClientDetail(clientId);
            if (clientDetail != null) {
                boolean check = clientDetail.getClientId().equals(oauthRequest.getClientId()) &&
                        clientDetail.getRedirectUri().equals(oauthRequest.getRedirectURI());
                if (!check) {
                    throw OAuthUtils.handleOAuthProblemException("Error in the request paramaters clientid and redirecturi");
                }

            }

        } catch (ApsSystemException e) {
            new OAuthSystemException(e);
            throw new ServletException("Error ", e);

        }
    }
}
