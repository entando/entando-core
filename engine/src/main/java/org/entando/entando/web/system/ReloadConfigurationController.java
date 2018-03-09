package org.entando.entando.web.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reloadConfiguration")
public class ReloadConfigurationController {

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reloadConfiguration(HttpServletRequest request) throws Throwable {
        ApsWebApplicationUtils.executeSystemRefresh(request);
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        return new ResponseEntity<>(new RestResponse(), HttpStatus.OK);
    }
}
