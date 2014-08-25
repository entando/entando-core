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
package com.agiletec.aps.tags;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;

import freemarker.template.SimpleSequence;

/**
 * Pager tag. Please note that the advanced mode of the tag is used when the list to 
 * iterate over is huge. The "offset" attribute lets you to control how the list is iterated
 * by specifying the numeric value of each forward or backward step.
 * @author E.Santoboni
 */
public class PagerTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(PagerTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			Object object = this.pageContext.getAttribute(this.getListName());
			Collection collection = (null != object && object instanceof SimpleSequence) ? ((SimpleSequence) object).toList() : (Collection) object;
			if (collection == null) {
				_logger.error("There is no list in the request");
			} else {
				PagerTagHelper helper = new PagerTagHelper();
				IPagerVO pagerVo = helper.getPagerVO(collection, this.getPagerId(), 
						this.isPagerIdFromFrame(), this.getMax(),  this.isAdvanced(), this.getOffset(), request);
				this.pageContext.setAttribute(this.getObjectName(), pagerVo);
			}
		} catch (Throwable t) {
			_logger.error("Error detected during tag initialization", t);
			//ApsSystemUtils.logThrowable(e, this, "doStartTag");
			throw new JspException("Error detected during tag initialization", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		this._listName = null;
		this._objectName = null;
		this._pagerId = null;
		this._max = 0;
		this._pagerIdFromFrame = false;
		this._advanced = false;
		this._offset = 0;
	}

	public String getListName() {
		return _listName;
	}
	public void setListName(String listName) {
		this._listName = listName;
	}

	public int getMax() {
		return _max;
	}
	public void setMax(int max) {
		this._max = max;
	}

	public String getObjectName() {
		return _objectName;
	}
	public void setObjectName(String objectName) {
		this._objectName = objectName;
	}

	public String getPagerId() {
		return _pagerId;
	}
	public void setPagerId(String pagerId) {
		this._pagerId = pagerId;
	}

	public boolean isPagerIdFromFrame() {
		return _pagerIdFromFrame;
	}
	public void setPagerIdFromFrame(boolean pagerIdFromFrame) {
		this._pagerIdFromFrame = pagerIdFromFrame;
	}

	public boolean isAdvanced() {
		return _advanced;
	}
	public void setAdvanced(boolean advanced) {
		this._advanced = advanced;
	}

	public int getOffset() {
		return _offset;
	}
	public void setOffset(int offset) {
		this._offset = offset;
	}

	private String _listName;
	private String _objectName;
	private String _pagerId;
	private boolean _pagerIdFromFrame;
	private int _max;

	private int _offset;
	private boolean _advanced;

}
