package com.challenge.pedrotorres.pacificbooking.commons.helpers;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ValidatorHelper {

    public static Boolean isValidLocalDate(String strDate) {
        try {
            LocalDate.parse(strDate);
            return true;
        }catch (Exception e) {
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

    public static Boolean isDateAfterToday(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    public static Long daysWithinPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0l;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static Boolean isValidEmail(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }
}
