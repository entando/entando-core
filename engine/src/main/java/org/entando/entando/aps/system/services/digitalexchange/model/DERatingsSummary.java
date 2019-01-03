package org.entando.entando.aps.system.services.digitalexchange.model;

import java.util.Objects;

public class DERatingsSummary {

    private String componentId;
    private int rating;
    private int numberOfInstalls;
    private int numberOfRatings;


    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumberOfInstalls() {
        return numberOfInstalls;
    }

    public void setNumberOfInstalls(int numberOfInstalls) {
        this.numberOfInstalls = numberOfInstalls;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public static DERatingsInfoBuilder builder() {
        return new DERatingsInfoBuilder();
    }


    public static class DERatingsInfoBuilder {
        private String componentId;
        private int rating;
        private int numberOfInstalls;
        private int numberOfRatings;

        public DERatingsInfoBuilder componentId(String componentId) {
            this.componentId = componentId;
            return this;
        }

        public DERatingsInfoBuilder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public DERatingsInfoBuilder numberOfInstalls(int numberOfInstalls) {
            this.numberOfInstalls = numberOfInstalls;
            return this;
        }

        public DERatingsInfoBuilder numberOfRatings(int numberOfRatings) {
            this.numberOfRatings = numberOfRatings;
            return this;
        }

        public DERatingsSummary build() {
            DERatingsSummary deRatingsInfo = new DERatingsSummary();
            deRatingsInfo.setComponentId(componentId);
            deRatingsInfo.setRating(rating);
            deRatingsInfo.setNumberOfInstalls(numberOfInstalls);
            deRatingsInfo.setNumberOfRatings(numberOfRatings);
            return deRatingsInfo;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DERatingsSummary that = (DERatingsSummary) o;
        return rating == that.rating &&
                numberOfInstalls == that.numberOfInstalls &&
                numberOfRatings == that.numberOfRatings &&
                Objects.equals(componentId, that.componentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentId, rating, numberOfInstalls, numberOfRatings);
    }

    @Override
    public String toString() {
        return "DERatingsSummary{" +
                "componentId='" + componentId + '\'' +
                ", rating=" + rating +
                ", numberOfInstalls=" + numberOfInstalls +
                ", numberOfRatings=" + numberOfRatings +
                '}';
    }
}
