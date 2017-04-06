package org.entando.entando.plugins.jacms.aps.system.services.content.helper;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public interface IContentHelper {

	public static String BEAN_NAME = "jacmsContentHelper";

	public Map<String, List<?>> getReferencingObjects(Content content) throws ApsSystemException;

	public Map<String, List<?>> getReferencingObjects(Content content, List<ContentUtilizer> contentUtilizers) throws ApsSystemException;

	public List<ContentUtilizer> getContentUtilizers();

}
