package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RefreshEntryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class RefreshEntryCommandParser implements Parser<RefreshEntryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an RefreshEntryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RefreshEntryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RefreshEntryCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RefreshEntryCommand.MESSAGE_USAGE), pe);
        }
    }
}
