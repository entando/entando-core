/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface ApiOAuth2ClientDetailChangedObserver extends ObserverService {
	
	public void updateFromApiOAuth2ClientDetailChanged(ApiOAuth2ClientDetailChangedEvent event);
	
}
