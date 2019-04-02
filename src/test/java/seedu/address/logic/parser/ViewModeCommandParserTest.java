package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STYLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_VIEWTYPE;
import static seedu.address.logic.commands.CommandTestUtil.STYLE_DESC_DARK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_BROWSER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_READER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.ui.ReaderViewStyle;
import seedu.address.ui.ViewMode;
import seedu.address.ui.ViewType;

public class ViewModeCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewModeCommand.MESSAGE_USAGE);

    private ViewModeCommandParser parser = new ViewModeCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreambleWithoutOptionalField_throwsParseException() {
        assertParseFailure(parser, INVALID_VIEWTYPE, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreambleWithInvalidOptionalField_throwsParseException() {
        String userInput = INVALID_VIEWTYPE + INVALID_STYLE_DESC;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreambleWithValidOptionalField_throwsParseException() {
        String userInput = INVALID_VIEWTYPE + STYLE_DESC_DARK;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validPreambleWithInvalidOptionalField_throwsParseException() {
        String userInput = VALID_VIEWTYPE_READER + INVALID_STYLE_DESC;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validPreambleWithoutOptionalField_returnsViewModeCommand() {
        ViewModeCommand expectedBrowserViewModeCommand = new ViewModeCommand(new ViewMode(ViewType.BROWSER));
        ViewModeCommand expectedReaderViewModeCommand = new ViewModeCommand(new ViewMode(ViewType.READER));

        // no leading and trailing whitespaces
        assertParseSuccess(parser, VALID_VIEWTYPE_BROWSER, expectedBrowserViewModeCommand);
        assertParseSuccess(parser, VALID_VIEWTYPE_READER, expectedReaderViewModeCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + VALID_VIEWTYPE_BROWSER + "   ", expectedBrowserViewModeCommand);
        assertParseSuccess(parser, " " + VALID_VIEWTYPE_READER + "   ", expectedReaderViewModeCommand);
    }

    @Test
    public void parse_validPreambleWithValidOptionalField_returnsViewModeCommand() {
        ViewModeCommand expectedCommand = new ViewModeCommand(new ViewMode(ViewType.READER, ReaderViewStyle.DARK));
        String userInput = VALID_VIEWTYPE_READER + STYLE_DESC_DARK;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
