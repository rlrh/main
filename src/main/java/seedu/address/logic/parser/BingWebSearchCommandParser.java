package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.UnsupportedEncodingException;

import seedu.address.logic.commands.BingWebSearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class BingWebSearchCommandParser implements Parser<BingWebSearchCommand> {

    @Override
    public BingWebSearchCommand parse(String userInput) throws ParseException {
        if (userInput.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BingWebSearchCommand.MESSAGE_USAGE));
        }

        try {
            return new BingWebSearchCommand(userInput.trim());
        } catch (UnsupportedEncodingException uee) {
            throw new ParseException("UTF-8 encoding not supported by system!", uee);
        }
    }
}
