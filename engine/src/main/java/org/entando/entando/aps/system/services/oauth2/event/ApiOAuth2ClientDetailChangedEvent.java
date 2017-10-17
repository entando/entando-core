/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;


public class ApiOAuth2ClientDetailChangedEvent extends ApsEvent {

    @Override
    public void notify(IManager srv) {
        ((ApiOAuth2ClientDetailChangedObserver) srv).updateFromApiOAuth2ClientDetailChanged(this);
    }

    @Override
    public Class getObserverInterface() {
        return ApiOAuth2ClientDetailChangedObserver.class;
    }

    public int getOperationCode() {
        return _operationCode;
    }

    public void setOperationCode(int operationCode) {
        this._operationCode = operationCode;
    }

    public OAuth2ClientDetail getApiOAuth2ClientDetail() {
        return _apiOAuth2ClientDetail;
    }

    public void setApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) {
        this._apiOAuth2ClientDetail = apiOAuth2ClientDetail;
    }

    private OAuth2ClientDetail _apiOAuth2ClientDetail;
    private int _operationCode;

    public static final int INSERT_OPERATION_CODE = 1;
    public static final int REMOVE_OPERATION_CODE = 2;
    public static final int UPDATE_OPERATION_CODE = 3;

}
