package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;

import java.util.*;

public interface DERatingsService {
    List<DERatingsSummary> getAllRatingsSummaries();

    List<DERatingsSummary> getRatingSummariesPage(long offset, long pageSize);

    Optional<DERatingsSummary> getComponentRatingsSummary(String componentId);

    Optional<DERatingsSummary> addRating(DERating deRatingUpdate);
}
