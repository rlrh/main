package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Address(null));
    }

    // All strings are valid addresses for construction
    /*
    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }
    */

    @Test
    public void isValidUserInputAddress() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Address.isValidUserInputAddress(null));

        // invalid addresses
        assertFalse(Address.isValidUserInputAddress("")); // empty string
        assertFalse(Address.isValidUserInputAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Address.isValidUserInputAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidUserInputAddress("-")); // one character
        assertTrue(Address.isValidUserInputAddress(
            "Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void isValidConstructionAddress() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Address.isValidConstructionAddress(null));

        // invalid addresses

        // valid addresses
        assertTrue(Address.isValidConstructionAddress("")); // empty string
        assertTrue(Address.isValidConstructionAddress(" ")); // spaces only

        assertTrue(Address.isValidConstructionAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidConstructionAddress("-")); // one character
        assertTrue(Address.isValidConstructionAddress(
            "Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }
}
