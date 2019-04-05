package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.util.Network;

/**
 * Refreshes an entry identified using its displayed index from the entry book.
 */
public class RefreshEntryCommand extends Command {

    public static final String COMMAND_WORD = "refresh";
    public static final String COMMAND_ALIAS = "r";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Refreshes the entry identified by the index number used in the displayed entry list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_REFRESH_ENTRY_SUCCESS = "Refreshed Entry: %1$s";
    public static final String MESSAGE_COULD_NOT_FETCH_ARTICLE =
        "Could not fetch article. Are you connected to the internet?";

    private final Index targetIndex;

    public RefreshEntryCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Entry> filteredEntryList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= filteredEntryList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry targetEntry = filteredEntryList.get(targetIndex.getZeroBased());

        Optional<byte[]> articleContent = Network.fetchArticleOptional(targetEntry.getLink().value);
        if (articleContent.isPresent()) {
            model.refreshEntry(filteredEntryList.get(targetIndex.getZeroBased()), articleContent.get());
        } else {
            throw new CommandException(MESSAGE_COULD_NOT_FETCH_ARTICLE);
        }

        return new CommandResult(String.format(MESSAGE_REFRESH_ENTRY_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RefreshEntryCommand // instanceof handles nulls
                && targetIndex.equals(((RefreshEntryCommand) other).targetIndex)); // state check
    }
}
