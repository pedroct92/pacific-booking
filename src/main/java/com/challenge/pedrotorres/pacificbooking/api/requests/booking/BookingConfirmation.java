package com.challenge.pedrotorres.pacificbooking.api.requests.booking;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class BookingConfirmation {

    private Long bookingId;
    private String bookingCode;
    private String camperFullName;

    public BookingConfirmation() {

    }

    public BookingConfirmation(JsonObject jsonObject) {
        BookingConfirmationConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BookingConfirmationConverter.toJson(this, json);
        return json;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getCamperFullName() {
        return camperFullName;
    }

    public void setCamperFullName(String camperFullName) {
        this.camperFullName = camperFullName;
    }
}
