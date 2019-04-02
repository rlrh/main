package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STYLE;

import java.util.Locale;

import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.ReaderViewStyle;
import seedu.address.ui.ViewMode;
import seedu.address.ui.ViewType;

/**
 * Parses input arguments and creates a new ViewCommand object
 */
public class ViewModeCommandParser implements Parser<ViewModeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewModeCommand parse(String args) throws ParseException {

        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STYLE);

        try {

            String trimmedPreamble = argMultimap.getPreamble().trim();
            if (trimmedPreamble.isEmpty()) {
                throw new IllegalArgumentException();
            }
            ViewType viewType = ViewType.valueOf(trimmedPreamble.toUpperCase(Locale.ENGLISH));

            ReaderViewStyle readerViewStyle = ReaderViewStyle.DEFAULT;
            if (argMultimap.getValue(PREFIX_STYLE).isPresent()) {
                readerViewStyle = ReaderViewStyle.valueOf(
                        argMultimap.getValue(PREFIX_STYLE).get().toUpperCase(Locale.ENGLISH)
                );
            }

            return new ViewModeCommand(new ViewMode(viewType, readerViewStyle));

        } catch (IllegalArgumentException iae) {

            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewModeCommand.MESSAGE_USAGE));

        }

    }
}
