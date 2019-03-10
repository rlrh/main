package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's link in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidConstructionLink(String)}
 */
public class Link {

    public static final String MESSAGE_CONSTRAINTS = "Links should be of the format protocol://domain/filename.";
    public static final String DEFAULT_LINK = "https://cs2103-ay1819s2-w10-1.github.io/main/";

    private static final String PATH = "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String USER_INPUT_VALIDATION_REGEX = "^(https?|ftp|file)://" + PATH;
    public static final String CONSTRUCTION_VALIDATION_REGEX = USER_INPUT_VALIDATION_REGEX;

    public final String value;

    /**
     * Constructs an {@code Link}.
     *
     * @param link A valid link address.
     */
    public Link(String link) {
        requireNonNull(link);
        checkArgument(isValidConstructionLink(link), MESSAGE_CONSTRAINTS);
        value = link;
    }

    /**
     * Returns if a given string is a valid user-input link.
     */
    public static boolean isValidUserInputLink(String test) {
        return test.matches(USER_INPUT_VALIDATION_REGEX);
    }

    /**
     * Returns if a given string is a valid link for construction.
     */
    public static boolean isValidConstructionLink(String test) {
        return test.matches(CONSTRUCTION_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Link // instanceof handles nulls
                && value.equals(((Link) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
