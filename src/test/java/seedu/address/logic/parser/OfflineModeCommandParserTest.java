package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.OfflineModeCommand;
import seedu.address.model.OfflineMode;

public class OfflineModeCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, OfflineModeCommand.MESSAGE_USAGE);

    private OfflineModeCommandParser parser = new OfflineModeCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidArgument_throwsParseException() {
        assertParseFailure(parser, "enabel", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "disabel", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "enable enable", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "enable disable", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "disable enable", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "disable disable", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "_enable", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "enab", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "enabl", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArgument_returnsOfflineModeCommand() {
        OfflineModeCommand enableCommand = new OfflineModeCommand(OfflineMode.ENABLED);
        OfflineModeCommand disableCommand = new OfflineModeCommand(OfflineMode.DISABLED);

        assertParseSuccess(parser, "enable", enableCommand);
        assertParseSuccess(parser, "       enable", enableCommand);
        assertParseSuccess(parser, "enable       ", enableCommand);
        assertParseSuccess(parser, "       enable      ", enableCommand);

        assertParseSuccess(parser, "disable", disableCommand);
        assertParseSuccess(parser, "       disable", disableCommand);
        assertParseSuccess(parser, "disable       ", disableCommand);
        assertParseSuccess(parser, "       disable      ", disableCommand);
    }

}
