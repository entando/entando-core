/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import java.util.List;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.oauth2.model.ApiOAuth2ClientDetail;

public interface IApiOAuth2ClientDetailDAO {

	List<Integer> searchApiOAuth2ClientDetails(FieldSearchFilter[] filters);
	
	ApiOAuth2ClientDetail loadApiOAuth2ClientDetail(int id);

	List<Integer> loadApiOAuth2ClientDetails();

	void removeApiOAuth2ClientDetail(int id);
	
	void updateApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail);

	void insertApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail);
	

}