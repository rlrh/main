package seedu.address.logic.parser;

import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    public static final String MESSAGE_INVALID_BOOL = "Invalid arguments.";

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns an ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        try {
            try {
                String trimmedString = args.trim();
                Boolean bool = Boolean.parseBoolean(trimmedString);
                return new ViewCommand(bool);
            } catch (Exception e) {
                throw new ParseException(MESSAGE_INVALID_BOOL);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE), pe);
        }
    }
}
