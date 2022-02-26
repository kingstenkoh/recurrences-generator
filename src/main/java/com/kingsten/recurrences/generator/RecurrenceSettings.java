package com.kingsten.recurrences.generator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public abstract class RecurrenceSettings {

    protected EndDateType endDateType = EndDateType.NotDefined;
    LocalDateTime startDate;
    LocalDateTime endDate;
    int recurrenceInterval = 1;
    int numberOfOccurrences = 0;
    int regenerationInterval = 0;

    public RecurrenceSettings(LocalDateTime startDate) {
        this.startDate = startDate;
        endDateType = EndDateType.NoEndDate;
    }

    public RecurrenceSettings(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        endDateType = EndDateType.SpecificDate;
    }

    public RecurrenceSettings(LocalDateTime startDate, int numberOfOccurrences) {
        this.startDate = startDate;
        this.numberOfOccurrences = numberOfOccurrences;
        endDateType = EndDateType.NumberOfOccurrences;
    }

    abstract LocalDateTime getNextDate(LocalDateTime currentDate) throws Exception;

    abstract RecurrenceValues getValues() throws Exception;

    abstract RecurrenceValues getValues(LocalDateTime startDate, int numberOfOccurrences) throws Exception;

    abstract RecurrenceValues getValues(LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    public boolean hasEndDate() {
        return endDate != null;
    }

    public enum EndDateType {
        NotDefined("-1"), NoEndDate("0"), SpecificDate("1"), NumberOfOccurrences("2");

        String strVal;

        EndDateType(String s) {
            this.strVal = s;
        }

        public static EndDateType fromValue(String strVal) {
            for (EndDateType e : EndDateType.values()) {
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

}
