package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.ui.ViewMode;

public class ViewModeCommandParserTest {

    private ViewModeCommandParser parser = new ViewModeCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser,
                "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewModeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsViewCommand() {
        // no leading and trailing whitespaces
        ViewModeCommand expectedViewModeCommand = new ViewModeCommand(new ViewMode());
        assertParseSuccess(parser, "browser", expectedViewModeCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " browser   ", expectedViewModeCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser,
                "nonsense",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewModeCommand.MESSAGE_USAGE));
    }

}
