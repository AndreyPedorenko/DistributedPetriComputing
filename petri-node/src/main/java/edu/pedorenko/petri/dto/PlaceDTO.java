package edu.pedorenko.petri.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class PlaceDTO {

    @Positive
    private long placeId;

    @NotNull
    @NotEmpty
    private String placeName;

    @PositiveOrZero
    private int marking;

    private boolean collectStatistics;

    public PlaceDTO(
            @Positive long placeId,
            @NotNull @NotEmpty String placeName,
            @PositiveOrZero int marking,
            boolean collectStatistics) {

        this.placeId = placeId;
        this.placeName = placeName;
        this.marking = marking;
        this.collectStatistics = collectStatistics;
    }

    public PlaceDTO() {
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getMarking() {
        return marking;
    }

    public void setMarking(int marking) {
        this.marking = marking;
    }

    public boolean isCollectStatistics() {
        return collectStatistics;
    }

    public void setCollectStatistics(boolean collectStatistics) {
        this.collectStatistics = collectStatistics;
    }
}
