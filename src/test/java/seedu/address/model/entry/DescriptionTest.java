package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid phone numbers
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only

        // valid phone numbers
        assertTrue(Description.isValidDescription("comment")); // non-numeric
        assertTrue(Description.isValidDescription("comment comment")); // non-numeric with spaces
        assertTrue(Description.isValidDescription("comment?")); // non-numeric with symbols
        assertTrue(Description.isValidDescription("comment@")); // non-numeric with symbols
        assertTrue(Description.isValidDescription("comment!")); // non-numeric with symbols
        assertTrue(Description.isValidDescription("comment-comment")); // non-numeric with spaces
        assertTrue(Description.isValidDescription("93121534")); // numeric
        assertTrue(Description.isValidDescription("9312153asd4asd219")); // alphanumeric
        assertTrue(Description.isValidDescription("9312153asd4 asd219")); // alphanumeric
        assertTrue(Description.isValidDescription("Five reasons why your best-friend is eating grass. 3# will shock you!"));

    }
}
