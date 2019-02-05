package org.entando.entando.aps.system.init.util;

import java.io.IOException;
import java.net.URL;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;
import org.entando.entando.aps.system.init.model.Component;

public class ComponentUtils {


    public static Component getEntandoComponent(String componentName) throws IOException, ApsSystemException {
        URL resourceUrl = ComponentUtils.class.getClassLoader().getResource("components/" + componentName + "/component.xml");
        String path = resourceUrl.getPath();
        String xml = FileTextReader.getText(resourceUrl.openStream());
        ComponentDefDOM componentDefDom = new ComponentDefDOM(xml, path);
        return componentDefDom.getComponent(null);

    }


}
