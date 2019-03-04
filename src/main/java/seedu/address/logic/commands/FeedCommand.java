package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class FeedCommand extends Command {

    public static final String COMMAND_WORD = "feed";

    public static final String MESSAGE_SUCCESS = "Opened feed %s";
    public static final String MESSAGE_FAILURE = "Cannot open feed";
    public static final String MESSAGE_EMPTY_URL = "URL cannot be empty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens a link as an RSS feed and displays its entries" +
            ".\n"
            + "Parameters: LINK\n"
            + "Example: " + COMMAND_WORD + " https://open.kattis.com/rss/new-problems";

    private String feedUrl;
    public FeedCommand(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, feedUrl));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FeedCommand // instanceof handles nulls
                && feedUrl.equals(((FeedCommand) other).feedUrl)); // state check
    }
}
