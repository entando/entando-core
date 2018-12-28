package org.entando.entando.web.digitalexchange.ratings;

import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsInfo;
import org.entando.entando.aps.system.services.digitalexchange.ratings.DERatingsService;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class DERatingsController implements DERatingsResource {

    private final DERatingsService ratingsService;

    @Autowired
    public DERatingsController(DERatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @Override
    public ResponseEntity<PagedRestResponse<DERatingsInfo>> getAllRatings(RestListRequest restListRequest) {
        return ResponseEntity.ok(ratingsService.getAllRatings());
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DERatingsInfo>> getComponentRating(String componentId) {
        Optional<DERatingsInfo> maybeComponentRatings =
                ratingsService.getComponentRatings(componentId);

        return maybeComponentRatings
                .map(rating -> ResponseEntity.ok(new SimpleRestResponse<>(rating)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DERatingsInfo>> addRating(
            @RequestBody DERatingUpdate deRatingUpdate) {
        Optional<DERatingsInfo> maybeComponentRatings =
                ratingsService.addRating(deRatingUpdate);

        return maybeComponentRatings
                .map(rating -> ResponseEntity.ok(new SimpleRestResponse<>(rating)))
                .orElse(ResponseEntity.notFound().build());
    }
}
