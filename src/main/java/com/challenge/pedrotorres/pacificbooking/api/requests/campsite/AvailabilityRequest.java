package com.challenge.pedrotorres.pacificbooking.api.requests.campsite;

import com.challenge.pedrotorres.pacificbooking.domain.campsite.Site;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject(generateConverter = true)
public class AvailabilityRequest {

    private Site site;
    private String startDate;
    private String endDate;

    public AvailabilityRequest() {

    }

    public AvailabilityRequest(JsonObject jsonObject) {
        AvailabilityRequestConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        AvailabilityRequestConverter.toJson(this, json);
        return json;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityRequest that = (AvailabilityRequest) o;
        return Objects.equals(site, that.site) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, startDate, endDate);
    }
}
