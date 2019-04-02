package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Unsubscribes from a feed identified using its displayed index.
 */
public class UnsubscribeCommand extends Command {
    public static final String COMMAND_WORD = "unsubscribe";
    public static final String COMMAND_ALIAS = "unsub";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unsubscribes from the feed identified by the index number used in the displayed entry list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_FEED_SUCCESS = "Deleted feed: %1$s";

    private final Index targetIndex;

    public UnsubscribeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Entry entryToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteFeedsEntry(entryToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_FEED_SUCCESS, entryToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnsubscribeCommand // instanceof handles nulls
                && targetIndex.equals(((UnsubscribeCommand) other).targetIndex)); // state check
    }
}
