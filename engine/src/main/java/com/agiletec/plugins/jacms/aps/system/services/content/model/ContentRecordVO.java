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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * Oggetto di utilita' per i contenuti. Ha la duplice funzione di rendere disponibili
 * i contenuti da essere visualizzati (sotto forma di lista) nell'interfaccia di
 * redazione, e di fare da tramite (tra dati estratti da db e parser) nel caricamento
 * di un contenuto completo.
 * @author E.Santoboni
 */
public class ContentRecordVO extends ApsEntityRecord {
	
	/**
	 * Restituisce la descrizione del contenuto.
	 * @return La descrizione del contenuto.
	 */
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Setta la descrizione del contenuto.
	 * @param descr La descrizione del contenuto.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}

	/**
	 * Restituisce lo stato del contenuto (in stato work).
	 * @return Lo stato del contenuto.
	 */
	public String getStatus() {
		return _status;
	}

	/**
	 * Setta lo stato del contenuto (in stato work).
	 * @param status Lo stato del contenuto.
	 */
	public void setStatus(String status) {
		this._status = status;
	}
	
	/**
	 * Restituisce la data di creazione del contenuto.
	 * @return La data di creazione del contenuto.
	 */
	public Date getCreate() {
		return _create;
	}

	/**
	 * Setta la data di creazione del contenuto.
	 * @param create La data di creazione del contenuto.
	 */
	public void setCreate(Date create) {
		this._create = create;
	}

	/**
	 * Restituisce la data di ultima modifica del contenuto.
	 * @return La data di ultima modifica del contenuto.
	 */
	public Date getModify() {
		return _modify;
	}

	/**
	 * Setta la data di ultima modifica del contenuto.
	 * @param modify La data di ultima modifica del contenuto.
	 */
	public void setModify(Date modify) {
		this._modify = modify;
	}

	/**
	 * Restituisce la stringa relativa all'xml del contenuto in stato Work.
	 * @return L'xml del contenuto in stato Work.
	 */
	public String getXmlWork() {
		return super.getXml();
	}
	
	/**
	 * Setta la stringa relativa all'xml del contenuto in stato Work.
	 * @param xmlWork L'xml del contenuto in stato Work.
	 */
	public void setXmlWork(String xmlWork) {
		super.setXml(xmlWork);
	}
	
	/**
	 * Restituisce il buleano indicante l'esistenza del contenuto in stato OnLine.
	 * @return True se esiste il contenuto in stato OnLine, false in caso contrario.
	 */
	public boolean isOnLine() {
		return _onLine;
	}
	
	/**
	 * Setta il buleano indicante l'esistenza del contenuto in stato OnLine.
	 * @param onLine True se esiste il contenuto in stato OnLine, false in caso contrario.
	 */
	public void setOnLine(boolean onLine) {
		this._onLine = onLine;
	}
	
	/**
	 * Restituisce il buleano caratterizzante il sincronismo tra il contenuto in
	 * stato work ed il contenuto in stato OnLine.
	 * Restituisce true se la stringa relativa all'xml del contenuto in stato OnLine
	 * è identica al quella del contenuto in stato work, false in caso contrario.
	 * @return Returns the sync.
	 */
	public boolean isSync() {
		return _sync;
	}
	
	/**
	 * Setta il buleano caratterizzante il sincronismo tra il contenuto in
	 * stato work ed il contenuto in stato OnLine.
	 * Setta true se la stringa relativa all'xml del contenuto in stato OnLine
	 * è identica al quella del contenuto in stato work, false in caso contrario.
	 * @param sync The sync to set.
	 */
	public void setSync(boolean sync) {
		this._sync = sync;
	}
	
	/**
	 * Restituisce la stringa relativa all'xml del contenuto in stato OnLine.
	 * @return L'xml del contenuto in stato OnLine.
	 */
	public String getXmlOnLine() {
		return _xmlOnLine;
	}

	/**
	 * Setta la stringa relativa all'xml del contenuto in stato OnLine.
	 * @param xmlOnLine L'xml del contenuto in stato OnLine.
	 */
	public void setXmlOnLine(String xmlOnLine) {
		this._xmlOnLine = xmlOnLine;
	}
	
	/**
	 * Return the code of owner group.
	 * @return The code of owner group.
	 */
	public String getMainGroupCode() {
		return _mainGroupCode;
	}
	
	/**
	 * Set the code of owner group.
	 * @param mainGroupCode The code of owner group.
	 */
	public void setMainGroupCode(String mainGroupCode) {
		this._mainGroupCode = mainGroupCode;
	}
	
	public String getVersion() {
		return _version;
	}
	public void setVersion(String version) {
		this._version = version;
	}
	
	public String getLastEditor() {
		return _lastEditor;
	}
	public void setLastEditor(String lastEditor) {
		this._lastEditor = lastEditor;
	}
	
	private String _descr;
	private String _status;
	private Date _create;
	private Date _modify;
	private boolean _onLine;
	private boolean _sync;
	private String _xmlOnLine;
	
	private String _mainGroupCode;
	
	private String _version;
	private String _lastEditor;
	
}