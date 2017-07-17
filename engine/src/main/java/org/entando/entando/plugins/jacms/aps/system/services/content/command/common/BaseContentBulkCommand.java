/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public abstract class BaseContentBulkCommand<C extends ContentBulkCommandContext> extends BaseBulkCommand<String, IContentManager, C> implements ApplicationContextAware {

	@Override
	protected boolean apply(String item) throws ApsSystemException {
		boolean performed = false;
		Content content = this.getContent(item);
		if (content == null) {
			this.getTracer().traceError(item, ApsCommandErrorCode.NOT_FOUND);
		} else if (!this.isAuthOnContent(content)) {
			this.getTracer().traceError(item, ApsCommandErrorCode.USER_NOT_ALLOWED);
		} else {
			performed = this.apply(content);
		}
		return performed;
	}

	protected boolean isAuthOnContent(Content content) throws ApsSystemException {
		UserDetails user = this.getCurrentUser();
		return user == null || this.getContentAuthHelper().isAuthToEdit(user, content);
	}

	protected boolean checkContentUtilizers(Content content) throws ApsSystemException {
		if (!Group.FREE_GROUP_NAME.equals(content.getMainGroup()) && 
				!content.getGroups().contains(Group.FREE_GROUP_NAME)) {
			IContentManager contentManager = this.getApplier();
			for (ContentUtilizer contentUtilizer : this.getContentUtilizers()) {
				List<?> utilizers = contentUtilizer.getContentUtilizers(content.getId());
				if (null != utilizers) {
					for (Object utilizer : utilizers) {
						if (contentUtilizer instanceof IContentManager) {
							Content refContent = contentManager.loadContent(utilizer.toString(), true);
							if (!content.getMainGroup().equals(refContent.getMainGroup()) && 
									!content.getGroups().contains(refContent.getMainGroup())) {
								return false;
							}
						} else if (utilizer instanceof IPage) {
							IPage page = (IPage) utilizer;
							if (!CmsPageUtil.isContentPublishableOnPageOnline(content, page)) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	protected abstract boolean apply(Content content) throws ApsSystemException;

	protected Content getContent(String id) throws ApsSystemException {
		return this.getApplier().loadContent(id, false);
	}

	protected UserDetails getCurrentUser() {
		return this.getContext().getCurrentUser();
	}

	protected IContentManager getContentManager() {
		return this.getApplier();
	}
	@Override
	public void setApplier(IContentManager applier) {
		super.setApplier(applier);
	}

	protected Collection<ContentUtilizer> getContentUtilizers() {
		if (this._contentUtilizers == null) {
			Map<String, ContentUtilizer> contentUtilizers = this.getApplicationContext().getBeansOfType(ContentUtilizer.class);
			this._contentUtilizers = contentUtilizers.values();
		}
		return _contentUtilizers;
	}
	protected void setContentUtilizers(Collection<ContentUtilizer> contentUtilizers) {
		this._contentUtilizers = contentUtilizers;
	}

	protected IContentAuthorizationHelper getContentAuthHelper() {
		return _contentAuthHelper;
	}
	public void setContentAuthHelper(IContentAuthorizationHelper contentAuthHelper) {
		this._contentAuthHelper = contentAuthHelper;
	}

	public ApplicationContext getApplicationContext() {
		return _applicationContext;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this._applicationContext = applicationContext;
	}

	private Collection<ContentUtilizer> _contentUtilizers;
	private IContentAuthorizationHelper _contentAuthHelper;
	private ApplicationContext _applicationContext;

}
