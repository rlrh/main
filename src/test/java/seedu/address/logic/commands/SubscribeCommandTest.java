package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.SubscribeCommand.MESSAGE_DUPLICATE_FEED;
import static seedu.address.logic.commands.SubscribeCommand.MESSAGE_FAILURE_NET_BASE_STRING;
import static seedu.address.logic.commands.SubscribeCommand.MESSAGE_FAILURE_XML;
import static seedu.address.logic.commands.SubscribeCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.TypicalEntries.ANIMEREVIEW_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.KATTIS_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.NONEXISTENT_DOMAIN;
import static seedu.address.testutil.TypicalEntries.NOT_A_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.ONE_ITEM_FEED_ENTRY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FeedUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.ModelStub;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;

public class SubscribeCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    /** Asserts that all links from {@code feed} are imported into the model. */
    private static void assertAllLinksImported(Entry feed) throws Exception {
        Model model = new ModelManagerStub();
        CommandHistory commandHistory = new CommandHistory();

        CommandResult expectedCommandResult = new CommandResult(String.format(MESSAGE_SUCCESS, feed));

        SubscribeCommand command = new SubscribeCommand(feed);
        try {
            CommandResult result = command.execute(model, commandHistory);
            assertEquals(expectedCommandResult, result);
            EntryBook ebFromFeed = FeedUtil.fromFeedUrl(feed.getLink().value, feed.getTags());
            assertTrue(ebFromFeed.getEntryList().stream().allMatch(entry -> model.hasEntry(entry)));
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    @Test
    public void equals() {
        SubscribeCommand firstSubCommand = new SubscribeCommand(ANIMEREVIEW_FEED_ENTRY);
        SubscribeCommand secondSubCommand = new SubscribeCommand(KATTIS_FEED_ENTRY);

        // same object -> returns true
        assertTrue(firstSubCommand.equals(firstSubCommand));

        // same values -> returns true
        SubscribeCommand firstSubCommandCopy = new SubscribeCommand(ANIMEREVIEW_FEED_ENTRY);
        assertTrue(firstSubCommand.equals(firstSubCommandCopy));

        // different types -> returns false
        assertFalse(firstSubCommand.equals(1));

        // null -> returns false
        assertFalse(firstSubCommand.equals(null));

        // different entry -> returns false
        assertFalse(firstSubCommand.equals(secondSubCommand));
    }

    @Test
    public void execute_localUrl_imported() throws Exception {
        assertAllLinksImported(ONE_ITEM_FEED_ENTRY);
    }

    @Test
    public void execute_emptyModel_importedWithTags() throws Exception {
        String[] tags = new String[] {"tech", "memes"};
        Entry feedWithTags = new EntryBuilder(ONE_ITEM_FEED_ENTRY).withTags(tags).build();
        Set<Tag> tagCollection = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        Predicate<Entry> containsAllTags = entry -> entry.getTags().containsAll(tagCollection);

        Model model = new ModelManagerStub();
        SubscribeCommand command = new SubscribeCommand(feedWithTags);
        command.execute(model, commandHistory);
        assertTrue(model.getListEntryBook().getEntryList().stream().allMatch(containsAllTags));
    }

    @Test
    public void execute_entryAcceptedByModel_subscribeSuccessful() throws Exception {
        ModelStubAcceptingEntriesAndFeedsAdded modelStub = new ModelStubAcceptingEntriesAndFeedsAdded();
        Entry validEntry = ANIMEREVIEW_FEED_ENTRY;

        CommandResult commandResult = new SubscribeCommand(validEntry).execute(modelStub, commandHistory);

        assertEquals(String.format(MESSAGE_SUCCESS, validEntry), commandResult.getFeedbackToUser());
        assertEquals(List.of(validEntry), modelStub.entriesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateEntry_commandFails() throws Exception {
        Entry validEntry = ANIMEREVIEW_FEED_ENTRY;
        SubscribeCommand subscribeCommand = new SubscribeCommand(validEntry);
        ModelStub modelStub = new ModelStubWithFeed(validEntry);

        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_DUPLICATE_FEED);
        subscribeCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_invalidDomain_commandFails() {
        Entry networkFailureEntry = new EntryBuilder()
                .withLink("http://" + NONEXISTENT_DOMAIN + "/rss.xml")
                .build();
        SubscribeCommand command = new SubscribeCommand(networkFailureEntry);

        // we inline parts of CommandTestUtil#assertCommandFailure for now, due to a platform-dependent error message
        // todo: dedup this
        EntryBook expectedEntryBook = new EntryBook(model.getListEntryBook());
        EntryBook expectedArchives = new EntryBook(model.getArchivesEntryBook());
        EntryBook expectedFeeds = new EntryBook(model.getFeedsEntryBook());
        List<Entry> expectedFilteredList = new ArrayList<>(model.getFilteredEntryList());
        Entry expectedSelectedEntry = model.getSelectedEntry();

        CommandHistory expectedCommandHistory = new CommandHistory(commandHistory);

        try {
            command.execute(model, commandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertTrue(e.getMessage().startsWith(MESSAGE_FAILURE_NET_BASE_STRING));
            assertTrue(e.getMessage().contains("UnknownHostException"));
            assertTrue(e.getMessage().contains(NONEXISTENT_DOMAIN));

            // Check that the model remains unchanged
            assertEquals(expectedEntryBook, model.getListEntryBook());
            assertEquals(expectedArchives, model.getArchivesEntryBook());
            assertEquals(expectedFeeds, model.getFeedsEntryBook());
            assertEquals(expectedFilteredList, model.getFilteredEntryList());
            assertEquals(expectedSelectedEntry, model.getSelectedEntry());

            assertEquals(expectedCommandHistory, commandHistory);
        }
    }

    @Test
    public void execute_malformedFeed_commandFails() {
        Entry notAFeedEntry = NOT_A_FEED_ENTRY;
        SubscribeCommand command = new SubscribeCommand(notAFeedEntry);
        String expectedMessage = String.format(MESSAGE_FAILURE_XML, notAFeedEntry.getLink().value);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    /** A model stub that accepts all feeds and entries added to it. */
    private class ModelStubAcceptingEntriesAndFeedsAdded extends ModelStub {
        private final ArrayList<Entry> entriesAdded = new ArrayList<>();

        @Override
        public boolean hasFeedsEntry(Entry listEntry) {
            requireNonNull(listEntry);
            return entriesAdded.stream().anyMatch(listEntry::isSameEntry);
        }

        @Override
        public void addFeedsEntry(Entry entry) {
            requireNonNull(entry);
            entriesAdded.add(entry);
        }

        @Override
        public ReadOnlyEntryBook getListEntryBook() {
            return new EntryBook();
        }

        @Override
        public boolean hasEntry(Entry entry) {
            return false;
        }

        @Override
        public void addListEntry(Entry entry, Optional<byte[]> articleContent) {
            // pretend to add
        }
    }

    /** A Model stub that contains a single feed. */
    private class ModelStubWithFeed extends ModelStub {
        private final Entry entry;

        ModelStubWithFeed(Entry entry) {
            requireNonNull(entry);
            this.entry = entry;
        }

        @Override
        public boolean hasFeedsEntry(Entry listEntry) {
            requireNonNull(listEntry);
            return this.entry.isSameEntry(listEntry);
        }

        @Override
        public void addFeedsEntry(Entry feed) {
            if (feed.equals(entry)) {
                throw new DuplicateEntryException();
            }
        }
    }
}
