package com.epam.gymApp.util;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Utility class for date-related calculations.
 * <p>
 * This class provides methods to perform various calculations involving dates and durations.
 */
public class DateUtil {

  /**
   * Calculate the end date based on the start date and duration.
   * <p>
   * This method takes a start date and a duration and calculates the end date by adding the
   * duration to the start date.
   *
   * @param startDate The starting date of the period.
   * @param duration  The duration to add to the start date.
   * @return The calculated end date.
   */
  public static LocalDate calculateEndDate(LocalDate startDate, Duration duration) {
    long days = duration.toDays();
    return startDate.plusDays(days);
  }
}
