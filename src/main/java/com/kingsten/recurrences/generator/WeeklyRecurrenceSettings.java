package com.kingsten.recurrences.generator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@Data
@ToString
public class WeeklyRecurrenceSettings extends RecurrenceSettings {

    private WeeklyRegenType regenType = WeeklyRegenType.OnEveryXWeeks;
    private SelectedDayOfWeekValues selectedDays;
    private int regenEveryXWeeks;
    private boolean getNextDateValue;
    private LocalDateTime nextDateValue;

    /// <summary>
    /// Get dates by Start date only. This is for no ending date values.
    /// </summary>
    /// <param name="startDate"></param>
    public WeeklyRecurrenceSettings(LocalDateTime startDate) {
        super(startDate);
    }

    /// <summary>
    /// Get dates by Start and End date boundries.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="endDate"></param>
    public WeeklyRecurrenceSettings(LocalDateTime startDate, LocalDateTime endDate) {
        super(startDate, endDate);
    }

    /// <summary>
    /// Get dates by Start date and number of occurrences.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="numberOfOccurrences"></param>
    public WeeklyRecurrenceSettings(LocalDateTime startDate, int numberOfOccurrences) {
        super(startDate, numberOfOccurrences);
    }

    static RecurrenceInfo getFriendlyRecurrenceInfo(String seriesInfo) {
        RecurrenceInfo info = new RecurrenceInfo();
        EndDateType endType = EndDateType.NotDefined;
        LocalDateTime endDateValue = LocalDateTime.MIN;
        int noOccurrences;

        // Exit if not a Weekly seriesInfo type
        if (!seriesInfo.startsWith("W"))
            return null;

        info.setSeriesInfo(seriesInfo);
        info.setRecurrenceType(RecurrenceInfo.RecurrenceType.Weekly);

        // FORMATTING DEFINITIONS
        //  W = Weekly Designator
        //  |       0 = Start Date (8 chars)
        //  |       |        1 = End Date (8 chars)
        //  |       |        |        2 = Number of occurrences (4 chars)
        //  |       |        |        |      3 = Regeneration Type (1 char)
        //  |       |        |        |      |    4 = End date type (1 char)
        //  |       |        |        |      |    |      5 = Regenerate on Sunday
        //  |       |        |        |      |    |      |       6 = Regenerate on Monday
        //  |       |        |        |      |    |      |       |       7 = Regenerate on Tuesday
        //  |       |        |        |      |    |      |       |       |       8 = Regenerate on Wednesday
        //  |       |        |        |      |    |      |       |       |       |      9 = Regenerate on Thursday
        //  |       |        |        |      |    |      |       |       |       |      |      10 = Regenerate on Friday
        //  |       |        |        |      |    |      |       |       |       |      |       |      11 = Regenerate on Saturday
        //  |       |        |        |      |    |      |       |       |       |      |       |      |        12  Regen Every x weeks
        //  |       |        |        |      |    |      |       |       |       |      |       |      |         |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22]  [23]    [24]    [25]    [26]   [27]     [28]   [29]      [30-32]
        //  W   20071231  20171231   0000    1     1     T       T      F        F      F       F      F        000
        String startDate = seriesInfo.substring(1, 9);

        LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime dtStartDate = date.atStartOfDay();

        String endDate = seriesInfo.substring(9, 17);
        SelectedDayOfWeekValues selectedDays = new SelectedDayOfWeekValues();
        String occurrences = seriesInfo.substring(17, 21);
        String weeklyRegenType = seriesInfo.substring(21, 22);
        String endDateType = seriesInfo.substring(22, 23);
        int regenXWeeks = Integer.valueOf(seriesInfo.substring(30, 33));

        endType = EndDateType.fromValue(endDateType);
        noOccurrences = Integer.valueOf(occurrences);

        selectedDays.Sunday = seriesInfo.substring(23, 24).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Monday = seriesInfo.substring(24, 25).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Tuesday = seriesInfo.substring(25, 26).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Wednesday = seriesInfo.substring(26, 27).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Thursday = seriesInfo.substring(27, 28).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Friday = seriesInfo.substring(28, 29).equalsIgnoreCase("Y") ? true : false;
        selectedDays.Saturday = seriesInfo.substring(29, 30).equalsIgnoreCase("Y") ? true : false;

        // Get the EndDate before any modifications on it are performed
        if (endType == EndDateType.SpecificDate) {
            date = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);
            endDateValue = date.atStartOfDay();
        }

        info.setEndDateType(endType);
        // Determine the Constructor by the type of End Date.
        // All constructors start with a Start date at a minimum.
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

        info.setWeeklyRegenType(WeeklyRegenType.fromValue(weeklyRegenType));
        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.weeklyRegenType) {
            case OnEveryXWeeks:
                info.setSelectedDayOfWeekValues(selectedDays);
                info.setRegenEveryXWeeks(regenXWeeks);
                break;

            case NotSet:

                break;
        }

        return info;


    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo) {
        return getRecurrenceSettings(seriesInfo, -1, LocalDateTime.MIN);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(LocalDateTime modifiedStartDate, String seriesInfo) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, LocalDateTime.MIN);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue) {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, LocalDateTime.MIN);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, int modifiedOccurrencesValue) {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, modifiedStartDate, LocalDateTime.MIN);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedEndDate) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedEndDate);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, modifiedEndDate);
    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) {
        WeeklyRecurrenceSettings settings = null;
        RecurrenceInfo info = WeeklyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);

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
                settings = new WeeklyRecurrenceSettings(modifiedStartDate, info.numberOfOccurrences);
                break;

            case SpecificDate:
                settings = new WeeklyRecurrenceSettings(modifiedStartDate, info.endDate);
                break;

            case NoEndDate:
                settings = new WeeklyRecurrenceSettings(modifiedStartDate);
                break;
        }

        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.weeklyRegenType) {
            case OnEveryXWeeks:
                settings.setValues(info.regenEveryXWeeks, info.selectedDayOfWeekValues);
                break;

            case NotSet:

                break;
        }

        return settings;

    }

    static WeeklyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedEndDate) {
        RecurrenceInfo info = WeeklyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);

        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, info.startDate, modifiedEndDate);
    }

    static String getPatternDefinition(String seriesInfo) {
        String returnValue = "";
        returnValue =
                " WEEKLY FORMATTING DEFINITIONS \r\n" +
                        "  W = Weekly Designator \r\n" +
                        "  |       0 = Start Date (8 chars) \r\n" +
                        "  |       |        1 = End Date (8 chars) \r\n" +
                        "  |       |        |        2 = Number of occurrences (4 chars) \r\n" +
                        "  |       |        |        |      3 = Regeneration Type (1 char) \r\n" +
                        "  |       |        |        |      |    4 = End date type (1 char) \r\n" +
                        "  |       |        |        |      |    |      5 = Regenerate on Sunday \r\n" +
                        "  |       |        |        |      |    |      |       6 = Regenerate on Monday \r\n" +
                        "  |       |        |        |      |    |      |       |       7 = Regenerate on Tuesday \r\n" +
                        "  |       |        |        |      |    |      |       |       |       8 = Regenerate on Wednesday \r\n" +
                        "  |       |        |        |      |    |      |       |       |       |      9 = Regenerate on Thursday \r\n" +
                        "  |       |        |        |      |    |      |       |       |       |      |      10 = Regenerate on Friday \r\n" +
                        "  |       |        |        |      |    |      |       |       |       |      |       |      11 = Regenerate on Saturday \r\n" +
                        "  |       |        |        |      |    |      |       |       |       |      |       |      |        12  Regen Every x weeks \r\n" +
                        "  |       |        |        |      |    |      |       |       |       |      |       |      |         | \r\n" +
                        " [0]    [1-8]    [9-16]  [17-20]  [21] [22]  [23]    [24]    [25]    [26]   [27]     [28]   [29]      [30-32] \r\n" +
                        String.format("  W   %s  %s   %s    %s     %s     %s       %s       %s      %s       %s       %s      %s         %s\r\n", seriesInfo.substring(1, 9),
                                seriesInfo.substring(9, 17), seriesInfo.substring(17, 21), seriesInfo.substring(21, 22), seriesInfo.substring(22, 23),
                                seriesInfo.substring(23, 24), seriesInfo.substring(24, 25), seriesInfo.substring(25, 26), seriesInfo.substring(26, 27), seriesInfo.substring(27, 28),
                                seriesInfo.substring(28, 29), seriesInfo.substring(29, 30), seriesInfo.substring(30, 33));
        return returnValue;
    }

    /// <summary>
    /// Get day values. This overload is for every x-weeks.
    /// </summary>
    /// <param name="regenEveryXDays">Interval of weeks. Every x-weeks.</param>
    /// <returns>RecurrenceValues</returns>
    public RecurrenceValues getValues(int regenEveryXWeeks, SelectedDayOfWeekValues selectedDays) throws Exception {
        this.regenEveryXWeeks = regenEveryXWeeks;
        regenType = WeeklyRegenType.OnEveryXWeeks;
        this.selectedDays = selectedDays;
        return getValues();
    }

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
            case OnEveryXWeeks:
                values = getEveryXWeeksValues();
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

    RecurrenceValues getEveryXWeeksValues() throws Exception {
        RecurrenceValues values = new RecurrenceValues();
        LocalDateTime dt = this.startDate.plusDays(-1); // Backup a day so the first instance of GetNextDay will increment to the next day.

        if (getNextDateValue) {
            do {
                dt = getNextDay(dt);
                values.AddDateValue(dt);
                if (values.values.get(values.values.size() - 1).compareTo(nextDateValue) > 0)
                    break;
            } while (dt.compareTo(nextDateValue.plusDays((regenEveryXWeeks * 7) + 7)) <= 0); // Ensure the last date if greater than what's needed for the next date in the cycle
        } else {
            switch (this.endDateType) {
                case NoEndDate:
                    throw new Exception("The ability to create recurring dates with no End date is not currently available.");

                case NumberOfOccurrences:

                    for (int i = 0; i < this.numberOfOccurrences; i++) {
                        dt = getNextDay(dt);
                        values.AddDateValue(dt);
                    }
                    break;

                case SpecificDate:
                    do {
                        dt = getNextDay(dt);
                        // Handle for dates past the end date
                        if (dt.compareTo(this.endDate) > 0)
                            break;

                        values.AddDateValue(dt);
                    } while (dt.compareTo(this.endDate) <= 0);
                    break;

                default:
                    throw new IllegalArgumentException("The TypeOfEndDate property has not been set.");
            }
        }

        return values;
    }

    LocalDateTime getNextDay(LocalDateTime input) {
        LocalDateTime returnDate = null;

        // Get the return date by incrementing the date
        // and checking the value against the selected days
        // of the week.
        do {
            input = input.plusDays(1);
            switch (input.getDayOfWeek()) {
                case SUNDAY:
                    if (selectedDays.Sunday)
                        returnDate = input;
                    break;
                case MONDAY:
                    if (selectedDays.Monday)
                        returnDate = input;
                    break;
                case TUESDAY:
                    if (selectedDays.Tuesday)
                        returnDate = input;
                    break;
                case WEDNESDAY:
                    if (selectedDays.Wednesday)
                        returnDate = input;
                    break;
                case THURSDAY:
                    if (selectedDays.Thursday)
                        returnDate = input;
                    break;
                case FRIDAY:
                    if (selectedDays.Friday)
                        returnDate = input;
                    break;
                case SATURDAY:
                    if (selectedDays.Saturday)
                        returnDate = input;
                    else {
                        // Increment by weeks if regenXWeeks has a value
                        // greater than 1 which is default.
                        // But only increment if we've gone over
                        // at least 7 days already.
                        if (regenEveryXWeeks > 1)
                            input = input.plusDays((regenEveryXWeeks - 1) * 7);
                    }
                    break;
            }
        } while (returnDate == null);
        return returnDate;
    }

    /// <summary>
    /// Get the Series information that's used to get dates at a later
    /// date. This is passed into the RecurrenceHelper to get date values.
    /// Most likely used for non-ending dates.
    /// </summary>
    /// <returns></returns>
    String getSeriesInfo() {
        String info = "";
        String endDate = "ZZZZZZZZ"; // Default for no ending date.
        String occurrences = String.format("%04d", this.numberOfOccurrences);
        String weeklyRegenType = this.regenType.toString();
        String endDateType = this.endDateType.toString();
        String regenXWeeks = String.format("%03d", this.regenEveryXWeeks);
        String daySelectedSunday = selectedDays.Sunday ? "Y" : "N";
        String daySelectedMonday = selectedDays.Monday ? "Y" : "N";
        String daySelectedTuesday = selectedDays.Tuesday ? "Y" : "N";
        String daySelectedWednesday = selectedDays.Wednesday ? "Y" : "N";
        String daySelectedThursday = selectedDays.Thursday ? "Y" : "N";
        String daySelectedFriday = selectedDays.Friday ? "Y" : "N";
        String daySelectedSaturday = selectedDays.Saturday ? "Y" : "N";

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
        //  |       |        |        |      |    |      5 = Regenerate on Sunday
        //  |       |        |        |      |    |      |       6 = Regenerate on Monday
        //  |       |        |        |      |    |      |       |       7 = Regenerate on Tuesday
        //  |       |        |        |      |    |      |       |       |       8 = Regenerate on Wednesday
        //  |       |        |        |      |    |      |       |       |       |      9 = Regenerate on Thursday
        //  |       |        |        |      |    |      |       |       |       |      |      10 = Regenerate on Friday
        //  |       |        |        |      |    |      |       |       |       |      |       |      11 = Regenerate on Saturday
        //  |       |        |        |      |    |      |       |       |       |      |       |      |        12  Regen Every x weeks
        //  |       |        |        |      |    |      |       |       |       |      |       |      |         |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22]  [23]    [24]    [25]    [26]   [27]     [28]   [29]      [30]
        //  W   20071231  01082017   0000    1     1     T       T      F        F      F       F      F        000
        info = String.format("W%s%s%s%s%s%s%s%s%s%s%s%s%s", DateTimeFormatter.BASIC_ISO_DATE.format(this.startDate), endDate, occurrences, weeklyRegenType, endDateType, daySelectedSunday, daySelectedMonday, daySelectedTuesday, daySelectedWednesday, daySelectedThursday, daySelectedFriday, daySelectedSaturday, regenXWeeks);
        return info;
    }

    /// <summary>
    ///     Get the next date
    /// </summary>
    /// <param name="currentDate" type="System.DateTime">
    ///     <para>
    ///
    ///     </para>
    /// </param>
    /// <returns>
    ///     A System.DateTime value...
    /// </returns>
    LocalDateTime getNextDate(LocalDateTime currentDate) throws Exception {
        getNextDateValue = true;
        nextDateValue = currentDate;
        // Now get the values and return the last date found.
        RecurrenceValues values = getValues();
        return values.endDate;
    }

    /// <summary>
    /// Set the values in preperation for getting the Next date in the series.
    /// </summary>
    /// <param name="regenEveryXWeeks">Value to regenerate the dates every x-weeks</param>
    /// <param name="selectedDays">Struct of days selected for the week.</param>
    void setValues(int regenEveryXWeeks, SelectedDayOfWeekValues selectedDays) {
        this.regenEveryXWeeks = regenEveryXWeeks;
        regenType = WeeklyRegenType.OnEveryXWeeks;
        this.selectedDays = selectedDays;
    }

    public enum WeeklyRegenType {
        NotSet("-1"), OnEveryXWeeks("0");
        String strVal;

        WeeklyRegenType(String s) {
            strVal = s;
        }

        public static WeeklyRegenType fromValue(String strVal) {
            for (WeeklyRegenType e : WeeklyRegenType.values()) {
                if (e.strVal.equalsIgnoreCase(strVal)) {
                    return e;
                }
            }
            return null;
        }

        public String toString() {
            return strVal;
        }
    }

    public static class SelectedDayOfWeekValues {
        public boolean Sunday;
        public boolean Monday;
        public boolean Tuesday;
        public boolean Wednesday;
        public boolean Thursday;
        public boolean Friday;
        public boolean Saturday;
    }
}
