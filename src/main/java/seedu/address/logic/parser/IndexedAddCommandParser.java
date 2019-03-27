package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.IndexedAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class IndexedAddCommandParser implements Parser<IndexedAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the IndexedAddCommand
     * and returns an IndexedAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public IndexedAddCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new IndexedAddCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, IndexedAddCommand.MESSAGE_USAGE), pe);
        }
    }
}
