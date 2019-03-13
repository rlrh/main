package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class TitleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Title(null));
    }

    @Test
    public void constructor_invalidTitle_throwsIllegalArgumentException() {
        String invalidTitle = " ";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Title(invalidTitle));
    }

    @Test
    public void isValidUserInputTitle() {
        // null title
        Assert.assertThrows(NullPointerException.class, () -> Title.isValidUserInputTitle(null));

        // invalid title
        assertFalse(Title.isValidUserInputTitle("")); // empty string
        assertFalse(Title.isValidUserInputTitle(" ")); // spaces only

        // valid title
        assertTrue(Title.isValidUserInputTitle("peter*")); // contains non-alphanumeric characters
        assertTrue(Title.isValidUserInputTitle("peter jack")); // alphabets only
        assertTrue(Title.isValidUserInputTitle("12345")); // numbers only
        assertTrue(Title.isValidUserInputTitle("peter the 2nd")); // alphanumeric characters
        assertTrue(Title.isValidUserInputTitle("Capital Tan")); // with capital letters
        assertTrue(Title.isValidUserInputTitle("David Roger Jackson Ray Jr 2nd")); // long titles
    }

    @Test
    public void isValidConstructionTitle() {
        // null title
        Assert.assertThrows(NullPointerException.class, () -> Title.isValidConstructionTitle(null));

        // invalid title
        assertFalse(Title.isValidConstructionTitle(" ")); // spaces only

        // valid title
        assertTrue(Title.isValidConstructionTitle("")); // empty string

        assertTrue(Title.isValidConstructionTitle("peter*")); // contains non-alphanumeric characters
        assertTrue(Title.isValidConstructionTitle("peter jack")); // alphabets only
        assertTrue(Title.isValidConstructionTitle("12345")); // numbers only
        assertTrue(Title.isValidConstructionTitle("peter the 2nd")); // alphanumeric characters
        assertTrue(Title.isValidConstructionTitle("Capital Tan")); // with capital letters
        assertTrue(Title.isValidConstructionTitle("David Roger Jackson Ray Jr 2nd")); // long titles
    }
}
