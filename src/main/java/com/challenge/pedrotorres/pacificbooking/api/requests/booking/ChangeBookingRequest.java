package com.challenge.pedrotorres.pacificbooking.api.requests.booking;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class ChangeBookingRequest extends BaseBookingRequest {

    public ChangeBookingRequest() {

    }

    public ChangeBookingRequest(JsonObject jsonObject) {
        ChangeBookingRequestConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ChangeBookingRequestConverter.toJson(this, json);
        return json;
    }
}
