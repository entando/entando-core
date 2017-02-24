package com.agiletec.apsadmin.system;

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
