package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import seedu.address.logic.commands.FeedCommand;

public class FeedCommandParserTest {
    private static final String TEST_URL = "https://open.kattis.com/rss/new-problems";

    private FeedCommandParser parser = new FeedCommandParser();

    @Test
    public void parse_validArgs_returnsFeedCommand() throws MalformedURLException {
        assertParseSuccess(parser, TEST_URL, new FeedCommand(new URL(TEST_URL)));
    }

    // Invalid URLs will only get caught when we attempt to connect, which is during command execution, so not
    // covered in here.

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeedCommand.MESSAGE_USAGE));
    }
}
