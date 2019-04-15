package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Test;

import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

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

    @Test
    public void parse_validArgs_success() throws ParseException, UnsupportedEncodingException, MalformedURLException {
        assertEquals(new GoogleNewsCommand("1"), parser.parse("1"));
    }

    @Test
    public void parse_argsAreTrimmed() throws ParseException {
        assertEquals(parser.parse("1"), parser.parse(" 1 "));
    }
}
