package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's title in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidConstructionTitle(String)}
 */
public class Title {

    public static final String MESSAGE_CONSTRAINTS =
            "Titles can take any values, and it should not be blank.";
    public static final String DEFAULT_TITLE = "";

    public static final String USER_INPUT_VALIDATION_REGEX = "[^\\s].*";
    public static final String CONSTRUCTION_VALIDATION_REGEX = "([^\\s].*)|(^$)";

    public final String fullTitle;

    /**
     * Constructs a {@code Title}.
     *
     * @param title A valid title.
     */
    public Title(String title) {
        requireNonNull(title);
        checkArgument(isValidConstructionTitle(title), MESSAGE_CONSTRAINTS);
        fullTitle = title;
    }

    /**
     * Returns true if a given string is a valid user-input title.
     */
    public static boolean isValidUserInputTitle(String test) {
        return test.matches(USER_INPUT_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid title for construction.
     */
    public static boolean isValidConstructionTitle(String test) {
        return test.matches(CONSTRUCTION_VALIDATION_REGEX);
    }

    public static String formExceptionMessage() {
        return MESSAGE_CONSTRAINTS;
    }

    public static String formExceptionMessage(String invalidInput) {
        return MESSAGE_CONSTRAINTS + " Entered: [" + invalidInput + "].";
    }

    @Override
    public String toString() {
        return fullTitle;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && fullTitle.equals(((Title) other).fullTitle)); // state check
    }

    @Override
    public int hashCode() {
        return fullTitle.hashCode();
    }

}
