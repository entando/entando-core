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
        logger.debug("requested {} configuration", pageCode);
        PageConfigurationDto pageConfiguration = this.getPageService().getPageConfiguration(pageCode, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(pageConfiguration, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageWidgets(@PathVariable String pageCode, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        logger.debug("requested {} widgets detail", pageCode);
        PageConfigurationDto pageConfiguration = this.getPageService().getPageConfiguration(pageCode, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(pageConfiguration.getWidgets(), null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets/{frameId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageWidget(@PathVariable String pageCode, @PathVariable int frameId, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        logger.debug("requested widget detail for page {} and frame {}", pageCode, frameId);
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
            @Valid @RequestBody WidgetConfigurationRequest widget,
            BindingResult bindingResult) {
        logger.debug("updating widget configuration in page {} and frame {}", pageCode, frameId);
        WidgetConfigurationDto widgetConfiguration = this.getPageService().updateWidgetConfiguration(pageCode, frameId, widget);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", IPageService.STATUS_DRAFT);
        return new ResponseEntity<>(new RestResponse(widgetConfiguration, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/widgets/{frameId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePageWidget(
            @PathVariable String pageCode,
            @PathVariable int frameId) {
        logger.debug("removing widget configuration in page {} and frame {}", pageCode, frameId);
        this.getPageService().deleteWidgetConfiguration(pageCode, frameId);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", IPageService.STATUS_DRAFT);
        return new ResponseEntity<>(new RestResponse(frameId, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/configuration/restore", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePageConfiguration(@PathVariable String pageCode) {
        logger.debug("restore configuration on page {}", pageCode);
        PageConfigurationDto pageConfiguration = this.getPageService().restorePageConfiguration(pageCode);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(pageConfiguration, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/configuration/defaultWidgets", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> applyDefaultWidgetsPageConfiguration(@PathVariable String pageCode) {
        logger.debug("applying default widgets on page {}", pageCode);
        PageConfigurationDto pageConfiguration = this.getPageService().applyDefaultWidgets(pageCode);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(pageConfiguration, null, metadata), HttpStatus.OK);
    }

}
