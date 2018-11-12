package com.challenge.pedrotorres.pacificbooking.commons;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class ValidatorHelper {

    public static Boolean isValidLocalDate(String strDate) {
        try {
            LocalDate.parse(strDate);
            return true;
        }catch (DateTimeParseException e) {
            return false;
        }
    }

    public static Boolean isStartDateGreaterThanEndDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && startDate.isAfter(endDate) && !startDate.isEqual(endDate);
    }

    public static Boolean isPeriodOverOneMonth(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && ChronoUnit.MONTHS.between(startDate, endDate) > 1l;
    }

    public static Boolean isDateBeforeToday(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
}
