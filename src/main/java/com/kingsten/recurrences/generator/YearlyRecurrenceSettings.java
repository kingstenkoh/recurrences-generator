package com.kingsten.recurrences.generator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@Data
@ToString
public class YearlyRecurrenceSettings extends RecurrenceSettings {

    //#region Private Fields
    private int regenerateOnSpecificDateDayValue;

    private int regenerateOnSpecificDateMonthValue;

    private int adjustmentValue;

    boolean getNextDateValue;

    private LocalDateTime nextDateValue;
    private YearlyRegenType regenType = YearlyRegenType.NotSet;
    private YearlySpecificDatePartOne specificDatePartOne = YearlySpecificDatePartOne.NotSet;
    //#endregion
    private YearlySpecificDatePartTwo specificDatePartTwo = YearlySpecificDatePartTwo.NotSet;
    private YearlySpecificDatePartThree specificDatePartThree = YearlySpecificDatePartThree.NotSet;

    //#region Constructors
    /// <summary>
    /// Get dates by Start date only. This is for no ending date values.
    /// </summary>
    /// <param name="startDate"></param>
    public YearlyRecurrenceSettings(LocalDateTime startDate) {
        super(startDate);
    }

    /// <summary>
    /// Get dates by Start and End date boundries.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="endDate"></param>
    public YearlyRecurrenceSettings(LocalDateTime startDate, LocalDateTime endDate) {
        super(startDate, endDate);
    }

    /// <summary>
    /// Get dates by Start date and number of occurrences.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="numberOfOccurrences"></param>
    public YearlyRecurrenceSettings(LocalDateTime startDate, int numberOfOccurrences) {
        super(startDate, numberOfOccurrences);
    }

    /// <summary>
    ///     Get a user-friendly class that is a easy way using Properties that define the Series Info
    /// </summary>
    /// <param name="seriesInfo" type="string">
    ///     <para>
    ///         String of Series Info that was first generated by this MonthlyRecurrenceSettings Class object.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceInfo value...
    /// </returns>
    static RecurrenceInfo getFriendlyRecurrenceInfo(String seriesInfo) {
        RecurrenceInfo info = new RecurrenceInfo();
        EndDateType endType = EndDateType.NotDefined;
        LocalDateTime endDateValue = LocalDateTime.MIN;
        int noOccurrences;

        // Exit if not a Yearly seriesInfo type
        if (!seriesInfo.startsWith("Y"))
            return null;

        info.setSeriesInfo(seriesInfo);
        info.setRecurrenceType(RecurrenceInfo.RecurrenceType.Yearly);
        // FORMATTING DEFINITIONS
        //  Y = Yearly Designator
        //  |       0 = Start Date (8 chars)
        //  |       |        1 = End Date (8 chars)
        //  |       |        |        2 = Number of occurrences (4 chars)
        //  |       |        |        |      3 = Regeneration Type (1 char)
        //  |       |        |        |      |    4 = End date type (1 char)
        //  |       |        |        |      |    |      5 = Regenerate on specific date DAY value (2 chars)
        //  |       |        |        |      |    |      |       6 = Regenerate on specific date MONTH value (2 chars)
        //  |       |        |        |      |    |      |       |       7 = Custom Date Part One (1 char)
        //  |       |        |        |      |    |      |       |       |       8 = Custom Date Part Two (1 char)
        //  |       |        |        |      |    |      |       |       |       |       9 = Custom Date Part Three (1 char)
        //  |       |        |        |      |    |      |       |       |       |       |       10 = Adjustment Value (3 chars)
        //  |       |        |        |      |    |      |       |       |       |       |       |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-24] [25-26]  [27]    [28]    [29] [31-33]
        //  Y   20071231  20171231   0000    1     1     00      00      A      A        A      000
        String startDate = seriesInfo.substring(1, 9);

        LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime dtStartDate = date.atStartOfDay();
        String endDate = seriesInfo.substring(9, 17);

        String occurrences = seriesInfo.substring(17, 21);
        String yearlyRegenType = seriesInfo.substring(21, 22);
        String endDateType = seriesInfo.substring(22, 23);
        String regenOnSpecificDateDayValue = seriesInfo.substring(23, 25);
        String regenOnSpecificDateMonthValue = seriesInfo.substring(25, 27);
        String specDatePartOne = seriesInfo.substring(27, 28);
        String specDatePartTwo = seriesInfo.substring(28, 29);
        String specDatePartThree = seriesInfo.substring(29, 30);
        String adjustValue = seriesInfo.substring(30, 33);
        endType = EndDateType.fromValue(endDateType);
        noOccurrences = Integer.valueOf(occurrences);
        YearlySpecificDatePartOne partOne = YearlySpecificDatePartOne.NotSet;
        YearlySpecificDatePartTwo partTwo = YearlySpecificDatePartTwo.NotSet;
        YearlySpecificDatePartThree partThree = YearlySpecificDatePartThree.NotSet;

        if (specDatePartOne == "Z")
            partOne = YearlySpecificDatePartOne.NotSet;
        else
            partOne = YearlySpecificDatePartOne.fromValue(specDatePartOne.charAt(0) - 65);

        if (specDatePartTwo == "Z")
            partTwo = YearlySpecificDatePartTwo.NotSet;
        else
            partTwo = YearlySpecificDatePartTwo.fromValue(specDatePartTwo.charAt(0) - 65);

        if (specDatePartThree == "Z")
            partThree = YearlySpecificDatePartThree.NotSet;
        else
            partThree = YearlySpecificDatePartThree.fromValue(specDatePartThree.charAt(0) - 64);

        // Get the EndDate before any modifications on it are performed
        if (endType == EndDateType.SpecificDate) {
            date = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);
            endDateValue = date.atStartOfDay();
        }

        info.setEndDateType(endType);
        // Determine the Constructor by the type of End Date.
        // All constructors start with a Start date at a minimum.
        switch (endType) {
            case NumberOfOccurrences:
                info.setStartDate(dtStartDate);
                info.setNumberOfOccurrences(noOccurrences);
                break;

            case SpecificDate:
                info.setStartDate(dtStartDate);
                info.setEndDate(endDateValue);
                break;

            case NoEndDate:
                info.setStartDate(dtStartDate);
                break;
        }

        // Set the adjusted values
        info.setAdjustmentValue(Integer.valueOf(adjustValue));

        info.setYearlyRegenType(YearlyRegenType.fromValue(Integer.valueOf(yearlyRegenType)));
        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.yearlyRegenType) {
            case OnSpecificDayOfYear:
                info.setSpecificDateDayValue(Integer.valueOf(regenOnSpecificDateDayValue));
                info.setSpecificDateMonthValue(Integer.valueOf(regenOnSpecificDateMonthValue));
                break;

            case OnCustomDateFormat:
                info.setYearlySpecificDatePartOne(partOne);
                info.setYearlySpecificDatePartTwo(partTwo);
                info.setYearlySpecificDatePartThree(partThree);
                break;

            case AfterOccurrenceCompleted:

                break;
        }

        return info;
    }

    //#region Internal Procedures
    static String getPatternDefinition(String seriesInfo) {
        String returnValue = "";
        returnValue =
                " YEARLY FORMATTING DEFINITIONS\r\n" +
                        "  Y = Yearly Designator\r\n" +
                        "  |       0 = Start Date (8 chars)\r\n" +
                        "  |       |        1 = End Date (8 chars)\r\n" +
                        "  |       |        |        2 = Number of occurrences (4 chars)\r\n" +
                        "  |       |        |        |      3 = Regeneration Type (1 char)\r\n" +
                        "  |       |        |        |      |    4 = End date type (1 char)\r\n" +
                        "  |       |        |        |      |    |      5 = Regenerate on specific date DAY value (2 chars)\r\n" +
                        "  |       |        |        |      |    |      |       6 = Regenerate on specific date MONTH value (2 chars)\r\n" +
                        "  |       |        |        |      |    |      |       |       7 = Custom Date Part One (1 char)\r\n" +
                        "  |       |        |        |      |    |      |       |       |       8 = Custom Date Part Two (1 char)\r\n" +
                        "  |       |        |        |      |    |      |       |       |       |       9 = Custom Date Part Three (1 char)\r\n" +
                        "  |       |        |        |      |    |      |       |       |       |       |       10 = Adjustment Value (3 chars)\r\n" +
                        "  |       |        |        |      |    |      |       |       |       |       |       |\r\n" +
                        " [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-24] [25-26]  [27]    [28]    [29] [31-33]\r\n" +
                        String.format("  Y   %s  %s   %s    %s    %s      %s      %s      %s      %s        %s      %s\r\n", seriesInfo.substring(1, 9), seriesInfo.substring(9, 17),
                                seriesInfo.substring(17, 21), seriesInfo.substring(21, 22), seriesInfo.substring(22, 23), seriesInfo.substring(23, 25), seriesInfo.substring(25, 27),
                                seriesInfo.substring(27, 28), seriesInfo.substring(28, 29), seriesInfo.substring(29, 30), seriesInfo.substring(30, 33)) +
                        "\r\n\r\n" +
                        " Legend:\r\n\r\n" +
                        " #3 - Regeneration Type\r\n" +
                        "     0 = On a Specific Day of the Year\r\n" +
                        "     1 = On Custom Date Format\r\n" +
                        "     2 = After Occurrence Completed\r\n\r\n" +
                        " #4 - End Date Type\r\n" +
                        "     0 = No End Date\r\n" +
                        "     1 = On a Specific Date\r\n" +
                        "     2 = After a Specific Number of occurrences\r\n";
        return returnValue;

    }

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo) {
        return getRecurrenceSettings(seriesInfo, -1, LocalDateTime.MIN);
    }

    static YearlyRecurrenceSettings getRecurrenceSettings(LocalDateTime modifiedStartDate, String seriesInfo) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, LocalDateTime.MIN);
    }
    //#endregion

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue) {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, LocalDateTime.MIN);
    }

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, int modifiedOccurrencesValue) {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, modifiedStartDate, LocalDateTime.MIN);
    }
//#endregion //Public GetValues

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedEndDate) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedEndDate);
    }
//#endregion //Internal GetValues

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, modifiedEndDate);
    }

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) {
        // Get the Recurrence Info object. This makes it easy to work with existing series of date patterns.
        RecurrenceInfo info = YearlyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
        YearlyRecurrenceSettings settings = null;

        // Check to see if this is to modify the SeriesInfo and run as endtype for occurrences
        if (modifiedOccurrencesValue != -1) {
            info.setEndDateType(EndDateType.NumberOfOccurrences);
            info.setNumberOfOccurrences(modifiedOccurrencesValue);
        }
        // Check to see if this is to modify the EndDate and run as endType for EndDate
        if (modifiedEndDate != LocalDateTime.MIN) {
            info.setEndDateType(EndDateType.SpecificDate);
            info.setEndDate(modifiedEndDate);
        }

        // Determine the Constructor by the type of End Date.
        // All constructors start with a Start date at a minimum.
        switch (info.endDateType) {
            case NumberOfOccurrences:
                settings = new YearlyRecurrenceSettings(modifiedStartDate, info.numberOfOccurrences);
                break;

            case SpecificDate:
                settings = new YearlyRecurrenceSettings(modifiedStartDate, info.endDate);
                break;

            case NoEndDate:
                settings = new YearlyRecurrenceSettings(modifiedStartDate);
                break;
        }

        settings.adjustmentValue = info.adjustmentValue;

        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.yearlyRegenType) {
            case OnSpecificDayOfYear:
                settings.setValues(info.specificDateDayValue, info.specificDateMonthValue);
                break;

            case OnCustomDateFormat:
                settings.setValues(info.yearlySpecificDatePartOne, info.yearlySpecificDatePartTwo, info.yearlySpecificDatePartThree);
                break;

            case AfterOccurrenceCompleted:

                break;
        }
        return settings;

    }

    static YearlyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedEndDate) {
        // Get the Recurrence Info object. This makes it easy to work with existing series of date patterns.
        RecurrenceInfo info = YearlyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, info.startDate, modifiedEndDate);
    }

    //#region Public GetValues
    /// <summary>
    /// Get dates for a specific day and month of the year.
    /// </summary>
    /// <param name="specificDateDayValue">Day of the month.</param>
    /// <param name="specificDateMonthValue">Month of the year.</param>
    /// <returns></returns>
    public RecurrenceValues getValues(int specificDateDayValue, int specificDateMonthValue) throws Exception {
        regenerateOnSpecificDateDayValue = specificDateDayValue;
        regenerateOnSpecificDateMonthValue = specificDateMonthValue;
        regenType = YearlyRegenType.OnSpecificDayOfYear;
        return getValues();
    }

    /// <summary>
    /// Get dates for a custom formatted date such as First weekend day of July.
    /// </summary>
    /// <param name="customDateFirstPart"></param>
    /// <param name="customDateSecondPart"></param>
    /// <param name="customDateThirdPart"></param>
    /// <returns></returns>
    public RecurrenceValues getValues(YearlySpecificDatePartOne customDateFirstPart, YearlySpecificDatePartTwo customDateSecondPart, YearlySpecificDatePartThree customDateThirdPart) throws Exception {
        specificDatePartOne = customDateFirstPart;
        specificDatePartTwo = customDateSecondPart;
        specificDatePartThree = customDateThirdPart;
        regenType = YearlyRegenType.OnCustomDateFormat;
        return getValues();
    }

    //#region Internal GetValues
    /// <summary>
    ///     Final overloaded function that gets the Recurrence Values.
    ///     This is called from the RecurrenceHelper staic methods only.
    /// </summary>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    RecurrenceValues getValues() throws Exception {
        return getRecurrenceValues();
    }

    RecurrenceValues getValues(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        this.startDate = startDate;
        this.endDate = endDate;
        // Change the end type to End Date as this original series info
        // may have been set to number of occurrences.
        this.endDateType = EndDateType.SpecificDate;
        return getRecurrenceValues();
    }

    RecurrenceValues getValues(LocalDateTime startDate, int numberOfOccurrences) throws Exception {
        this.numberOfOccurrences = numberOfOccurrences;
        this.startDate = startDate;
        // Change the end type to number of occurrences.
        // This must be set because the original starting Series Info may
        // be set to have an End Date type.
        this.endDateType = EndDateType.NumberOfOccurrences;
        return getRecurrenceValues();
    }

    RecurrenceValues getRecurrenceValues() throws Exception {
        RecurrenceValues values = null;
        switch (regenType) {
            case OnSpecificDayOfYear:
                values = getSpecificDayOfYearDates();
                break;

            case OnCustomDateFormat:
                values = getCustomDayOfYearDates();
                break;

            case AfterOccurrenceCompleted:

                break;

        }
        if (values.values.size() > 0) {
            values.setStartDate(values.values.get(0));

            // Get the end date if not open-ended
            if (this.endDateType != EndDateType.NoEndDate)
                values.setEndDate(values.values.get(values.values.size() - 1));
        }
        // Set the Series information that's used to get the next date
        // values for no ending dates.
        values.setSeriesInfo(getSeriesInfo());

        return values;
    }

    void setValues(int specificDateDayValue, int specificDateMonthValue) {
        regenerateOnSpecificDateDayValue = specificDateDayValue;
        regenerateOnSpecificDateMonthValue = specificDateMonthValue;
        regenType = YearlyRegenType.OnSpecificDayOfYear;
    }

    void setValues(YearlySpecificDatePartOne customDateFirstPart, YearlySpecificDatePartTwo customDateSecondPart, YearlySpecificDatePartThree customDateThirdPart) {
        specificDatePartOne = customDateFirstPart;
        specificDatePartTwo = customDateSecondPart;
        specificDatePartThree = customDateThirdPart;
        regenType = YearlyRegenType.OnCustomDateFormat;
    }

    LocalDateTime getNextDate(LocalDateTime currentDate) throws Exception {
        getNextDateValue = true;
        nextDateValue = currentDate;
        // Now get the values and return the last date found.
        RecurrenceValues values = getValues();
        return values.endDate;
    }

    //#region Private Procedures
    /// <summary>
    /// Get the Series information that's used to get dates at a later
    /// date. This is passed into the RecurrenceHelper to get date values.
    /// Most likely used for non-ending dates.
    /// </summary>
    /// <returns></returns>
    String getSeriesInfo() {
        String info;
        String endDate = "ZZZZZZZZ"; // Default for no ending date.
        String occurrences = String.format("%04d", this.numberOfOccurrences);
        String yearlyRegenType = this.regenType.toString();
        String endDateType = this.endDateType.toString();
        String regenOnSpecificDateDayValue = String.format("%02d", this.regenerateOnSpecificDateDayValue);
        String regenOnSpecificDateMonthValue = String.format("%02d", this.regenerateOnSpecificDateMonthValue);
        String specDatePartOne = "Z";
        String specDatePartTwo = "Z";
        String specDatePartThree = "Z";

        if (specificDatePartOne != YearlySpecificDatePartOne.NotSet)
            specDatePartOne = specificDatePartOne.toString();

        if (specificDatePartTwo != YearlySpecificDatePartTwo.NotSet)
            specDatePartTwo = specificDatePartTwo.toString();

        if (specificDatePartThree != YearlySpecificDatePartThree.NotSet)
            specDatePartThree = specificDatePartThree.toString();

        String adjustValue = String.format("%03d", this.adjustmentValue);

        // If the Adjustment value is less than Zero then account for the "-"
        if (adjustmentValue < 0)
            adjustValue = String.format("%02d", this.adjustmentValue);

        // End Date may be null if no ending date.
        if (this.hasEndDate())
            endDate = DateTimeFormatter.BASIC_ISO_DATE.format(this.endDate);

        // FORMATTING DEFINITIONS
        //  Y = Yearly Designator
        //  |       0 = Start Date (8 chars)
        //  |       |        1 = End Date (8 chars)
        //  |       |        |        2 = Number of occurrences (4 chars)
        //  |       |        |        |      3 = Regeneration Type (1 char)
        //  |       |        |        |      |    4 = End date type (1 char)
        //  |       |        |        |      |    |      5 = Regenerate on specific date DAY value (2 chars)
        //  |       |        |        |      |    |      |       6 = Regenerate on specific date MONTH value (2 chars)
        //  |       |        |        |      |    |      |       |       7 = Custom Date Part One (1 char)
        //  |       |        |        |      |    |      |       |       |       8 = Custom Date Part Two (1 char)
        //  |       |        |        |      |    |      |       |       |       |       9 = Custom Date Part Three (1 char)
        //  |       |        |        |      |    |      |       |       |       |       |       10 = Adjustment Value (3 chars)
        //  |       |        |        |      |    |      |       |       |       |       |       |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-24] [25-26]  [27]    [28]    [29] [31-33]
        //  Y   20071237  20171231   0000    1     1     00      00      A      A        A      000
        info = String.format("Y%s%s%s%s%s%s%s%s%s%s%s", DateTimeFormatter.BASIC_ISO_DATE.format(this.startDate), endDate, occurrences, yearlyRegenType, endDateType, regenOnSpecificDateDayValue, regenOnSpecificDateMonthValue, specDatePartOne, specDatePartTwo, specDatePartThree, adjustValue);
        return info;
    }

    /// <summary>
    /// Get recurring dates from a specific constant date such as 27 July.
    /// </summary>
    /// <returns></returns>
    RecurrenceValues getSpecificDayOfYearDates() throws Exception {
        RecurrenceValues values = new RecurrenceValues();
        LocalDateTime dt = this.startDate;
        int dayValue = regenerateOnSpecificDateDayValue;
        int daysOfMonth = YearMonth.of(dt.getYear(), regenerateOnSpecificDateMonthValue).lengthOfMonth();
        // Get the max days of the month and make sure it's not
        // less than the specified day value trying to be set.
        if (daysOfMonth < regenerateOnSpecificDateDayValue)
            dayValue = daysOfMonth;

        // Determine if start date is greater than the Day and Month values
        // for a specific date.
        LocalDateTime newDate = LocalDateTime.of(dt.getYear(), regenerateOnSpecificDateMonthValue, dayValue, 0, 0);
        // Is the specific date before the start date, if so
        // then make the specific date next year.
        if (newDate.compareTo(dt) < 0)
            dt = newDate.plusYears(1);
        else
            dt = newDate;

        if (getNextDateValue) {
            do {
                values.AddDateValue(dt, adjustmentValue);
                if (values.values.get(values.values.size() - 1).compareTo(nextDateValue) > 0)
                    break;
                dt = dt.plusYears(1);
                dt = GetCorrectedDate(dt);
            } while (dt.compareTo(nextDateValue.plusYears(1)) <= 0); // Ensure the last date if greater than what's needed for the next date in the cycle
        } else {
            switch (this.endDateType) {
                case NoEndDate:
                    throw new Exception("The ability to create recurring dates with no End date is not currently available.");

                case NumberOfOccurrences:
                    for (int i = 0; i < this.numberOfOccurrences; i++) {
                        values.AddDateValue(GetCorrectedDate(dt.plusYears(i)), adjustmentValue);
                    }
                    break;

                case SpecificDate:
                    do {
                        values.AddDateValue(dt, adjustmentValue);
                        dt = dt.plusYears(1);
                        dt = GetCorrectedDate(dt);
                    } while (dt.compareTo(this.endDate) <= 0);
                    break;

                default:
                    throw new IllegalArgumentException("The TypeOfEndDate property has not been set.");
            }
        }

        return values;
    }

    /// <summary>
    /// Correct an input date to be equal to or less than the specified day value.
    /// </summary>
    /// <param name="input">Date to check to ensure it matches the specified day value or the max number of days for that month, whichever comes first.</param>
    /// <returns>DateTime</returns>
    LocalDateTime GetCorrectedDate(LocalDateTime input) {
        LocalDateTime dt = input;
        // Ensure the day value hasn't changed.
        // This will occurr if the month is Feb. All
        // dates after that will have the same day.
        if (dt.getDayOfMonth() < this.regenerateOnSpecificDateDayValue && YearMonth.of(dt.getYear(), dt.getMonth()).lengthOfMonth() > dt.getDayOfMonth()) {
            // The Specified day is greater than the number of days in the month.
            if (this.regenerateOnSpecificDateDayValue > YearMonth.of(dt.getYear(), dt.getMonth()).lengthOfMonth())
                dt = LocalDateTime.of(dt.getYear(), dt.getMonth(), YearMonth.of(dt.getYear(), dt.getMonth()).lengthOfMonth(), 0, 0);
            else
                // The specified date is less than number of days in month.
                dt = LocalDateTime.of(dt.getYear(), dt.getMonth(), this.regenerateOnSpecificDateDayValue, 0, 0);
        }
        return dt;
    }

    /// <summary>
    /// Get recurring dates from custom date such as First Sunday of July.
    /// </summary>
    /// <returns></returns>
    RecurrenceValues getCustomDayOfYearDates() throws Exception {
        if (this.specificDatePartOne == YearlySpecificDatePartOne.NotSet)
            throw new Exception("The First part of the custom date has not been set.");
        if (this.specificDatePartTwo == YearlySpecificDatePartTwo.NotSet)
            throw new Exception("The Second part of the custom date has not been set.");
        if (this.specificDatePartThree == YearlySpecificDatePartThree.NotSet)
            throw new Exception("The Third part of the custom date has not been set.");

        RecurrenceValues values = new RecurrenceValues();
        LocalDateTime dt = this.startDate;
        int year = dt.getYear();

        if (getNextDateValue) {
            do {
                dt = getCustomDate(year);
                // If the date returned is less than the start date
                // then do it again to increment past the start date
                if (dt.compareTo(this.startDate) < 0) {
                    year++;
                    dt = getCustomDate(year);
                }
                year++;
                values.AddDateValue(dt, adjustmentValue);
                if (values.values.get(values.values.size() - 1).compareTo(nextDateValue) > 0)
                    break;
            } while (dt.compareTo(nextDateValue.plusYears(1)) <= 0); // Ensure the last date if greater than what's needed for the next date in the cycle
        } else {
            switch (this.endDateType) {
                case NoEndDate:
                    throw new Exception("The ability to create recurring dates with no End date is not currently available.");

                case NumberOfOccurrences:
                    for (int i = 0; i < this.numberOfOccurrences; i++) {
                        dt = getCustomDate(year);
                        // If the date returned is less than the start date
                        // then do it again to increment past the start date
                        if (dt.compareTo(this.startDate) < 0) {
                            year++;
                            dt = getCustomDate(year);
                        }
                        year++;
                        values.AddDateValue(dt, adjustmentValue);
                    }
                    break;

                case SpecificDate:
                    do {
                        dt = getCustomDate(year);
                        // If the date returned is less than the start date
                        // then do it again to increment past the start date
                        if (dt.compareTo(this.startDate) < 0) {
                            year++;
                            dt = getCustomDate(year);
                        }
                        year++;
                        values.AddDateValue(dt, adjustmentValue);
                    } while (dt.compareTo(this.endDate) <= 0);
                    break;

                default:
                    throw new IllegalArgumentException("The TypeOfEndDate property has not been set.");
            }
        }
        return values;

    }
//#endregion //Internal Procedures

    /// <summary>
    /// Get the custom value from the 1st, 2nd, and 3rd custom date parts
    /// </summary>
    /// <param name="year"></param>
    /// <returns></returns>
    LocalDateTime getCustomDate(int year) {
        LocalDateTime dt = LocalDateTime.of(year, specificDatePartThree.getValue(), 1, 0, 0); //new LocalDateTime(year, (int) SpecificDatePartThree, 1);
        int day = 1;
        int firstPart = specificDatePartOne.getValue() + 1;
        int daysOfMonth = YearMonth.of(year, dt.getMonth()).lengthOfMonth();

        switch (specificDatePartTwo) {
            case Day:
                // If only getting the Last day of the month
                if (specificDatePartOne == YearlySpecificDatePartOne.Last)
                    dt = LocalDateTime.of(year, dt.getMonth(), YearMonth.of(year, dt.getMonth()).lengthOfMonth(), 0, 0);
                else
                    // Get a specific day of the month such as First, Second, Third, Fourth
                    dt = LocalDateTime.of(year, dt.getMonth(), firstPart, 0, 0);
                break;

            case Weekday:
                int weekDayCount = 0;
                LocalDateTime lastWeekday = dt;
                do {
                    // Check for anything other than Saturday and Sunday
//                    if (dt.DayOfWeek != DayOfWeek.Saturday && dt.DayOfWeek != DayOfWeek.Sunday) {
                    if (dt.getDayOfWeek() != DayOfWeek.SATURDAY && dt.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        // Get a specific Weekday of the Month
                        if (specificDatePartOne != YearlySpecificDatePartOne.Last) {
                            // Add up the weekday count
                            weekDayCount++;
                            // If the current weekday count matches then exit
                            if (weekDayCount == firstPart)
                                break;
                        } else {
                            // Get the last weekday of the month
                            lastWeekday = dt;
                        }
                    }
                    dt = dt.plusDays(1);
                    day++;
                } while (day <= daysOfMonth);

                // If getting the last weekday of the month then
                // set the returning value to the last weekday found.
                if (specificDatePartOne == YearlySpecificDatePartOne.Last)
                    dt = lastWeekday;

                break;

            case WeekendDay:
                int weekendDayCount = 0;
                LocalDateTime lastWeekendday = dt;
                do {
                    // Check for anything other than Saturday and Sunday
                    if (dt.getDayOfWeek() == DayOfWeek.SATURDAY || dt.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        // Get a specific Weekday of the Month
                        if (specificDatePartOne != YearlySpecificDatePartOne.Last) {
                            // Add up the weekday count
                            weekendDayCount++;
                            // If the current weekday count matches then exit
                            if (weekendDayCount == firstPart)
                                break;
                        } else {
                            // Get the last weekday of the month
                            lastWeekendday = dt;
                        }
                    }
                    dt = dt.plusDays(1);
                    day++;
                } while (day <= daysOfMonth);

                // If getting the last weekday of the month then
                // set the returning value to the last weekday found.
                if (specificDatePartOne == YearlySpecificDatePartOne.Last)
                    dt = lastWeekendday;

                break;

            case Monday:
                dt = getCustomWeekday(dt, DayOfWeek.MONDAY, daysOfMonth, firstPart);
                break;

            case Tuesday:
                dt = getCustomWeekday(dt, DayOfWeek.TUESDAY, daysOfMonth, firstPart);
                break;

            case Wednesday:
                dt = getCustomWeekday(dt, DayOfWeek.WEDNESDAY, daysOfMonth, firstPart);
                break;

            case Thursday:
                dt = getCustomWeekday(dt, DayOfWeek.THURSDAY, daysOfMonth, firstPart);
                break;

            case Friday:
                dt = getCustomWeekday(dt, DayOfWeek.FRIDAY, daysOfMonth, firstPart);
                break;

            case Saturday:
                dt = getCustomWeekday(dt, DayOfWeek.SATURDAY, daysOfMonth, firstPart);
                break;

            case Sunday:
                dt = getCustomWeekday(dt, DayOfWeek.SUNDAY, daysOfMonth, firstPart);
                break;
        }
        return dt;
    }

    LocalDateTime getCustomWeekday(LocalDateTime startDate, DayOfWeek weekDay, int daysOfMonth, int firstDatePart) {
        int day = 1;
        int dayCount = 0;
        LocalDateTime lastDOW = startDate;
        LocalDateTime returnDate = startDate;
        do {
            // Check for day of the week
            if (returnDate.getDayOfWeek() == weekDay) {
                // Get a specific Weekday of the Month
                if (specificDatePartOne != YearlySpecificDatePartOne.Last) {
                    // Add up the days found count
                    dayCount++;
                    // If the current weekday count matches then exit
                    if (dayCount == firstDatePart) {
                        break;
                    }
                } else {
                    // Get the current date value
                    lastDOW = returnDate;
                }
            }
            returnDate = returnDate.plusDays(1);
            day++;
        } while (day <= daysOfMonth);

        // If getting the last weekday of the month then
        // set the returning value to the last weekday found.
        if (specificDatePartOne == YearlySpecificDatePartOne.Last)
            returnDate = lastDOW;

        return returnDate;
    }

    int getDatePartOneValue() {
        int val = 0;
        switch (specificDatePartOne) {
            case First:
                val = 1;
                break;
            case Second:
                val = 2;
                break;
            case Third:
                val = 3;
                break;
            case Fourth:
                val = 4;
                break;
        }
        return val;
    }

    /// <summary>
    /// The Regeneration type. Is this on a specific day of the month, a custom date, or after the occurrence is completed.
    /// </summary>
    public enum YearlyRegenType {
        NotSet(-1), OnSpecificDayOfYear(0), OnCustomDateFormat(1), AfterOccurrenceCompleted(2);
        int intVal;

        YearlyRegenType(int i) {
            intVal = i;
        }

        public static YearlyRegenType fromValue(int intVal) {
            for (YearlyRegenType e : YearlyRegenType.values()) {
                if (e.intVal == intVal) {
                    return e;
                }
            }
            return null;
        }

        public String toString() {
            return String.valueOf(intVal);
        }
    }

    /// <summary>
    /// First part of a custom date. This would be First, Second, etc. item of the month.
    /// </summary>
    public enum YearlySpecificDatePartOne {
        NotSet(-1), First(0), Second(1), Third(2), Fourth(3), Last(4);

        int intVal;

        YearlySpecificDatePartOne(int s) {
            intVal = s;
        }

        public static YearlySpecificDatePartOne fromValue(int intVal) {
            for (YearlySpecificDatePartOne e : YearlySpecificDatePartOne.values()) {
                if (e.intVal == intVal) {
                    return e;
                }
            }
            return null;
        }

        public String toString() {
            return Character.toString((char) (intVal + 65));
        }

        public int getValue() {
            return intVal;
        }
    }

    /// <summary>
    /// Second part of a custom date. This is day of week, weekend day, etc.
    /// </summary>
    public enum YearlySpecificDatePartTwo {
        NotSet(-1), Day(0), Weekday(1), WeekendDay(2), Sunday(3), Monday(4), Tuesday(5), Wednesday(6), Thursday(7), Friday(8), Saturday(9);

        int intVal;

        YearlySpecificDatePartTwo(int i) {
            intVal = i;
        }

        public static YearlySpecificDatePartTwo fromValue(int intVal) {
            for (YearlySpecificDatePartTwo e : YearlySpecificDatePartTwo.values()) {
                if (e.intVal == intVal) {
                    return e;
                }
            }
            return null;
        }

        public String toString() {
            return Character.toString((char) (intVal + 65));
        }
    }

    /// <summary>
    /// Third part of a custom date. This is the Month of the year for which the custom date confines to..
    /// The value of this enum matches the ordinal position of the given month. So Jan = 1, Feb = 2, etc.
    /// </summary>
    public enum YearlySpecificDatePartThree {
        NotSet(-1), January(1), February(2), March(3), April(4), May(5), June(6), July(7), August(8), September(9), October(10), November(11), December(12);

        int intVal;

        YearlySpecificDatePartThree(int i) {
            intVal = i;
        }

        public static YearlySpecificDatePartThree fromValue(int intVal) {
            for (YearlySpecificDatePartThree e : YearlySpecificDatePartThree.values()) {
                if (e.intVal == intVal) {
                    return e;
                }
            }
            return null;
        }

        public int getValue() {
            return intVal;
        }

        public String toString() {
            return Character.toString((char) (intVal + 64));
        }
    }
//#endregion //Private Procedures

}
