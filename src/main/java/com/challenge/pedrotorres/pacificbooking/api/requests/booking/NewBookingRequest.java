package com.challenge.pedrotorres.pacificbooking.api.requests.booking;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class NewBookingRequest extends BaseBookingRequest{

    public NewBookingRequest() {

    }

    public NewBookingRequest(JsonObject jsonObject) {
        NewBookingRequestConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        NewBookingRequestConverter.toJson(this, json);
        return json;
    }
}
