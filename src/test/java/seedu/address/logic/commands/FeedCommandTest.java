package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_FAILURE_NET;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_FAILURE_XML;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_SUCCESS;

import org.junit.Test;

import seedu.address.MainApp;
import seedu.address.commons.util.FeedUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

public class FeedCommandTest {
    private static final String TEST_URL = "https://cs2103-ay1819s2-w10-1.github.io/main/networktests/rss.xml";
    private static final String TEST_URL_LOCAL = MainApp.class.getResource("/RssFeedTest/rss.xml")
            .toExternalForm();

    private static final String MALFORMED_URL = "notavalidprotocol://malformed.url/invalid";
    private static final String NOTAFEED_URL =
        "https://cs2103-ay1819s2-w10-1.github.io/main/networktests/notafeed.notxml";

    private Model model = new ModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    /** Asserts that executing a FeedCommand with the given url imports the Entry list. */
    public void assertFeedSuccessfullyLoaded(String feedUrl) throws Exception {
        Model model = new ModelManagerStub();
        Model expectedModel = new ModelManagerStub();
        CommandHistory commandHistory = new CommandHistory();

        String expectedMessage = String.format(MESSAGE_SUCCESS, feedUrl);
        FeedCommand command = new FeedCommand(feedUrl);
        EntryBook expectedDisplayedEntryBook = FeedUtil.fromFeedUrl(feedUrl);

        expectedModel.setSearchEntryBook(expectedDisplayedEntryBook);
        expectedModel.setContext(ModelContext.CONTEXT_SEARCH);

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        String firstUrl = "https://open.kattis.com/rss/new-problems";
        String secondUrl = "https://en.wikipedia.org/w/index.php?title=Special:RecentChanges&feed=rss";

        FeedCommand feedFirstCommand = new FeedCommand(firstUrl);
        FeedCommand feedSecondCommand = new FeedCommand(secondUrl);

        // same object -> returns true
        assertTrue(feedFirstCommand.equals(feedFirstCommand));

        // same values -> returns true
        FeedCommand feedFirstCommandCopy = new FeedCommand(firstUrl);
        assertTrue(feedFirstCommand.equals(feedFirstCommandCopy));

        // different types -> returns false
        assertFalse(feedFirstCommand.equals(1));

        // null -> returns false
        assertFalse(feedFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(feedFirstCommand.equals(feedSecondCommand));
    }

    @Test
    public void execute_localUrl_success() throws Exception {
        assertFeedSuccessfullyLoaded(TEST_URL_LOCAL);
    }

    @Test
    public void execute_remoteUrl_success() throws Exception {
        assertFeedSuccessfullyLoaded(TEST_URL);
    }

    @Test
    public void execute_malformedUrlGiven_commandFails() {
        String expectedMessage = String.format(MESSAGE_FAILURE_NET,
                "java.net.MalformedURLException: unknown protocol: notavalidprotocol");
        FeedCommand command = new FeedCommand(MALFORMED_URL);

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_urlIsNotAFeed_commandFails() {
        String expectedMessage = String.format(MESSAGE_FAILURE_XML, NOTAFEED_URL);
        FeedCommand command = new FeedCommand(NOTAFEED_URL);

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }
}
