package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import seedu.address.logic.commands.FeedCommand;
import seedu.address.model.entry.Link;

public class FeedCommandParserTest {
    private static final String TEST_URL = "https://open.kattis.com/rss/new-problems";
    private static final String TEST_MALFORMED_URL = "notavalidprotocol://malformed.url/invalid";

    private FeedCommandParser parser = new FeedCommandParser();

    @Test
    public void parse_validArgs_returnsFeedCommand() throws MalformedURLException {
        assertParseSuccess(parser, TEST_URL, new FeedCommand(new URL(TEST_URL)));
    }

    @Test
    public void execute_malformedUrlGiven_commandFails() {
        assertParseFailure(parser, TEST_MALFORMED_URL, Link.formExceptionMessage(TEST_MALFORMED_URL));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeedCommand.MESSAGE_USAGE));
    }
}
