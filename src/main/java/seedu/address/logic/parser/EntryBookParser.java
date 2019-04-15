package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.BingWebSearchCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FeedCommand;
import seedu.address.logic.commands.FeedsCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.OfflineModeCommand;
import seedu.address.logic.commands.SubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

/**
 * Represents the base parser for any context.
 * It successfully parses a command if and only if the command is context-switching, exit, help or history.
 */
public abstract class EntryBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    protected static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     * Parses successfully if and only if the command is context-switching, exit, help or history.

     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public abstract Command parseCommand(String userInput) throws ParseException;

    /**
     * Parses user input into command for execution.
     * Parses successfully if and only if the command is context-switching, exit, help or history.

     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    protected Command parseCommand(String userInput, ModelContext currentContext) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case ArchivesCommand.COMMAND_WORD:
        case ArchivesCommand.COMMAND_ALIAS:
            return new ArchivesCommand();

        case BingWebSearchCommand.COMMAND_WORD:
            return new BingWebSearchCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case FeedCommand.COMMAND_WORD:
            return new FeedCommandParser().parse(arguments);

        case FeedsCommand.COMMAND_WORD:
            return new FeedsCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case GoogleNewsCommand.COMMAND_WORD:
        case GoogleNewsCommand.COMMAND_ALIAS:
            return new GoogleNewsCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_ALIAS:
            return new HelpCommand();

        case OfflineModeCommand.COMMAND_WORD:
            return new OfflineModeCommandParser().parse(arguments);

        // the following commands are actually context specific but accessible everywhere for convenience

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case SubscribeCommand.COMMAND_WORD:
        case SubscribeCommand.COMMAND_ALIAS:
            return new SubscribeCommandParser().parse(arguments);

        default:
            throw new ParseException(String.format(MESSAGE_UNKNOWN_COMMAND, currentContext));
        }
    }

}
