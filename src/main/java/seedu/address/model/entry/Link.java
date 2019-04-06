package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a Entry's link in the address book.
 * Guarantees: immutable
 */
public class Link {

    public static final String MESSAGE_CONSTRAINTS = "Links should be of the format protocol://domain/filename.";

    public final URL value;

    /**
     * Constructs an {@code Link}.
     *
     * @param link A valid link address.
     */
    public Link(String link) throws MalformedURLException {
        requireNonNull(link);
        value = new URL(link);
    }

    /**
     * Constructs an {@code Link}.
     *
     * @param link A valid link address.
     */
    public Link(URL link) {
        requireNonNull(link);
        value = link;
    }

    public static String formExceptionMessage() {
        return MESSAGE_CONSTRAINTS;
    }

    public static String formExceptionMessage(String invalidInput) {
        return MESSAGE_CONSTRAINTS + " Entered: [" + invalidInput + "].";
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Link // instanceof handles nulls
                && value.toString().toLowerCase().equals(((Link) other).value.toString().toLowerCase())); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
