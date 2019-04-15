package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showNoEntry;
import static seedu.address.logic.commands.UnsubscribeCommand.MESSAGE_DELETE_FEED_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.StorageStub;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.entry.Entry;

/** Unit tests and integration (with Model) tests for {@code UnsubscribeCommand}. */
public class UnsubscribeCommandTest {

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();


    @Before
    public void setUp() {
        model.setContext(ModelContext.CONTEXT_FEEDS);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Entry entryToUnsubscribe = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(MESSAGE_DELETE_FEED_SUCCESS, entryToUnsubscribe);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
                model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_FEEDS);
        expectedModel.deleteFeedsEntry(entryToUnsubscribe);

        assertCommandSuccess(unsubscribeCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEntryList().size() + 1);
        UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(outOfBoundIndex);

        assertCommandFailure(unsubscribeCommand, model, commandHistory, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Entry entryToUnsubscribe = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(MESSAGE_DELETE_FEED_SUCCESS, entryToUnsubscribe);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
                model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_FEEDS);
        expectedModel.deleteFeedsEntry(entryToUnsubscribe);
        showNoEntry(expectedModel);

        assertCommandSuccess(unsubscribeCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getListEntryBook().getEntryList().size());

        UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(outOfBoundIndex);

        assertCommandFailure(unsubscribeCommand, model, commandHistory, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnsubscribeCommand unsubscribeFirstCommand = new UnsubscribeCommand(INDEX_FIRST_ENTRY);
        UnsubscribeCommand unsubscribeSecondCommand = new UnsubscribeCommand(INDEX_SECOND_ENTRY);

        // same object -> returns true
        assertTrue(unsubscribeFirstCommand.equals(unsubscribeFirstCommand));

        // same values -> returns true
        UnsubscribeCommand unsubscribeFirstCommandCopy = new UnsubscribeCommand(INDEX_FIRST_ENTRY);
        assertTrue(unsubscribeFirstCommand.equals(unsubscribeFirstCommandCopy));

        // different types -> returns false
        assertFalse(unsubscribeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unsubscribeFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(unsubscribeFirstCommand.equals(unsubscribeSecondCommand));
    }
}
