package com.challenge.pedrotorres.pacificbooking.api.requests.booking;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class CancelBookingRequest extends BaseBookingRequest {

    public CancelBookingRequest() {

    }

    public CancelBookingRequest(JsonObject jsonObject) {
        CancelBookingRequestConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CancelBookingRequestConverter.toJson(this, json);
        return json;
    }
}
