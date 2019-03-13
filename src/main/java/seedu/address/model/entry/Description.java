package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's description in the entry book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUserInputDescription(String)}
 */
public class Description {


    public static final String MESSAGE_CONSTRAINTS =
            "Descriptions can take any values, and it should not be blank";
    public static final String DEFAULT_DESCRIPTION = "";
    public static final String USER_INPUT_VALIDATION_REGEX = "[^\\s].*";
    public static final String CONSTRUCTION_VALIDATION_REGEX = "([^\\s].*)|(^$)";
    public final String value;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidConstructionDescription(description), MESSAGE_CONSTRAINTS);
        value = description;
    }

    /**
     * Returns true if a given string is a valid user-input description.
     */
    public static boolean isValidUserInputDescription(String test) {
        return test.matches(USER_INPUT_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid description for construction.
     */
    public static boolean isValidConstructionDescription(String test) {
        return test.matches(CONSTRUCTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
