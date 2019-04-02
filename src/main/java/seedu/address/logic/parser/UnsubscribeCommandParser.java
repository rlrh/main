package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnsubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnsubscribeCommand object
 */
public class UnsubscribeCommandParser implements Parser<UnsubscribeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnsubscribeCommand
     * and returns an UnsubscribeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnsubscribeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnsubscribeCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsubscribeCommand.MESSAGE_USAGE), pe);
        }
    }

}
