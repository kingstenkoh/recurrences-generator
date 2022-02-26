package com.kingsten.recurrences.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class WeeklyRecurrenceSettingsTest {

    @Test
    public void WeeklyRecurrenceSettingsSuccessTest() throws Exception {

        WeeklyRecurrenceSettings we;
        WeeklyRecurrenceSettings.SelectedDayOfWeekValues selectedValues = new WeeklyRecurrenceSettings.SelectedDayOfWeekValues();
        we = new WeeklyRecurrenceSettings(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), 10);
        selectedValues.Sunday = true;
        selectedValues.Monday = true;
        RecurrenceValues values = we.getValues(1, selectedValues);
        System.out.println(values);

        RecurrenceSettings returnValues = we.getRecurrenceSettings(values.seriesInfo);
        System.out.println(values.seriesInfo);

        LocalDate date = LocalDate.parse("20211228", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime currentDate = date.atStartOfDay();

        System.out.println(returnValues.getNextDate(currentDate));

        returnValues = we.getRecurrenceSettings(values.seriesInfo, 19);
        System.out.println(returnValues.getValues());
        System.out.println(returnValues.getNextDate(currentDate));

        date = LocalDate.parse("20220101", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime endDate = date.atStartOfDay();
        returnValues = we.getRecurrenceSettings(values.seriesInfo, endDate);
        System.out.println(returnValues.getValues());
        System.out.println(returnValues.getNextDate(currentDate));


        System.out.println(WeeklyRecurrenceSettings.getPatternDefinition(values.seriesInfo));

    }
}