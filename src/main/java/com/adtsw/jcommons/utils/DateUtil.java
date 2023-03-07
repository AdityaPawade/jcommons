package com.adtsw.jcommons.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;

public class DateUtil {
        
    public static final ZoneId kolkataZoneId = ZoneId.of("Asia/Kolkata");
    public static final ZoneId utcZoneId = ZoneId.of("UTC");

    // yyyy-MM-dd'T'HH:mm:ssXXX : 2022-10-14T09:35:00+05:30
    // yyyy-MM-dd'T'HH:mm:ssX   : 2022-10-14T09:35:00+0530
    
    private static final DateTimeFormatterBuilder dateTimeReadFormatterBuilder = new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ofPattern(
            "[yyyy-MM-dd'T'HH:mm:ssXXX]" + 
            "[yyyy-MM-dd'T'HH:mm:ssX]" + 
            "[yyyy-MM-dd'T'HH:mm:ss]" + 
            "[yyyy-MM-dd' 'HH:mm:ss]")
        );

    private static final DateTimeFormatter indianDateTimeReadFormatter = dateTimeReadFormatterBuilder.toFormatter()
        .withZone(kolkataZoneId);

    private static final DateTimeFormatterBuilder dateTimeWriteFormatterBuilder = new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ofPattern("[yyyy-MM-dd'T'HH:mm:ssX]"));
    
    private static final DateTimeFormatter indianDateTimeWriteFormatter = dateTimeWriteFormatterBuilder.toFormatter()
        .withZone(kolkataZoneId);

    public static OffsetDateTime getCurrentIndianDate() {
        return OffsetDateTime.now(kolkataZoneId);
    }

    public static OffsetDateTime getCurrentDate(ZoneId zoneId) {
        return OffsetDateTime.now(zoneId);
    }
    
    public static long getCurrentEpochSecond() {
        return getCurrentIndianDate().toEpochSecond();
    }

    public static String getIndianDateTime(long timestamp) {
        OffsetDateTime offsetDateTime = getIndianOffsetDateTimeFromEpochMilli(timestamp);
        return offsetDateTime.format(indianDateTimeWriteFormatter);
    }

    public static String getDateTime(ZoneId zoneId, long timestamp) {
        OffsetDateTime offsetDateTime = getOffsetDateTimeFromEpochMilli(zoneId, timestamp);
        return offsetDateTime.format(dateTimeWriteFormatterBuilder.toFormatter().withZone(zoneId));
    }

    public static OffsetDateTime getIndianOffsetDateTimeFromEpochMilli(long timestamp) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), kolkataZoneId);
    }

    public static OffsetDateTime getOffsetDateTimeFromEpochMilli(ZoneId zoneId, long timestamp) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
    }

    public static ZonedDateTime getIndianZonedDateTimeFromEpochMilli(long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), kolkataZoneId);
    }

    public static ZonedDateTime getZonedDateTimeFromEpochMilli(ZoneId zoneId, long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
    }

    public static long getIndianTimestampEpochSecond(String dateTime) {
        Instant result = Instant.from(indianDateTimeReadFormatter.parse(dateTime));
        return result.getEpochSecond();
    }

    public static long getTimestampEpochSecond(ZoneId zoneId, String dateTime) {
        Instant result = Instant.from(dateTimeReadFormatterBuilder.toFormatter()
            .withZone(zoneId).parse(dateTime));
        return result.getEpochSecond();
    }
    
    public static long getElapsedMinEpochSecondFromEpochMilli(ZoneId zoneId, long timestamp, int minuteInterval) {
        return getElapsedMinEpochSecondFromEpochMilli(zoneId, timestamp, minuteInterval, 0);
    }
    
    public static long getElapsedMinEpochSecondFromEpochMilli(ZoneId zoneId, long timestamp, int minuteInterval, int offset) {
        ZonedDateTime elapsedDateTime = getElapsedMinZonedDateTimeFromEpochMilli(zoneId, timestamp, minuteInterval, offset);
        return elapsedDateTime.toEpochSecond();
    }

    public static ZonedDateTime getElapsedMinZonedDateTimeFromEpochMilli(ZoneId zoneId, long timestamp, int minuteInterval, int offset) {
        ZonedDateTime dateTime = getZonedDateTimeFromEpochMilli(zoneId, timestamp);
        ZonedDateTime elapsedDateTime = dateTime.truncatedTo(ChronoUnit.HOURS)
                                .plusMinutes(minuteInterval * (dateTime.getMinute() / minuteInterval))
                                .minusMinutes(offset * minuteInterval);
        return elapsedDateTime;
    }

    public static long getElapsedEpochSecondFromEpochMilli(ZoneId zoneId, long timestamp, ChronoUnit unit) {
        return getElapsedEpochSecondFromEpochMilli(zoneId, timestamp, unit, 0);
    }

    public static long getElapsedEpochSecondFromEpochMilli(ZoneId zoneId, long timestamp, ChronoUnit unit, int offset) {
        ZonedDateTime truncatedDateTimeWithOffset = getZonedDateTimeFromEpochMilli(zoneId, timestamp, unit, offset);
        return truncatedDateTimeWithOffset.toEpochSecond();
    }

    public static ZonedDateTime getZonedDateTimeFromEpochMilli(ZoneId zoneId, long timestamp, ChronoUnit unit, int offset) {
        ZonedDateTime dateTime = getZonedDateTimeFromEpochMilli(zoneId, timestamp);
        ZonedDateTime truncatedDateTime = getElapsedZonedDateTimeFromDateTime(dateTime, unit);
        ZonedDateTime truncatedDateTimeWithOffset = truncatedDateTime.minus(offset, unit);
        return truncatedDateTimeWithOffset;
    }

    private static ZonedDateTime getElapsedZonedDateTimeFromDateTime(ZonedDateTime dateTime, ChronoUnit unit) {
        // LocalDateTime only supports truncatedTo(TemporalUnit) up to ChronoUnit.DAYS.
        switch (unit) {
            case NANOS:
            case MICROS:
            case MILLIS:
            case SECONDS:
            case MINUTES:
            case HOURS:
            case HALF_DAYS:
            case DAYS:
                ZonedDateTime truncatedDateWithinDays = dateTime.truncatedTo(unit);
                return truncatedDateWithinDays;
            default: 
                // else; we can't use LocalDateTime.truncatedTo(TemporalUnit) past ChronoUnit.DAYS, 
                //so we truncate up to DAYS and continue from there.
                return getElapsedZonedDateTimeFromDateTimeUnitsBeyondDays(dateTime, unit);
        }
    }

    private static ZonedDateTime getElapsedZonedDateTimeFromDateTimeUnitsBeyondDays(ZonedDateTime dateTime, ChronoUnit unit) {
        ZonedDateTime truncatedDate = dateTime.truncatedTo(ChronoUnit.DAYS);
        int year = 0;
        switch (unit) {
            case WEEKS:
                // subtract days to the last Monday.
                int daysToLastMonday = DayOfWeek.MONDAY.getValue() - truncatedDate.getDayOfWeek().getValue();
                truncatedDate = truncatedDate.plus(daysToLastMonday, ChronoUnit.DAYS);
                return truncatedDate;
            case MONTHS:
                truncatedDate = truncatedDate.with(TemporalAdjusters.firstDayOfMonth());
                return truncatedDate;
            case YEARS:
                truncatedDate = truncatedDate.with(TemporalAdjusters.firstDayOfYear());
                return truncatedDate;
            case DECADES:
                truncatedDate = truncatedDate.with(TemporalAdjusters.firstDayOfYear());
                year = truncatedDate.getYear();
                int decadeYear = (year/10)*10; // int division rounds down, same as trunc(year/10)*10.
                truncatedDate = truncatedDate.plus(decadeYear-year, ChronoUnit.YEARS);
                return truncatedDate;
            case CENTURIES:
                truncatedDate = truncatedDate.with(TemporalAdjusters.firstDayOfYear());
                year = truncatedDate.getYear();
                int centuryYear = (year/100)*100; // int division rounds down, same as trunc(year/100)*100.
                truncatedDate = truncatedDate.plus(centuryYear-year, ChronoUnit.YEARS);
                return truncatedDate;
            case MILLENNIA:
                truncatedDate = truncatedDate.with(TemporalAdjusters.firstDayOfYear());
                year = truncatedDate.getYear();
                int millenniumYear = (year/1000)*1000; // int division rounds down, same as trunc(year/1000)*1000.
                truncatedDate = truncatedDate.plus(millenniumYear-year, ChronoUnit.YEARS);
                return truncatedDate;
            default: // ChronoUnit.ERA || ChronoUnit.FOREVER:
                throw new UnsupportedTemporalTypeException(
                    "Unable to truncate to unit = '" + String.valueOf(unit) + "', not well supported!"
                );
        }
    }
}
