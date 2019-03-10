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

    // All strings are valid descriptions for construction
    /*
    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidDescription = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }
    */

    @Test
    public void isValidUserInputDescription() {
        // null description
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidUserInputDescription(null));

        // invalid description
        assertFalse(Description.isValidUserInputDescription("")); // empty string
        assertFalse(Description.isValidUserInputDescription(" ")); // spaces only

        // valid description
        assertTrue(Description.isValidUserInputDescription("comment")); // non-numeric
        assertTrue(Description.isValidUserInputDescription("comment comment")); // non-numeric with spaces
        assertTrue(Description.isValidUserInputDescription("comment?")); // non-numeric with symbols
        assertTrue(Description.isValidUserInputDescription("comment@")); // non-numeric with symbols
        assertTrue(Description.isValidUserInputDescription("comment!")); // non-numeric with symbols
        assertTrue(Description.isValidUserInputDescription("comment-comment")); // non-numeric with spaces
        assertTrue(Description.isValidUserInputDescription("93121534")); // numeric
        assertTrue(Description.isValidUserInputDescription("9312153asd4asd219")); // alphanumeric
        assertTrue(Description.isValidUserInputDescription("9312153asd4 asd219")); // alphanumeric
        assertTrue(Description.isValidUserInputDescription("Five reasons why your best-friend is eating grass. 3# will shock you!"));

    }

    @Test
    public void isValidConstructionDescription() {
        // null description
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidConstructionDescription(null));

        // invalid description

        // valid description
        assertTrue(Description.isValidConstructionDescription("")); // empty string
        assertTrue(Description.isValidConstructionDescription(" ")); // spaces only

        assertTrue(Description.isValidConstructionDescription("comment")); // non-numeric
        assertTrue(Description.isValidConstructionDescription("comment comment")); // non-numeric with spaces
        assertTrue(Description.isValidConstructionDescription("comment?")); // non-numeric with symbols
        assertTrue(Description.isValidConstructionDescription("comment@")); // non-numeric with symbols
        assertTrue(Description.isValidConstructionDescription("comment!")); // non-numeric with symbols
        assertTrue(Description.isValidConstructionDescription("comment-comment")); // non-numeric with spaces
        assertTrue(Description.isValidConstructionDescription("93121534")); // numeric
        assertTrue(Description.isValidConstructionDescription("9312153asd4asd219")); // alphanumeric
        assertTrue(Description.isValidConstructionDescription("9312153asd4 asd219")); // alphanumeric
        assertTrue(Description.isValidConstructionDescription("Five reasons why your best-friend is eating grass. 3# will shock you!"));

    }
}
