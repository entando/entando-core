package com.agiletec.apsadmin.system;

import org.apache.commons.lang3.ArrayUtils;

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;

public class DraftPageNode extends TreeNode {

	public DraftPageNode(IPage entity) {
		this.setEntity(entity);
	}

	@Override
	public ApsProperties getTitles() {
		PageMetadata metadata = this.getPageMetadata();
		if (null != metadata) {
			return metadata.getTitles();
		}
		return null;
	}

	@Override
	public String getTitle(String langCode) {
		PageMetadata metadata = this.getPageMetadata();
		if (null != metadata ) {
			if (null != metadata.getTitles()) {
				return metadata.getTitles().getProperty(langCode);
			}
		}
		return null;
	}


	protected PageMetadata getPageMetadata() {
		PageMetadata metadata = this.getEntity().getDraftMetadata();
		return metadata;
	}

	public IPage getEntity() {
		return entity;
	}

	public void setEntity(IPage entity) {
		this.entity = entity;
	}

	private IPage entity;
}
