package com.challenge.pedrotorres.pacificbooking.api.responses.booking;

import com.challenge.pedrotorres.pacificbooking.domain.booking.Booking;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class BookingInfo {

    private Long bookingId;
    private String bookingCode;
    private String camperName;
    private Booking.BookingStatus status;
    private String startDate;
    private String endDate;

    public BookingInfo() {

    }

    public BookingInfo(JsonObject jsonObject) {
        BookingInfoConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BookingInfoConverter.toJson(this, json);
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

    public String getCamperName() {
        return camperName;
    }

    public void setCamperName(String camperName) {
        this.camperName = camperName;
    }

    public Booking.BookingStatus getStatus() {
        return status;
    }

    public void setStatus(Booking.BookingStatus status) {
        this.status = status;
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

    public static BookingInfo mapFromBooking(Booking booking) {
        BookingInfo bookingInfo = new BookingInfo();

        bookingInfo.setBookingCode(booking.getCode());
        bookingInfo.setBookingId(booking.getId());
        bookingInfo.setStatus(booking.getStatus());
        bookingInfo.setStartDate(booking.getStartDate().toString());
        bookingInfo.setEndDate(booking.getEndDate().toString());
        bookingInfo.setCamperName(booking.getCamper().getFirstName() + " " + booking.getCamper().getLastName());
        return bookingInfo;
    }
}
