package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidConstructionAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS = "Addresses can take any values, and it should not be blank";
    public static final String DEFAULT_ADDRESS = "";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String USER_INPUT_VALIDATION_REGEX = "[^\\s].*";
    public static final String CONSTRUCTION_VALIDATION_REGEX = "^.*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidConstructionAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid user-input address.
     */
    public static boolean isValidUserInputAddress(String test) {
        return test.matches(USER_INPUT_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid address for construction.
     */
    public static boolean isValidConstructionAddress(String test) {
        return test.matches(CONSTRUCTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && value.equals(((Address) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
