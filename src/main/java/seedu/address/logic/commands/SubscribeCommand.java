package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.io.IOException;
import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import seedu.address.commons.util.FeedUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.util.EntryAutofill;
import seedu.address.util.Network;

/** Subscribes to a feed and adds the feed to the feedEntryBook. */
public class SubscribeCommand extends Command {
    public static final String COMMAND_WORD = "subscribe";
    public static final String COMMAND_ALIAS = "sub";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a feed subscription to the feed list. "
            + "Parameters: "
            + PREFIX_LINK + "FEED_LINK "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_DESCRIPTION + "COMMENT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LINK + "https://open.kattis.com/rss/new-problems "
            + PREFIX_TITLE + "Example Title "
            + PREFIX_DESCRIPTION + "Example Description "
            + PREFIX_TAG + "programming "
            + PREFIX_TAG + "tech";

    public static final String MESSAGE_SUCCESS = "New feed added: %1$s";
    public static final String MESSAGE_DUPLICATE_FEED = "This feed already exists in the feed list";
    public static final String MESSAGE_FAILURE_NET = "Feed not subscribed! Fetching resource failed:\n%s";
    public static final String MESSAGE_FAILURE_XML = "Feed not subscribed! %s is not a valid RSS/Atom feed!";

    private final Entry toSubscribe;

    public SubscribeCommand(Entry feedToSubscribe) {
        toSubscribe = feedToSubscribe;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        if (model.hasFeedsEntry(toSubscribe)) {
            throw new CommandException(MESSAGE_DUPLICATE_FEED);
        }

        URL feedUrl = toSubscribe.getLink().value;
        SyndFeed feed;
        try {
            // we ensure the link is a feed here
            feed = FeedUtil.fetchAsFeed(feedUrl);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_NET, e));
        } catch (FeedException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_XML, toSubscribe.getLink().value));
        }

        EntryAutofill autofill = new EntryAutofill(toSubscribe);
        autofill.extractFromUrl(feedUrl);
        autofill.extractFromFeed(feed);
        Entry updatedToSubscribe = autofill.getFilledEntry();

        model.addFeedsEntry(updatedToSubscribe);

        // initial import into reading list
        EntryBook feedEntries = FeedUtil.serializeToEntryBook(feed, feedUrl.toString());
        feedEntries.getEntryList().stream()
                .filter(entry -> !model.hasEntry(entry))
                .forEach(entry -> model.addListEntry(entry, Network.fetchArticleOptional(entry.getLink().value)));

        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedToSubscribe));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SubscribeCommand // instanceof handles nulls
                && toSubscribe.equals(((SubscribeCommand) other).toSubscribe));
    }
}
