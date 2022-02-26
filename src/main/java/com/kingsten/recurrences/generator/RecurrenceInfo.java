package com.kingsten.recurrences.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class RecurrenceInfo {
    RecurrenceSettings.EndDateType endDateType = RecurrenceSettings.EndDateType.NotDefined;

    ;
    int numberOfOccurrences;
    int adjustmentValue;
    String seriesInfo;
    LocalDateTime startDate;
    LocalDateTime endDate = null;
    RecurrenceType recurrenceType = RecurrenceType.NotSet;
    DailyRecurrenceSettings.DailyRegenType dailyRegenType;
    int dailyRegenEveryXDays;
    WeeklyRecurrenceSettings.SelectedDayOfWeekValues selectedDayOfWeekValues;
    WeeklyRecurrenceSettings.WeeklyRegenType weeklyRegenType;
    int regenEveryXWeeks;
    MonthlyRecurrenceSettings.MonthlyRegenType monthlyRegenType;
    MonthlyRecurrenceSettings.MonthlySpecificDatePartOne monthlySpecificDatePartOne;
    MonthlyRecurrenceSettings.MonthlySpecificDatePartTwo monthlySpecificDatePartTwo;
    int monthlyRegenerateOnSpecificDateDayValue;
    int regenEveryXMonths;
    int specificDateDayValue;
    int specificDateMonthValue;
    YearlyRecurrenceSettings.YearlyRegenType yearlyRegenType = YearlyRecurrenceSettings.YearlyRegenType.NotSet;
    YearlyRecurrenceSettings.YearlySpecificDatePartOne yearlySpecificDatePartOne = YearlyRecurrenceSettings.YearlySpecificDatePartOne.NotSet;
    YearlyRecurrenceSettings.YearlySpecificDatePartTwo yearlySpecificDatePartTwo = YearlyRecurrenceSettings.YearlySpecificDatePartTwo.NotSet;
    YearlyRecurrenceSettings.YearlySpecificDatePartThree yearlySpecificDatePartThree = YearlyRecurrenceSettings.YearlySpecificDatePartThree.NotSet;

    public enum RecurrenceType {
        NotSet("-1"), Daily("0"), Weekly("1"), Monthly("2"), Yearly("3");
        private String strVal;

        RecurrenceType(String s) {
            this.strVal = s;
        }

        public String toString() {
            return this.strVal;
        }
    }
}
