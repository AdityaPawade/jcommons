package com.adtsw.jcommons.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {
    
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    @Test
    public void getCurrentIndianEpochSecondTest() {
        
        OffsetDateTime currentIndianDateTime = DateUtil.getCurrentIndianDate();
        String currentIndianDateTimeString = currentIndianDateTime.format(dateTimeFormatter);
        
        OffsetDateTime currentUTCDateTime = DateUtil.getCurrentDate(DateUtil.utcZoneId);
        String currentUTCDateTimeString = currentUTCDateTime.format(dateTimeFormatter);

        long currentIndianEpochSecond = DateUtil.getCurrentEpochSecond();
        
        long ts = 1665720500000L;
        System.out.println(1665720500 + " : " + DateUtil.getIndianDateTime(ts) + " [ORIGINAL]");
        
        long elapsed5MinEpoch = DateUtil.getElapsedMinEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, 5);
        System.out.println(elapsed5MinEpoch + " : " + DateUtil.getIndianDateTime(elapsed5MinEpoch * 1000) + " [5 min]");
        Assert.assertEquals("2022-10-14T09:35:00+0530", DateUtil.getIndianDateTime(elapsed5MinEpoch * 1000));
        
        long elapsed15MinEpoch = DateUtil.getElapsedMinEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, 15);
        System.out.println(elapsed15MinEpoch + " : " + DateUtil.getIndianDateTime(elapsed15MinEpoch * 1000) + " [15 min]");
        Assert.assertEquals("2022-10-14T09:30:00+0530", DateUtil.getIndianDateTime(elapsed15MinEpoch * 1000));
        
        long elapsed15MinEpoch1Offset = DateUtil.getElapsedMinEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, 15, 1);
        System.out.println(elapsed15MinEpoch1Offset + " : " + DateUtil.getIndianDateTime(elapsed15MinEpoch1Offset * 1000) + " [15 min 1 offset]");
        Assert.assertEquals("2022-10-14T09:15:00+0530", DateUtil.getIndianDateTime(elapsed15MinEpoch1Offset * 1000));
        
        long elapsed30MinEpoch = DateUtil.getElapsedMinEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, 30);
        System.out.println(elapsed30MinEpoch + " : " + DateUtil.getIndianDateTime(elapsed30MinEpoch * 1000) + " [30 min]");
        Assert.assertEquals("2022-10-14T09:30:00+0530", DateUtil.getIndianDateTime(elapsed30MinEpoch * 1000));

        long elapsedHOURSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.HOURS);
        System.out.println(elapsedHOURSEpoch + " : " + DateUtil.getIndianDateTime(elapsedHOURSEpoch * 1000) + " [HOURS]");
        System.out.println(elapsedHOURSEpoch + " : " + DateUtil.getDateTime(DateUtil.utcZoneId, elapsedHOURSEpoch * 1000) + " [HOURS UTC]");
        Assert.assertEquals("2022-10-14T09:30:00+0530", DateUtil.getIndianDateTime(elapsedHOURSEpoch * 1000));
        Assert.assertEquals("2022-10-14T04:00:00Z", DateUtil.getDateTime(DateUtil.utcZoneId, elapsedHOURSEpoch * 1000));
        
        long elapsedDAYSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.DAYS);
        System.out.println(elapsedDAYSEpoch + " : " + DateUtil.getIndianDateTime(elapsedDAYSEpoch * 1000) + " [DAYS]");
        Assert.assertEquals("2022-10-14T05:30:00+0530", DateUtil.getIndianDateTime(elapsedDAYSEpoch * 1000));
        
        long elapsedUTCDAYSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.DAYS);
        System.out.println(elapsedUTCDAYSEpoch + " : " + DateUtil.getDateTime(DateUtil.utcZoneId, elapsedUTCDAYSEpoch * 1000) + " [DAYS UTC]");
        Assert.assertEquals("2022-10-14T00:00:00Z", DateUtil.getDateTime(DateUtil.utcZoneId, elapsedUTCDAYSEpoch * 1000));
        
        long elapsedIndianDAYSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.kolkataZoneId, ts, ChronoUnit.DAYS);
        System.out.println(elapsedIndianDAYSEpoch + " : " + DateUtil.getIndianDateTime(elapsedIndianDAYSEpoch * 1000) + " [DAYS IST]");
        Assert.assertEquals("2022-10-14T00:00:00+0530", DateUtil.getIndianDateTime(elapsedIndianDAYSEpoch * 1000));
        
        long elapsedWEEKSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.WEEKS);
        System.out.println(elapsedWEEKSEpoch + " : " + DateUtil.getIndianDateTime(elapsedWEEKSEpoch * 1000) + " [WEEKS]");
        Assert.assertEquals("2022-10-10T05:30:00+0530", DateUtil.getIndianDateTime(elapsedWEEKSEpoch * 1000));
        
        long elapsedMONTHSEpoch = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.MONTHS);
        System.out.println(elapsedMONTHSEpoch + " : " + DateUtil.getIndianDateTime(elapsedMONTHSEpoch * 1000) + " [MONTHS]");
        Assert.assertEquals("2022-10-01T05:30:00+0530", DateUtil.getIndianDateTime(elapsedMONTHSEpoch * 1000));
        
        long elapsedMONTHSEpoch1Offset = DateUtil.getElapsedEpochSecondFromEpochMilli(DateUtil.utcZoneId, ts, ChronoUnit.MONTHS, 1);
        System.out.println(elapsedMONTHSEpoch1Offset + " : " + DateUtil.getIndianDateTime(elapsedMONTHSEpoch1Offset * 1000) + " [MONTHS 1 offset]");
        Assert.assertEquals("2022-09-01T05:30:00+0530", DateUtil.getIndianDateTime(elapsedMONTHSEpoch1Offset * 1000));

        long indianTimestamp = DateUtil.getIndianTimestampEpochSecond("2022-11-23T12:25:05+0530");
        Assert.assertEquals(1669186505L, indianTimestamp);
    }
}
