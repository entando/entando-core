package org.entando.entando.web.digitalexchange.ratings;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable
public class DERating {

    @DatabaseField(dataType = DataType.LONG, canBeNull = false, id = true)
    private long id;

    @DatabaseField(dataType = DataType.STRING, width = 30, canBeNull = false)
    private String componentId;

    @DatabaseField(dataType = DataType.STRING, width = 30, canBeNull = false)
    private String reviewerId;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    private int rating;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DERating rating1 = (DERating) o;
        return id == rating1.id &&
                rating == rating1.rating &&
                Objects.equals(componentId, rating1.componentId) &&
                Objects.equals(reviewerId, rating1.reviewerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, componentId, reviewerId, rating);
    }
}
