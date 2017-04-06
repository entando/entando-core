package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.ArrayList;
import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.JoinCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.RemoveCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.JoinGroupBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.RemoveGroupBulkCommand;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.user.UserDetails;

public class TestContentBulkCommand extends BaseTestCase {
	
	public void testGroupCommands() {
		Collection<String> items = new ArrayList<String>();
		Collection<String> groups = new ArrayList<String>();
		UserDetails currentUser = null;
		BaseContentPropertyBulkCommand<String> groupCommand = this.initGroupsCommand(JoinGroupBulkCommand.BEAN_NAME, items, groups, currentUser);
		assertNotNull(groupCommand);

		groupCommand = this.initGroupsCommand(RemoveGroupBulkCommand.BEAN_NAME, items, groups, currentUser);
		assertNotNull(groupCommand);
	}
	
	public void testCategoryCommands() {
		Collection<String> items = new ArrayList<String>();
		Collection<Category> categories = new ArrayList<Category>();
		UserDetails currentUser = null;
		BaseContentPropertyBulkCommand<Category> categoryCommand = this.initCategoriesCommand(JoinCategoryBulkCommand.BEAN_NAME, items, categories, currentUser);
		assertNotNull(categoryCommand);

		categoryCommand = this.initCategoriesCommand(RemoveCategoryBulkCommand.BEAN_NAME, items, categories, currentUser);
		assertNotNull(categoryCommand);
	}

	private BaseContentPropertyBulkCommand<Category> initCategoriesCommand(String commandBeanName, 
			Collection<String> items, Collection<Category> categories, UserDetails currentUser) {
		ApplicationContext applicationContext = this.getApplicationContext();
		BaseContentPropertyBulkCommand<Category> command = (BaseContentPropertyBulkCommand<Category>) applicationContext.getBean(commandBeanName);
		ContentPropertyBulkCommandContext<Category> context = new ContentPropertyBulkCommandContext<Category>(items, 
				categories, currentUser, new DefaultBulkCommandTracer<String>());
		command.init(context);
		return command;
	}

	private BaseContentPropertyBulkCommand<String> initGroupsCommand(String commandBeanName, 
			Collection<String> items, Collection<String> groups, UserDetails currentUser) {
		ApplicationContext applicationContext = this.getApplicationContext();
		BaseContentPropertyBulkCommand<String> command = (BaseContentPropertyBulkCommand<String>) applicationContext.getBean(commandBeanName);
		ContentPropertyBulkCommandContext<String> context = new ContentPropertyBulkCommandContext<String>(items, 
				groups, currentUser, new DefaultBulkCommandTracer<String>());
		command.init(context);
		return command;
	}
	
}
