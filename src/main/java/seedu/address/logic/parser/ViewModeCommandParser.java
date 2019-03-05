package seedu.address.logic.parser;

import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.ViewMode;

import java.util.Locale;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new ViewCommand object
 */
public class ViewModeCommandParser implements Parser<ViewModeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewModeCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();
            if (trimmedArgs.isEmpty()) {
                throw new IllegalArgumentException();
            }
            ViewMode viewMode = ViewMode.valueOf(trimmedArgs.toUpperCase(Locale.ENGLISH));
            return new ViewModeCommand(viewMode);
        } catch (IllegalArgumentException ioe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewModeCommand.MESSAGE_USAGE));
        }
    }
}
