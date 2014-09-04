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
package com.agiletec.plugins.jacms.apsadmin.content.helper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Interfaccia per gli Helper della ContentAction.
 * @author E.Santoboni
 */
public interface IContentActionHelper extends IEntityActionHelper {

	public List<Group> getAllowedGroups(UserDetails currentUser);

	/**
	 * Controlla le referenziazioni di un contenuto. Verifica la referenziazione di un contenuto con altri contenuti o pagine nel caso
	 * di operazioni di ripubblicazione di contenuti non del gruppo ad accesso libero.
	 * L'operazione si rende necessaria per ovviare a casi nel cui il contenuto, di un particolare gruppo, sia stato
	 * pubblicato precedentemente in una pagina o referenziato in un'altro contenuto grazie alla associazione di questo con
	 * altri gruppi abilitati alla visualizzazione. Il controllo evidenzia quali devono essere i gruppi al quale il contenuto
	 * deve essere necessariamente associato (ed il perchè) per salvaguardare le precedenti relazioni.
	 * @param content Il contenuto da analizzare.
	 * @param action L'action da valorizzare con i messaggi di errore.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void scanReferences(Content content, ActionSupport action) throws ApsSystemException;

	/**
     * Verifica che l'utente corrente possegga
     * i diritti di accesso al contenuto selezionato.
     * @param content Il contenuto.
     * @param currentUser Il contenuto corrente.
     * @return True nel caso che l'utente corrente abbia i permessi
     * di lettura/scrittura sul contenuto, false in caso contrario.
     */
	public boolean isUserAllowed(Content content, UserDetails currentUser);

	public Map getReferencingObjects(Content content, HttpServletRequest request) throws ApsSystemException;

	public EntitySearchFilter getOrderFilter(String groupBy, String lastOrder);

	public ActivityStreamInfo createActivityStreamInfo(Content content, int strutsAction, boolean addLink);

}
