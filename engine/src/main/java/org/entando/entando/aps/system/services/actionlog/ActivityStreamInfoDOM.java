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
package org.entando.entando.aps.system.services.actionlog;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ActivityStreamInfoDOM {

	private static final Logger _logger = LoggerFactory.getLogger(ActivityStreamInfoDOM.class);
	
	public static String marshalInfo(ActivityStreamInfo activityStreamInfo) throws ApsSystemException {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(ActivityStreamInfo.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(activityStreamInfo, writer);
		} catch (Throwable t) {
			_logger.error("Error binding object", t);
			//ApsSystemUtils.logThrowable(t, ActivityStreamInfoDOM.class, "bindInfo", "Error binding object");
			throw new ApsSystemException("Error binding object", t);
		}
		return writer.toString();
	}
	
	public static ActivityStreamInfo unmarshalInfo(String xml) throws ApsSystemException {
		ActivityStreamInfo bodyObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(ActivityStreamInfo.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
			bodyObject = (ActivityStreamInfo) unmarshaller.unmarshal(is);
		} catch (Throwable t) {
			_logger.error("Error unmarshalling activity stream info config. xml: {}", xml, t);
			//ApsSystemUtils.logThrowable(t, UnmarshalUtils.class, "unmarshalInfo");
			throw new ApsSystemException("Error unmarshalling activity stream info config", t);
		}
		return bodyObject;
	}
	
}