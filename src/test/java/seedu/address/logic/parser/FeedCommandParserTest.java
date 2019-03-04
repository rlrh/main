package seedu.address.logic.parser;

import static seedu.address.logic.commands.FeedCommand.MESSAGE_EMPTY_URL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.FeedCommand;

public class FeedCommandParserTest {

    private FeedCommandParser parser = new FeedCommandParser();
    private static final String TEST_URL = "https://open.kattis.com/rss/new-problems";

    @Test
    public void parse_validArgs_returnsFeedCommand() {
        assertParseSuccess(parser, TEST_URL, new FeedCommand(TEST_URL));
    }

    // Invalid URLs will only get caught when we attempt to connect, which is during command execution, so not
    // covered in here.

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", MESSAGE_EMPTY_URL);
    }
}
