package org.entando.entando.web.digitalexchange.ratings;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.aps.system.services.digitalexchange.ratings.DERatingsService;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class DERatingsController implements DERatingsResource {

    private final DERatingsService ratingsService;

    @Autowired
    public DERatingsController(DERatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @Override
    public ResponseEntity<PagedRestResponse<DERatingsSummary>> getAllRatings(RestListRequest restListRequest) {
        return ResponseEntity.ok(ratingsService.getAllRatingsSummaries());
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DERatingsSummary>> getComponentRatingSummary(
            @PathVariable String componentId) {

        Optional<DERatingsSummary> maybeComponentRatings =
                ratingsService.getComponentRatingsSummary(componentId);

        return maybeComponentRatings
                .map(rating -> ResponseEntity.ok(new SimpleRestResponse<>(rating)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DERatingsSummary>> addRating(
            @Valid @RequestBody DERating deRatingUpdate, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        Optional<DERatingsSummary> maybeComponentRatings =
                ratingsService.addRating(deRatingUpdate);

        return maybeComponentRatings
                .map(rating -> ResponseEntity.ok(new SimpleRestResponse<>(rating)))
                .orElse(ResponseEntity.notFound().build());
    }
}
