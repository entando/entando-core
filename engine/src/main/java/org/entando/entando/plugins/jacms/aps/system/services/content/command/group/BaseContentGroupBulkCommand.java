package org.entando.entando.plugins.jacms.aps.system.services.content.command.group;

import java.util.List;

import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentPropertyBulkCommand;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.IReferenceableAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.SymbolicLinkValidator;

public abstract class BaseContentGroupBulkCommand extends BaseContentPropertyBulkCommand<String> {

	protected boolean checkContentReferences(Content content) {
		List<Lang> systemLangs = this.getSystemLangs();
		IPageManager pageManager = this.getPageManager();
		SymbolicLinkValidator validator = new SymbolicLinkValidator(this.getApplier(), pageManager);
		for (AttributeInterface attribute : content.getAttributeList()) {
			if (attribute instanceof IReferenceableAttribute) {
				List<CmsAttributeReference> references = ((IReferenceableAttribute) attribute).getReferences(systemLangs);
				if (references != null) {
					for (CmsAttributeReference reference : references) {
						SymbolicLink symbolicLink = this.convertToSymbolicLink(reference);
						if (symbolicLink != null) {
							String result = validator.scan(symbolicLink, content);
							if (ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS.equals(result) || ICmsAttributeErrorCodes.INVALID_RESOURCE_GROUPS.equals(result) || 
									ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS.equals(result)) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	protected SymbolicLink convertToSymbolicLink(CmsAttributeReference reference) {
		SymbolicLink symbolicLink = null;
		if (reference != null) {
			String resourceId = reference.getRefResource();
			String contentId = reference.getRefContent();
			String pageCode = reference.getRefPage();
			symbolicLink = new SymbolicLink();
			if (resourceId != null) {
				symbolicLink.setDestinationToResource(resourceId);
			} else if (contentId != null) {
				if (pageCode != null) {
					symbolicLink.setDestinationToContentOnPage(contentId, pageCode);
				} else {
					symbolicLink.setDestinationToContent(contentId);
				}
			} else {
				symbolicLink.setDestinationToPage(pageCode);
			}
		}
		return symbolicLink;
	}

	protected List<Lang> getSystemLangs() {
		return _systemLangs;
	}
	protected void setSystemLangs(List<Lang> systemLangs) {
		this._systemLangs = systemLangs;
	}

	public void setLangManager(ILangManager langManager) {
		this.setSystemLangs(langManager.getLangs());
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private List<Lang> _systemLangs;
	private IPageManager _pageManager;

}
