package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RefreshCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RefreshCommand object
 */
public class RefreshCommandParser implements Parser<RefreshCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RefreshCommand
     * and returns an RefreshCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RefreshCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RefreshCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RefreshCommand.MESSAGE_USAGE), pe);
        }
    }

}
