package seedu.address.logic.parser;

import static seedu.address.logic.parser.ParserUtil.parseEntryFromArgs;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Entry;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        Entry entry = parseEntryFromArgs(args);

        return new AddCommand(entry);
    }
}
