package org.entando.entando.apsadmin.portal.rs.validator;

import org.entando.entando.apsadmin.portal.rs.model.SwapWidgetRequest;

import com.agiletec.apsadmin.portal.PageConfigAction;

public interface ISwapWidgetRequestValidator {
	
	public static String BEAN_NAME = "SwapWidgetRequestValidator";
	
	public void validateRequest(SwapWidgetRequest swapWidgetRequest, PageConfigAction pageConfigAction);
	
}