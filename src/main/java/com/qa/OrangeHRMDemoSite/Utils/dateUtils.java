package com.qa.OrangeHRMDemoSite.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class dateUtils {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String today() {
        return LocalDate.now().format(formatter);
    }

    public static String futureDate(int days) {
        return LocalDate.now().plusDays(days).format(formatter);
    }

    public static String pastDate(int days) {
        return LocalDate.now().minusDays(days).format(formatter);
    }

    public static String customDate(int year, int month, int day) {
        return LocalDate.of(year, month, day).format(formatter);
    }
}
