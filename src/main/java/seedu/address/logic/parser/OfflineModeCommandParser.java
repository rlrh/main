package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.OfflineModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.OfflineMode;

/**
 * Parses input arguments and creates a new ViewCommand object
 */
public class OfflineModeCommandParser implements Parser<OfflineModeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public OfflineModeCommand parse(String args) throws ParseException {

        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.equals("enable")) {
            return new OfflineModeCommand(OfflineMode.ENABLED);
        } else if (trimmedArgs.equals("disable")) {
            return new OfflineModeCommand(OfflineMode.DISABLED);
        } else {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OfflineModeCommand.MESSAGE_USAGE));
        }

    }
}
