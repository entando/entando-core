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
package com.agiletec.apsadmin.tags.util;

import org.apache.struts2.components.Component;

import com.agiletec.aps.tags.util.IPagerVO;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Componente specifico per il paginatore dell'interfaccia di amministrazione.
 * @author E.Santoboni
 */
public class ComponentPagerVO extends Component implements IPagerVO {
	
	public ComponentPagerVO(ValueStack stack) {
		super(stack);
	}
	
	public void initPager(IPagerVO pagerVo) {
		this._begin = pagerVo.getBegin();
		this._currItem = pagerVo.getCurrItem();
		this._end = pagerVo.getEnd();
		this._max = pagerVo.getMax();
		this._maxItem = pagerVo.getMaxItem();
		this._nextItem = pagerVo.getNextItem();
		this._prevItem = pagerVo.getPrevItem();
		this._size = pagerVo.getSize();
		this._advanced = pagerVo.isAdvanced();
		this._offset = pagerVo.getOffset();
		this._pagerId = pagerVo.getPagerId();
		this._beginItemAnchor = pagerVo.getBeginItemAnchor();
		this._endItemAnchor = pagerVo.getEndItemAnchor();
	}
	
	/**
	 * Array di utilità; restituisce l'array ordinato degli indici numerici degli item.
	 * @return L'array ordinato degli indici numerici degli item.
	 */
	@Override
	public int[] getItems() {
		int[] items = new int[this.getMaxItem()];
		for (int i = 0; i<this.getMaxItem(); i++) {
			items[i] = i+1;
		}
		return items;
	}
	
	/**
	 * Costruisce e restituisce il nome del parametro tramite il quale 
	 * individuare dalla request l'identificativo del item richiesto.
	 * Il metodo viene richiamato all'interno della jsp che genera il paginatore.
	 * @return Il nome del parametro tramite il quale 
	 * individuare dalla request l'identificativo del item richiesto.
	 */
	@Override
	public String getParamItemName() {
		return "item";
	}
	
	/**
	 * Restituisce il numero massimo di elementi della lista per ogni item.
	 * @return Il numero massimo di elementi della lista per ogni item.
	 */
	@Override
	public int getMax() {
		return _max;
	}

	/**
	 * Restituisce l'identificativo numerico del gruppo item precedente.
	 * @return L'identificativo numerico del gruppo item precedente.
	 */
	@Override
	public int getPrevItem() {
		return _prevItem;
	}

	/**
	 * Restituisce il size della lista principale.
	 * @return Il size della lista principale.
	 */
	@Override
	public int getSize() {
		return _size;
	}

	/**
	 * Restituisce l'identificativo numerico del gruppo item successivo.
	 * @return L'identificativo numerico del gruppo item successivo.
	 */
	@Override
	public int getNextItem() {
		return _nextItem;
	}

	/**
	 * Restituisce l'identificativo numerico del gruppo item corrente.
	 * @return L'identificativo numerico del gruppo item corrente.
	 */
	@Override
	public int getCurrItem() {
		return _currItem;
	}

	/**
	 * Restituisce l'indice di partenza sulla lista principale dell'item corrente.
	 * @return L'indice di partenza sulla lista principale dell'item corrente.
	 */
	@Override
	public int getBegin() {
		return _begin;
	}
	
	/**
	 * Restituisce l'indice di arrivo sulla lista principale dell'item corrente.
	 * @return L'indice di arrivo sulla lista principale dell'item corrente.
	 */
	@Override
	public int getEnd() {
		return _end;
	}

	/**
	 * Restituisce l'identificativo numerico dell'ultimo gruppo iter.
	 * @return L'identificativo numerico dell'ultimo gruppo item.
	 */
	@Override
	public int getMaxItem() {
		return _maxItem;
	}
	
	@Override
	public String getPagerId() {
		return _pagerId;
	}
	
	@Override
	public int getBeginItemAnchor() {
		return _beginItemAnchor;
	}
	
	@Override
	public int getEndItemAnchor() {
		return _endItemAnchor;
	}
	
	@Override
	public boolean isAdvanced() {
		return _advanced;
	}
	
	@Override
	public int getOffset() {
		return _offset;
	}
	
	private int _prevItem;
	private int _currItem;
	private int _nextItem;
	private int _maxItem;
	private int _begin;
	private int _end;
	private int _size;
	private int _max;
	
	private String _pagerId;
	private int _offset;
	private int _beginItemAnchor;
	private int _endItemAnchor;
	private boolean _advanced;
	
}
