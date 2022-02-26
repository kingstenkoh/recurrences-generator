package com.kingsten.recurrences.generator;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecurrenceValues {

    List<LocalDateTime> values = new ArrayList<>();
    LocalDateTime endDate;
    LocalDateTime startDate;
    String seriesInfo;

    public RecurrenceValues() {
    }

    public LocalDateTime lastDate() {
        if (!values.isEmpty())
            return values.get(values.size() - 1);
        else
            return LocalDateTime.MAX;
    }

    /// <summary>
    /// Add a date to the List of Values.
    /// </summary>
    /// <param name="recurrenceDate"></param>
    void AddDateValue(LocalDateTime recurrenceDate) {
        values.add(recurrenceDate);
    }

    /// <summary>
    /// Add a date to the List of Values adjusting it with the plus/minus x-days value
    /// </summary>
    /// <param name="recurrenceDate"></param>
    /// <param name="adjustedValue"></param>
    void AddDateValue(LocalDateTime recurrenceDate, int adjustedValue) {
        values.add(recurrenceDate.plusDays(adjustedValue));
    }

    public LocalDateTime getLastDate() {
        if (values.size() > 0)
            return values.get(values.size() - 1);
        else
            return LocalDateTime.MAX;
    }

}
