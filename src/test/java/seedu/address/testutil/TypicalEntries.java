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

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    // For AddCommandTest
    public static final String DUMMY_TITLE = "Dummy Title";
    public static final String DUMMY_DESCRIPTION = "Dummy description";

    public static final String STUB_LINK_URL = "http://www.description.test/title/title_test.html";
    public static final String STUB_LINK_TITLE = "Title Test";
    public static final String STUB_LINK_DESCRIPTION = "www.description.test";

    public static final String REAL_LINK_URL = MainApp.class
            .getResource("/ModelManagerTest/NUS_School_of_Computing.html").toExternalForm();
    public static final String REAL_LINK_TITLE = "NUS School of Computing - Wikipedia";
    public static final String REAL_LINK_DESCRIPTION = "NUS School of Computing is a faculty within the National "
            + "University of Singapore (NUS). The School was established in 1998, although its history reaches…";

    public static final String CRUX_LINK_URL = MainApp.class
            .getResource("/ModelManagerTest/ProPublica.html").toExternalForm();
    public static final String CRUX_LINK_TITLE = "Our Reporting on Michael Cohen: A Reading Guide — ProPublica";
    public static final String CRUX_LINK_DESCRIPTION = "This week’s testimony by President Donald Trump’s former "
            + "personal attorney and fixer held millions rapt with allegations of fraud, coded orders to lie and "
            + "hundreds of threats. Many of those assertions had been explored before, as these articles show.";

    public static final Entry STUB_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(STUB_LINK_TITLE)
            .withDescription(STUB_LINK_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry STUB_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(STUB_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry STUB_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(STUB_LINK_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(REAL_LINK_TITLE)
            .withDescription(REAL_LINK_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(REAL_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(REAL_LINK_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry CRUX_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(CRUX_LINK_URL)
            .build();
    public static final Entry CRUX_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(CRUX_LINK_TITLE)
            .withDescription(CRUX_LINK_DESCRIPTION)
            .withLink(CRUX_LINK_URL)
            .build();

    public static final Entry CRUX_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(CRUX_LINK_URL)
            .build();
    public static final Entry CRUX_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(CRUX_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(CRUX_LINK_URL)
            .build();

    public static final Entry CRUX_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(CRUX_LINK_URL)
            .build();
    public static final Entry CRUX_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(CRUX_LINK_DESCRIPTION)
            .withLink(CRUX_LINK_URL)
            .build();

    public static final Entry CRUX_LINK_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(CRUX_LINK_URL)
            .build();

    // For testing of networking
    public static final Entry VALID_HTTPS_LINK = new EntryBuilder()
            .withTitle("Valid HTTPS Link")
            .withDescription("Valid https link")
            .withLink("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .withAddress("Valid https link")
            .build();
    public static final Entry VALID_HTTP_LINK = new EntryBuilder()
            .withTitle("Valid HTTP Link")
            .withDescription("Valid http link")
            .withLink("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .withAddress("Valid http link")
            .build();
    public static final Entry VALID_FILE_LINK = new EntryBuilder()
            .withTitle("Valid File Link")
            .withDescription("Valid file link")
            .withLink("file://" + MainApp.class.getResource(
                    "/NetworkTest/default.html").toExternalForm().substring(5))
            .withAddress("Valid file link")
            .build();
    public static final Entry INVALID_FILE_LINK = new EntryBuilder()
            .withTitle("Invalid File Link")
            .withDescription("Invalid file link")
            .withLink("file:///folder/file.type")
            .withAddress("Invalid file link")
            .build();
    public static final String FILE_TEST_CONTENTS = "<!DOCTYPE html>\n<html>\n</html>\n";

    // For BrowserPanelTest
    public static final Entry BROWSER_PANEL_TEST_ENTRY = new EntryBuilder()
            .withTitle("Browser Panel Test Web Page")
            .withDescription("Browser panel test web page")
            .withLink(MainApp.class.getResource("/view/BrowserPanelTest/test.html").toExternalForm())
            .withAddress("Browser panel test web page")
            .build();
    public static final String BROWSER_PANEL_TEST_ENTRY_BASE_URL =
            MainApp.class.getResource("/view/BrowserPanelTest/test.html").toExternalForm();
    public static final Entry WIKIPEDIA_ENTRY = new EntryBuilder()
            .withTitle("Wikipedia Test Web Page")
            .withDescription("Wikipedia test web page")
            .withLink(MainApp.class.getResource("/view/BrowserPanelTest/wikipedia.html").toExternalForm())
            .withAddress("Wikipedia test web page")
            .build();
    public static final String WIKIPEDIA_ENTRY_BASE_URL = "http://en.wikipedia.org/wiki/Therapsids";

    private TypicalEntries() {} // prevents instantiation

    /**
     * Returns an {@code EntryBook} with all the typical persons.
     */
    public static EntryBook getTypicalEntryBook() {
        EntryBook ab = new EntryBook();
        for (Entry entry : getTypicalEntries()) {
            ab.addEntry(entry);
        }
        return ab;
    }

    public static List<Entry> getTypicalEntries() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
