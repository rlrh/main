package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;
import seedu.address.logic.commands.GoogleNewsCommand;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class GoogleNewsCommandParserTest {
    private GoogleNewsCommandParser parser = new GoogleNewsCommandParser();

    @Test
    public void parse_emptyArgs_returnsGoogleNewsCommand() throws MalformedURLException {
        assertParseSuccess(parser, "    ", new GoogleNewsCommand());
    }

    @Test
    public void parse_validArgs_returnsGoogleNewsCommand() throws MalformedURLException, UnsupportedEncodingException {
        assertParseSuccess(parser, "keywords", new GoogleNewsCommand("keywords"));
    }
}
