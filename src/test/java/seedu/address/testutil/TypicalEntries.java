package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.MainApp;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class containing a list of {@code Entry} objects to be used in tests.
 */
public class TypicalEntries {

    public static final Entry ALICE = new EntryBuilder()
            .withTitle("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withLink("https://alice.example.com")
            .withDescription("Description place-holder")
            .withTags("friends")
            .build();
    public static final Entry BENSON = new EntryBuilder()
            .withTitle("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withLink("https://johnd.example.com")
            .withDescription("Description place-holder")
            .withTags("owesMoney", "friends")
            .build();
    public static final Entry CARL = new EntryBuilder()
            .withTitle("Carl Kurz")
            .withDescription("Description place-holder")
            .withLink("https://heinz.example.com")
            .withAddress("wall street")
            .build();
    public static final Entry DANIEL = new EntryBuilder()
            .withTitle("Daniel Meier")
            .withDescription("Description place-holder")
            .withLink("https://cornelia.example.com")
            .withAddress("10th street")
            .withTags("friends")
            .build();
    public static final Entry ELLE = new EntryBuilder()
            .withTitle("Elle Meyer")
            .withDescription("Description place-holder")
            .withLink("https://werner.example.com")
            .withAddress("michegan ave")
            .build();
    public static final Entry FIONA = new EntryBuilder()
            .withTitle("Fiona Kunz")
            .withDescription("Description place-holder")
            .withLink("https://lydia.example.com")
            .withAddress("little tokyo")
            .build();
    public static final Entry GEORGE = new EntryBuilder()
            .withTitle("George Best")
            .withDescription("Description place-holder")
            .withLink("https://anna.example.com")
            .withAddress("4th street")
            .build();

    // Manually added
    public static final Entry HOON = new EntryBuilder()
            .withTitle("Hoon Meier")
            .withDescription("Description place-holder")
            .withLink("https://stefan.example.com")
            .withAddress("little india")
            .build();
    public static final Entry IDA = new EntryBuilder()
            .withTitle("Ida Mueller")
            .withDescription("Description place-holder")
            .withLink("https://hans.example.com")
            .withAddress("chicago ave")
            .build();

    // Must have valid link
    public static final Entry VALID_LINK = new EntryBuilder()
            .withTitle("Valid Link")
            .withDescription("Valid link")
            .withLink("file://"
                    + MainApp.class.getResource("/view/BrowserPanelTest/default.html").toExternalForm().substring(5))
            .withAddress("Valid link")
            .build();
    // Must have invalid link
    public static final Entry INVALID_LINK = new EntryBuilder()
            .withTitle("Invalid Link")
            .withDescription("Invalid link")
            .withLink("file:///folder/file.type")
            .withAddress("Invalid link")
            .build();

    // Manually added - Entry's details found in {@code CommandTestUtil}
    public static final Entry AMY = new EntryBuilder()
            .withTitle(VALID_TITLE_AMY)
            .withDescription(VALID_DESCRIPTION_AMY)
            .withLink(VALID_LINK_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withTags(VALID_TAG_TECH)
            .build();
    public static final Entry BOB = new EntryBuilder()
            .withTitle(VALID_TITLE_BOB)
            .withDescription(VALID_DESCRIPTION_BOB)
            .withLink(VALID_LINK_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH)
            .build();

    // For testing of networking
    public static final Entry VALID_HTTPS_LINK = new EntryBuilder()
            .withTitle("Valid https Link")
            .withDescription("Valid https link")
            .withLink("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .withAddress("Valid https link")
            .build();
    public static final Entry VALID_HTTP_LINK = new EntryBuilder()
            .withTitle("Valid https Link")
            .withDescription("Valid https link")
            .withLink("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .withAddress("Valid https link")
            .build();
    public static final Entry VALID_FILE_LINK = new EntryBuilder()
            .withTitle("Valid file Link")
            .withDescription("Valid file link")
            .withLink("file://" + MainApp.class.getResource(
            "/view/NetworkTest/default.html").toExternalForm().substring(5))
            .withAddress("Valid file link")
            .build();
    public static final String FILE_TEST_CONTENTS = "<!DOCTYPE html>\n<html>\n</html>\n";

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalEntries() {} // prevents instantiation

    /**
     * Returns an {@code EntryBook} with all the typical persons.
     */
    public static EntryBook getTypicalEntryBook() {
        EntryBook ab = new EntryBook();
        for (Entry entry : getTypicalEntries()) {
            ab.addPerson(entry);
        }
        return ab;
    }

    public static List<Entry> getTypicalEntries() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
