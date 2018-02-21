package org.entando.entando.web.group.validator;

import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.web.group.GroupController;
import org.entando.entando.web.group.model.GroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class GroupValidator implements Validator {

    @Autowired
    private IGroupManager groupManager;

	@Override
	public boolean supports(Class<?> paramClass) {

		return GroupRequest.class.equals(paramClass);
	}

    @Override
    public void validate(Object target, Errors errors) {
		GroupRequest request = (GroupRequest) target;
		String groupName = request.getName();
		if (null != groupManager.getGroup(groupName)) {
            errors.reject(GroupController.ERR_CODE_GROUP_ALREADY_EXISTS, new String[]{groupName}, "group.exists");
		}
	}

}
