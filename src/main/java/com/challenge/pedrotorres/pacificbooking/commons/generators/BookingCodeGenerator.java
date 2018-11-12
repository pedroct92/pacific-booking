package com.challenge.pedrotorres.pacificbooking.commons.generators;

import java.time.LocalDate;

public class BookingCodeGenerator {

    private static final String CHARS_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getBookingCode() {
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            codeBuilder.append(CHARS_SOURCE.charAt((int) Math.floor(Math.random() * CHARS_SOURCE.length())));
        }

        codeBuilder.append(LocalDate.now().getYear());

        return codeBuilder.toString();
    }
}
