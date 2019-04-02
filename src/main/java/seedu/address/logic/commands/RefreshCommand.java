package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.rometools.rome.io.FeedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FeedUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.util.Network;

/**
 * Refreshes from a feed identified using its displayed index.
 */
public class RefreshCommand extends Command {
    public static final String COMMAND_WORD = "refresh";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Refreshes the feed identified by the index number used in the displayed entry list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_REFRESH_FEED_SUCCESS = "Refreshed feed: %1$s";
    public static final String MESSAGE_FAILURE_NET = "Refresh failed:\n%s";
    public static final String MESSAGE_FAILURE_XML = "Failed to parse resource at %s. Is resource outdated?";

    private final Index targetIndex;

    public RefreshCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry feedToRefresh = lastShownList.get(targetIndex.getZeroBased());

        // todo: dedup
        EntryBook feedEntries;
        try {
            // we ensure the link is a feed here
            feedEntries = FeedUtil.fromFeedUrl(feedToRefresh.getLink().value);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_NET, e));
        } catch (FeedException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_XML));
        }


        feedEntries.getEntryList().stream()
                .filter(entry -> !model.hasEntry(entry))
                .forEach(entry -> {
                    Optional<byte[]> articleContent = Network.fetchArticleOptional(entry.getLink().value);
                    model.addListEntry(entry, articleContent);
                });


        return new CommandResult(String.format(MESSAGE_REFRESH_FEED_SUCCESS, feedToRefresh));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RefreshCommand // instanceof handles nulls
                && targetIndex.equals(((RefreshCommand) other).targetIndex)); // state check
    }
}
