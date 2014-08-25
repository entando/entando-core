/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.portal.PageTreeAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

/**
 * Classe action delegata alla gestione dei jAPSLinks (link interni al testo degli attributi Hypertext) su pagina.
 * @author E.Santoboni
 */
public class PageLinkAttributeAction extends PageTreeAction {

	@Override
	protected Collection<String> getNodeGroupCodes() {
		Set<String> groupCodes = new HashSet<String>();
		groupCodes.add(Group.FREE_GROUP_NAME);
		Content currentContent = this.getContent();
		if (null != currentContent.getMainGroup()) {
			groupCodes.add(currentContent.getMainGroup());
		}
		return groupCodes;
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