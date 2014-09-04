/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.hypertext;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;

/**
 * Classe action delegata alla gestione dei jAPSLinks (link interni al testo degli attributi Hypertext) su contenuto.
 * @author E.Santoboni
 */
public class ContentLinkAttributeAction extends ContentFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentLinkAttributeAction.class);
	
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
			result = this.getContentManager().loadPublicContentsId(null, this.getFilters(), allowedGroups);
		} catch (Throwable t) {
			_logger.error("error in getContents", t);
			//ApsSystemUtils.logThrowable(t, this, "getContents");
			throw new RuntimeException("error in getContents", t);
		}
		return result;
	}
	
	/**
	 * Sovrascrittura del metodo della {@link ContentFinderAction}.
	 * Il metodo fà in modo di ricercare i contenuti che hanno, come gruppo proprietario o gruppo abilitato alla visualizzazione, 
	 * o il gruppo "free" o il gruppo proprietario del contenuto in redazione.
	 */
	@Override
	protected List<String> getContentGroupCodes() {
		List<String> allowedGroups = new ArrayList<String>();
		allowedGroups.add(Group.FREE_GROUP_NAME);
		Content currentContent = this.getContent();
		allowedGroups.add(currentContent.getMainGroup());
		return allowedGroups;
	}
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	private String _contentOnSessionMarker;
	
}