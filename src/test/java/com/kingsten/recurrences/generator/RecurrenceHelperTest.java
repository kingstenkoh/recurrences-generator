package com.kingsten.recurrences.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class RecurrenceHelperTest {

    @Test
    public void DailyRecurrenceSettings_ByNoOfOccurrences_Success_Test() throws Exception {

        //set start date for 20 times
        LocalDateTime startDate = LocalDateTime.of(2021,1,1,0,0);
        DailyRecurrenceSettings da = new DailyRecurrenceSettings(startDate, 20);

        //set recurrence to everyday
        RecurrenceValues values = da.getValues(1);
        assertEquals("D20210101ZZZZZZZZ002002001", values.getSeriesInfo());
        assertEquals(20, values.getValues().size());
        assertEquals("20210101",DateTimeFormatter.BASIC_ISO_DATE.format(values.getStartDate()));
        assertEquals("20210120",DateTimeFormatter.BASIC_ISO_DATE.format(values.getEndDate()));

        //values.seriesInfo can be stored in db and parsed back using RecurrenceHelper.getRecurrenceValues
        RecurrenceValues returnValues = RecurrenceHelper.getRecurrenceValues(values.seriesInfo);
        assertEquals(returnValues.getSeriesInfo(), values.getSeriesInfo());
        assertEquals(returnValues.getValues().size(), values.getValues().size());
        assertEquals(returnValues.getStartDate(),values.getStartDate());
        assertEquals(returnValues.getEndDate(), values.getEndDate());

        //get next day trigger
        LocalDate date = LocalDate.parse("20210111", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();
        //values.seriesInfo can be stored in db and parsed back using RecurrenceHelper
        assertEquals(LocalDateTime.of(2021,1,12,0,0),
                RecurrenceHelper.getNextDate(currentDate, returnValues.getSeriesInfo()));

        //to adjust occurrence
        RecurrenceValues adjustedOccurrenceValues = RecurrenceHelper.getRecurrenceValues(values.seriesInfo, 10);
        assertEquals(10, adjustedOccurrenceValues.getValues().size());
        assertEquals("20210101",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues.getStartDate()));
        assertEquals("20210110",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues.getEndDate()));

        //to adjust startdate  and noOfOccurrences
        RecurrenceValues adjustedOccurrenceValues2 = RecurrenceHelper.getRecurrenceValues(values.seriesInfo,
                LocalDateTime.of(2021,2,20,0,0),10);
//        System.out.println(adjustedOccurrenceValues2);
        assertEquals(10, adjustedOccurrenceValues2.getValues().size());
        assertEquals("20210220",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues2.getStartDate()));
        assertEquals("20210301",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues2.getEndDate()));

        //to adjust startdate  and endDate instead of noOfOccurrences
        RecurrenceValues adjustedOccurrenceValues3 = RecurrenceHelper.getRecurrenceValues(values.seriesInfo,
                LocalDateTime.of(2021,2,20,0,0),
                LocalDateTime.of(2021,3,2,0,0));
//        System.out.println(adjustedOccurrenceValues3);
        assertEquals(11, adjustedOccurrenceValues3.getValues().size());
        assertEquals("20210220",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues3.getStartDate()));
        assertEquals("20210302",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues3.getEndDate()));

        //post recurrence to extend another 5 (no use case yet)
        RecurrenceValues adjustedOccurrenceValues4 = RecurrenceHelper.getPostRecurrenceValues(adjustedOccurrenceValues3.getSeriesInfo(), 5);
//        System.out.println(adjustedOccurrenceValues4);

        //to adjust endDate
        RecurrenceValues adjustedOccurrenceValues5 = RecurrenceHelper.getRecurrenceValues(adjustedOccurrenceValues3.seriesInfo,
                LocalDateTime.of(2021,3,3,0,0));
//        System.out.println(adjustedOccurrenceValues5);
        assertEquals(12, adjustedOccurrenceValues5.getValues().size());
        assertEquals("20210220",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues5.getStartDate()));
        assertEquals("20210303",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues5.getEndDate()));

        //to adjust startDate
        RecurrenceValues adjustedOccurrenceValues6 = RecurrenceHelper.getRecurrenceValues( LocalDateTime.of(2021,2,21,0,0),
                adjustedOccurrenceValues5.seriesInfo);
        System.out.println(adjustedOccurrenceValues6);
        assertEquals(11, adjustedOccurrenceValues6.getValues().size());
        assertEquals("20210221",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues6.getStartDate()));
        assertEquals("20210303",DateTimeFormatter.BASIC_ISO_DATE.format(adjustedOccurrenceValues6.getEndDate()));


        //to print out the daily recurrence pattern
        System.out.println(RecurrenceHelper.getPatternDefinitioin(adjustedOccurrenceValues6.getSeriesInfo()));

    }

}