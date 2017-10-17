/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.oauth2.api.JAXBApiOAuth2ClientDetail;
import org.entando.entando.aps.system.services.oauth2.event.ApiOAuth2ClientDetailChangedEvent;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class OAuth2ClientDetailManager extends AbstractService implements IApiOAuth2ClientDetailManager {

    private static final Logger _logger = LoggerFactory.getLogger(IApiOAuth2ClientDetailManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    public OAuth2ClientDetail getApiOAuth2ClientDetail(final String clientId) throws ApsSystemException {
        OAuth2ClientDetail apiOAuth2ClientDetail = null;
        try {
            apiOAuth2ClientDetail = this.getApiOAuth2ClientDetailDAO().loadApiOAuth2ClientDetail(clientId);
        } catch (Throwable t) {
            _logger.error("Error loading apiOAuth2ClientDetail with id '{}'", clientId, t);
            throw new ApsSystemException("Error loading apiOAuth2ClientDetail with clientId: " + clientId, t);
        }
        return apiOAuth2ClientDetail;
    }

    @Override
    public List<String> getApiOAuth2ClientDetails() throws ApsSystemException {
        List<String> apiOAuth2ClientDetails = new ArrayList<String>();
        try {
            apiOAuth2ClientDetails = this.getApiOAuth2ClientDetailDAO().loadApiOAuth2ClientDetails();
        } catch (Throwable t) {
            _logger.error("Error loading ApiOAuth2ClientDetail list", t);
            throw new ApsSystemException("Error loading ApiOAuth2ClientDetail ", t);
        }
        return apiOAuth2ClientDetails;
    }

    @Override
    public List<String> searchApiOAuth2ClientDetails(FieldSearchFilter filters[]) throws ApsSystemException {
        List<String> apiOAuth2ClientDetails = new ArrayList<String>();
        try {
            this.getApiOAuth2ClientDetailDAO().searchApiOAuth2ClientDetails(filters);
        } catch (Throwable t) {
            _logger.error("Error searching ApiOAuth2ClientDetails", t);
            throw new ApsSystemException("Error searching ApiOAuth2ClientDetails", t);
        }
        return apiOAuth2ClientDetails;
    }

    @Override
    public void addApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException {
        try {

            this.getApiOAuth2ClientDetailDAO().insertApiOAuth2ClientDetail(apiOAuth2ClientDetail);
            this.notifyApiOAuth2ClientDetailChangedEvent(apiOAuth2ClientDetail, ApiOAuth2ClientDetailChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error adding ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error adding ApiOAuth2ClientDetail", t);
        }
    }

    @Override
    public void updateApiOAuth2ClientDetail(OAuth2ClientDetail apiOAuth2ClientDetail) throws ApsSystemException {
        try {
            this.getApiOAuth2ClientDetailDAO().updateApiOAuth2ClientDetail(apiOAuth2ClientDetail);
            this.notifyApiOAuth2ClientDetailChangedEvent(apiOAuth2ClientDetail, ApiOAuth2ClientDetailChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error updating ApiOAuth2ClientDetail", t);
            throw new ApsSystemException("Error updating ApiOAuth2ClientDetail " + apiOAuth2ClientDetail, t);
        }
    }

    @Override
    public void deleteApiOAuth2ClientDetail(final String clientId) throws ApsSystemException {
        try {
            OAuth2ClientDetail apiOAuth2ClientDetail = this.getApiOAuth2ClientDetail(clientId);
            this.getApiOAuth2ClientDetailDAO().removeApiOAuth2ClientDetail(clientId);
            this.notifyApiOAuth2ClientDetailChangedEvent(apiOAuth2ClientDetail, ApiOAuth2ClientDetailChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error deleting ApiOAuth2ClientDetail with clientId {}", clientId, t);
            throw new ApsSystemException("Error deleting ApiOAuth2ClientDetail with clientId:" + clientId, t);
        }
    }


    /**
     * GET http://localhost:8080/<portal>/api/rs/en/apiOAuth2ClientDetails?
     *
     * @param properties
     * @return
     * @throws Throwable
     */
    public List<JAXBApiOAuth2ClientDetail> getApiOAuth2ClientDetailsForApi(Properties properties) throws Throwable {
        List<JAXBApiOAuth2ClientDetail> list = new ArrayList<JAXBApiOAuth2ClientDetail>();
        List<String> idList = this.getApiOAuth2ClientDetails();
        if (null != idList && !idList.isEmpty()) {
            Iterator<String> apiOAuth2ClientDetailIterator = idList.iterator();
            while (apiOAuth2ClientDetailIterator.hasNext()) {
                String currentid = apiOAuth2ClientDetailIterator.next();
                OAuth2ClientDetail apiOAuth2ClientDetail = this.getApiOAuth2ClientDetail(currentid);
                if (null != apiOAuth2ClientDetail) {
                    list.add(new JAXBApiOAuth2ClientDetail(apiOAuth2ClientDetail));
                }
            }
        }
        return list;
    }

    /**
     * GET http://localhost:8080/<portal>/api/rs/en/apiOAuth2ClientDetail?clientid=""
     *
     * @param properties
     * @return
     * @throws Throwable
     */
    public JAXBApiOAuth2ClientDetail getApiOAuth2ClientDetailForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("clientid");

        JAXBApiOAuth2ClientDetail jaxbApiOAuth2ClientDetail = null;
        OAuth2ClientDetail apiOAuth2ClientDetail = this.getApiOAuth2ClientDetail(idString);
        if (null == apiOAuth2ClientDetail) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "ApiOAuth2ClientDetail with clientid '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbApiOAuth2ClientDetail = new JAXBApiOAuth2ClientDetail(apiOAuth2ClientDetail);
        return jaxbApiOAuth2ClientDetail;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/apiOAuth2ClientDetail
     *
     * @param jaxbApiOAuth2ClientDetail
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addApiOAuth2ClientDetailForApi(JAXBApiOAuth2ClientDetail jaxbApiOAuth2ClientDetail) throws ApiException, ApsSystemException {
        if (null != this.getApiOAuth2ClientDetail(jaxbApiOAuth2ClientDetail.getClientId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "ApiOAuth2ClientDetail with id " + jaxbApiOAuth2ClientDetail.getId() + " already exists", Response.Status.CONFLICT);
        }
        OAuth2ClientDetail apiOAuth2ClientDetail = jaxbApiOAuth2ClientDetail.getApiOAuth2ClientDetail();
        this.addApiOAuth2ClientDetail(apiOAuth2ClientDetail);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/apiOAuth2ClientDetail
     *
     * @param jaxbApiOAuth2ClientDetail
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateApiOAuth2ClientDetailForApi(JAXBApiOAuth2ClientDetail jaxbApiOAuth2ClientDetail) throws ApiException, ApsSystemException {
        if (null == this.getApiOAuth2ClientDetail(jaxbApiOAuth2ClientDetail.getClientId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "ApiOAuth2ClientDetail with id " + jaxbApiOAuth2ClientDetail.getId() + " does not exist", Response.Status.CONFLICT);
        }
        OAuth2ClientDetail apiOAuth2ClientDetail = jaxbApiOAuth2ClientDetail.getApiOAuth2ClientDetail();
        this.updateApiOAuth2ClientDetail(apiOAuth2ClientDetail);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/apiOAuth2ClientDetail?clientid=""
     *
     * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteApiOAuth2ClientDetailForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("clientid");
        this.deleteApiOAuth2ClientDetail(idString);
    }

    private void notifyApiOAuth2ClientDetailChangedEvent(OAuth2ClientDetail apiOAuth2ClientDetail, int operationCode) {
        ApiOAuth2ClientDetailChangedEvent event = new ApiOAuth2ClientDetailChangedEvent();
        event.setApiOAuth2ClientDetail(apiOAuth2ClientDetail);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    protected IKeyGeneratorManager getKeyGeneratorManager() {
        return _keyGeneratorManager;
    }

    public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
        this._keyGeneratorManager = keyGeneratorManager;
    }

    public void setApiOAuth2ClientDetailDAO(IApiOAuth2ClientDetailDAO apiOAuth2ClientDetailDAO) {
        this._apiOAuth2ClientDetailDAO = apiOAuth2ClientDetailDAO;
    }

    protected IApiOAuth2ClientDetailDAO getApiOAuth2ClientDetailDAO() {
        return _apiOAuth2ClientDetailDAO;
    }

    private IKeyGeneratorManager _keyGeneratorManager;
    private IApiOAuth2ClientDetailDAO _apiOAuth2ClientDetailDAO;
}
