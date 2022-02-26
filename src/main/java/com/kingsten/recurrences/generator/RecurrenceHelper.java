package com.kingsten.recurrences.generator;

import java.time.LocalDateTime;

public class RecurrenceHelper {


    /// <summary>
    ///     Get the Series Info in a user-friendly object that can be used as a means to 
    ///     populate UI controls.
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         String of the Series Info.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceInfo value...
    /// </returns>
    public static RecurrenceInfo getFriendlySeriesInfo(String seriesInfo) {
        RecurrenceInfo returnValue = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                returnValue = YearlyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
                break;

            case "M":   // Monthly
                returnValue = MonthlyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
                break;

            case "W":   // Weekly
                returnValue = WeeklyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
                break;

            case "D":   // Daily
                returnValue = DailyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
                break;

        }
        return returnValue;
    }

    /// <summary>
    ///     Get the next date in the series given the current date in the series 
    ///     and the series information
    /// </summary>
    /// <param name="currentDate" type="System.DateTime">
    ///     <para>
    ///         The current date in the recurrence dates. This is the date just before 
    ///         the one you're trying to locate.
    ///     </para>
    /// </param>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         The recurrence pattern series information.
    ///     </para>
    /// </param>
    /// <returns>
    ///     The next date in the recurrence pattern as defined by the series 
    ///     information string.
    /// </returns>
    public static LocalDateTime getNextDate(LocalDateTime currentDate, String seriesInfo) throws Exception {
        RecurrenceSettings returnValues = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                returnValues = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "M":   // Monthly
                returnValues = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "W":   // Weekly
                returnValues = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "D":   // Daily
                returnValues = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

        }
        // Return just the next date. The function "getNextDate" is an abstract 
        // method overriden in each of the RecurrenceSettings classes.
        return returnValues.getNextDate(currentDate);
    }

    /// <summary>
    ///     Get the Values for a specific recurrence series by passing in the series info 
    ///     that defines the recurrence patter.
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         Recurrence pattern information.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A BOCA.RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    public static RecurrenceValues getRecurrenceValues(String seriesInfo) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

        }
        // Return the RecurrenceValues
        return settings.getValues();
    }

    /// <summary>
    /// Get recurrence values using an existin Series Info value as well as a modified Start Date.
    /// </summary>
    /// <param name="modifiedStartDateValue"></param>
    /// <param name="seriesInfo"></param>
    /// <returns></returns>
    public static RecurrenceValues getRecurrenceValues(LocalDateTime modifiedStartDateValue, String seriesInfo) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(modifiedStartDateValue, seriesInfo);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(modifiedStartDateValue, seriesInfo);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(modifiedStartDateValue, seriesInfo);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(modifiedStartDateValue, seriesInfo);
                break;

        }
        // Return the RecurrenceValues
        return settings.getValues();
    }

    /// <summary>
    ///     Get the recurrence values using an existing Series Info value and modified Occurrence Values.
    ///     This is used to get a modified set of values where you want the occurrence count to be different.
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         The recurrence pattern series information.
    ///     </para>
    /// </param>
    /// <param name="modifiedOccurrencesValue" type="int">
    ///     <para>
    ///         Integer value of the modified occurrence count.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    public static RecurrenceValues getRecurrenceValues(String seriesInfo, int modifiedOccurrencesValue) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue);
                break;

        }

        // Return the RecurrenceValues
        return settings.getValues();
    }

    /// <summary>
    /// Get the reccurrence values using an existing SeriesInfo value but with adjusted number of occurrences and a modified start date.
    /// </summary>
    /// <param name="seriesInfo">Existing SeriesInfo value</param>
    /// <param name="modifiedStartDateValue">Modified Start Date</param>
    /// <param name="modifiedOccurrencesValue">Modified number of occurrences</param>
    /// <returns></returns>
    public static RecurrenceValues getRecurrenceValues(String seriesInfo, LocalDateTime modifiedStartDateValue, int modifiedOccurrencesValue) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedOccurrencesValue);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedOccurrencesValue);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedOccurrencesValue);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedOccurrencesValue);
                break;

        }

        // Return the RecurrenceValues
        return settings.getValues();
    }


    /// <summary>
    ///     Get a modified collection of recurrence values from an existing Series Info value and modified
    ///     End Date value.
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         The recurrence pattern series information.
    ///     </para>
    /// </param>
    /// <param name="modifiedEndDateValue" type="System.DateTime">
    ///     <para>
    ///         LocalDateTime of the modified end date that you want the recurrence values to continue until.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    public static RecurrenceValues getRecurrenceValues(String seriesInfo, LocalDateTime modifiedEndDateValue) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedEndDateValue);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedEndDateValue);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedEndDateValue);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedEndDateValue);
                break;

        }

        // Return the RecurrenceValues
        return settings.getValues();
    }

    public static RecurrenceValues getRecurrenceValues(String seriesInfo, LocalDateTime modifiedStartDateValue, LocalDateTime modifiedEndDateValue) throws Exception {
        RecurrenceSettings settings = null;

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedEndDateValue);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedEndDateValue);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedEndDateValue);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo, modifiedStartDateValue, modifiedEndDateValue);
                break;

        }

        // Return the RecurrenceValues
        return settings.getValues();
    }

    /// <summary>
    ///     Get a collection of recurrence values that are AFTER the values as defined by the 
    ///     Series Info value. The endDate param. defines how far past the existing end date
    ///     as defined by the Series Info.
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         The recurrence pattern series information.
    ///     </para>
    /// </param>
    /// <param name="endDate" type="System.DateTime">
    ///     <para>
    ///         LocalDateTime of the end date that is past the end date in the Series Info where
    ///         you want the collection of values to end.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    public static RecurrenceValues getPostRecurrenceValues(String seriesInfo, LocalDateTime endDate) throws Exception {
        RecurrenceSettings settings = null;
        RecurrenceValues tempValues;
        LocalDateTime lastDate;
        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

        }

        // get the RecurrenceValues
        tempValues = settings.getValues();
        // Add one day to the last date so it now becomes the Start date past the last 
        // date in the series
        lastDate = tempValues.getLastDate().plusDays(1);

        return settings.getValues(lastDate, endDate);

    }

    public static RecurrenceValues getPostRecurrenceValues(String seriesInfo, int modifiedOccurrencesValue) throws Exception {
        RecurrenceSettings settings = null;
        RecurrenceValues tempValues;
        LocalDateTime lastDate;
        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                settings = YearlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "M":   // Monthly
                settings = MonthlyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "W":   // Weekly
                settings = WeeklyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

            case "D":   // Daily
                settings = DailyRecurrenceSettings.getRecurrenceSettings(seriesInfo);
                break;

        }

        // get the RecurrenceValues
        tempValues = settings.getValues();
        // Add one day to the last date so it now becomes the Start date past the last 
        // date in the series
        lastDate = tempValues.getLastDate().plusDays(1);

        return settings.getValues(lastDate, modifiedOccurrencesValue);

    }

    /// <summary>
    ///     Get a static text definition of the Series Info
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         Series Info String that was generated when a series of recurring dates were created.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A String value of the Series Info definition
    /// </returns>
    public static String getPatternDefinitioin(String seriesInfo) {
        String value = "";

        switch (seriesInfo.substring(0, 1)) {
            case "Y":   // Yearly
                value = YearlyRecurrenceSettings.getPatternDefinition(seriesInfo);
                break;

            case "M":   // Monthly
                value = MonthlyRecurrenceSettings.getPatternDefinition(seriesInfo);
                break;

            case "W":   // Weekly
                value = WeeklyRecurrenceSettings.getPatternDefinition(seriesInfo);
                break;

            case "D":   // Daily
                value = DailyRecurrenceSettings.getPatternDefinition(seriesInfo);
                break;

        }

        return value;
    }
}

