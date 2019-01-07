package org.entando.entando.aps.system.services.digitalexchange;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;

import java.util.*;

public interface DERatingsDAO  {
    List<DERatingsSummary> getAllRatingsSummaries();

    Optional<DERatingsSummary> getComponentRatingsSummary(String componentId);

    void saveOrUpdate(DERating deRating);

    void save(DERating rating);

    Optional<DERating> findById(long i);

    void update(DERating rating);

    void delete(long id);

    List<DERatingsSummary> getRatingSummariesPage(long offset, long pageSize);
}
