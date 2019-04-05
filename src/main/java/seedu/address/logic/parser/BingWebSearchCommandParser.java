package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import seedu.address.logic.commands.BingWebSearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new BingWebSearchCommand object
 */
public class BingWebSearchCommandParser implements Parser<BingWebSearchCommand> {

    /**
     * Parses the given arguments into a BingWebSearchCommand.
     * @throws ParseException if we cannot encode the URL somehow
     */
    @Override
    public BingWebSearchCommand parse(String userInput) throws ParseException {
        if (userInput.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BingWebSearchCommand.MESSAGE_USAGE));
        }

        try {
            return new BingWebSearchCommand(userInput.trim());
        } catch (UnsupportedEncodingException uee) {
            throw new ParseException("UTF-8 encoding not supported by system!", uee);
        } catch (MalformedURLException mue) {
            throw new ParseException("Invalid link format! " + mue.getMessage(), mue);
        }
    }
}
