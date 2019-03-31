package seedu.address.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Optional;

/**
 * Utility functions related to dates
 */
public class DateUtil {

    /**
     * Tries to parse a given string into a human readable date if possible else return original
     * @param dateTime ISO 8601 string
     * @param dateStyle date style
     * @return localized human readable date if possible else original
     */
    public static String getHumanReadableDateFrom(String dateTime, FormatStyle dateStyle) {
        // try parsing given string and return human readable date if possible else return original
        return OptionalCandidate
                .with((DateTimeFormatter formatter) -> {
                    try {
                        return Optional.of(formatter.parse(dateTime, LocalDate::from)); // try parsing into LocalDate
                    } catch (DateTimeParseException dtpe) {
                        return Optional.empty();
                    }
                })
                .tryout(DateTimeFormatter.ISO_DATE)
                .tryout(DateTimeFormatter.ISO_DATE_TIME)
                .tryout(DateTimeFormatter.ISO_INSTANT)
                .getOptional()
                .map(DateTimeFormatter.ofLocalizedDate(dateStyle)::format)
                .orElse(dateTime);
    }

    /**
     * Tries to parse a given string into a human readable date if possible else return original
     * @param dateTime ISO 8601 string
     * @return localized human readable date if possible else original
     */
    public static String getHumanReadableDateFrom(String dateTime) {
        return getHumanReadableDateFrom(dateTime, FormatStyle.LONG);
    }

    /**
     * Tries to parse a given string into a human readable datetime if possible else return original
     * @param dateTime ISO 8601 string
     * @param dateStyle date style
     * @param timeStyle time style
     * @return localized human readable datetime if possible else original
     */
    public static String getHumanReadableDateTimeFrom(String dateTime, FormatStyle dateStyle, FormatStyle timeStyle) {
        // try parsing given string and return human readable datetime if possible else return original
        return OptionalCandidate
                .with((DateTimeFormatter formatter) -> {
                    try {
                        return Optional.of(formatter.parseBest(
                                dateTime,
                                ZonedDateTime::from,
                                OffsetDateTime::from,
                                LocalDateTime::from,
                                LocalDate::from)); // try parsing into TemporalAccessor
                    } catch (DateTimeParseException dtpe) {
                        return Optional.empty();
                    }
                })
                .tryout(DateTimeFormatter.ISO_DATE)
                .tryout(DateTimeFormatter.ISO_DATE_TIME)
                .tryout(DateTimeFormatter.ISO_INSTANT)
                .getOptional()
                .map(temporalAccessor -> {
                    if (temporalAccessor instanceof LocalDate) {
                        return DateTimeFormatter.ofLocalizedDate(dateStyle).format(temporalAccessor);
                    } else {
                        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).format(temporalAccessor);
                    }
                })
                .orElse(dateTime);
    }

    /**
     * Tries to parse a given string into a human readable datetime if possible else return original
     * @param dateTime ISO 8601 string
     * @return localized human readable datetime if possible else original
     */
    public static String getHumanReadableDateTimeFrom(String dateTime) {
        // try parsing given string and return human readable datetime if possible else return original
        return getHumanReadableDateTimeFrom(dateTime, FormatStyle.LONG, FormatStyle.SHORT);
    }

}
