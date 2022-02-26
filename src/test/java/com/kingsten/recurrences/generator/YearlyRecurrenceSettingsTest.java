package com.kingsten.recurrences.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class YearlyRecurrenceSettingsTest {

    @Test
    public void YearlyRecurrenceSettingsSuccessTest() throws Exception {

        YearlyRecurrenceSettings yr;
        yr = new YearlyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 10);
        RecurrenceValues values = yr.getValues(31, 2);

        System.out.println(values);

        RecurrenceSettings returnValues = yr.getRecurrenceSettings(values.seriesInfo);
        System.out.println(values.seriesInfo);

        LocalDate date = LocalDate.parse("20220131", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();

        System.out.println(returnValues.getNextDate(currentDate));

        System.out.println(YearlyRecurrenceSettings.getPatternDefinition(values.seriesInfo));

    }

    @Test
    public void YearlyRecurrenceSettingsSuccess2Test() throws Exception {
        YearlyRecurrenceSettings yr = null;
        yr = new YearlyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 10);
        RecurrenceValues values = yr.getValues(YearlyRecurrenceSettings.YearlySpecificDatePartOne.First, YearlyRecurrenceSettings.YearlySpecificDatePartTwo.Day, YearlyRecurrenceSettings.YearlySpecificDatePartThree.February);
        System.out.println(values);
        RecurrenceSettings returnValues = yr.getRecurrenceSettings(values.seriesInfo);

        LocalDate date = LocalDate.parse("20240202", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();
        System.out.println(returnValues.getNextDate(currentDate));

        System.out.println(YearlyRecurrenceSettings.getPatternDefinition(values.seriesInfo));
    }
}