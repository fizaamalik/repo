package com.book.services;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class Time {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Converts a normal date-time string into a Unix timestamp (seconds since epoch).
     * @param dateTimeStr the date-time string in format "yyyy-MM-dd HH:mm:ss"
     * @return Unix timestamp as a long (seconds since 1970-01-01T00:00:00Z)
     */
    public static long toUnixTimestamp(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    // Simple main to test the method
    public static void main(String[] args) {
        String dateTime = "2025-05-27 15:30:00";
        long unixTimestamp = toUnixTimestamp(dateTime);
        System.out.println("Unix Timestamp for " + dateTime + " = " + unixTimestamp);
    }
}
