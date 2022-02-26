package com.kingsten.recurrences.generator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.kingsten.recurrences.generator.RecurrenceException.GENERIC_EXCEPTION_CODE;

@Data
@NoArgsConstructor
@ToString
public class DailyRecurrenceSettings extends RecurrenceSettings {

    private DailyRegenType regenType = DailyRegenType.ON_EVERY_WEEKDAY;
    private int regenEveryXDays = 1;
    private boolean getNextDateValue;
    private LocalDateTime nextDateValue;
    private LocalDateTime finalNextDateValue;

    /// <summary>
    /// Get dates by Start date only. This is for no ending date values.
    /// </summary>
    /// <param name="startDate"></param>
    public DailyRecurrenceSettings(LocalDateTime startDate) {
        super(startDate);
    }

    /// <summary>
    /// Get dates by Start and End date boundries.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="endDate"></param>
    public DailyRecurrenceSettings(LocalDateTime startDate, LocalDateTime endDate) {
        super(startDate, endDate);
    }

    /// <summary>
    /// Get dates by Start date and number of occurrences.
    /// </summary>
    /// <param name="startDate"></param>
    /// <param name="numberOfOccurrences"></param>
    public DailyRecurrenceSettings(LocalDateTime startDate, int numberOfOccurrences) {
        super(startDate, numberOfOccurrences);
    }

    static String getPatternDefinition(String seriesInfo) {
        String returnValue = "";
        returnValue =
                " DAILY FORMATTING DEFINITIONS\r\n" +
                        "  D = Daily Designator\r\n" +
                        "  |       0 = Start Date (8 chars)\r\n" +
                        "  |       |        1 = End Date (8 chars)\r\n" +
                        "  |       |        |        2 = Number of occurrences (4 chars)\r\n" +
                        "  |       |        |        |      3 = Regeneration Type (1 char)\r\n" +
                        "  |       |        |        |      |    4 = End date type (1 char)\r\n" +
                        "  |       |        |        |      |    |      5 = Regen Every x weeks\r\n" +
                        "  |       |        |        |      |    |      |\r\n" +
                        "  |       |        |        |      |    |      |\r\n" +
                        " [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-25]\r\n" +
                        String.format("  D   %s  %s   %s    %s     %s     %s \r%n", seriesInfo.substring(1, 9), seriesInfo.substring(9, 17), seriesInfo.substring(17, 21),
                                seriesInfo.substring(21, 22), seriesInfo.substring(22, 23), seriesInfo.substring(23, 26));
        return returnValue;
    }

    static RecurrenceInfo getFriendlyRecurrenceInfo(String seriesInfo) {
        RecurrenceInfo info = new RecurrenceInfo();
        LocalDateTime endDateValue = LocalDateTime.MIN;
        int noOccurrences;

        // Exit if not a Daily seriesInfo type
        if (!seriesInfo.startsWith("D"))
            return null;

        info.setRecurrenceType(RecurrenceInfo.RecurrenceType.Daily);
        info.setSeriesInfo(seriesInfo);
        // FORMATTING DEFINITIONS
        //  D = Daily Designator
        //  |       0 = Start Date (8 chars)
        //  |       |        1 = End Date (8 chars)
        //  |       |        |        2 = Number of occurrences (4 chars)
        //  |       |        |        |      3 = Regeneration Type (1 char)
        //  |       |        |        |      |    4 = End date type (1 char)
        //  |       |        |        |      |    |      5 = Regen Every x weeks
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-25]
        //  D   20071231  20171231   0000    1     1    000
        String startDate = seriesInfo.substring(1, 9);

        LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime dtStartDate = date.atStartOfDay();

        String endDate = seriesInfo.substring(9, 17);
        String occurrences = seriesInfo.substring(17, 21);
        String dailyRegenType = seriesInfo.substring(21, 22);
        String endDateType = seriesInfo.substring(22, 23);
        int regenXDays = Integer.parseInt(seriesInfo.substring(23, 26));
        EndDateType endType = EndDateType.fromValue(endDateType);
        noOccurrences = Integer.parseInt(occurrences);

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
            default:
                break;
        }

        info.setDailyRegenType(DailyRegenType.fromValue(dailyRegenType));
        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.dailyRegenType) {
            case ON_EVERY_X_DAYS:
                info.setDailyRegenEveryXDays(regenXDays);
                break;

            case ON_EVERY_WEEKDAY:
                // This is default. Nothing to set
                break;

            case NOTSET:

                break;
        }

        return info;


    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, -1, LocalDateTime.MIN);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(LocalDateTime modifiedStartDate, String seriesInfo) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, LocalDateTime.MIN);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, LocalDateTime.MIN);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, int modifiedOccurrencesValue) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, modifiedStartDate, LocalDateTime.MIN);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedEndDate) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, -1, modifiedEndDate);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedEndDate) throws RecurrenceException {
        RecurrenceInfo info = DailyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
        if (Objects.isNull(info))
            throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "failed to parse recurrence info");
        return getRecurrenceSettings(seriesInfo, modifiedOccurrencesValue, info.startDate, modifiedEndDate);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) throws RecurrenceException {
        return getRecurrenceSettings(seriesInfo, -1, modifiedStartDate, modifiedEndDate);
    }

    static DailyRecurrenceSettings getRecurrenceSettings(String seriesInfo, int modifiedOccurrencesValue, LocalDateTime modifiedStartDate, LocalDateTime modifiedEndDate) throws RecurrenceException {
        DailyRecurrenceSettings settings = null;
        RecurrenceInfo info = DailyRecurrenceSettings.getFriendlyRecurrenceInfo(seriesInfo);
        if (Objects.isNull(info))
            throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "failed to parse recurrence info");
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
                settings = new DailyRecurrenceSettings(modifiedStartDate, info.numberOfOccurrences);
                break;

            case SpecificDate:
                settings = new DailyRecurrenceSettings(modifiedStartDate, info.endDate);
                break;

            case NoEndDate:
                settings = new DailyRecurrenceSettings(modifiedStartDate);
                break;
            default:
                throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "Invalid EndDate Type: " + info.endDateType);
        }

        // Determine the Type of dates to get, specific, custom, etc.
        switch (info.dailyRegenType) {
            case ON_EVERY_X_DAYS:
                settings.setValues(info.dailyRegenEveryXDays);
                break;

            case ON_EVERY_WEEKDAY:
                // This is default. Nothing to set
                break;

            case NOTSET:

                break;
        }

        return settings;
    }

    /// <summary>
    /// Get day values. This overload is for every x-days.
    /// </summary>
    /// <param name="regenEveryXDays">Interval of days. Every x-days.</param>
    /// <returns>RecurrenceValues</returns>
    public RecurrenceValues getValues(int regenEveryXDays) throws Exception {
        this.regenEveryXDays = regenEveryXDays;
        regenType = DailyRegenType.ON_EVERY_X_DAYS;
        return getValues();
    }

    /// <summary>
    ///     An overload to use to get either every weekday or just every x-days
    /// </summary>
    /// <param name="regenEveryXDays" type="int">
    ///     <para>
    ///         Interval of days. Every x-days.
    ///     </para>
    /// </param>
    /// <param name="regenType" type="BOCA.RecurrenceGenerator.DailyRegenType">
    ///     <para>
    ///         Type of regeneration to perform. Every x-days or every weekday.
    ///     </para>
    /// </param>
    /// <returns>
    ///     A RecurrenceGenerator.RecurrenceValues value...
    /// </returns>
    public RecurrenceValues getValues(int regenEveryXDays, DailyRegenType regenType) throws Exception {
        this.regenEveryXDays = regenEveryXDays;
        this.regenType = regenType;
        return getValues();
    }

    @Override
    LocalDateTime getNextDate(LocalDateTime currentDate) throws Exception {
        getNextDateValue = true;
        nextDateValue = currentDate;
        // Run the date processing to set the last date value.
        getValues();
        return finalNextDateValue;
    }

    @Override
    RecurrenceValues getValues() throws Exception {
        return getRecurrenceValues();
    }

    void setValues(int regenEveryXDays) {
        this.regenEveryXDays = regenEveryXDays;
        regenType = DailyRegenType.ON_EVERY_X_DAYS;
    }

    @Override
    RecurrenceValues getValues(LocalDateTime startDate, int numberOfOccurrences) throws Exception {
        this.numberOfOccurrences = numberOfOccurrences;
        this.startDate = startDate;
        // Change the end type to number of occurrences.
        // This must be set because the original starting Series Info may
        // be set to have an End Date type.
        this.endDateType = EndDateType.NumberOfOccurrences;

        return getRecurrenceValues();
    }

    @Override
    RecurrenceValues getValues(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        this.startDate = startDate;
        this.endDate = endDate;
        // Change the end type to End Date as this original series info
        // may have been set to number of occurrences.
        this.endDateType = EndDateType.SpecificDate;
        return getRecurrenceValues();
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
        String dailyRegenType = this.regenType.toString();
        String endDateType = this.endDateType.toString();
        String regenXDays = String.format("%03d", regenEveryXDays);

        // End Date may be null if no ending date.
        if (this.hasEndDate())
            endDate = DateTimeFormatter.BASIC_ISO_DATE.format(this.endDate);

        // FORMATTING DEFINITIONS
        //  D = Daily Designator
        //  |       0 = Start Date (8 chars)
        //  |       |        1 = End Date (8 chars)
        //  |       |        |        2 = Number of occurrences (4 chars)
        //  |       |        |        |      3 = Regeneration Type (1 char)
        //  |       |        |        |      |    4 = End date type (1 char)
        //  |       |        |        |      |    |      5 = Regen Every x weeks
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        //  |       |        |        |      |    |      |
        // [0]    [1-8]    [9-16]  [17-20]  [21] [22] [23-25]
        //  D   20071231  20171231   0000    1     1    000
        info = String.format("D%s%s%s%s%s%s", DateTimeFormatter.BASIC_ISO_DATE.format(this.startDate), endDate, occurrences, dailyRegenType, endDateType, regenXDays);
        return info;
    }

    RecurrenceValues getRecurrenceValues() throws RecurrenceException {
        RecurrenceValues values = null;
        switch (regenType) {
            case ON_EVERY_X_DAYS:
                values = getEveryXDaysValues();
                break;

            case ON_EVERY_WEEKDAY:
                values = getEveryWeekday();
                break;
            default:
                throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "unknown regenType");
        }
        // Values will be null if just getting next date in series. No need
        // to fill the RecurrenceValues collection if all we need is the last date.
        if (values != null) {
            if (!values.values.isEmpty()) {
                values.setStartDate(values.values.get(0));

                // Get the end date if not open-ended
                if (this.endDateType != EndDateType.NoEndDate)
                    values.setEndDate(values.values.get(values.values.size() - 1));
            }
            // Set the Series information that's used to get the next date
            // values for no ending dates.
            values.setSeriesInfo(getSeriesInfo());
        }

        return values;

    }

    /// <summary>
    /// Get the values for just weekdays.
    /// </summary>
    /// <returns>RecurrenceValues</returns>
    RecurrenceValues getEveryWeekday() throws RecurrenceException {
        RecurrenceValues values;
        LocalDateTime dt = this.startDate;
        // Make sure the first date is a weekday
        if (dt.getDayOfWeek() == DayOfWeek.SATURDAY || dt.getDayOfWeek() == DayOfWeek.SUNDAY)
            dt = getNextWeekday(dt);

        if (getNextDateValue) {
            do {
                finalNextDateValue = dt;
                if (finalNextDateValue.compareTo(nextDateValue) > 0)
                    break;
                dt = getNextWeekday(dt);
            } while (dt.compareTo(nextDateValue.plusDays(3)) <= 0);  // Make sure it's past what may possibly be the next weekday.
            return null;
        } else {
            values = new RecurrenceValues();
            switch (this.endDateType) {
                case NoEndDate:
                    throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "The ability to create recurring dates with no End date is not currently available.");

                case NumberOfOccurrences:

                    for (int i = 0; i < this.numberOfOccurrences; i++) {
                        values.AddDateValue(dt);
                        dt = getNextWeekday(dt);
                    }
                    break;

                case SpecificDate:
                    do {
                        values.AddDateValue(dt);
                        dt = getNextWeekday(dt);
                    } while (dt.compareTo(this.endDate) <= 0);
                    break;

                default:
                    throw new IllegalArgumentException("The TypeOfEndDate property has not been set.");
            }
            return values;
        }
    }

    /// <summary>
    /// Get the next Weekday value. This will increment the input date until it finds the next non-Saturday and non-Sunday dates.
    /// </summary>
    /// <param name="input"></param>
    /// <returns>DateTime</returns>
    LocalDateTime getNextWeekday(LocalDateTime input) {
        do {
            input = input.plusDays(1);
        } while (input.getDayOfWeek() == DayOfWeek.SATURDAY || input.getDayOfWeek() == DayOfWeek.SUNDAY);

        return input;
    }

    /// <summary>
    /// Get dates for every x-days starting from the start date.
    /// </summary>
    /// <returns></returns>
    RecurrenceValues getEveryXDaysValues() throws RecurrenceException {
        RecurrenceValues values;
        LocalDateTime dt = this.startDate;

        if (getNextDateValue) {
            do {
                finalNextDateValue = dt;
                if (finalNextDateValue.compareTo(nextDateValue) > 0)
                    break;
                dt = dt.plusDays(regenEveryXDays);
            } while (dt.compareTo(nextDateValue.plusDays(Long.valueOf(regenEveryXDays) + 3)) <= 0); // Make sure it's past what may possibly be the next weekday.
            return null;
        } else {
            values = new RecurrenceValues();
            switch (this.endDateType) {
                case NoEndDate:
                    throw new RecurrenceException(GENERIC_EXCEPTION_CODE, "The ability to create recurring dates with no End date is not currently available.");

                case NumberOfOccurrences:

                    for (int i = 0; i < this.numberOfOccurrences; i++) {
                        values.AddDateValue(dt);
                        dt = dt.plusDays(regenEveryXDays);
                    }
                    break;

                case SpecificDate:
                    do {
                        values.AddDateValue(dt);
                        dt = dt.plusDays(regenEveryXDays);
                    } while (dt.compareTo(this.endDate) <= 0);
                    break;

                default:
                    throw new IllegalArgumentException("The TypeOfEndDate property has not been set.");
            }
            return values;
        }
    }

    public enum DailyRegenType {
        NOTSET("-1"), ON_EVERY_X_DAYS("0"), ON_EVERY_WEEKDAY("1");
        String strVal;

        DailyRegenType(String s) {
            strVal = s;
        }

        public static DailyRegenType fromValue(String strVal) {
            for (DailyRegenType e : DailyRegenType.values()) {
                if (e.strVal.equalsIgnoreCase(strVal)) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return strVal;
        }
    }
}
