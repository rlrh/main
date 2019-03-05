package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FeedCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FeedCommand object
 */
public class FeedCommandParser implements Parser<FeedCommand> {

    @Override
    public FeedCommand parse(String userInput) throws ParseException {
        if (userInput.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeedCommand.MESSAGE_USAGE));
        }
        return new FeedCommand(userInput.trim());
    }
}
