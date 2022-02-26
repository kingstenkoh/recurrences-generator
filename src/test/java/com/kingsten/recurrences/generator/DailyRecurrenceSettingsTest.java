package com.kingsten.recurrences.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DailyRecurrenceSettingsTest {

    @Test
    public void DailyRecurrenceSettingsSuccessTest() throws Exception {

        DailyRecurrenceSettings da = new DailyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 20);
        RecurrenceValues values = da.getValues(2);
        LocalDate date = LocalDate.parse("20220111", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();

        System.out.println(currentDate);
        System.out.println(values);
        RecurrenceSettings returnValues = da.getRecurrenceSettings(values.seriesInfo);
        System.out.println(returnValues.getValues());
        System.out.println(returnValues.getNextDate(currentDate));


        date = LocalDate.parse("20220113", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime endDate = date.atStartOfDay();
        returnValues = da.getRecurrenceSettings(values.seriesInfo, endDate);
        System.out.println(returnValues.getValues());
        System.out.println(returnValues.getNextDate(currentDate));

        returnValues = da.getRecurrenceSettings(values.seriesInfo, 19);
        System.out.println(returnValues.getValues());
        System.out.println(returnValues.getNextDate(currentDate));

        System.out.println(DailyRecurrenceSettings.getPatternDefinition(values.seriesInfo));
    }
}