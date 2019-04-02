package seedu.address.logic.parser;

import static seedu.address.logic.parser.ParserUtil.parseEntryFromArgs;

import seedu.address.logic.commands.SubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Entry;

/**
 * Parses input arguments and creates a new SubscribeCommand object
 */
public class SubscribeCommandParser implements Parser<SubscribeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SubscribeCommand
     * and returns an SubscribeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SubscribeCommand parse(String args) throws ParseException {
        Entry entry = parseEntryFromArgs(args, SubscribeCommand.MESSAGE_USAGE);

        return new SubscribeCommand(entry);
    }
}
