package org.entando.entando.web.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reloadConfiguration")
public class ReloadConfigurationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> reloadConfiguration(HttpServletRequest request) throws Throwable {
        logger.debug("reload configuration: start..");
        ApsWebApplicationUtils.executeSystemRefresh(request);
        logger.debug("reload configuration: done!");
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }
}
