/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.IReferenceableAttribute;
import org.entando.entando.plugins.jacms.aps.system.services.page.CmsPageManagerWrapper;

/**
 * @author E.Santoboni - M.E.Minnai
 */
public class ContentInspectionAction extends AbstractContentAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(ContentFinderAction.class);
	
	public List<IPage> getReferencingPages() {
		List<IPage> referencingPages;
		try {
			referencingPages = ((ContentUtilizer) this.getPageManagerWrapper()).getContentUtilizers(this.getContentId());
		} catch (Throwable t) {
			_logger.error("Error getting referencing pages by content {}", this.getContentId(), t);
			String msg = "Error getting referencing pages by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencingPages;
	}
	
	/**
	 * Return the list of the id of the referencing contents
	 * @return The list of content id.
	 */
	public List<String> getReferencingContentsId() {
		List<String> referencingContentsId = null;
		try {
			referencingContentsId = ((ContentUtilizer) this.getContentManager()).getContentUtilizers(this.getContentId());
		} catch (Throwable t) {
			_logger.error("Error getting referencing contents id by content {}", this.getContentId(), t);
			String msg = "Error getting referencing contents id by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencingContentsId;
	}
	
	/**
	 * Return the list of the referencing contents.
	 * @return the list of the referencing contents.
	 * @deprecated use getReferencingContentsId() method
	 */
	public List<ContentRecordVO> getReferencingContents() {
		List<ContentRecordVO> referencingContents = new ArrayList<ContentRecordVO>();
		try {
			List<String> referencingContentsId = this.getReferencingContentsId();
			if (null != referencingContentsId) {
				for (int i = 0; i < referencingContentsId.size(); i++) {
					ContentRecordVO currentReferencedContent = this.getContentManager().loadContentVO(referencingContentsId.get(i));
					referencingContents.add(currentReferencedContent);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting referencing contents by content {}", this.getContentId(), t);
			String msg = "Error getting referencing contents by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencingContents;
	}
	
	/**
	 * Return the list of the id of the referenced contents
	 * @return The list of content id.
	 */
	public List<String> getReferencedContentsId() {
		List<String> referencedContentsId = new ArrayList<String>();
		try {
			Content content = this.getContent();
			if (null == content) return referencedContentsId;
			EntityAttributeIterator attributeIter = new EntityAttributeIterator(content);
			while (attributeIter.hasNext()) {
				AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
				if (currAttribute instanceof IReferenceableAttribute) {
					IReferenceableAttribute cmsAttribute = (IReferenceableAttribute) currAttribute;
					List<CmsAttributeReference> refs = cmsAttribute.getReferences(this.getLangs());
					for (int scanRefs = 0; scanRefs < refs.size(); scanRefs++) {
						CmsAttributeReference ref = refs.get(scanRefs);
						String contentId = ref.getRefContent();
						if (null == contentId) continue; 
						if (!referencedContentsId.contains(contentId)) referencedContentsId.add(contentId);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting referenced contents id by content {}", this.getContentId(), t);
			String msg = "Error getting referenced contents id by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencedContentsId;
	}
	
	/**
	 * Return the list of the referenced contents.
	 * @return the list of the referenced contents.
	 * @deprecated use getReferencedContentsId() method
	 */
	public List<ContentRecordVO> getReferencedContents() {
		List<ContentRecordVO> referencedContents = new ArrayList<ContentRecordVO>();
		try {
			List<String> referencedContentsId = this.getReferencedContentsId();
			if (null != referencedContentsId) {
				for (int i = 0; i < referencedContentsId.size(); i++) {
					ContentRecordVO currentReferencedContent = this.getContentManager().loadContentVO(referencedContentsId.get(i));
					referencedContents.add(currentReferencedContent);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting referenced contents by content {}", this.getContentId(), t);
			String msg = "Error getting referenced contents by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencedContents;
	}
	
	public List<IPage> getReferencedPages() {
		List<IPage> referencedPages = new ArrayList<IPage>();
		try {
			Content content = this.getContent();
			if (null == content) return referencedPages;
			EntityAttributeIterator attributeIter = new EntityAttributeIterator(content);
			while (attributeIter.hasNext()) {
				AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
				if (currAttribute instanceof IReferenceableAttribute) {
					IReferenceableAttribute cmsAttribute = (IReferenceableAttribute) currAttribute;
					List<CmsAttributeReference> refs = cmsAttribute.getReferences(this.getLangs());
					for (int scanRefs = 0; scanRefs < refs.size(); scanRefs++) {
						CmsAttributeReference ref = refs.get(scanRefs);
						if (null == ref.getRefPage()) continue;
						IPage page = this.getPageManager().getPage(ref.getRefPage());
						if (null == page) continue; 
						if (!referencedPages.contains(page)) {
							referencedPages.add(page);
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting referenced pages by content {}", this.getContentId(), t);
			String msg = "Error getting referenced pages by content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return referencedPages;
	}
	
	@Override
	public Content getContent() {
		if (this._content != null) return this._content;
		try {
			this._content = this.getContentManager().loadContent(this.getContentId(), this.isCurrentPublicVersion());
		} catch (Throwable t) {
			_logger.error("Error getting the content {}", this.getContentId(), t);
			String msg = "Error getting the content " + this.getContentId();
			throw new RuntimeException(msg, t);
		}
		return _content;
	}
	
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	public String getContentId() {
		return _contentId;
	}
	
	public boolean isCurrentPublicVersion() {
		return _currentPublicVersion;
	}
	public void setCurrentPublicVersion(boolean currentPublicVersion) {
		this._currentPublicVersion = currentPublicVersion;
	}
	
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	public IPageManager getPageManager() {
		return _pageManager;
	}

	protected CmsPageManagerWrapper getPageManagerWrapper() {
		return _pageManagerWrapper;
	}
	public void setPageManagerWrapper(CmsPageManagerWrapper pageManagerWrapper) {
		this._pageManagerWrapper = pageManagerWrapper;
	}
	
	private String _contentId;
	private boolean _currentPublicVersion;
	private Content _content;
	
	private IPageManager _pageManager;
	private CmsPageManagerWrapper _pageManagerWrapper;
	
}