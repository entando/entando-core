package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiAuthorizationCodeManager;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2ClientDetailManager;
import org.entando.entando.aps.system.services.oauth2.model.AuthorizationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenEndPointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(TokenEndPointServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OAuthTokenRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthTokenRequest(request);
            this.validateClient(oauthRequest, request, response);
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            final String refreshToken = oauthIssuerImpl.refreshToken();

            System.out.println("** TOKEN: ACCESS " + accessToken + " : REFRESH " + refreshToken);

            OAuthResponse r = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn("3600")
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();

            response.setStatus(r.getResponseStatus());
            PrintWriter pw = response.getWriter();
            pw.print(r.getBody());
            pw.flush();
            pw.close();

        } catch (OAuthProblemException e) {
            OAuthResponse res = null;
            try {
                res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e).buildJSONMessage();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter pw = response.getWriter();
                pw.print(res.getBody());
                pw.flush();
                pw.close();
            } catch (OAuthSystemException e1) {
                e1.printStackTrace();
            }

        } catch (ApsSystemException | OAuthSystemException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private void validateClient(OAuthTokenRequest oauthRequest, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        IApiOAuth2ClientDetailManager clientDetailManager = (IApiOAuth2ClientDetailManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_CLIENT_DETAIL_MANAGER, request);
        IApiAuthorizationCodeManager codeManager = (IApiAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);

        final String clientId = oauthRequest.getClientId();
        final String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
        final String clientSecret = oauthRequest.getClientSecret();


        AuthorizationCode authorizationCode = codeManager.getAuthorizationCode(authCode);
        if (authorizationCode == null) {
            _logger.error("OAuth2 authorizationcode not found");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            //errorHandler(response, OAuthError.TokenResponse.INVALID_GRANT, "authorizationcode not found", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }




        /*if (!clientId.equals(authorizationCode.getClientId())) {
            errorHandler(response, OAuthError.TokenResponse.INVALID_CLIENT, "client_id not found", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/

        //do checking for different grant types
        if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                .equals(GrantType.AUTHORIZATION_CODE.toString())) {

            if (!codeManager.verifyAccess(clientId,clientSecret,clientDetailManager)){
                _logger.error("OAuth2 authentication failed");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

/*
            if (!authCode.equals(authorizationCode.getAuthorizationCode())) {
                errorHandler(response, OAuthError.TokenResponse.INVALID_GRANT, "invalid authorization code", HttpServletResponse.SC_BAD_REQUEST);
                return;

            } else {
                _logger.info("current time {} < auhtorizationcide expires {}", System.currentTimeMillis(), authorizationCode.getExpires());
                boolean check = System.currentTimeMillis() < authorizationCode.getExpires();
                _logger.info("check {}", check);
                if (!check) {
                    errorHandler(response, "Authorization code expires", "The Authoritazion code is expires", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                final String clientSecretDb = clientDetailManager.getApiOAuth2ClientDetail(clientId).getClientSecret();
                if (!clientSecret.equals(clientSecretDb)) {
                    errorHandler(response, OAuthError.TokenResponse.INVALID_GRANT, "clientsecret is wrong", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
*/

        }

    }

    private void errorHandler(HttpServletResponse response, final String error, final String errorDescription, final int httpResponse) throws OAuthSystemException, IOException {
        OAuthResponse res = OAuthASResponse.errorResponse(httpResponse)
                .setError(error).setErrorDescription(errorDescription)
                .buildJSONMessage();
        response.setStatus(httpResponse);
        PrintWriter pw = response.getWriter();
        pw.print(res.getBody());
        pw.flush();
        pw.close();
    }
}
