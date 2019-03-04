package seedu.address.logic.parser;

import static seedu.address.logic.commands.FeedCommand.MESSAGE_EMPTY_URL;

import seedu.address.logic.commands.FeedCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class FeedCommandParser implements Parser<FeedCommand> {

    @Override
    public FeedCommand parse(String userInput) throws ParseException {
        if (userInput.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_URL);
        }
        return new FeedCommand(userInput);
    }
}
