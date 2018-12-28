package org.entando.entando.web.digitalexchange.ratings;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.*;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsInfo;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"digital-exchange", "ratings"})
@RequestMapping(value = "/digitalExchange/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
public interface DERatingsResource {

    @ApiOperation(value = "Returns ratings for all components")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping
    ResponseEntity<PagedRestResponse<DERatingsInfo>> getAllRatings(RestListRequest restListRequest);


    @ApiOperation(value = "Get rating for component")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NotFound")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping("/{componentId}")
    ResponseEntity<SimpleRestResponse<DERatingsInfo>> getComponentRating(@PathVariable String componentId);


    @ApiOperation(value = "Add rating for component")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NotFound")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping
    ResponseEntity<SimpleRestResponse<DERatingsInfo>> addRating(@RequestBody DERatingUpdate deRatingUpdate);
}
