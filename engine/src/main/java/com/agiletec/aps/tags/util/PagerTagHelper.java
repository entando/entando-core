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
package com.agiletec.aps.tags.util;

import java.util.Collection;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

/**
 * Classe helper per il tag PagerTag.
 * @author E.Santoboni
 */
public class PagerTagHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PagerTagHelper.class);
	
	/**
	 * Restituisce l'oggetto necessario per fornire gli elementi necessari 
	 * a determinare l'item corrente.
	 * @param collection La collection degli elementi da paginare.
	 * @param pagerId L'identificativo (specificato nel tag) del paginatore. Può essere null.
	 * @param pagerIdFromFrame Determina se ricavare l'identificativo dall'id del frame 
	 * dove è inserito il paginatore. 
	 * @param max Il numero massimo (specificato nel tag) di elementi per item. 
	 * Nel caso che sia 0 (o non sia stato specificato nel tag) il valore 
	 * viene ricercato nei parametri di configurazione della showlet. 
	 * @param isAdvanced Specifica se il pginatore è in modalità avanzata.
	 * @param offset Campo offset, considerato solo nel caso di paginatore avanzato.
	 * @param request La request.
	 * @return L'oggetto necessario per fornire gli elementi necessari a determinare l'item corrente.
	 * @throws ApsSystemException In caso di errori nella costruzione dell'oggetto richiesto.
	 */
	public IPagerVO getPagerVO(Collection collection, String pagerId, boolean pagerIdFromFrame, 
			int max, boolean isAdvanced, int offset, ServletRequest request) throws ApsSystemException {
		IPagerVO pagerVo = null;
		try {
			String truePagerId = this.getPagerId(pagerId, pagerIdFromFrame, request);
			int item = this.getItemNumber(truePagerId, request);
			int maxElement = this.getMaxElementForItem(max, request);
			pagerVo = this.buildPageVO(collection, item, maxElement, truePagerId, isAdvanced, offset);
		} catch (Throwable t) {
			_logger.error("Error while preparing the pagerVo object", t);
			//ApsSystemUtils.logThrowable(t, this, "getPagerVO");
			throw new ApsSystemException("Error while preparing the pagerVo object", t);
		}
		return pagerVo;
	}
	
	protected int getMaxElementForItem(int maxItems, ServletRequest request) {
		if (maxItems == 0) {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			if (reqCtx != null) {
				Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
				ApsProperties config = widget.getConfig();
				String stringMax = (null != config) ? (String) config.get("maxElemForItem") : null;
				if (stringMax != null && stringMax.length() > 0) {
					maxItems = Integer.parseInt(stringMax);
				}
			}
		}
		return maxItems;
	}
	
	private int getItemNumber(String truePagerId, ServletRequest request) {
		String stringItem = null;
		if (null != truePagerId) {
			stringItem = request.getParameter(truePagerId+"_item");
		} else {
			stringItem = request.getParameter("item");
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (NumberFormatException e) {
				_logger.error("Error while parsing the stringItem {}", stringItem, e);
			}
		}
		return item;
	}
	
	private String getPagerId(String pagerId, boolean pagerIdFromFrame, ServletRequest request) {
		String truePagerId = pagerId;
		if (null == truePagerId && pagerIdFromFrame) {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			if (reqCtx != null) {
				int currentFrame = this.getCurrentFrame(reqCtx);
				truePagerId = "frame" + currentFrame;
			}
		}
		return truePagerId;
	}
	
	private int getCurrentFrame(RequestContext reqCtx) {
		Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		int currentFrame = frame.intValue();
		return currentFrame;
	}
	
	protected IPagerVO buildPageVO(Collection object, int item, 
			int maxElement, String pagerId, boolean isAdvanced, int offset) {
		PagerVO pagerVo = new PagerVO();
		int size = 0;
		if (null != object) size = object.size();
		try {
			pagerVo.setPagerId(pagerId);
			pagerVo.setSize(size);
			if (maxElement == 0) {
				maxElement = size;
			}
			pagerVo.setMax(maxElement);
			
			int itemMax;
			if (maxElement == 0) {
				//caso in cui non abbia specificato il max e la lista sia vuota
				itemMax = 0;
			} else {
				//Tutti altri casi
				if (size%maxElement != 0) {
					itemMax = size/maxElement+1;
				} else {
					itemMax = size/maxElement;
				}
			}
			pagerVo.setMaxItem(itemMax);
			
			int currItem = Math.max(1, item);
			currItem = Math.min(currItem, itemMax);
			pagerVo.setCurrItem(currItem);
			
			int to = currItem*maxElement;
			to = Math.min(to, size);
			int end = to - 1;
			if (end < 0) end = 0;
			pagerVo.setEnd(end);
			
			int from = (currItem - 1)*maxElement + 1;
			from = Math.max(from, 1);
			int begin = from - 1;
			pagerVo.setBegin(begin);
			
			int prevItem = currItem - 1;
			prevItem = Math.max(prevItem, 1);
			pagerVo.setPrevItem(prevItem);
			
			int nextItem = currItem + 1;
			nextItem = Math.min(nextItem, itemMax);
			pagerVo.setNextItem(nextItem);
			
			if (offset == 0) {
				offset = DEFAULT_OFFSET;
			}
			pagerVo.setOffset(offset);
			
			int beginItemAnchor = 1;
			int endItemAnchor = itemMax;
			if (isAdvanced) {
				int[] result = this.calcolateItemAnchorBounds(currItem, offset, itemMax);
				beginItemAnchor = result[0];
				endItemAnchor = result[1];
			}
			pagerVo.setAdvanced(isAdvanced);
			pagerVo.setBeginItemAnchor(beginItemAnchor);
			pagerVo.setEndItemAnchor(endItemAnchor);
		} catch (Throwable t) {
			_logger.error("error in buildPageVO", t);
			//ApsSystemUtils.logThrowable(t, this, "buildPageVO");
		}
		return pagerVo;
	}
	
	private int[] calcolateItemAnchorBounds(int currItem, int offset, int itemMax) {
		int[] result = new int[2];
		if (currItem <= offset) {
			result[0] = 1;
		} else {
			result[0] = currItem - offset + 1;
		}
		if (itemMax >= (currItem+offset)) {
			result[1] = (currItem+offset-1);
		} else {
			result[1] = itemMax;
		}
		return result;
	}
	
	public static final int DEFAULT_OFFSET = 10;
	
}
