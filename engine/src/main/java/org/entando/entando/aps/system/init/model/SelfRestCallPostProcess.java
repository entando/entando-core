/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.init.model;

import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * @author E.Santoboni
 */
public class SelfRestCallPostProcess implements IPostProcess {

	private static final Logger _logger = LoggerFactory.getLogger(SelfRestCallPostProcess.class);
	
	/*
	<selfRestCall langCode="" namespace="jacms" resourceName="contentType" 
			method="POST" expected="202" printresponse="true" failonerror="true|false">
		<query>
			<!--
			<parameter name="param1" value="param1Value" />
			<parameter name="param2" value="param2Value" />
			<parameter name="param3" value="param3Value" />
			-->
		</query>
		<contentBody content-type="application/xml" path="classpath:component/plugins/jacms/postprocess/ANN.xml" />
	</selfRestCall>
	<!--
	<selfRestCall langCode="" namespace="jacms" resourceName="contentType" 
				  method="POST" expected="202" printresponse="true" >
		<query>
			<parameter name="param1" value="param1Value" />
			<parameter name="param2" value="param2Value" />
			<parameter name="param3" value="param3Value" />
		</query>
		<contentBody content-type="application/xml">
			RequestBody.....
		</contentBody>
	</selfRestCall>
	-->
	*/
	
	@Override
	public String getCode() {
		return "selfRestCall";
	}
	
	@Override
	public void createConfig(Element element) {
		try {
			this.setLangCode(element.getAttributeValue("langCode"));
			this.setNamespace(element.getAttributeValue("namespace"));
			this.setResourceName(element.getAttributeValue("resourceName"));
			String methodString = element.getAttributeValue("method");
			if (null != methodString) {
                this.setMethod(Enum.valueOf(ApiMethod.HttpMethod.class, methodString.toUpperCase()));
            } else {
                this.setMethod(ApiMethod.HttpMethod.GET);
            }
			String expectedString = element.getAttributeValue("expected");
			if (null != expectedString) {
				try {
					this.setExpectedResult(Integer.parseInt(expectedString));
				} catch (Exception e) {}
			}
			this.setPrintResponse(Boolean.parseBoolean(element.getAttributeValue("printresponse")));
			String failOnError = element.getAttributeValue("failonerror");
			if (null != failOnError) {
				this.setFailOnError(Boolean.parseBoolean(failOnError));
			}
			Element parametersElement = element.getChild("query");
			if (null != parametersElement) {
				List<Element> parameterElements = parametersElement.getChildren("parameter");
				for (int i = 0; i < parameterElements.size(); i++) {
					Element parameterElement = parameterElements.get(i);
					String name = parameterElement.getAttributeValue("name");
					String value = parameterElement.getAttributeValue("value");
					if (null != name && null != value) {
						this.getQueryParameters().put(name, value);
					}
				}
			}
			Element contentBodyElement = element.getChild("contentBody");
			if (null != contentBodyElement) {
				String contentTypeString = contentBodyElement.getAttributeValue("content-type");
				this.setContentType(MediaType.valueOf(contentTypeString));
				String text = contentBodyElement.getText();
				if (null == text || text.trim().length() == 0) {
					String path = contentBodyElement.getAttributeValue("path");
					if (null != path) {
						this.setContentBodyPath(path);
					}
				}
				if (null != text) {
					this.setContentBody(text);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating Self rest call", t);
			//ApsSystemUtils.logThrowable(t, this, "createConfig");
			throw new RuntimeException("Error creating Self rest call", t);
		}
	}
	
	public String getLangCode() {
		return _langCode;
	}
	protected void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	public String getNamespace() {
		return _namespace;
	}
	protected void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getResourceName() {
		return _resourceName;
	}
	protected void setResourceName(String resourceName) {
		this._resourceName = resourceName;
	}
	
	public ApiMethod.HttpMethod getMethod() {
		return _method;
	}
	protected void setMethod(ApiMethod.HttpMethod method) {
		this._method = method;
	}
	
	public Integer getExpectedResult() {
		return _expectedResult;
	}
	protected void setExpectedResult(Integer expectedResult) {
		this._expectedResult = expectedResult;
	}
	
	public boolean isPrintResponse() {
		return _printResponse;
	}
	protected void setPrintResponse(boolean printResponse) {
		this._printResponse = printResponse;
	}
	
	public boolean isFailOnError() {
		return _failOnError;
	}
	protected void setFailOnError(boolean failOnError) {
		this._failOnError = failOnError;
	}
	
	public Properties getQueryParameters() {
		return _queryParameters;
	}
	protected void setQueryParameters(Properties queryParameters) {
		this._queryParameters = queryParameters;
	}
	
	public MediaType getContentType() {
		return _contentType;
	}
	protected void setContentType(MediaType contentType) {
		this._contentType = contentType;
	}
	
	public String getContentBody() {
		return _contentBody;
	}
	protected void setContentBody(String contentBody) {
		this._contentBody = contentBody;
	}
	
	public String getContentBodyPath() {
		return _contentBodyPath;
	}
	protected void setContentBodyPath(String contentBodyPath) {
		this._contentBodyPath = contentBodyPath;
	}
	
	private String _langCode;
	private String _namespace;
	private String _resourceName;
	private ApiMethod.HttpMethod _method;
	private Integer _expectedResult;
	private boolean _printResponse;
	private boolean _failOnError = true;
	private Properties _queryParameters = new Properties();
	private MediaType _contentType;
	private String _contentBody;
	private String _contentBodyPath;
	
}