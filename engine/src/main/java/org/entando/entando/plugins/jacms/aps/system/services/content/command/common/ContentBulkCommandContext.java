package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.context.BaseBulkCommandContext;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.services.user.UserDetails;

public class ContentBulkCommandContext extends BaseBulkCommandContext<String> {

	public ContentBulkCommandContext(Collection<String> items, UserDetails currentUser, BulkCommandTracer<String> tracer) {
		super(items, tracer);
		this.setCurrentUser(currentUser);
	}

	public UserDetails getCurrentUser() {
		return _currentUser;
	}
	public void setCurrentUser(UserDetails currentUser) {
		this._currentUser = currentUser;
	}

	private UserDetails _currentUser;

}
