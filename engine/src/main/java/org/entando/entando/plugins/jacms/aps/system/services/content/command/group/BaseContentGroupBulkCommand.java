package org.entando.entando.plugins.jacms.aps.system.services.content.command.group;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.SystemConstants;
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

public abstract class BaseContentGroupBulkCommand extends BaseContentBulkCommand<String> {

	public BaseContentGroupBulkCommand(Collection<String> items, Collection<String> groups, 
			IContentManager manager, BulkCommandTracer<String> tracer, WebApplicationContext wax) {
		super(items, groups, manager, tracer);
		Map<String, ContentUtilizer> contentUtilizers = wax.getBeansOfType(ContentUtilizer.class);
		this.setContentUtilizers(contentUtilizers.values());
		this.setSystemLangs(((ILangManager) wax.getBean(SystemConstants.LANGUAGE_MANAGER)).getLangs());
		this.setPageManager((IPageManager) wax.getBean(SystemConstants.PAGE_MANAGER));
	}

	protected boolean checkContentUtilizers(Content content) throws ApsSystemException {
		Collection<String> groupToRemove = this.getItemProperties();
		if (!content.getMainGroup().equals(groupToRemove) && !Group.FREE_GROUP_NAME.equals(content.getMainGroup()) && 
				!content.getGroups().contains(Group.FREE_GROUP_NAME)) {
			IContentManager contentManager = this.getApplier();
			for (ContentUtilizer contentUtilizer : this.getContentUtilizers()) {
				List<?> utilizers = contentUtilizer.getContentUtilizers(content.getId());
				for (Object utilizer : utilizers) {
					if (contentUtilizer instanceof IContentManager) {
						Content refContent = contentManager.loadContent(utilizer.toString(), true);
						if (!content.getMainGroup().equals(refContent.getMainGroup()) && 
								!content.getGroups().contains(refContent.getMainGroup())) {
							this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_ALLOWED);
							return false;
						}
					} else if (utilizer instanceof IPage) {
						IPage page = (IPage) utilizer;
						if (!CmsPageUtil.isContentPublishableOnPageOnline(content, page)) {
							this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_ALLOWED);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

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
								this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_ALLOWED);
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

	public List<Lang> getSystemLangs() {
		return systemLangs;
	}
	protected void setSystemLangs(List<Lang> systemLangs) {
		this.systemLangs = systemLangs;
	}

	public Collection<ContentUtilizer> getContentUtilizers() {
		return _contentUtilizers;
	}
	protected void setContentUtilizers(Collection<ContentUtilizer> contentUtilizers) {
		this._contentUtilizers = contentUtilizers;
	}

	public IPageManager getPageManager() {
		return pageManager;
	}
	protected void setPageManager(IPageManager pageManager) {
		this.pageManager = pageManager;
	}

	private List<Lang> systemLangs;
	private Collection<ContentUtilizer> _contentUtilizers;
	private IPageManager pageManager;

}
