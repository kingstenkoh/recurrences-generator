package com.kingsten.recurrences.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MonthlyRecurrenceSettingsTest {

    @Test
    public void MonthlyRecurrenceSettingsSuccessTest() throws Exception {

        MonthlyRecurrenceSettings mo;
        mo = new MonthlyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 10);
        RecurrenceValues values = mo.getValues(31, 1);

        System.out.println(values);

        RecurrenceSettings returnValues = mo.getRecurrenceSettings(values.seriesInfo);
        System.out.println(values.seriesInfo);

        LocalDate date = LocalDate.parse("20220131", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();

        System.out.println(returnValues.getNextDate(currentDate));

        System.out.println(MonthlyRecurrenceSettings.getPatternDefinition(values.seriesInfo));

    }

    @Test
    public void MonthlyRecurrenceSettingsSuccess2Test() throws Exception {
        MonthlyRecurrenceSettings mo = null;
        mo = new MonthlyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 10);
        RecurrenceValues values = mo.getValues(MonthlyRecurrenceSettings.MonthlySpecificDatePartOne.First, MonthlyRecurrenceSettings.MonthlySpecificDatePartTwo.Day, 1);
        System.out.println(values);
        RecurrenceSettings returnValues = mo.getRecurrenceSettings(values.seriesInfo);

        LocalDate date = LocalDate.parse("20220302", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();
        System.out.println(returnValues.getNextDate(currentDate));

        System.out.println(MonthlyRecurrenceSettings.getPatternDefinition(values.seriesInfo));

        System.out.println(RecurrenceHelper.getNextDate(currentDate,values.seriesInfo));
        values = RecurrenceHelper.getRecurrenceValues(values.seriesInfo);
        System.out.println(values);
    }
}