package org.entando.entando.web.digitalexchange.ratings;

import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsInfo;
import org.entando.entando.aps.system.services.digitalexchange.ratings.DERatingsService;
import org.entando.entando.web.common.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DERatingsControllerTest {

    @Mock
    private DERatingsService service;

    @InjectMocks
    private DERatingsController controller;


    @Test
    public void getAllRatings() {
        // Setup
        ResponseEntity<PagedRestResponse<DERatingsInfo>> expected =
                createExpectedGetRatingsResponse();

        // Record
        when(service.getAllRatings()).thenReturn(expected.getBody());

        // Play
        RestListRequest request = new RestListRequest();
        ResponseEntity<PagedRestResponse<DERatingsInfo>> ratingsResponse =
                controller.getAllRatings(request);

        // Assert
        assertThat(ratingsResponse).isEqualTo(expected);
    }

    private ResponseEntity<PagedRestResponse<DERatingsInfo>> createExpectedGetRatingsResponse() {
        DERatingsInfo expectedRatingsInfo = createExpectedRatingsInfo();

        List<DERatingsInfo> expectedRatings =
                Collections.singletonList(expectedRatingsInfo);

        PagedRestResponse<DERatingsInfo> expectedResponse = new PagedRestResponse<>();
        expectedResponse.setPayload(expectedRatings);

        return ResponseEntity.ok(expectedResponse);
    }

    private DERatingsInfo createExpectedRatingsInfo() {
        return DERatingsInfo.builder()
                    .componentId("ABC")
                    .numberOfInstalls(10)
                    .numberOfRatings(4)
                    .rating(75)
                    .build();
    }

    @Test
    public void getComponentRatingOK() {
        // Setup
        DERatingsInfo expectedRatingsInfo = createExpectedRatingsInfo();

        // Record
        when(service.getComponentRatings(expectedRatingsInfo.getComponentId()))
                .thenReturn(Optional.of(expectedRatingsInfo));

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsInfo>> componentRatingResponse =
                controller.getComponentRating(expectedRatingsInfo.getComponentId());

        // Assert
        assertThat(componentRatingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(componentRatingResponse.getBody()).isNotNull();
        assertThat(componentRatingResponse.getBody().getPayload()).isNotNull();
        assertThat(componentRatingResponse.getBody().getPayload()).isEqualTo(expectedRatingsInfo);
    }

    @Test
    public void getComponentRatingNotFound() {
        // Setup
        String notFoundComponentId = "NOT_FOUND_COMPONENT";

        // Record
        when(service.getComponentRatings(notFoundComponentId))
                .thenReturn(Optional.empty());

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsInfo>> componentRatingResponse =
                controller.getComponentRating(notFoundComponentId);

        // Assert
        assertThat(componentRatingResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addRatingOK() {
        // Setup
        DERatingUpdate ratingUpdate = createRatingUpdate();

        DERatingsInfo expected = DERatingsInfo.builder()
                .rating(ratingUpdate.getRating())
                .componentId(ratingUpdate.getComponentId())
                .build();

        // Record
        when(service.addRating(ratingUpdate)).thenReturn(Optional.of(expected));

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsInfo>> updateResponse =
                controller.addRating(ratingUpdate);

        // Assert
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getPayload()).isEqualTo(expected);
    }

    private DERatingUpdate createRatingUpdate() {
        DERatingUpdate ratingUpdate = new DERatingUpdate();
        ratingUpdate.setComponentId("ABC");
        ratingUpdate.setUpdaterId("mario");
        ratingUpdate.setRating(50);
        return ratingUpdate;
    }

    @Test
    public void addRatingNotFound() {
        // Setup
        DERatingUpdate ratingUpdate = new DERatingUpdate();
        ratingUpdate.setComponentId("DOES NOT EXIST");

        // Record
        when(service.addRating(ratingUpdate)).thenReturn(Optional.empty());

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsInfo>> updateResponse =
                controller.addRating(ratingUpdate);

        // Assert
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}