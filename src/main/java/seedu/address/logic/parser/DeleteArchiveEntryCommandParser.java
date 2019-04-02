package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteArchiveEntryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteArchiveEntryCommandParser implements Parser<DeleteArchiveEntryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteArchiveEntryCommand
     * and returns an DeleteArchiveEntryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteArchiveEntryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteArchiveEntryCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteArchiveEntryCommand.MESSAGE_USAGE), pe);
        }
    }

}
