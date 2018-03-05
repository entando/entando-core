package org.entando.entando.web.page;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PageConfigurationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageService pageService;

    protected IPageService getPageService() {
        return pageService;
    }

    public void setPageService(IPageService pageService) {
        this.pageService = pageService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/configuration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageConfiguration(@PathVariable String pageCode, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        PageConfigurationDto pageConfiguration = this.getPageService().getPageConfiguration(pageCode, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(pageConfiguration, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageWidgets(@PathVariable String pageCode, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        PageConfigurationDto pageConfiguration = this.getPageService().getPageConfiguration(pageCode, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(pageConfiguration.getWidgets(), null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets/{frameId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageWidget(@PathVariable String pageCode, @PathVariable int frameId, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        WidgetConfigurationDto widgetConfiguration = this.getPageService().getWidgetConfiguration(pageCode, frameId, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(widgetConfiguration, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets/{frameId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePageWidget(
                                              @PathVariable String pageCode, 
                                              @PathVariable int frameId, 
                                              @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status,
                                              @Valid @RequestBody WidgetConfigurationRequest widget,
                                              BindingResult bindingResult) {
        WidgetConfigurationDto widgetConfiguration = this.getPageService().updateWidgetConfiguration(pageCode, frameId, status, widget);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(widgetConfiguration, null, metadata), HttpStatus.OK);
    }


}
