package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class LinkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Link(null));
    }

    @Test
    public void constructor_invalidLink_throwsIllegalArgumentException() {
        String invalidLink = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Link(invalidLink));
    }

    @Test
    public void isValidUserInputEmail() {
        // null email
        Assert.assertThrows(NullPointerException.class, () -> Link.isValidUserInputLink(null));

        // blank email
        assertFalse(Link.isValidUserInputLink("")); // empty string
        assertFalse(Link.isValidUserInputLink(" ")); // spaces only

        // missing parts
        assertFalse(Link.isValidUserInputLink("@example.com")); // missing local part
        assertFalse(Link.isValidUserInputLink("peterjackexample.com")); // missing '@' symbol
        assertFalse(Link.isValidUserInputLink("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Link.isValidUserInputLink("peterjack.com")); // no protocol
        assertFalse(Link.isValidUserInputLink("htps://peterjack.com")); // invalid protocol
        assertFalse(Link.isValidUserInputLink("https://peterjack.example.com.")); // invalid ending character
        assertFalse(Link.isValidUserInputLink("https://peter jack.example.com")); // spaces in sub-domain
        assertFalse(Link.isValidUserInputLink("https://peterjack.exam ple.com")); // spaces in domain name
        assertFalse(Link.isValidUserInputLink(" https://peterjack@example.com")); // leading space
        assertFalse(Link.isValidUserInputLink("https://peterjack@example.com ")); // trailing space

        /*
        // Legacy tests
        assertFalse(Link.isValidUserInputLink("https://peterjack@.example.com")); // domain name starts with a period
        assertFalse(Link.isValidUserInputLink("https://peterjack@example.com.")); // domain name ends with a period
        assertFalse(Link.isValidUserInputLink("https://peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Link.isValidUserInputLink("https://peterjack@example.com-")); // domain name ends with a hyphen
        assertTrue(Link.isValidUserInputLink("https://!#$%&'*+/=?`{|}~^.-@example.org")); // special characters
        */

        // valid link
        assertTrue(Link.isValidUserInputLink("http://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidUserInputLink("https://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidUserInputLink("https://peterJack_1190.example.com:443")); // with port
        assertTrue(Link.isValidUserInputLink("https://peterJack_1190.example.com/folder/to/file")); // with path to file
        assertTrue(Link.isValidUserInputLink("https://www.peterJack_1190.example.com")); // with www

        assertTrue(Link.isValidUserInputLink("file:///c/Desktop/file.txt")); // file protocol

        assertTrue(Link.isValidUserInputLink("http://localhost")); // localhost
        assertTrue(Link.isValidUserInputLink("http://localhost/path/to/file.txt")); // localhost
        assertTrue(Link.isValidUserInputLink("http://localhost:8080")); // localhost with port

        assertTrue(Link.isValidUserInputLink("https://208.67.222.222")); // IP Address
        assertTrue(Link.isValidUserInputLink("https://208.67.222.222/path/to/file.txt")); // IP Address with path

        assertTrue(Link.isValidUserInputLink("https://test.localhost")); // alphabets only
        assertTrue(Link.isValidUserInputLink(
            "https://a1+be!.example1.com")); // mixture of alphanumeric and special characters

        assertTrue(Link.isValidUserInputLink(
            "https://peter_jack-very-very-very-long-example.com")); // long domain name
        assertTrue(Link.isValidUserInputLink(
            "https://if.you.dream.it_you.can.do.it.example.com")); // multiple sub-domains
    }

    @Test
    public void isValidConstructionEmail() {
        // null email
        Assert.assertThrows(NullPointerException.class, () -> Link.isValidConstructionLink(null));

        // blank email
        assertFalse(Link.isValidConstructionLink("")); // empty string
        assertFalse(Link.isValidConstructionLink(" ")); // spaces only

        // missing parts
        assertFalse(Link.isValidConstructionLink("@example.com")); // missing local part
        assertFalse(Link.isValidConstructionLink("peterjackexample.com")); // missing '@' symbol
        assertFalse(Link.isValidConstructionLink("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Link.isValidConstructionLink("peterjack.com")); // no protocol
        assertFalse(Link.isValidConstructionLink("htps://peterjack.com")); // invalid protocol
        assertFalse(Link.isValidConstructionLink("https://peterjack.example.com.")); // invalid ending character
        assertFalse(Link.isValidConstructionLink("https://peter jack.example.com")); // spaces in sub-domain
        assertFalse(Link.isValidConstructionLink("https://peterjack.exam ple.com")); // spaces in domain name
        assertFalse(Link.isValidConstructionLink(" https://peterjack@example.com")); // leading space
        assertFalse(Link.isValidConstructionLink("https://peterjack@example.com ")); // trailing space

        /*
        // Legacy tests
        assertFalse(Link.isValidConstructionLink("https://peterjack@.example.com")); // domain name starts with a period
        assertFalse(Link.isValidConstructionLink("https://peterjack@example.com.")); // domain name ends with a period
        assertFalse(Link.isValidConstructionLink("https://peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Link.isValidConstructionLink("https://peterjack@example.com-")); // domain name ends with a hyphen
        assertTrue(Link.isValidConstructionLink("https://!#$%&'*+/=?`{|}~^.-@example.org")); // special characters
        */

        // valid link
        assertTrue(Link.isValidConstructionLink("http://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidConstructionLink("https://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidConstructionLink("https://peterJack_1190.example.com:443")); // with port
        assertTrue(Link.isValidConstructionLink(
            "https://peterJack_1190.example.com/folder/to/file")); // with path to file
        assertTrue(Link.isValidConstructionLink("https://www.peterJack_1190.example.com")); // with www

        assertTrue(Link.isValidConstructionLink("file:///c/Desktop/file.txt")); // file protocol
        assertTrue(Link.isValidConstructionLink("file:/c/Desktop/file.txt")); // file protocol no 2 forward slash

        assertTrue(Link.isValidConstructionLink("http://localhost")); // localhost
        assertTrue(Link.isValidConstructionLink("http://localhost/path/to/file.txt")); // localhost
        assertTrue(Link.isValidConstructionLink("http://localhost:8080")); // localhost with port

        assertTrue(Link.isValidConstructionLink("https://208.67.222.222")); // IP Address
        assertTrue(Link.isValidConstructionLink("https://208.67.222.222/path/to/file.txt")); // IP Address with path

        assertTrue(Link.isValidConstructionLink("https://test.localhost")); // alphabets only
        assertTrue(Link.isValidConstructionLink(
            "https://a1+be!.example1.com")); // mixture of alphanumeric and special characters

        assertTrue(Link.isValidConstructionLink(
            "https://peter_jack-very-very-very-long-example.com")); // long domain name
        assertTrue(Link.isValidConstructionLink(
            "https://if.you.dream.it_you.can.do.it.example.com")); // multiple sub-domains
    }
}
