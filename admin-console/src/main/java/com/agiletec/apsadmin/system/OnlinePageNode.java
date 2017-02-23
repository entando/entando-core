package com.agiletec.apsadmin.system;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;

public class OnlinePageNode extends DraftPageNode {

	public OnlinePageNode(IPage entity) {
		super(entity);
	}

	protected PageMetadata getPageMetadata() {
		PageMetadata metadata = this.getEntity().getOnlineMetadata();
		return metadata;
	}

}
